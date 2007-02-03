/*
 * MiscUtilities.java
 *
 * Created on January 25, 2007, 5:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.text.Segment;

/**
 *
 * @author Yves Zoundi
 */
public class MiscUtilities {
    
    /**
     * This encoding is not supported by Java, yet it is useful.
     * A UTF-8 file that begins with 0xEFBBBF.
     */
    public static final String UTF_8_Y = "UTF-8Y";
    
    //{{{ Path name methods

    //{{{ canonPath() method
    /**
     * Returns the canonical form of the specified path name. Currently
     * only expands a leading <code>~</code>. <b>For local path names
     * only.</b>
     * @param path The path name
     */
    public static String canonPath(String path)
    {
        if(File.separatorChar == '\\')
        {
            // get rid of mixed paths on Windows
            path = path.replace('/','\\');
        }

        if(path.startsWith("~" + File.separator))
        {
            path = path.substring(2);
            String home = System.getProperty("user.home");

            if(home.endsWith(File.separator))
                return home + path;
            else
                return home + File.separator + path;
        }
        else if(path.equals("~"))
            return System.getProperty("user.home");
        else
            return path;
    } //}}}

    //{{{ isPathAbsolute() method
    /**
     * Returns if the specified path name is an absolute path or URL.
     */
    public static boolean isAbsolutePath(String path)
    {
        if(isURL(path))
            return true;
        else if(OperatingSystem.isDOSDerived())
        {
            if(path.length() >= 2 && path.charAt(1) == ':')
                return true;
            if(path.startsWith("\\\\"))
                return true;
        }
        else if(OperatingSystem.isUnix())
        {
            // nice and simple
            if(path.length() > 0 && path.charAt(0) == '/')
                return true;
        }

        return false;
    } //}}} 

    //{{{ concatPath() method
    /**
     * Like {@link #constructPath}, except <code>path</code> will be
     * appended to <code>parent</code> even if it is absolute.
     * @param path
     * @param parent
     */
    public static String concatPath(String parent, String path)
    {
        parent = canonPath(parent);
        path = canonPath(path);

        // Make all child paths relative.
        if (path.startsWith(File.separator))
            path = path.substring(1);
        else if ((path.length() >= 3) && (path.charAt(1) == ':'))
            path = path.replace(':', File.separatorChar);

        if (parent == null)
            parent = System.getProperty("user.dir");

        if (parent.endsWith(File.separator))
            return parent + path;
        else
            return parent + File.separator + path;
    } //}}}

    //{{{ getFileExtension() method
    /**
     * Returns the extension of the specified filename, or an empty
     * string if there is none.
     * @param name The file name
     */
    public static String getFileExtension(String name)
    {
        int index = name.indexOf('.');
        if(index == -1)
            return "";
        else
            return name.substring(index);
    } //}}}

    //{{{ getFileName() method
    /**
     * Returns the last component of the specified path.
     * This method is VFS-aware.
     * @param path The path name
     */
    public static String getFileName(String path) {
        if (path.equals("/")) {
            return path;
        }
        
        int count = Math.max(0,path.length() - 2);
        int index = Math.max(path.lastIndexOf('/',count), path.lastIndexOf(File.separatorChar,count));
        if (index == -1) {
            index = path.indexOf(':');
        }
        
        // don't want getFileName("roots:") to return ""
        if (index == -1 || index == path.length() - 1) {
            return path;
        }
        
        return path.substring(index + 1);
    } //}}}

   // //{{{ getFileNameNoExtension() method
   // /**
   //  * Returns the last component of the specified path name without the
   //  * trailing extension (if there is one).
   //  * @param path The path name
   //  * @since jEdit 4.0pre8
   //  */
   // public static String getFileNameNoExtension(String path)
   // {
   //     String name = getFileName(path);
   //     int index = name.lastIndexOf('.');
   //     if(index == -1)
   //         return name;
   //     else
   //         return name.substring(0,index);
   // } //}}}

    //{{{ getParentOfPath() method
    /**
     * Returns the parent of the specified path.
     * @param path The path name
     * @since jsXe 0.4 pre3
     */
    public static String getParentOfPath(String path) {
        // ignore last character of path to properly handle
        // paths like /foo/bar/
        int count = Math.max(0,path.length() - 2);
        int index = path.lastIndexOf(File.separatorChar,count);
        if(index == -1)
            index = path.lastIndexOf('/',count);
        if(index == -1)
        {
            // this ensures that getFileParent("protocol:"), for
            // example, is "protocol:" and not "".
            index = path.lastIndexOf(':');
        }

        return path.substring(0,index + 1);
    } //}}}

    //{{{ getProtocolOfURL() method
    /**
     * Returns the protocol specified by a URL.
     * @param url The URL
     */
    public static String getProtocolOfURL(String url)
    {
        return url.substring(0,url.indexOf(':'));
    } //}}}

    //{{{ isURL() method
    /**
     * Checks if the specified string is a URL.
     * @param str The string to check
     * @return True if the string is a URL, false otherwise
     */
    public static boolean isURL(String str)
    {
        int fsIndex = Math.max(str.indexOf(File.separatorChar), str.indexOf('/'));
        
        if(fsIndex == 0) // /etc/passwd
            return false;
        else if(fsIndex == 2) // C:\AUTOEXEC.BAT
            return false;

        int cIndex = str.indexOf(':');
        if(cIndex <= 1) // D:\WINDOWS
            return false;
        else if(fsIndex != -1 && cIndex > fsIndex) // /tmp/RTF::read.pm
            return false;

        return true;
    } //}}}

    //{{{ saveBackup() method
    /**
     * Saves a backup (optionally numbered) of a file.
     * @param file A local file
     * @param backups The number of backups. Must be >= 1. If > 1, backup
     * files will be numbered.
     * @param backupPrefix The backup file name prefix
     * @param backupSuffix The backup file name suffix
     * @param backupDirectory The directory where to save backups; if null,
     * they will be saved in the same directory as the file itself.
     */
    public static void saveBackup(File file, int backups,
        String backupPrefix, String backupSuffix,
        String backupDirectory)
    {
        if(backupPrefix == null)
            backupPrefix = "";
        if(backupSuffix == null)
            backupSuffix = "";

        String name = file.getName();

        // If backups is 1, create ~ file
        if(backups == 1)
        {
            File backupFile = new File(backupDirectory,
                backupPrefix + name + backupSuffix);
            backupFile.delete();
            file.renameTo(backupFile);
        }
        // If backups > 1, move old ~n~ files, create ~1~ file
        else
        {
            new File(backupDirectory,
                backupPrefix + name + backupSuffix
                + backups + backupSuffix).delete();

            for(int i = backups - 1; i > 0; i--)
            {
                File backup = new File(backupDirectory,
                    backupPrefix + name + backupSuffix
                    + i + backupSuffix);

                backup.renameTo(new File(backupDirectory,
                    backupPrefix + name + backupSuffix
                    + (i+1) + backupSuffix));
            }

            file.renameTo(new File(backupDirectory,
                backupPrefix + name + backupSuffix
                + "1" + backupSuffix));
        }
    } //}}}

    //{{{ fileToClass() method
    /**
     * Converts a file name to a class name. All slash characters are
     * replaced with periods and the trailing '.class' is removed.
     * @param name The file name
     */
    public static String fileToClass(String name)
    {
        char[] clsName = name.toCharArray();
        for(int i = clsName.length - 6; i >= 0; i--)
            if(clsName[i] == '/')
                clsName[i] = '.';
        return new String(clsName,0,clsName.length - 6);
    } //}}}

    //{{{ classToFile() method
    /**
     * Converts a class name to a file name. All periods are replaced
     * with slashes and the '.class' extension is added.
     * @param name The class name
     */
    public static String classToFile(String name)
    {
        return name.replace('.','/').concat(".class");
    } //}}}

    //{{{ resolveSymlinks() method
    public static String resolveSymlinks(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch(IOException io) {
            return path;
        }
    } //}}}
    
    //}}}

    //{{{ Text methods

    //{{{ getLeadingWhiteSpace() method
    /**
     * Returns the number of leading white space characters in the
     * specified string.
     * @param str The string
     */
    public static int getLeadingWhiteSpace(String str) {
        int whitespace = 0;
loop:   for(;whitespace < str.length();) {
            switch(str.charAt(whitespace)) {
                case ' ': case '\t':
                    whitespace++;
                    break;
                default:
                    break loop;
            }
        }
        return whitespace;
    } //}}}

    //{{{ getTrailingWhiteSpace() method
    /**
     * Returns the number of trailing whitespace characters in the
     * specified string.
     * @param str The string
     */
    public static int getTrailingWhiteSpace(String str) {
        int whitespace = 0;
loop:   for(int i = str.length() - 1; i >= 0; i--) {
            switch(str.charAt(i)) {
                case ' ': case '\t':
                    whitespace++;
                    break;
                default:
                    break loop;
            }
        }
        return whitespace;
    } //}}}

    //{{{ getLeadingWhiteSpaceWidth() method
    /**
     * Returns the width of the leading white space in the specified
     * string.
     * @param str The string
     * @param tabSize The tab size
     */
    public static int getLeadingWhiteSpaceWidth(String str, int tabSize)
    {
        int whitespace = 0;
loop:       for(int i = 0; i < str.length(); i++)
        {
            switch(str.charAt(i))
            {
            case ' ':
                whitespace++;
                break;
            case '\t':
                whitespace += (tabSize - whitespace % tabSize);
                break;
            default:
                break loop;
            }
        }
        return whitespace;
    } //}}}

    //{{{ getVirtualWidth() method
    /**
     * Returns the virtual column number (taking tabs into account) of the
     * specified offset in the segment.
     *
     * @param seg The segment
     * @param tabSize The tab size
     */
    public static int getVirtualWidth(Segment seg, int tabSize)
    {
        int virtualPosition = 0;

        for (int i = 0; i < seg.count; i++)
        {
            char ch = seg.array[seg.offset + i];

            if (ch == '\t')
            {
                virtualPosition += tabSize
                    - (virtualPosition % tabSize);
            }
            else
            {
                ++virtualPosition;
            }
        }

        return virtualPosition;
    } //}}}

    //{{{ getOffsetOfVirtualColumn() method
    /**
     * Returns the array offset of a virtual column number (taking tabs
     * into account) in the segment.
     *
     * @param seg The segment
     * @param tabSize The tab size
     * @param column The virtual column number
     * @param totalVirtualWidth If this array is non-null, the total
     * virtual width will be stored in its first location if this method
     * returns -1.
     *
     * @return -1 if the column is out of bounds
     */
    public static int getOffsetOfVirtualColumn(Segment seg, int tabSize,
        int column, int[] totalVirtualWidth)
    {
        int virtualPosition = 0;

        for (int i = 0; i < seg.count; i++)
        {
            char ch = seg.array[seg.offset + i];

            if (ch == '\t')
            {
                int tabWidth = tabSize
                    - (virtualPosition % tabSize);
                if(virtualPosition >= column)
                    return i;
                else
                    virtualPosition += tabWidth;
            }
            else
            {
                if(virtualPosition >= column)
                    return i;
                else
                    ++virtualPosition;
            }
        }

        if(totalVirtualWidth != null)
            totalVirtualWidth[0] = virtualPosition;
        return -1;
    } //}}}

    //{{{ createWhiteSpace() method
    /**
     * Creates a string of white space with the specified length.<p>
     *
     * To get a whitespace string tuned to the current buffer's
     * settings, call this method as follows:
     *
     * <pre>myWhitespace = MiscUtilities.createWhiteSpace(myLength,
     *     (buffer.getBooleanProperty("noTabs") ? 0
     *     : buffer.getTabSize()));</pre>
     *
     * @param len The length
     * @param tabSize The tab size, or 0 if tabs are not to be used
     */
    public static String createWhiteSpace(int len, int tabSize)
    {
        StringBuffer buf = new StringBuffer();
        if(tabSize == 0)
        {
            while(len-- > 0)
                buf.append(' ');
        }
        else
        {
            int count = len / tabSize;
            while(count-- > 0)
                buf.append('\t');
            count = len % tabSize;
            while(count-- > 0)
                buf.append(' ');
        }
        return buf.toString();
    } //}}}

    //{{{ globToRE() method
    /**
     * Converts a Unix-style glob to a regular expression.<p>
     *
     * ? becomes ., * becomes .*, {aa,bb} becomes (aa|bb).
     * @param glob The glob pattern
     */
    public static String globToRE(String glob)
    {
        StringBuffer buf = new StringBuffer();
        boolean backslash = false;
        boolean insideGroup = false;
        boolean insideNegativeLookahead = false;

        for(int i = 0; i < glob.length(); i++)
        {
            char c = glob.charAt(i);
            if(backslash)
            {
                buf.append('\\');
                buf.append(c);
                backslash = false;
                continue;
            }

            switch(c)
            {
            case '\\':
                backslash = true;
                break;
            case '?':
                buf.append('.');
                break;
            case '.':
                buf.append("\\.");
                break;
            case '*':
                buf.append(".*");
                break;
            case '{':
                buf.append('(');
                if(i + 1 != glob.length() && glob.charAt(i + 1) == '!')
                {
                    buf.append('?');
                    insideNegativeLookahead = true;
                }
                else
                    insideGroup = true;
                break;
            case ',':
                if(insideGroup)
                {
                    if(insideNegativeLookahead)
                    {
                        buf.append(").*");
                        insideNegativeLookahead = false;
                    }
                    buf.append('|');
                }
                else
                    buf.append(',');
                break;
            case '}':
                if(insideNegativeLookahead)
                {
                    buf.append(").*");
                    insideNegativeLookahead = false;
                }
                else if(insideGroup)
                {
                    buf.append(')');
                    insideGroup = false;
                }
                else
                    buf.append('}');
                break;
            default:
                buf.append(c);
            }
        }

        return buf.toString();
    } //}}}

    //{{{ escapesToChars() method
    /**
     * Converts "\n" and "\t" escapes in the specified string to
     * newlines and tabs.
     * @param str The string
     */
    public static String escapesToChars(String str)
    {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            switch(c)
            {
            case '\\':
                if(i == str.length() - 1)
                {
                    buf.append('\\');
                    break;
                }
                c = str.charAt(++i);
                switch(c)
                {
                case 'n':
                    buf.append('\n');
                    break;
                case 't':
                    buf.append('\t');
                    break;
                default:
                    buf.append(c);
                    break;
                }
                break;
            default:
                buf.append(c);
            }
        }
        return buf.toString();
    } //}}}

    //{{{ charsToEscapes() method
    /**
     * Escapes newlines, tabs, backslashes, and quotes in the specified
     * string.
     * @param str The string
     */
    public static String charsToEscapes(String str)
    {
        return charsToEscapes(str,"\n\t\\\"'");
    } //}}}

    //{{{ charsToEscapes() method
    /**
     * Escapes the specified characters in the specified string.
     * @param str The string
     * @param extra Any characters that require escaping
     */
    public static String charsToEscapes(String str, String toEscape)
    {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if(toEscape.indexOf(c) != -1)
            {
                if(c == '\n')
                    buf.append("\\n");
                else if(c == '\t')
                    buf.append("\\t");
                else
                {
                    buf.append('\\');
                    buf.append(c);
                }
            }
            else
                buf.append(c);
        }
        return buf.toString();
    } //}}}

    //{{{ compareStrings() method
    /**
     * Compares two strings.<p>
     *
     * Unlike <function>String.compareTo()</function>,
     * this method correctly recognizes and handles embedded numbers.
     * For example, it places "My file 2" before "My file 10".<p>
     *
     * @param str1 The first string
     * @param str2 The second string
     * @param ignoreCase If true, case will be ignored
     * @return negative If str1 &lt; str2, 0 if both are the same,
     * positive if str1 &gt; str2
     */
    public static int compareStrings(String str1, String str2, boolean ignoreCase) {
        char[] char1 = str1.toCharArray();
        char[] char2 = str2.toCharArray();

        int len = Math.min(char1.length,char2.length);

        for (int i = 0, j = 0; i < len && j < len; i++, j++) {
            char ch1 = char1[i];
            char ch2 = char2[j];
            if (Character.isDigit(ch1) && Character.isDigit(ch2) && ch1 != '0' && ch2 != '0') {
                int _i = i + 1;
                int _j = j + 1;

                for (; _i < char1.length; _i++) {
                    if(!Character.isDigit(char1[_i])) {
                        //_i--;
                        break;
                    }
                }

                for (; _j < char2.length; _j++) {
                    if(!Character.isDigit(char2[_j])) {
                        //_j--;
                        break;
                    }
                }

                int len1 = _i - i;
                int len2 = _j - j;
                if (len1 > len2) {
                    return 1;
                } else {
                    if(len1 < len2) {
                        return -1;
                    } else {
                        for (int k = 0; k < len1; k++) {
                            ch1 = char1[i + k];
                            ch2 = char2[j + k];
                            if (ch1 != ch2) {
                                return ch1 - ch2;
                            }
                        }
                    }
                }

                i = _i - 1;
                j = _j - 1;
            } else {
                if (ignoreCase) {
                    ch1 = Character.toLowerCase(ch1);
                    ch2 = Character.toLowerCase(ch2);
                }

                if(ch1 != ch2) {
                    return ch1 - ch2;
                }
            }
        }

        return char1.length - char2.length;
    } //}}}

    //{{{ stringsEqual() method
    /**
     * Returns if two strings are equal. This correctly handles null pointers,
     * as opposed to calling <code>s1.equals(s2)</code>.
     */
    public static boolean stringsEqual(String s1, String s2)
    {
        if(s1 == null)
        {
            if(s2 == null)
                return true;
            else
                return false;
        }
        else if(s2 == null)
            return false;
        else
            return s1.equals(s2);
    } //}}}

    //}}}

    //{{{ Sorting methods

    //{{{ quicksort() method
    /**
     * Sorts the specified array. Equivalent to calling
     * <code>Arrays.sort()</code>.
     * @param obj The array
     * @param compare Compares the objects
     */
    public static void quicksort(Object[] obj, Comparator compare)
    {
        Arrays.sort(obj,compare);
    } //}}}

    //{{{ quicksort() method
    /**
     * Sorts the specified vector.
     * @param vector The vector
     * @param compare Compares the objects
     */
    public static void quicksort(Vector vector, Comparator compare) {
        Collections.sort(vector,compare);
    } //}}}

    //{{{ quicksort() method
    /**
     * Sorts the specified list.
     * @param list The list
     * @param compare Compares the objects
     */
    public static void quicksort(List list, Comparator compare) {
        Collections.sort(list,compare);
    } //}}}

    //{{{ StringCompare class
    /**
     * Compares strings.
     */
    public static class StringCompare implements Comparator {
        public int compare(Object obj1, Object obj2) {
            return compareStrings(obj1.toString(), obj2.toString(),false);
        }
    } //}}}

    //{{{ StringICaseCompare class
    /**
     * Compares strings ignoring case.
     */
    public static class StringICaseCompare implements Comparator {
        public int compare(Object obj1, Object obj2) {
            return compareStrings(obj1.toString(), obj2.toString(),true);
        }
    } //}}}

    //{{{ MenuItemCompare class
    /**
     * Compares menu item labels.
     */
    public static class MenuItemCompare implements Comparator {
        public int compare(Object obj1, Object obj2) {
            return compareStrings(((JMenuItem)obj1).getText(), ((JMenuItem)obj2).getText(),true);
        }
    } //}}}

    //}}}

    //{{{ buildToVersion() method
    /**
     * Converts an internal version number (build) into a
     * `human-readable' form.
     * @param build The build
     */
    public static String buildToVersion(String build)
    {
        if (build.length() != 11) {
            return "<unknown version: " + build + ">";
        }
        try {
            // First 2 chars are the major version number
            int major = Integer.parseInt(build.substring(0,2));
            // Second 2 are the minor number
            int minor = Integer.parseInt(build.substring(3,5));
            // Then the pre-release status
            int beta = Integer.parseInt(build.substring(6,8));
            // Finally the bug fix release
            int bugfix = Integer.parseInt(build.substring(9,11));
    
            return "" + major + "." + minor +
                (beta != 99 ? " pre" + beta :
                (bugfix != 0 ? "." + bugfix : "") +
                (major >= 1 ? " final" : " beta"));
        } catch (NumberFormatException e) {
            return "<unknown version: " + build + ">";
        }
    } //}}} 

    //{{{ parsePermissions() method
    /**
     * Parse a Unix-style permission string (rwxrwxrwx).
     * @param str The string (must be 9 characters long).
     */
    public static int parsePermissions(String s)
    {
        int permissions = 0;

        if(s.length() == 9)
        {
            if(s.charAt(0) == 'r')
                permissions += 0400;
            if(s.charAt(1) == 'w')
                permissions += 0200;
            if(s.charAt(2) == 'x')
                permissions += 0100;
            else if(s.charAt(2) == 's')
                permissions += 04100;
            else if(s.charAt(2) == 'S')
                permissions += 04000;
            if(s.charAt(3) == 'r')
                permissions += 040;
            if(s.charAt(4) == 'w')
                permissions += 020;
            if(s.charAt(5) == 'x')
                permissions += 010;
            else if(s.charAt(5) == 's')
                permissions += 02010;
            else if(s.charAt(5) == 'S')
                permissions += 02000;
            if(s.charAt(6) == 'r')
                permissions += 04;
            if(s.charAt(7) == 'w')
                permissions += 02;
            if(s.charAt(8) == 'x')
                permissions += 01;
            else if(s.charAt(8) == 't')
                permissions += 01001;
            else if(s.charAt(8) == 'T')
                permissions += 01000;
        }

        return permissions;
    } //}}}
    
    //{{{ XML Methods
    
    //{{{ getLocalNameFromQualifiedName()
    /**
     * Extracts the XML local name from a qualified name
     * @param qualifiedName the qualified name
     * @return the local name
     * @since jsXe 0.4 pre1
     */
    public static String getLocalNameFromQualifiedName(String qualifiedName) {
        int index = qualifiedName.indexOf(":");
        String localName;
        if (index > 0) { // not -1 and not 0
            localName = qualifiedName.substring(index+1);
        } else {
            localName = qualifiedName;
        }
        return localName;
    }//}}}
    
    //{{{ getNSPrefixFromQualifiedName()
    /**
     * Extracts an XML namespace prefix from a qualified name. If there is no
     * namespace prefix this method returns null.
     * @param qualifiedName the qualified name
     * @return the namespace prefix. null if no namespace
     * @since jsXe 0.4 pre1
     */
    public static String getNSPrefixFromQualifiedName(String qualifiedName) {
        int index = qualifiedName.indexOf(":");
        String prefix = null;
        if (index != -1) {
            prefix = qualifiedName.substring(0,index);
        }
        return prefix;
    }//}}}
    
    //{{{ charactersToEntities()
    /**
     * Converts characters to entities.
     * @param s The string in which to convert characters to entities
     * @param hash a Map containing character to entity name mappings
     */
    public static String charactersToEntities(String s, Map hash) {
        
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= 0x7f || ch == '<' || ch == '>' || ch == '&' || ch == '"' || ch == '\'') {
                Character c = new Character(ch);
                String entity = (String)hash.get(c);
                if (entity != null) {
                    buf.append('&');
                    buf.append(entity);
                    buf.append(';');
                    continue;
                }
            }
            buf.append(ch);
        }

        return buf.toString();
    } //}}}

    //{{{ entitiesToCharacters()
    /**
     * Converts entities to characters
     * @param s The string in which to convert characters to entities
     * @param hash a Map containing entity name to character mappings
     */
    public static String entitiesToCharacters(String s, Map hash) {
        StringBuffer buf = new StringBuffer();
        
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '&') {
                int index = s.indexOf(';',i);
                if (index != -1) {
                    String entityName = s.substring(i + 1,index);
                    Character c = (Character)hash.get(entityName);
                    if (c != null) {
                        buf.append(c.charValue());
                        i = index;
                        continue;
                    }
                }
            }

            buf.append(ch);
        }

        return buf.toString();
    } //}}}
    
    //{{{ uriToFile() method
    public static String uriToFile(String uri) {
        if (uri.startsWith("file:/")) {
            int start;
            if (uri.startsWith("file:///") && OperatingSystem.isDOSDerived())
                start = 8;
            else if (uri.startsWith("file://"))
                start = 7;
            else
                start = 5;

            StringBuffer buf = new StringBuffer();
            for(int i = start; i < uri.length(); i++) {
                char ch = uri.charAt(i);
                if (ch == '/')
                    buf.append(java.io.File.separatorChar);
                else if(ch == '%') {
                    String str = uri.substring(i + 1,i + 3);
                    buf.append((char)Integer.parseInt(str,16));
                    i += 2;
                }
                else
                    buf.append(ch);
            }
            uri = buf.toString();
        }
        return uri;
    } //}}}
    
    //}}}
    
    //{{{ Encoding methods
    
    //{{{ isSupportedEncoding() method
    /**
     * Returns if the given character encoding is supported.
     * @since jsXe 0.4pre4
     */
    public static boolean isSupportedEncoding(String encoding) {
        if (UTF_8_Y.equals(encoding)) {
            return true;
        }
        return java.nio.charset.Charset.isSupported(encoding);
    }//}}}
    
    //{{{ getSupportedEncodings() method
    /**
     * Returns a list of supported character encodings.
     * @since jsXe 0.4pre4
     */
    public static String[] getSupportedEncodings() {
        List returnValue = new ArrayList();

        Map map = (Map)java.nio.charset.Charset.availableCharsets();
        Iterator iter = map.keySet().iterator();

        returnValue.add(UTF_8_Y);

        while(iter.hasNext()) {
            returnValue.add(iter.next());
        }

        return (String[])returnValue.toArray(new String[returnValue.size()]);
    } //}}}
    
    //}}}
    
    //{{{ mergeProperties()
    /**
     * Merges two Properties sets together into a new Properties object
     * giving precidence to the properties in the first argument. If either
     * Properties object is null, the other Properties object is returned. 
     * If both are null then null is returned.
     *
     * @param props1 the first Properties object whose properties are given
     *               precidence.
     * @param props2 the second Properties object whose properties are merged
     *               with that of the first.
     */
    public static Properties mergeProperties(Properties props1, Properties props2) {
        if (props1 == null) {
            return props2;
        }
        
        if (props2 == null) {
            return props1;
        }
        
        Properties props = new Properties();
        
        Enumeration names = props2.propertyNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            props.setProperty(name, props2.getProperty(name));
        }
        
        names = props1.propertyNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            props.setProperty(name, props1.getProperty(name));
        }
        
        return props;
    }//}}}
    
    //{{{ isTrue()
    /**
     * Returns true if the value of the string is true
     * @param str the boolean string
     */
    public static boolean isTrue(String str) {
        return "true".equalsIgnoreCase(str);
    }//}}}
    
    //{{{ equals()
    /**
     * Determines if object a equals object b. Returns false if one is null and the other is not.
     * This was written mainly because you cannot do a.equals(b) if a could be null.
     * @return true if the objects are equal based on the equals method of a or b or if both are null.
     * @since jsXe 0.4 pre1
     */
    public static boolean equals(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        } else {
            if (a != null) {
                return a.equals(b);
            } else {
                return b.equals(a);
            }
        }
    }//}}}
    
    //{{{ Private members
    private MiscUtilities() {}
    //}}}
}
