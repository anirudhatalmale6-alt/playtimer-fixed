package com.mp3.player;

import java.text.*;
import java.util.Date;
import java.util.Locale;


public abstract class Formats {
    
    public final static Formats TIMESTAMP = new FormatsTIMESTAMP();
    public final static Formats DATE = new FormatsDATE();
    public final static Formats TIME = new FormatsTIME();
    public final static Formats HOURMIN = new FormatsHOURMIN();

    /** Added; Thanks  TSirwani 3 Mar 11 */
    public final static Formats SIMPLEDATE = new FormatsSIMPLEDATE();
    private static DateFormat m_dateformat = DateFormat.getDateInstance();
    private static DateFormat m_timeformat = DateFormat.getTimeInstance();
    private static DateFormat m_datetimeformat = DateFormat.getDateTimeInstance(); 
//    		new SimpleDateFormat("dd/mm/yyyy HH:mm:ss a"); 
   
    private static DateFormat m_hourminformat = new SimpleDateFormat("HH:mm:ss");
    private static DateFormat m_simpledate = new SimpleDateFormat("dd-MM-yyyy");
    /** Creates a new instance of Formats */
    protected Formats() {
    }
    
    public String formatValue(Object value) {
        if (value == null) {
            return "";
        } else {
            return formatValueInt(value);
        }
    }
    
    public Object parseValue(String value, Object defvalue)  {
        if (value == null || "".equals(value)) {
            return defvalue;
        } else {
            try {
            	return parseValueInt(value);
            } catch (ParseException e) {
//                throw new BasicException(e.getMessage(), e);
            }
        }  
        
        return null;
    }
    
    public Object parseValue(String value) {
        return parseValue(value, null);
    }


    
    public static void setDatePattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_dateformat = DateFormat.getDateInstance();
        } else {
            m_dateformat = new SimpleDateFormat(pattern);
        }
    }
    
    public static void setTimePattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_timeformat = DateFormat.getTimeInstance();
        } else {
            m_timeformat = new SimpleDateFormat(pattern);
        }
    }
    
    public static void setDateTimePattern(String pattern) {
        if (pattern == null || pattern.equals("")) {
            m_datetimeformat = DateFormat.getDateTimeInstance();
        } else {
            m_datetimeformat = new SimpleDateFormat(pattern);
        }
    }
    
    protected abstract String formatValueInt(Object value);
    protected abstract Object parseValueInt(String value) throws ParseException;
    public abstract int getAlignment();

    private static final class FormatsNULL extends Formats {       
        @Override
        protected String formatValueInt(Object value) {
            return null;
        }       
        @Override
        protected Object parseValueInt(String value) throws ParseException {
            return null;
        }
        @Override
        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }
    }
    private static final class FormatsSTRING extends Formats {       
        @Override
        protected String formatValueInt(Object value) {
            return (String) value;
        }   
        @Override
        protected Object parseValueInt(String value) throws ParseException {
            return value;
        }
        @Override
        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }
    }    
    
    
    private static final class FormatsBOOLEAN extends Formats {       
        @Override
        protected String formatValueInt(Object value) {
            return ((Boolean) value).toString();
        }   
        @Override
        protected Object parseValueInt(String value) throws ParseException {
            return Boolean.valueOf(value);
        }
        @Override
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }    
    private static final class FormatsTIMESTAMP extends Formats {       
        @Override
        protected String formatValueInt(Object value) {
            return m_datetimeformat.format((Date) value);
        }   
        @Override
        protected Object parseValueInt(String value) throws ParseException {
            try {
                return m_datetimeformat.parse(value);
            } catch (ParseException e) {
                // segunda oportunidad como fecha normalita
                return m_dateformat.parse(value);
            }
        }
        @Override
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }
    private static final class FormatsDATE extends Formats {       
        @Override
        protected String formatValueInt(Object value) {
            return m_dateformat.format((Date) value);
        }   
        @Override
        protected Object parseValueInt(String value) throws ParseException {
            return m_dateformat.parse(value);
        }
        @Override
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }  
    private static final class FormatsTIME extends Formats {       
        @Override
        protected String formatValueInt(Object value) {
            return m_timeformat.format((Date) value);
        }   
        @Override
        protected Object parseValueInt(String value) throws ParseException {
            return m_timeformat.parse(value);
        }
        @Override
        public int getAlignment() {
            return javax.swing.SwingConstants.CENTER;
        }
    }    
    private static final class FormatsBYTEA extends Formats {       
        @Override
        protected String formatValueInt(Object value) {
            try {
                return new String((byte[]) value, "UTF-8");
            } catch (java.io.UnsupportedEncodingException eu) {
                return "";
            }
        }   
        @Override
        protected Object parseValueInt(String value) throws ParseException {
            try {
               return value.getBytes("UTF-8");
            } catch (java.io.UnsupportedEncodingException eu) {
               return new byte[0];
            }
        }
        @Override
        public int getAlignment() {
            return javax.swing.SwingConstants.LEADING;
        }
    }     
		     private static final class FormatsHOURMIN extends Formats {
                         @Override
	protected String formatValueInt(Object value) {
	return m_hourminformat.format(value);
}
                         @Override
	protected Date parseValueInt(String value) throws ParseException {
	 return m_hourminformat.parse(value);
	 }
                         @Override
                         public int getAlignment() {

             return javax.swing.SwingConstants.CENTER;

         }

     }

/** Added; Thanks  TSirwani 3 Mar 11 */
         private static final class FormatsSIMPLEDATE extends Formats {

             @Override
         protected String formatValueInt(Object value) {

             return m_simpledate.format(value);

 }

             @Override
         protected Date parseValueInt(String value) throws ParseException {

             return m_simpledate.parse(value);

         }

             @Override
         public int getAlignment() {

             return javax.swing.SwingConstants.CENTER;

         }

     }
}
