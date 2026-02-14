package com.win.util;
import java.io.*;
import javax.swing.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


public class TextEditor extends JFrame{

private JEditorPane editorpane;
JScrollPane editorScrollPane;
String filename="I:\\Projects\\Whep\\PlayTimer\\logs\\playtimer.log";
File filereader;

public TextEditor()
{       
        editorpane= new JEditorPane();
        editorpane.setEditable(false);
        Document doc = editorpane.getDocument();
        doc.putProperty(Document.StreamDescriptionProperty, null);

        if (filename != null) 
        {
            try 
            {
                filereader = new File(filename);
//                editorpane.setPage(filename);
                editorpane.setPage(filereader.toURI().toURL());
            }

            catch (IOException e) 
            {
                System.err.println("Attempted to read a bad file " + filename);
            }
         }

        else
        {
            System.err.println("Couldn't find file");
        }

        //Put the editor pane in a scroll pane.
        editorScrollPane = new JScrollPane(editorpane);
            editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));
        add(editorScrollPane);

}

public static void main(String[] args) 
{
    TextEditor obj= new TextEditor();
    obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    obj.setSize(600,600);
    obj.setLocation(100,100);
    obj.setVisible(true);
    
    while (true) {
    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    	obj.editorpane.repaint();
    	obj.editorpane.revalidate();
    	obj.repaint();
    	obj.revalidate();
    }

}
}