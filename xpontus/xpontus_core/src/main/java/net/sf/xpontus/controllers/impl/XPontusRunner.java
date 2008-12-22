/*
 * XPontusRunner.java
 *
 * Created on 28-Jul-2007, 12:39:41 PM
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
 */
package net.sf.xpontus.controllers.impl;

import net.sf.xpontus.actions.impl.AbstractXPontusActionImpl;
import net.sf.xpontus.actions.impl.CheckXMLActionImpl;
import net.sf.xpontus.actions.impl.CopyActionImpl;
import net.sf.xpontus.actions.impl.CreateNewFileActionImpl;
import net.sf.xpontus.actions.impl.CutActionImpl;
import net.sf.xpontus.actions.impl.ExitActionImpl;
import net.sf.xpontus.actions.impl.IndentContentActionImpl;
import net.sf.xpontus.actions.impl.OpenActionImpl;
import net.sf.xpontus.actions.impl.PasteActionImpl;
import net.sf.xpontus.actions.impl.PrintActionImpl;
import net.sf.xpontus.actions.impl.RecentFilesActionImpl;
import net.sf.xpontus.actions.impl.RedoActionImpl;
import net.sf.xpontus.actions.impl.ReloadActionImpl;
import net.sf.xpontus.actions.impl.SaveActionImpl;
import net.sf.xpontus.actions.impl.SaveAsActionImpl;
import net.sf.xpontus.actions.impl.SelectAllActionImpl;
import net.sf.xpontus.actions.impl.SimpleValidationActionImpl;
import net.sf.xpontus.actions.impl.UndoActionImpl;
import net.sf.xpontus.actions.impl.ViewMessagesWindowActionImpl;
import net.sf.xpontus.actions.impl.ViewOutlineWindowActionImpl;
import net.sf.xpontus.actions.impl.ViewToolbarActionImpl;
import net.sf.xpontus.actions.impl.ViewXPathWindowActionImpl;
import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.constants.XPontusMenuConstantsIF;
import net.sf.xpontus.constants.XPontusToolbarConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.SplashScreen;
import net.sf.xpontus.modules.gui.components.XPontusTopComponentIF;
import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.plugins.actions.ActionPlugin;
import net.sf.xpontus.plugins.completion.CodeCompletionPlugin;
import net.sf.xpontus.plugins.evaluator.EvaluatorPlugin;
import net.sf.xpontus.plugins.evaluator.EvaluatorPluginConfiguration;
import net.sf.xpontus.plugins.evaluator.ExpressionEvaluatorPanel;
import net.sf.xpontus.plugins.gendoc.DocConfiguration;
import net.sf.xpontus.plugins.gendoc.DocumentationPlugin;
import net.sf.xpontus.plugins.indentation.IndentationPlugin;
import net.sf.xpontus.plugins.ioc.IOCPlugin;
import net.sf.xpontus.plugins.lexer.LexerPlugin;
import net.sf.xpontus.plugins.menubar.MenuBarPlugin;
import net.sf.xpontus.plugins.menubar.MenuBarPluginIF;
import net.sf.xpontus.plugins.outline.OutlinePlugin;
import net.sf.xpontus.plugins.perspectives.PerspectivePlugin;
import net.sf.xpontus.plugins.preferences.PreferencesPlugin;
import net.sf.xpontus.plugins.preview.PreviewPlugin;
import net.sf.xpontus.plugins.quicktoolbar.QuickToolBarPlugin;
import net.sf.xpontus.plugins.scenarios.ScenarioPlugin;
import net.sf.xpontus.plugins.settings.DefaultSettingsModuleImpl;
import net.sf.xpontus.plugins.settings.SettingsModuleIF;
import net.sf.xpontus.plugins.themes.ThemePlugin;
import net.sf.xpontus.plugins.toolbar.ToolBarPlugin;
import net.sf.xpontus.plugins.toolbar.ToolBarPluginIF;
import net.sf.xpontus.utils.DocumentAwareComponentHolder;
import net.sf.xpontus.utils.DocumentContainerChangeEvent;

import org.java.plugin.PluginManager;

import java.awt.event.ActionEvent;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


/**
 * Class which start the application
 * This class should not be called (XPontusServerManager is the launcher)
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPontusRunner
{
    static
    {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setSecurityManager(null);
        System.setProperty("javax.xml.transform.TransformerFactory",
            "org.apache.xalan.processor.TransformerFactoryImpl");
        System.setProperty("swing.aatext", "true");
        // the default processor to use is xalan        
        System.setProperty("org.xml.sax.parser",
            "org.apache.xerces.parsers.SAXParser");
        System.setProperty("javax.xml.parsers.SAXParserFactory",
            "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
            "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }

    static volatile JWindow splash;
    private static volatile boolean showSplash = true;

    private XPontusRunner()
    {
    }

    /**
     * @param menu
     * @param actions
     * @return
     */
    public static MenuBarPluginIF createMenuExtension(final String menu,
        final Object[] actions)
    {
        MenuBarPluginIF plugin = new MenuBarPluginIF()
            {
                public List getMenuNames()
                {
                    return Arrays.asList(new String[] { menu });
                }

                public Map getActionMap()
                {
                    Map m = new HashMap();
                    m.put(menu, Arrays.asList(actions));

                    return m;
                }
            };

        return plugin;
    }

    /**
     * @param tb
     * @param actions
     * @return
     */
    public static ToolBarPluginIF createToolbarExtension(final String tb,
        final Object[] actions)
    {
        // the toolbar extension
        ToolBarPluginIF toolbar = new ToolBarPluginIF()
            {
                public String[] getToolBarNames()
                {
                    return new String[] { tb };
                }

                public Map getActions()
                {
                    Map m = new HashMap();
                    m.put(tb, Arrays.asList(actions));

                    return m;
                }
            };

        return toolbar;
    }

    private static final Action createDisabledAction(String text)
    {
        Action a = new AbstractAction(text)
            {
                public void actionPerformed(ActionEvent e)
                {
                }
            };

        a.setEnabled(false);

        return a;
    }

    /**
     * Application's entry point
     * @param args  command line arguments
     * @throws java.lang.Exception An exception
     */
    public static void main(final String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // initialize the settings
            SettingsModuleIF settings = DefaultSettingsModuleImpl.getInstance();
            settings.init();
            settings.start();

            showSplash = XPontusConfig.getValue(
                    "xpontus.showSplashScreenOnStartup").toString()
                                      .equals("true");

            if (showSplash)
            {
                SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            splash = new SplashScreen();
                            splash.setLocationRelativeTo(splash.getOwner());
                            splash.setVisible(true);
                        }
                    });
            }

            XPontusPluginManager controller = new XPontusPluginManager();

            controller.startApplication();

            final XPontusTopComponentIF window = DefaultXPontusWindowImpl.getInstance();

            String[] pluginIdentifiers = 
                {
                    IOCPlugin.PLUGIN_IDENTIFIER, ThemePlugin.PLUGIN_IDENTIFIER,
                    MenuBarPlugin.PLUGIN_IDENTIFIER,
                    LexerPlugin.PLUGIN_IDENTIFIER,
                    ToolBarPlugin.PLUGIN_IDENTIFIER,
                    IndentationPlugin.PLUGIN_IDENTIFIER,
                    DocumentationPlugin.PLUGIN_IDENTIFIER,
                    ScenarioPlugin.PLUGIN_IDENTIFIER,
                    EvaluatorPlugin.PLUGIN_IDENTIFIER,
                    CodeCompletionPlugin.PLUGIN_IDENTIFIER,
                    QuickToolBarPlugin.PLUGIN_IDENTIFIER,
                    OutlinePlugin.PLUGIN_IDENTIFIER,
                    PreviewPlugin.PLUGIN_IDENTIFIER,
                    PreferencesPlugin.PLUGIN_IDENTIFIER,
                    PerspectivePlugin.PLUGIN_IDENTIFIER
                };

            PluginManager manager = XPontusPluginManager.getPluginManager();

            // init plugins
            for (String pluginIdentifier : pluginIdentifiers)
            {
                XPontusPlugin plugin = (XPontusPlugin) manager.getPlugin(pluginIdentifier);
                plugin.init();
            }

            // get some plugins
            MenuBarPlugin menubarPlugin = (MenuBarPlugin) manager.getPlugin(MenuBarPlugin.PLUGIN_IDENTIFIER);

            ToolBarPlugin toolbarPlugin = (ToolBarPlugin) manager.getPlugin(ToolBarPlugin.PLUGIN_IDENTIFIER);

            final IOCPlugin iocPlugin = (IOCPlugin) manager.getPlugin(IOCPlugin.PLUGIN_IDENTIFIER);

            DefaultXPontusWindowImpl.getInstance().setIOCContainer(iocPlugin);

            if (iocPlugin.getContainer() != null)
            {
                final String[] actions = 
                    {
                        CreateNewFileActionImpl.BEAN_ALIAS,
                        "action.fromtemplate", OpenActionImpl.BEAN_ALIAS,
                        ReloadActionImpl.BEAN_ALIAS, SaveActionImpl.BEAN_ALIAS,
                        SaveAsActionImpl.BEAN_ALIAS, PrintActionImpl.BEAN_ALIAS,
                        ExitActionImpl.BEAN_ALIAS
                    };

                final Object[] actionsList = new Object[actions.length];

                for (int j = 0; j < actions.length; j++)
                {
                    actionsList[j] = iocPlugin.getBean(actions[j]);
                }

                final String[] viewActions = 
                    {
                        ViewToolbarActionImpl.BEAN_ALIAS,
                        ViewOutlineWindowActionImpl.BEAN_ALIAS,
                        ViewMessagesWindowActionImpl.BEAN_ALIAS,
                        ViewXPathWindowActionImpl.BEAN_ALIAS
                    };
                final Object[] viewActionsList = new Object[viewActions.length];

                for (int i = 0; i < viewActions.length; i++)
                {
                    viewActionsList[i] = iocPlugin.getBean(viewActions[i]);
                }

                final String[] editActions = 
                    {
                        SelectAllActionImpl.BEAN_ALIAS,
                        CopyActionImpl.BEAN_ALIAS, CutActionImpl.BEAN_ALIAS,
                        PasteActionImpl.BEAN_ALIAS, UndoActionImpl.BEAN_ALIAS,
                        RedoActionImpl.BEAN_ALIAS, "action.findreplace",
                        "action.gotoline"
                    };

                final Object[] validateActionsList = new Object[]
                    {
                        iocPlugin.getBean(SimpleValidationActionImpl.BEAN_ALIAS)
                    };

                final String[] toolsActions = 
                    {
                        CheckXMLActionImpl.BEAN_ALIAS,
                        IndentContentActionImpl.BEAN_ALIAS, "action.docgen"
                    };

                final String[] helpActions = { "action.about", "action.help" };
                final Object[] helpActionsList = new Object[helpActions.length];

                for (int i = 0; i < helpActions.length; i++)
                {
                    helpActionsList[i] = iocPlugin.getBean(helpActions[i]);
                }

                final String[] scenariosActions = 
                    {
                        "action.scenariomanager", "action.scenariorunner"
                    };
                final Object[] scenariosActionsList = new Object[scenariosActions.length];

                for (int i = 0; i < scenariosActionsList.length; i++)
                {
                    scenariosActionsList[i] = iocPlugin.getBean(scenariosActions[i]);
                }

                final Object[] toolsActionsList = new Object[toolsActions.length];

                for (int i = 0; i < toolsActionsList.length; i++)
                {
                    toolsActionsList[i] = iocPlugin.getBean(toolsActions[i]);
                }

                final Object[] editActionsList = new Object[editActions.length];

                for (int j = 0; j < editActions.length; j++)
                {
                    editActionsList[j] = iocPlugin.getBean(editActions[j]);
                }

                Object[] optionsActionsList = 
                    {
                        iocPlugin.getBean("action.preferences")
                    };

                MenuBarPluginIF optionsMenuExt = createMenuExtension(XPontusMenuConstantsIF.OPTIONS_MENU_ID,
                        optionsActionsList);

                // the menu extension
                MenuBarPluginIF fileMenuExt = createMenuExtension(XPontusMenuConstantsIF.FILE_MENU_ID,
                        actionsList);

                MenuBarPluginIF editMenuExt = createMenuExtension(XPontusMenuConstantsIF.EDIT_MENU_ID,
                        editActionsList);

                MenuBarPluginIF viewMenuExt = createMenuExtension(XPontusMenuConstantsIF.VIEW_MENU_ID,
                        viewActionsList);

                MenuBarPluginIF bindingsMenuExt = new MenuBarPluginIF()
                    {
                        public List getMenuNames()
                        {
                            return Arrays.asList(new String[]
                                {
                                    XPontusMenuConstantsIF.KEYBINDINGS_MENU_ID
                                });
                        }

                        public Map getActionMap()
                        {
                            Map<String, List<Action>> m = new HashMap<String, List<Action>>();
                            String id = XPontusMenuConstantsIF.KEYBINDINGS_MENU_ID;
                            List<Action> li = new ArrayList<Action>();

                            li.add(createDisabledAction(
                                    "Quick Search (Firefox style) - Control+Q"));
                            li.add(createDisabledAction(
                                    "Next document - Control+PageUp"));
                            li.add(createDisabledAction(
                                    "Previous document - Control+PageDown"));

                            m.put(id, li);

                            return m;
                        }
                    };

                MenuBarPluginIF toolMenuExt = new MenuBarPluginIF()
                    {
                        public List getMenuNames()
                        {
                            return Arrays.asList(new String[]
                                {
                                    XPontusMenuConstantsIF.TOOLS_MENU_ID
                                });
                        }

                        public Map getActionMap()
                        {
                            Map<String, List<Object>> m = new HashMap<String, List<Object>>();
                            String id = XPontusMenuConstantsIF.TOOLS_MENU_ID;
                            List<Object> li = new ArrayList<Object>();

                            li.add(validateActionsList[0]);
                            li.addAll(Arrays.asList(toolsActionsList));
                            li.addAll(Arrays.asList(scenariosActionsList));

                            m.put(id, li);

                            return m;
                        }
                    };

                // the menu extension
                MenuBarPluginIF helpMenuExt = createMenuExtension(XPontusMenuConstantsIF.HELP_MENU_ID,
                        helpActionsList);

                menubarPlugin.initExtension(fileMenuExt);
                menubarPlugin.initExtension(bindingsMenuExt);
                menubarPlugin.initExtension(helpMenuExt);
                menubarPlugin.initExtension(toolMenuExt);
                menubarPlugin.initExtension(optionsMenuExt);
                menubarPlugin.initExtension(editMenuExt);
                menubarPlugin.initExtension(viewMenuExt);

                JMenu recentFilesMenu = new JMenu("Recent files");
                AbstractXPontusActionImpl rAction = new RecentFilesActionImpl(recentFilesMenu);
                recentFilesMenu.addActionListener(rAction);

                JMenu parent = menubarPlugin.getOrCreateMenu(XPontusMenuConstantsIF.FILE_MENU_ID);
                parent.add(recentFilesMenu);

                // the toolbar extension
                ToolBarPluginIF fileToolBarExt = createToolbarExtension(XPontusToolbarConstantsIF.TB_GENERAL,
                        actionsList);

                ToolBarPluginIF editToolbarExt = createToolbarExtension(XPontusToolbarConstantsIF.TB_EDIT,
                        editActionsList);

                ToolBarPluginIF toolsToolbarExt = createToolbarExtension(XPontusToolbarConstantsIF.TB_TOOLS,
                        toolsActionsList);

                ToolBarPluginIF scenariosToolbarExt = createToolbarExtension(XPontusToolbarConstantsIF.TB_SCENARIOS,
                        scenariosActionsList);

                ToolBarPluginIF helpToolbarExt = createToolbarExtension(XPontusToolbarConstantsIF.TB_HELP,
                        helpActionsList);

                toolbarPlugin.initExtension(fileToolBarExt);

                toolbarPlugin.initExtension(editToolbarExt);

                toolbarPlugin.initExtension(helpToolbarExt);
                toolbarPlugin.initExtension(toolsToolbarExt);

                toolbarPlugin.initExtension(createToolbarExtension(
                        XPontusToolbarConstantsIF.TB_VALIDATION,
                        validateActionsList));

                toolbarPlugin.initExtension(scenariosToolbarExt);

                if (EvaluatorPluginConfiguration.getInstance().getEngines()
                                                    .size() > 0)
                {
                    toolbarPlugin.getOrCreateToolBar("xpath")
                                 .add(new ExpressionEvaluatorPanel());
                }

                ((XPontusPlugin) manager.getPlugin(ActionPlugin.PLUGIN_IDENTIFIER)).init();
            }

            if (DocConfiguration.getInstane().getEnginesNames().length == 0)
            {
                Action m_action = (Action) iocPlugin.getBean("action.docgen");
                m_action.setEnabled(false);
                m_action.putValue(Action.SHORT_DESCRIPTION,
                    "Install some plugins in the category documentation");
            }

            DocumentAwareComponentHolder.getInstance()
                                        .notifyComponents(new DocumentContainerChangeEvent(
                    null));

            SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        if (showSplash)
                        {
                            try
                            {
                                Thread.sleep(800);
                                splash.dispose();
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }

                        window.activateComponent();

                        if (args.length > 0)
                        {
                            for (String arg : args)
                            {
                                try
                                {
                                    File f = new File(arg);

                                    if (f.exists())
                                    {
                                        DefaultXPontusWindowImpl.getInstance()
                                                                .getDocumentTabContainer()
                                                                .createEditorFromFile(f);
                                    }
                                }
                                catch (Exception e)
                                {
                                }
                            }
                        }
                    }
                });
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
    }
}
