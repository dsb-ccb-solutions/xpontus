/*
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi
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
 *
 *
 */
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.plugins.SimplePluginDescriptor;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;

import org.java.plugin.util.IoUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.lucene.index.Term;
import org.java.plugin.registry.PluginAttribute;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginPrerequisite;

/**
 *
 * @author Yves Zoundi
 */
public class PluginsUtils {

    private static PluginsUtils INSTANCE;
    private FSDirectory m_installedFSDirectory;
    private IndexWriter m_installedIndexWriter;
    private IndexReader m_installedIndexReader;
    private final String[] fields = {
        "author", "license", "date", "version", "homepage", "description",
        "category", "builtin", "uid", "displayname", "dependencies"
    };

    private PluginsUtils() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    if (m_installedIndexReader != null) {
                        m_installedIndexReader.close();
                    }

                    if (m_installedIndexWriter != null) {
                        m_installedIndexWriter.close();
                    }

                    if (m_installedFSDirectory != null) {
                        m_installedFSDirectory.close();
                    }
                } catch (Exception err) {
                }
            }
            });
    }

    public static PluginsUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PluginsUtils();
        }

        return INSTANCE;
    }
    
    public String dependenciesMet(){
        return null;
    }
    

    public Map<String, SimplePluginDescriptor> searchIndex(String str,
            String index) {
        Map<String, SimplePluginDescriptor> results = new HashMap<String, SimplePluginDescriptor>();


        if (str.trim().equals("")) {
            return results;
        }

        IndexSearcher searcher = null;

        try {
            searcher = new IndexSearcher(m_installedFSDirectory);

            Analyzer m_analyzer = new UTF8AccentRemoverAnalyzer();

            QueryParser parser = new MultiFieldQueryParser(fields, m_analyzer);

            Query query = parser.parse(str);

            Hits hits = searcher.search(query);

            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                SimplePluginDescriptor spd = new SimplePluginDescriptor();
                spd.setAuthor(doc.get("author"));
                spd.setVersion(doc.get("version"));
                spd.setId(doc.get("uid"));
                spd.setCategory(doc.get("category"));
                spd.setBuiltin(doc.get("builtin"));
                spd.setDate(doc.get("date"));
                spd.setDisplayname(doc.get("displayname"));
                spd.setHomepage(doc.get("homepage"));
                spd.setLicense(doc.get("license"));
                spd.setDescription(doc.get("description"));
                spd.setDependencies(doc.get("dependencies"));
                results.put(spd.getId(), spd);
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }

        return results;
    }

    public void initInstalledPluginsIndex() {
        try {
            boolean create = false;

            if (!XPontusConfigurationConstantsIF.INSTALLED_PLUGINS_SEARCHINDEX_DIR.exists()) {
                XPontusConfigurationConstantsIF.INSTALLED_PLUGINS_SEARCHINDEX_DIR.mkdirs();
            }

            m_installedFSDirectory = FSDirectory.getDirectory(XPontusConfigurationConstantsIF.INSTALLED_PLUGINS_SEARCHINDEX_DIR);

            Analyzer m_analyzer = new UTF8AccentRemoverAnalyzer();

            if (!IndexReader.indexExists(m_installedFSDirectory)) {
                m_installedIndexWriter = new IndexWriter(m_installedFSDirectory,
                        true, m_analyzer);
                m_installedIndexWriter.close();
            }

            m_installedIndexReader = IndexReader.open(m_installedFSDirectory);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void shouldAddToIndex(Map<String, SimplePluginDescriptor> map) {
        if (m_installedIndexReader.numDocs() == 0) {
            try {


                Iterator<SimplePluginDescriptor> it = map.values().iterator();

                while (it.hasNext()) {
                    SimplePluginDescriptor spd = it.next();

                    synchronized (spd) {
                        addToIndex(spd);
                    }
                }


            } catch (Exception err) {
            }
        }
    }
    
    public static SimplePluginDescriptor toSimplePluginDescriptor(PluginDescriptor pds){ 
            String id = pds.getId().toString();
            String category = pds.getAttribute("Category").getValue();
            String homepage = pds.getAttribute("Homepage").getValue();
            String builtin = pds.getAttribute("Built-in").getValue();
            String displayname = pds.getAttribute("DisplayName").getValue();
            String description = pds.getAttribute("Description").getValue();
            String license = pds.getAttribute("License").getValue();
            String date = pds.getAttribute("date").getValue();
            String version = pds.getVersion().toString();
            SimplePluginDescriptor spd = new SimplePluginDescriptor();

            String vendor = "Yves Zoundi";

            spd.setAuthor(vendor);
            spd.setDate(date);
            spd.setBuiltin(builtin);
            spd.setCategory(category);
            spd.setDescription(description);
            spd.setDisplayname(displayname);
            spd.setHomepage(homepage);
            spd.setId(id);
            spd.setLicense(license);
            spd.setVersion(version);
            
            Collection<PluginPrerequisite> deps = pds.getPrerequisites();
            if(deps.size() > 0){
                StrBuilder sb = new StrBuilder();
                Iterator<PluginPrerequisite> ppIt = deps.iterator();
                while(ppIt.hasNext()){
                    PluginPrerequisite pp = ppIt.next();
                    sb.append(pp.getPluginId()  );
                    PluginDescriptor ad = XPontusPluginManager.getPluginManager()
                                                   .getRegistry().getPluginDescriptor(pp.getPluginId());
                    PluginAttribute pat = ad.getAttribute("Built-in");
                    if(pat!=null){
                        if(pat.getValue().equals("true")){
                            sb.append("(Builtin)");
                        }
                    } 
                    sb.append("<br/>");
                }
                spd.setDependencies(sb.toString());
            }
            return spd;
    }

    public void removeFromIndex(String pluginId) throws Exception {
        m_installedIndexReader.deleteDocuments(new Term("uid", pluginId));
    }

    public void addToIndex(SimplePluginDescriptor spd) {
        try {

            Analyzer m_analyzer = new UTF8AccentRemoverAnalyzer();
            m_installedIndexWriter = new IndexWriter(m_installedFSDirectory,
                    false, m_analyzer); 

            Document doc = new Document();

            if (spd.getDate() == null) {
                System.out.println("Adding date to:" + spd.getId());

                Date s = new Date();
                StringBuffer sb = new StringBuffer();
                sb.append(s.getDay()).append("-").append(s.getMonth()).append("-").append(s.getYear());
                spd.setDate(sb.toString());
            }

            if ((spd.getAuthor() == null)) {
                spd.setAuthor("Yves Zoundi");
            }

            if (spd.getAuthor().trim().equals("")) {
                spd.setAuthor("Yves Zoundi");
            }

            if (spd.getBuiltin() == null) {
                spd.setBuiltin("false");
            }

            if ((spd.getLicense() == null) ||
                    spd.getLicense().trim().equals("")) {
                spd.setLicense("http://www.gnu.org/licenses/gpl-2.0.txt");
            }

            System.out.println("Building index document for plugin:" +
                    spd.getId());

            //            spd.print();
            doc.add(new Field("uid", spd.getId(), Field.Store.YES,
                    Field.Index.TOKENIZED));
            doc.add(new Field("category", spd.getCategory(), Field.Store.YES,
                    Field.Index.TOKENIZED));
            doc.add(new Field("author", spd.getAuthor(), Field.Store.YES,
                    Field.Index.TOKENIZED));
            doc.add(new Field("displayname", spd.getDisplayname(),
                    Field.Store.YES, Field.Index.TOKENIZED));
            doc.add(new Field("version", spd.getVersion(), Field.Store.YES,
                    Field.Index.TOKENIZED));
            doc.add(new Field("description", spd.getDescription(),
                    Field.Store.YES, Field.Index.TOKENIZED));
            doc.add(new Field("builtin", spd.getBuiltin(), Field.Store.YES,
                    Field.Index.TOKENIZED));
            doc.add(new Field("date", spd.getDate(), Field.Store.YES,
                    Field.Index.TOKENIZED));
            doc.add(new Field("homepage", spd.getHomepage(), Field.Store.YES,
                    Field.Index.TOKENIZED));
            doc.add(new Field("license", spd.getLicense(), Field.Store.YES,
                    Field.Index.TOKENIZED));
             doc.add(new Field("dependencies", spd.getDependencies(), Field.Store.YES,
                    Field.Index.TOKENIZED));

            m_installedIndexWriter.addDocument(doc);
            m_installedIndexWriter.optimize();

            m_installedIndexWriter.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void unzip(String zipFilename, String outdir)
            throws IOException {
        System.out.println("In unzip method...");

        ZipFile zipFile = new ZipFile(zipFilename);
        Enumeration entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            System.out.println("Entry name:" + entry.getName());

            boolean isDirectory = entry.isDirectory();
            byte[] buffer = new byte[1024];
            int len;

            File destDir = new File(outdir + File.separator + entry.getName());

            if (isDirectory) {
                System.out.println("DestDir:" + destDir.getAbsolutePath());

                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
            } else {
                InputStream zipin = zipFile.getInputStream(entry);
                String path = outdir +
                        File.separator + entry.getName(); 
                BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(path));

                while ((len = zipin.read(buffer)) >= 0) {
                    fileout.write(buffer, 0, len);
                }

               
                fileout.flush();
                fileout.close(); 
            }
        }
    }

    public URL getManifestUrl(final File file) {
        try {
            if (file.isDirectory()) {
                File result = new File(file, "plugin.xml"); //$NON-NLS-1$

                if (result.isFile()) {
                    return IoUtil.file2url(result);
                }

                result = new File(file, "plugin-fragment.xml"); //$NON-NLS-1$

                if (result.isFile()) {
                    return IoUtil.file2url(result);
                }

                return null;
            }

            if (!file.isFile()) {
                return null;
            }

            URL url = new URL("jar:" //$NON-NLS-1$
                    + IoUtil.file2url(file).toExternalForm() + "!/plugin.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                    + IoUtil.file2url(file).toExternalForm() +
                    "!/plugin-fragment.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                    + IoUtil.file2url(file).toExternalForm() +
                    "!/META-INF/plugin.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                    + IoUtil.file2url(file).toExternalForm() +
                    "!/META-INF/plugin-fragment.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }
        } catch (MalformedURLException mue) {
        // ignore
        }

        return null;
    }
}
