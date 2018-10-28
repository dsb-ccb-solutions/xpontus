/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author proprietary
 */
public class WebUtils {

    
    public static void downloadResource(String archiveURL, String destdir){
       boolean completed = false;
       
       try{
           File m_tempFile = File.createTempFile("tmpxpontus", ".tmp");
           URL m_url = new URL(archiveURL);          
           FileUtils.copyURLToFile(m_url, m_tempFile);
           unzip(m_tempFile.getAbsolutePath(), destdir);
           m_tempFile.delete();
       }
       catch(Exception e){
           JOptionPane.showMessageDialog(null, e.getMessage());
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
                String path = outdir +
                        File.separator + entry.getName();
                System.out.println("Extracting:" + path);
                BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(path));

                while ((len = zipin.read(buffer)) >= 0) {
                    fileout.write(buffer, 0, len);
                }

               
                fileout.flush();
                fileout.close(); 
            }
        }
    }
    
    
    
    
}
