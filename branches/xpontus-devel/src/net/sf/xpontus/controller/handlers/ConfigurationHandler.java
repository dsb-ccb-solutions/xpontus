/*
 * ConfigurationHandler.java
 *
 * Created on 1 ao?t 2005, 17:46
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
package net.sf.xpontus.controller.handlers;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;
import net.sf.xpontus.core.utils.IconUtils;
import net.sf.xpontus.model.ScenarioListModel;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.model.options.GeneralOptionModel;
import net.sf.xpontus.model.options.NekoHTMLOptionModel;
import net.sf.xpontus.model.options.XMLOptionModel;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.io.IOUtils;

import java.awt.Font;

import java.io.IOException;
import java.io.InputStream;

import java.util.Locale;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * A class which checks for xpontus settings and initialize them
 * @author Yves Zoundi
 */
public class ConfigurationHandler
  {
    private static boolean inited = false;
    private final String TFACTORY = "javax.xml.parsers.TransformerFactory";
    private final String SAXON_IMPL = "com.icl.saxon.TransformerFactoryImpl";

    /**
     * Creates a new instance of ConfigurationHandler
     */
    private ConfigurationHandler()
      {
      }

    private static void initEnvironment()
      {
        try
          {
            GeneralOptionModel m = new GeneralOptionModel();
            m = (GeneralOptionModel) m.load();

            String loc = "/net/sf/xpontus/conf/languages.properties";

            Properties props = new Properties();
            InputStream isProps = XPontusFormController.class.getResourceAsStream(loc);
            props.load(isProps);

            loc = "/net/sf/xpontus/conf/theme.properties";
            isProps = XPontusFormController.class.getResourceAsStream(loc);
            props.load(isProps);

            loc = "/net/sf/xpontus/conf/xsl.properties";
            isProps = XPontusFormController.class.getResourceAsStream(loc);
            props.load(isProps);

            String _locale = m.getLanguage();
            String[] languages = props.getProperty(_locale).split(",");

            System.setProperty("user.language", languages[0]);
            System.setProperty("user.country", languages[1]);
            Locale.setDefault(new Locale(languages[0], languages[1]));

            final String _theme = m.getIconSet();

            if (m.isAntialias())
              {
                System.setProperty("swing.aatext", "true");
              }

            IconUtils.getInstance().setStyle(_theme);

            String look = m.getTheme();

            System.setProperty("Plastic.defaultTheme", "ExperienceBlue");

            String laf = null;

            if (look.equals("System"))
              {
                laf = UIManager.getSystemLookAndFeelClassName();
              }
            else if (look.equals("Java"))
              {
                laf = UIManager.getCrossPlatformLookAndFeelClassName();
              }
            else
              {
                laf = props.getProperty(look);
              }

            UIManager.setLookAndFeel(laf);

            EditorOptionModel em = new EditorOptionModel();
            em = (EditorOptionModel) em.load();

            String fontSize = em.getFontSize();
            String fontName = em.getFontName();

            XPontusWindow.splashenabled = m.isShowSplashScreen();

            UIManager.put("EditorPane.font",
                new Font(fontName, Font.PLAIN, Integer.parseInt(fontSize)));

            XMLOptionModel em2 = new XMLOptionModel();
            em2 = (XMLOptionModel) em2.load();

            String newValue = em2.getXsltProcessor().replaceAll(" ", "");

            System.setProperty(props.getProperty("TFACTORY"),
                props.getProperty(newValue));

            IOUtils.closeQuietly(isProps);
          }
        catch (NumberFormatException ex)
          {
            ex.printStackTrace();
          }
        catch (IllegalAccessException ex)
          {
            ex.printStackTrace();
          }
        catch (ClassNotFoundException ex)
          {
            ex.printStackTrace();
          }
        catch (IOException ex)
          {
            ex.printStackTrace();
          }
        catch (UnsupportedLookAndFeelException ex)
          {
            ex.printStackTrace();
          }
        catch (InstantiationException ex)
          {
            ex.printStackTrace();
          }
      }

    public static final void init()
      {
        if (inited)
          {
            return;
          }

        initSettings();
        initEnvironment();
        inited = true;
      }

    private static void initSettings()
      {
        java.io.File file = null;
        ConfigurationModel model = null;

        if (!XPontusConstants.PREF_DIR.exists())
          {
            XPontusConstants.PREF_DIR.mkdirs();
          }

        // save general preferences defaults
        file = XPontusConstants.GENERAL_PREF;

        if (!file.exists())
          {
            model = new GeneralOptionModel();
            model.save();
          }

        // save editor preferences defaults
        file = XPontusConstants.EDITOR_PREF;

        if (!file.exists())
          {
            model = new EditorOptionModel();
            model.save();
          }

        // save jtidy default properties
        file = XPontusConstants.JTIDY_PREF;

        if (!file.exists())
          {
            model = new NekoHTMLOptionModel();
            model.save();
          }

        // save jtidy default properties
        file = XPontusConstants.XML_PREF;

        if (!file.exists())
          {
            model = new XMLOptionModel();
            model.save();
          }

        // save an empty scenario list if necessary
        file = XPontusConstants.SCENARIO_FILE;

        if (!file.exists())
          {
            model = new ScenarioListModel();
            model.save();
          }
      }
  }
