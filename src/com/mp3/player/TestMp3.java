package com.mp3.player;

import java.io.File;

public class TestMp3 {
	
	public static void main(String[] args) {
		
		MP3Player pla = new MP3Player(new File("F:\\temp\\Songs\\jilla\\Jingunamani - Www.Tamilkey.Com.mp3"));
		pla.play();
		
	}
}
