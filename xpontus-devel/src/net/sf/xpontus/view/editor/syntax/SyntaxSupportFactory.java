/*
 * SyntaxInfoFactory.java
 *
 * Created on December 22, 2006, 1:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor.syntax;


import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A class responsible for providing the right syntax coloring parser
 * @author Yves Zoundi
 */
public class SyntaxSupportFactory
{
    private static Properties syntaxDefinitions;
    private static Log log = LogFactory.getLog(SyntaxSupportFactory.class);

    /** Creates a new instance of SyntaxInfoFactory */
    private SyntaxSupportFactory()
    {
    }

    private static void init()
    {
        if (syntaxDefinitions == null)
        {
            syntaxDefinitions = new Properties();

            Class cl = SyntaxSupportFactory.class;
            String loc = "/net/sf/xpontus/conf/syntax.properties";
            InputStream is = cl.getResourceAsStream(loc);

            try
            {
                syntaxDefinitions.load(is);
                IOUtils.closeQuietly(is);
            }
            catch (IOException e)
            {
            }
        }
    }

    /**
     * @param extension
     * @return
     */
    public static SyntaxSupport getSyntax(String extension)
    {
        init();

        java.util.Iterator it = syntaxDefinitions.keySet().iterator();
          String mParser = syntaxDefinitions.getProperty("plain.parser");
        String mColorer = syntaxDefinitions.getProperty("plain.colorer");
        
        boolean lexerFound = false;
        
        while(it.hasNext()){
            if(lexerFound){
                break;
            }
            String prop = it.next().toString();
            
            
            if(prop.endsWith(".parser")){                
                String prop_key = prop.split("\\.")[0];
                System.out.println("property:" + prop_key);
                String[]  extensions = syntaxDefinitions.getProperty(prop_key + ".extensions").split(",");
                for(int i=0;i<extensions.length;i++){
                    if(extensions[i].trim().equals(extension)){ 
                        mParser = syntaxDefinitions.getProperty(prop);
                        mColorer = syntaxDefinitions.getProperty(prop_key + ".colorer");
                        lexerFound = true; 
                        log.info("using colorer class : " + mParser);
                        break;
                    }
                }
                
                
            }
        } 

        ILexer lexer = null;
        IColorProvider syntaxSupport = null;
 
        try
        {
            // create a new lexer
            Object lexerObj = Class.forName(mParser).newInstance();
            lexer = (ILexer) lexerObj;

            // create a new syntax provider
            Object syntaxObj = Class.forName(mColorer).newInstance();
            syntaxSupport = (IColorProvider) syntaxObj;
        }
        catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SyntaxSupport _info = new SyntaxSupport(lexer, syntaxSupport);

        return _info;
    }
}
