/*
 * EncodingHelper.java
 *
 * Created on February 15, 2007, 8:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.utils;

import com.ibm.icu.text.CharsetDetector;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *
 * @author Owner
 */
public class EncodingHelper {
    
    /** Creates a new instance of EncodingHelper */
    private EncodingHelper() {
    }
    
      public static final Reader getReader(InputStream is){
        try{
        BufferedInputStream bis = new BufferedInputStream(is);
            CharsetDetector detect = new CharsetDetector();
            detect.setText(bis);
            String ch = detect.detect().getName();
            
            return new InputStreamReader(bis, ch);
        }
        catch(Exception er){
            return new InputStreamReader(is);
        }
    }
    
}
