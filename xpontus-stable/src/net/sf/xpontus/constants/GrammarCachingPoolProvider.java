/*
 * GrammarCachingPoolProvider.java
 *
 * Created on February 15, 2007, 7:15 PM
 *
 *  Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.constants;

 import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.*;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

/**
 * A class which provides grammar caching
 * @author Yves Zoundi
 */
public class GrammarCachingPoolProvider {
    
    private static GrammarCachingPoolProvider INSTANCE;
    private XMLGrammarPool  pool;
    private SymbolTable table;
    
    public SymbolTable getSymbolTable(){
        return table;
    }
    
    public XMLGrammarPool getGrammarPool(){
        return pool;
    }
    
    public static GrammarCachingPoolProvider getInstance(){
        if(INSTANCE == null){
            INSTANCE = new GrammarCachingPoolProvider();
        }
        return INSTANCE;
    }
    /** Creates a new instance of GrammarCachingPoolProvider */
    private GrammarCachingPoolProvider() {
        table = new SymbolTable();
        pool = new XMLGrammarPoolImpl();
    }
    
}
