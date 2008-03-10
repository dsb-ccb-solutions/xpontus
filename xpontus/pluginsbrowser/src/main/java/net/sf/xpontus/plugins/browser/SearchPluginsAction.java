/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.browser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.awt.event.ActionEvent;

import java.util.Vector;

import javax.swing.AbstractAction;


/**
 *
 * @author mrcheeks
 */
public class SearchPluginsAction extends AbstractAction {
    private final String[] fields = {
            "author", "license", "date", "contributors", "version", "homepage",
            "description", "category", "builtin", "id", "displayname", "archive",
            "packagename"
        };
    private Analyzer analyzer;

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            IndexSearcher searcher = new IndexSearcher("");
            String q = "";
            MultiFieldQueryParser mparser = new MultiFieldQueryParser(fields,
                    getAnalyzer());
            Query query = mparser.parse(q);

            Hits hits = searcher.search(query);

            Vector results = null;

            for (int i = 0; i < hits.length(); i++) {
                if (results == null) {
                    results = new Vector();
                }

                Document doc = hits.doc(i);

                results.add(doc);
            }

            searcher.close();
        } catch (Exception e) {
        }
    }
}
