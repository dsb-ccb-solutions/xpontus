/*
 * Entry.java
 *
 * Created on January 27, 2007, 12:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.codecompletion.xml;

/**
 *
 * @author Owner
 */
public class Entry {
        
        //{{{ Public static members
        /**
         * A system ID entry
         */
        public static final int SYSTEM = 0;
        /**
         * A public ID entry
         */
        public static final int PUBLIC = 1;
        //}}}
        
        public int type;
        public String id;
        public String uri;

        //{{{ Entry constructor
        /**
         * Creates a new Entry for a PUBLIC or SYSTEM identifier.
         * @param type the Entry type
         * @param id the id
         * @param uri the uri of the identifier
         */
        public Entry(int type, String id, String uri) {
            this.type = type;
            this.id = id;
            this.uri = uri;
        }//}}}

        //{{{ equals()
        public boolean equals(Object o) {
            if (o instanceof Entry) {
                Entry e = (Entry)o;
                return e.type == type && e.id.equals(id);
            } else
                return false;
        }//}}}

        //{{{ hashCode
        public int hashCode() {
            return id.hashCode();
        }//}}}
    } //}}