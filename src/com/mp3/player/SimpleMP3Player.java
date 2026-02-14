package com.mp3.player;

import jaco.mp3.resources.Decoder;
import jaco.mp3.resources.Frame;
import jaco.mp3.resources.SampleBuffer;
import jaco.mp3.resources.SoundStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * Simple MP3 player that uses jaco's decoder directly,
 * without the JPanel/UI overhead of jaco.mp3.player.MP3Player.
 * This is more reliable for headless/scheduled playback.
 */
public class SimpleMP3Player {

    private volatile boolean stopped = true;
    private volatile Thread playbackThread = null;
    private volatile SourceDataLine currentLine = null;
    private volatile int volume = 80;

    public SimpleMP3Player() {
    }

    /**
     * Play an MP3 file. Blocks the calling thread until playback completes or stop() is called.
     * If called while already playing, stops the current playback first.
     */
    public void playFile(File file) {
        LOG.log.info("SimpleMP3Player.playFile: " + file.getAbsolutePath());

        if (!file.exists()) {
            LOG.log.error("SimpleMP3Player: File does not exist: " + file.getAbsolutePath());
            return;
        }

        if (!file.canRead()) {
            LOG.log.error("SimpleMP3Player: Cannot read file: " + file.getAbsolutePath());
            return;
        }

        // Stop any current playback
        stop();

        stopped = false;

        playbackThread = new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                SourceDataLine line = null;
                try {
                    is = new FileInputStream(file);
                    SoundStream soundStream = new SoundStream(is);
                    Decoder decoder = new Decoder();

                    LOG.log.info("SimpleMP3Player: Decoder initialized, starting frame loop");

                    int frameCount = 0;
                    while (!stopped) {
                        Frame frame = soundStream.readFrame();
                        if (frame == null) {
                            LOG.log.info("SimpleMP3Player: End of stream after " + frameCount + " frames");
                            break;
                        }

                        // Setup audio output on first frame
                        if (line == null) {
                            int sampleRate = frame.frequency();
                            int channels = (frame.mode() == 3) ? 1 : 2;
                            AudioFormat format = new AudioFormat(sampleRate, 16, channels, true, false);
                            LOG.log.info("SimpleMP3Player: Audio format: " + sampleRate + "Hz, " + channels + "ch");

                            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                            line = (SourceDataLine) AudioSystem.getLine(info);
                            line.open(format);
                            line.start();
                            currentLine = line;

                            // Set initial volume
                            setLineVolume(line, volume);
                            LOG.log.info("SimpleMP3Player: Audio line opened and started");
                        }

                        // Decode frame
                        SampleBuffer output = (SampleBuffer) decoder.decodeFrame(frame, soundStream);
                        short[] buffer = output.getBuffer();
                        int len = output.getBufferLength();

                        // Convert short[] to byte[] (little-endian)
                        byte[] byteBuffer = new byte[len * 2];
                        int idx = 0;
                        for (int i = 0; i < len; i++) {
                            short s = buffer[i];
                            byteBuffer[idx++] = (byte) s;
                            byteBuffer[idx++] = (byte) (s >>> 8);
                        }

                        // Write to audio output
                        line.write(byteBuffer, 0, byteBuffer.length);
                        soundStream.closeFrame();
                        frameCount++;
                    }

                    // Drain or flush
                    if (line != null) {
                        if (!stopped) {
                            line.drain();
                        } else {
                            line.flush();
                        }
                        line.stop();
                        line.close();
                    }

                    soundStream.close();
                    LOG.log.info("SimpleMP3Player: Playback finished normally");

                } catch (Exception e) {
                    LOG.log.error("SimpleMP3Player: Error during playback: " + e.getMessage());
                    e.printStackTrace();
                    if (line != null) {
                        try {
                            line.stop();
                            line.close();
                        } catch (Exception ex) {
                            // ignore
                        }
                    }
                } finally {
                    if (is != null) {
                        try { is.close(); } catch (Exception ex) { }
                    }
                    currentLine = null;
                    stopped = true;
                    playbackThread = null;
                }
            }
        }, "SimpleMP3Player-Playback");

        playbackThread.setDaemon(true);
        playbackThread.start();
    }

    /**
     * Stop current playback.
     */
    public void stop() {
        stopped = true;
        Thread t = playbackThread;
        if (t != null) {
            try {
                t.join(5000); // Wait up to 5 seconds
                if (t.isAlive()) {
                    LOG.log.warn("SimpleMP3Player: Playback thread did not stop in time, interrupting");
                    t.interrupt();
                }
            } catch (InterruptedException e) {
                // ignore
            }
        }
        playbackThread = null;
    }

    /**
     * Check if currently playing.
     */
    public boolean isPlaying() {
        return !stopped && playbackThread != null;
    }

    /**
     * Set volume (0-100).
     */
    public void setVolume(int vol) {
        this.volume = Math.max(0, Math.min(100, vol));
        SourceDataLine line = currentLine;
        if (line != null) {
            setLineVolume(line, this.volume);
        }
    }

    private void setLineVolume(SourceDataLine source, int vol) {
        try {
            if (vol == 0) {
                BooleanControl muteControl = (BooleanControl) source.getControl(BooleanControl.Type.MUTE);
                muteControl.setValue(true);
            } else {
                try {
                    BooleanControl muteControl = (BooleanControl) source.getControl(BooleanControl.Type.MUTE);
                    muteControl.setValue(false);
                } catch (Exception e) {
                    // Mute control may not be available
                }
                FloatControl gainControl = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);
                float gain = (float) (Math.log((double) vol / 100.0) / Math.log(10.0) * 20.0);
                gainControl.setValue(gain);
            }
        } catch (Exception e) {
            LOG.log.warn("SimpleMP3Player: Cannot set volume: " + e.getMessage());
        }
    }
}
