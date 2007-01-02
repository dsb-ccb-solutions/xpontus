/*
 * SyntaxInfoFactory.java
 *
 * Created on December 22, 2006, 1:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor.syntax;

import net.sf.xpontus.view.editor.ILexer;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;


/**
 *
 * @author Owner
 */
public class SyntaxSupportFactory
  {
    private static Properties syntaxDefinitions;

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
    public static SyntaxSupport createSyntax(String extension)
      {
        init();

        String mParser = extension + ".parser";
        String mColorer = extension + ".colorer";
        String parser_key = syntaxDefinitions.getProperty(mParser);
        String parser_colorer = syntaxDefinitions.getProperty(mColorer);

        if (parser_key == null)
          {
            parser_key = syntaxDefinitions.getProperty("java.parser");
            parser_colorer = syntaxDefinitions.getProperty("java.colorer");
          }

        ILexer lexer = null;
        IColorProvider syntaxSupport = null;

        try
          {
            // create a new lexer
            Object lexerObj = Class.forName(parser_key).newInstance();
            lexer = (ILexer) lexerObj;

            // create a new syntax provider
            Object syntaxObj = Class.forName(parser_colorer).newInstance();
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
