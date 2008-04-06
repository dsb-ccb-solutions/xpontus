/*
 * GrammarPoolHolder.java
 *
 * Created on May 26, 2007, 8:11 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.utils;

import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;


/**
 * Grammar caching
 * @author Yves Zoundi
 * @version 0.0.1
 */
public class GrammarPoolHolder {
    private static GrammarPoolHolder INSTANCE;
    private XMLGrammarPool m_GrammarPool;
    private SymbolTable m_SymbolTable;

    private GrammarPoolHolder() {
        this(2034);
    }

    private GrammarPoolHolder(int symbolTableSize) {
        m_GrammarPool = new XMLGrammarPoolImpl();
        m_SymbolTable = new SymbolTable(symbolTableSize);
    }

    /**
     * Returns the symbol table
     * @return The symbol table
     */
    public SymbolTable getSymbolTable() {
        return m_SymbolTable;
    }

    /**
     * Returns the grammar pool
     * @return the grammar ppol
     */
    public XMLGrammarPool getGrammarPool() {
        return m_GrammarPool;
    }

    /**
     * SINGLETON
     * @return the single instance of this class
     */
    public static GrammarPoolHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GrammarPoolHolder();
        }

        return INSTANCE;
    }
}
