/*
 * XPontusCatalogResolver.java
 *
 * Created on February 14, 2007, 3:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.cache;

import net.sf.xpontus.constants.XPontusConstants;

import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.parser.XMLInputSource;

import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogEntry;
import org.apache.xml.resolver.CatalogManager;

import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileReader;

import java.net.URL;

import java.util.Vector;


/**
 *
 * @author Owner
 */
public class XPontusCatalogResolver extends CatalogManager {
    private static XPontusCatalogResolver resolver;
    private CatalogManager catalogManager;
    private Catalog catalog;

    public static void main(String[] argv) throws Exception {
        System.setProperty("xml.catalog.staticCatalog", "true");
        System.setProperty("xml.catalog.files",
            "C:\\tests\\docbook-xml-4.4\\docbook.cat");

        CatalogManager manager = new CatalogManager();

        Catalog catalog = manager.getCatalog();
        String pubid = argv[0];
        String uri = argv[1];
        pubid = pubid.substring(1, pubid.length() - 1);

        String s = manager.getCatalog().resolvePublic(pubid, uri);

        s = s.substring(5, s.length());

        InputSource is = new InputSource(new FileReader(s));

        XMLResourceIdentifier identifier = new XMLResourceIdentifierImpl();
        identifier.setPublicId("-//OASIS//DTD DocBook XML V4.4//EN");
        identifier.setLiteralSystemId(s);
        identifier.setBaseSystemId(s);

        DTDGrammar grammar = (DTDGrammar) new XMLDTDLoader().loadGrammar(new XMLInputSource(
                    identifier));
    }
}
