/*
 * XPontusFormController.java
 *
 * Created on 18 juillet 2005, 02:29
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

import java.awt.Font;
import javax.swing.UIManager;
import net.sf.xpontus.core.utils.IconUtils;
import net.sf.xpontus.core.utils.L10nHelper;
import net.sf.xpontus.core.utils.WindowUtilities;
import net.sf.xpontus.model.options.GeneralOptionModel;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;

import java.util.Properties;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.model.options.XMLOptionModel;


/**
 * Main controller/Launcher of the application
 * @author Yves Zoundi
 */
public class XPontusFormController {
    private static String theme = null;
    private static Log logger = LogFactory.getLog(XPontusFormController.class);
    
    static{        
        System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");
    }
    
    /** Creates a new instance of XPontusFormController */
    public XPontusFormController() {
        String i18nfile = "net.sf.xpontus.i18n.application";
         
        L10nHelper.registerLocalizationFile(i18nfile);
        
        
        
        String conf = "/net/sf/xpontus/conf/configuration.xml";
        
        ApplicationContext context = new ClassPathXmlApplicationContext(conf);
        
        XPontusWindow form = XPontusWindow.getInstance();
        
        form.setApplicationContext(context);
        
        form.initActions();
        
        form.getFrame().setTitle("XPontus XML Editor 1.0.0-rc2");
        
        logger.info("building main window");
        
        javax.swing.ImageIcon frameicon;
        String loc = "/net/sf/xpontus/icons/Sun/icone.png";
        frameicon = IconUtils.getInstance().getIcon(loc);
        form.getFrame().setIconImage(frameicon.getImage());
        form.getFrame().pack();
        WindowUtilities.centerOnScreen(form.getFrame());
        
        
        form.getFrame().setVisible(true);
    }
    
    /**
     * Main method of the class
     * @param argv Command line arguments
     */
    public static void main(String[] argv) throws Exception {
        //System.setProperty("swing.aatext", "true");
        System.setProperty("Plastic.defaultTheme", "ExperienceBlue");
        
        new ConfigurationHandler().init();
        
        Properties themeProperties = new Properties();
        String loc = "/net/sf/xpontus/conf/theme.properties";
        InputStream is = XPontusFormController.class.getResourceAsStream(loc);
        themeProperties.load(is);
        
        GeneralOptionModel m = new GeneralOptionModel();
        GeneralOptionModel model1 = (GeneralOptionModel) m.load();
        
        if (model1 == null) {
            model1 = new GeneralOptionModel();
        }
        
        final String _theme = model1.getIconSet(); // m1.getTheme();
        IconUtils.getInstance().setStyle(_theme);
        
        String look = model1.getTheme();
        
        String lf = themeProperties.getProperty(look);
        
        UIManager.setLookAndFeel(lf);
        
        EditorOptionModel em = new EditorOptionModel();
        EditorOptionModel emodel1 = (EditorOptionModel) em.load();
        String fontSize = emodel1.getFontSize();
        String fontName = emodel1.getFontName();
        
        UIManager.put("EditorPane.font", new Font(fontName, Font.PLAIN, Integer.parseInt(fontSize)));
        
        XMLOptionModel em2 = new XMLOptionModel();
        XMLOptionModel emodel2 = (XMLOptionModel) em2.load();
        String newValue = emodel2.getXsltProcessor();
        if(newValue.equals("Saxon 6.5.5")){
            System.setProperty("javax.xml.transform.TransformerFactory",
                    "com.icl.saxon.TransformerFactoryImpl") ;
        } else if(newValue.equals("Saxon-B 8.5.1")){
            System.setProperty("javax.xml.transform.TransformerFactory",
                    "net.sf.saxon.TransformerFactoryImpl") ;
        } else if(newValue.equals("Jd.xslt 1.5.5")){
            System.setProperty("javax.xml.transform.TransformerFactory",
                    "jd.xml.xslt.trax.TransformerFactoryImpl");
        } else{
            System.setProperty("javax.xml.transform.TransformerFactory",
                    "org.apache.xalan.processor.TransformerFactoryImpl") ;
        }
        is.close();
        themeProperties = null;
        
        new XPontusFormController();
    }
}
