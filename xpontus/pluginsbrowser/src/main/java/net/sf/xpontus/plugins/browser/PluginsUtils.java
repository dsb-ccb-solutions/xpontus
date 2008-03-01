/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.plugins.SimplePluginDescriptor;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

import org.java.plugin.util.IoUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 *
 * @author mrcheeks
 */
public class PluginsUtils {
    private static FSDirectory m_installedFSDirectory;
    private static IndexWriter m_installedIndexWriter;
    private static IndexReader m_installedIndexReader;

    public static void initInstalledPluginsIndex() {
        try {
            boolean create = false;

            if (!XPontusConfigurationConstantsIF.INSTALLED_PLUGINS_SEARCHINDEX_DIR.exists()) {
                XPontusConfigurationConstantsIF.INSTALLED_PLUGINS_SEARCHINDEX_DIR.mkdirs();
            }

            m_installedFSDirectory = FSDirectory.getDirectory(XPontusConfigurationConstantsIF.INSTALLED_PLUGINS_SEARCHINDEX_DIR);
            create = IndexReader.indexExists(m_installedFSDirectory);
            m_installedIndexWriter = new IndexWriter(m_installedFSDirectory,
                    create, new UTF8AccentRemoverAnalyzer()); 
            
            m_installedIndexReader = IndexReader.open(m_installedFSDirectory);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        try {
                            m_installedIndexWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void shouldAddToIndex(Map<String, SimplePluginDescriptor> map) {
        if (m_installedIndexReader.numDocs() == 0) {
            Iterator<SimplePluginDescriptor> it = map.values().iterator();

            while (it.hasNext()) {
                SimplePluginDescriptor spd = it.next();
                addToIndex(spd);
            }
        }
    }

    public static void addToIndex(SimplePluginDescriptor spd) {
        try {
            Document doc = new Document();

            if (spd.getDate() == null) {
                System.out.println("Adding date to:" + spd.getId());
                Date s = new Date();
                StringBuffer sb = new StringBuffer();
                sb.append(s.getDay()).append("-").append(s.getMonth())
                  .append("-").append(s.getYear());
                spd.setDate(sb.toString());
            } 

            if (spd.getAuthor() == null) {
                spd.setAuthor("Yves Zoundi");
            }

            if (spd.getLicense() == null) {
                spd.setLicense("http://www.gnu.org/licenses/gpl-2.0.txt");
            }

            doc.add(new Field("id", spd.getId(), Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
            doc.add(new Field("author", spd.getAuthor(), Field.Store.YES,
                    Field.Index.TOKENIZED));
            doc.add(new Field("displayname", spd.getDisplayname(),
                    Field.Store.YES, Field.Index.TOKENIZED));
            doc.add(new Field("description", spd.getDescription(),
                    Field.Store.YES, Field.Index.TOKENIZED));
            doc.add(new Field("builtin", spd.getBuiltin(), Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
            doc.add(new Field("date", spd.getDate(), Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
            doc.add(new Field("homepage", spd.getHomepage(), Field.Store.YES,
                    Field.Index.TOKENIZED));

            m_installedIndexWriter.addDocument(doc);
             

            m_installedIndexWriter.optimize();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void unzip(String zipFilename, String outdir)
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
                BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(outdir +
                            File.separator + entry.getName()));

                while ((len = zipin.read(buffer)) >= 0)
                    fileout.write(buffer, 0, len);

                zipin.close();
                fileout.flush();
                fileout.close();
            }
        }
    }

    public static URL getManifestUrl(final File file) {
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
                     +IoUtil.file2url(file).toExternalForm() + "!/plugin.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
                    "!/plugin-fragment.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
                    "!/META-INF/plugin.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
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
