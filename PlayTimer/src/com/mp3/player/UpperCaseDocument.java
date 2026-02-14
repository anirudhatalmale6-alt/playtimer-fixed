package com.mp3.player;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class UpperCaseDocument 
extends PlainDocument {


	@Override
    public void insertString( int offs, String str, AttributeSet a )
            throws BadLocationException {

        if ( str == null ) {
            return;
        }

        char[] chars = str.toCharArray();
        boolean ok = true;

        for ( int i = 0; i < chars.length; i++ ) {

            try {
                Integer.parseInt( String.valueOf( chars[i] ) );
            } catch ( NumberFormatException exc ) {
                ok = false;
                break;
            }


        }

        if ( ok )
            super.insertString( offs, new String( chars ), a );

    }
}
