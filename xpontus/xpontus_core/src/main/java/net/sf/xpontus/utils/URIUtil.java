/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.utils;

import java.io.UnsupportedEncodingException;


/**
 *
 * @author mrcheeks
 */
public class URIUtil {
    private final static String __CHARSET = "UTF-8";

    /* ------------------------------------------------------------ */
    /** Encode a URI path.
     * This is the same encoding offered by URLEncoder, except that
     * the '/' character is not encoded.
     * @param path The path the encode
     * @return The encoded path
     */
    public static String encodePath(String path) {
        if ((path == null) || (path.length() == 0)) {
            return path;
        }

        StringBuffer buf = encodePath(null, path);

        return (buf == null) ? path : buf.toString();
    }

    /* ------------------------------------------------------------ */
    /* Decode a URI path.
     * @param path The path the encode
     * @param buf StringBuffer to encode path into
     */
    public static String decodePath(String path) {
        if (path == null) {
            return null;
        }

        char[] chars = null;
        int n = 0;
        byte[] bytes = null;
        int b = 0;

        int len = path.length();

        for (int i = 0; i < len; i++) {
            char c = path.charAt(i);

            if ((c == '%') && ((i + 2) < len)) {
                if (chars == null) {
                    chars = new char[len];
                    bytes = new byte[len];
                    path.getChars(0, i, chars, 0);
                }

                bytes[b++] = (byte) (0xff &
                     parseInt(path, i + 1, 2, 16));
                i += 2;

                continue;
            } else if (bytes == null) {
                n++;

                continue;
            }

            if (b > 0) {
                String s;

                try {
                    s = new String(bytes, 0, b, __CHARSET);
                } catch (UnsupportedEncodingException e) {
                    s = new String(bytes, 0, b);
                }

                s.getChars(0, s.length(), chars, n);
                n += s.length();
                b = 0;
            }

            chars[n++] = c;
        }

        if (chars == null) {
            return path;
        }

        if (b > 0) {
            String s;

            try {
                s = new String(bytes, 0, b, __CHARSET);
            } catch (UnsupportedEncodingException e) {
                s = new String(bytes, 0, b);
            }

            s.getChars(0, s.length(), chars, n);
            n += s.length();
        }

        return new String(chars, 0, n);
    }

    /* ------------------------------------------------------------ */
    /** Parse an int from a substring.
     * Negative numbers are not handled.
     * @param s String
     * @param offset Offset within string
     * @param length Length of integer or -1 for remainder of string
     * @param base base of the integer
     * @exception NumberFormatException
     */
    public static int parseInt(String s, int offset, int length, int base)
        throws NumberFormatException {
        int value = 0;

        if (length < 0) {
            length = s.length() - offset;
        }

        for (int i = 0; i < length; i++) {
            char c = s.charAt(offset + i);

            int digit = c - '0';

            if ((digit < 0) || (digit >= base) || (digit >= 10)) {
                digit = (10 + c) - 'A';

                if ((digit < 10) || (digit >= base)) {
                    digit = (10 + c) - 'a';
                }
            }

            if ((digit < 0) || (digit >= base)) {
                throw new NumberFormatException(s.substring(offset,
                        offset + length));
            }

            value = (value * base) + digit;
        }

        return value;
    }

    /* ------------------------------------------------------------ */
    /** Parse an int from a byte array of ascii characters.
     * Negative numbers are not handled.
     * @param b byte array
     * @param offset Offset within string
     * @param length Length of integer or -1 for remainder of string
     * @param base base of the integer
     * @exception NumberFormatException
     */
    public static int parseInt(byte[] b, int offset, int length, int base)
        throws NumberFormatException {
        int value = 0;

        if (length < 0) {
            length = b.length - offset;
        }

        for (int i = 0; i < length; i++) {
            char c = (char) (0xff & b[offset + i]);

            int digit = c - '0';

            if ((digit < 0) || (digit >= base) || (digit >= 10)) {
                digit = (10 + c) - 'A';

                if ((digit < 10) || (digit >= base)) {
                    digit = (10 + c) - 'a';
                }
            }

            if ((digit < 0) || (digit >= base)) {
                throw new NumberFormatException(new String(b, offset, length));
            }

            value = (value * base) + digit;
        }

        return value;
    }

    /* ------------------------------------------------------------ */
    /* Decode a URI path.
     * @param path The path the encode
     * @param buf StringBuffer to encode path into
     */
    public static String decodePath(byte[] buf, int offset, int length) {
        byte[] bytes = null;
        int n = 0;

        for (int i = 0; i < length; i++) {
            byte b = buf[i + offset];

            if ((b == '%') && ((i + 2) < length)) {
                b = (byte) (0xff & parseInt(buf, i + offset + 1, 2, 16));
                i += 2;
            } else if (bytes == null) {
                n++;

                continue;
            }

            if (bytes == null) {
                bytes = new byte[length];

                for (int j = 0; j < n; j++)
                    bytes[j] = buf[j + offset];
            }

            bytes[n++] = b;
        }

        try {
            if (bytes == null) {
                return new String(buf, offset, length, __CHARSET);
            }

            return new String(bytes, 0, n, __CHARSET);
        } catch (UnsupportedEncodingException e) {
            if (bytes == null) {
                return new String(buf, offset, length);
            }

            return new String(bytes, 0, n);
        }
    }

    /* ------------------------------------------------------------ */
    /** Encode a URI path.
     * @param path The path the encode
     * @param buf StringBuffer to encode path into (or null)
     * @return The StringBuffer or null if no substitutions required.
     */
    public static StringBuffer encodePath(StringBuffer buf, String path) {
        if (buf == null) {
loop: 
            for (int i = 0; i < path.length(); i++) {
                char c = path.charAt(i);

                switch (c) {
                case '%':
                case '?':
                case ';':
                case '#':
                case ' ':
                    buf = new StringBuffer(path.length() << 1);

                    break loop;
                }
            }

            if (buf == null) {
                return null;
            }
        }

        synchronized (buf) {
            for (int i = 0; i < path.length(); i++) {
                char c = path.charAt(i);

                switch (c) {
                case '%':
                    buf.append("%25");

                    continue;

                case '?':
                    buf.append("%3F");

                    continue;

                case ';':
                    buf.append("%3B");

                    continue;

                case '#':
                    buf.append("%23");

                    continue;

                case ' ':
                    buf.append("%20");

                    continue;

                default:
                    buf.append(c);

                    continue;
                }
            }
        }

        return buf;
    }
}
