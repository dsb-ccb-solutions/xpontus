/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.utils;


/**
 *
 * @author mrcheeks
 */
public class URIUtil {
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
