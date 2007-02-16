/*
 * GrammarCachingPoolProvider.java
 *
 * Created on February 15, 2007, 7:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.constants;

 import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.*;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

/**
 *
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
