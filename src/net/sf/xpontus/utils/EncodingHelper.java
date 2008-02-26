/*
 * EncodingHelper.java
 *
 * Created on February 15, 2007, 8:04 PM
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.utils;

import com.ibm.icu.text.CharsetDetector;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * A class which attempts to detect the encoding of a stream
 * @author Yves Zoundi
 */
public class EncodingHelper {
    /** Creates a new instance of EncodingHelper */
    private EncodingHelper() {
    }

    /**
     * Encoding detection of a stream
     * @param is A stream
     * @return reader for the stream which a guessed encoding
     */
    public static final Reader getReader(InputStream is) {
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            CharsetDetector detect = new CharsetDetector();
            detect.setText(bis);

            String ch = detect.detect().getName();

            return new InputStreamReader(bis, ch);
        } catch (Exception er) {
            return new InputStreamReader(is);
        }
    }
}
