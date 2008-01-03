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

import net.sf.xpontus.actions.impl.CheckXMLActionImpl;
import net.sf.xpontus.actions.impl.CopyActionImpl;
import net.sf.xpontus.actions.impl.CreateNewFileActionImpl;
import net.sf.xpontus.actions.impl.CutActionImpl;
import net.sf.xpontus.actions.impl.ExitActionImpl;
import net.sf.xpontus.actions.impl.IndentContentActionImpl;
import net.sf.xpontus.actions.impl.OpenActionImpl;
import net.sf.xpontus.actions.impl.PasteActionImpl;
import net.sf.xpontus.actions.impl.PrintActionImpl;
import net.sf.xpontus.actions.impl.RedoActionImpl;
import net.sf.xpontus.actions.impl.SaveActionImpl;
import net.sf.xpontus.actions.impl.SaveAsActionImpl;
import net.sf.xpontus.actions.impl.SelectAllActionImpl;
import net.sf.xpontus.actions.impl.UndoActionImpl;
import net.sf.xpontus.constants.XPontusMenuConstantsIF;
import net.sf.xpontus.constants.XPontusToolbarConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.SplashScreen;
import net.sf.xpontus.modules.gui.components.XPontusTopComponentIF;
import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.plugins.completion.CodeCompletionPlugin;
import net.sf.xpontus.plugins.evaluator.EvaluatorPlugin;
import net.sf.xpontus.plugins.evaluator.ExpressionEvaluatorPanel;
import net.sf.xpontus.plugins.gendoc.DocumentationPlugin;
import net.sf.xpontus.plugins.gui.menubar.MenuBarPlugin;
import net.sf.xpontus.plugins.gui.menubar.MenuBarPluginIF;
import net.sf.xpontus.plugins.gui.toolbar.ToolBarPlugin;
import net.sf.xpontus.plugins.gui.toolbar.ToolBarPluginIF;
import net.sf.xpontus.plugins.indentation.IndentationPlugin;
import net.sf.xpontus.plugins.ioc.IOCPlugin;
import net.sf.xpontus.plugins.lexer.LexerPlugin;
import net.sf.xpontus.plugins.scenarios.ScenarioPlugin;
import net.sf.xpontus.plugins.themes.ThemePlugin;
import net.sf.xpontus.utils.DocumentAwareComponentHolder;
import net.sf.xpontus.utils.DocumentContainerChangeEvent;

import java.awt.*;

import java.io.*;
import java.io.File;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


/**
 * Main class of the program
 * @author Yves Zoundi
 */
public class XPontusRunner {
    private DefaultXPontusWindowImpl m_window;

    private XPontusRunner() {
    }

    public DefaultXPontusWindowImpl getMainWindow() {
        return m_window;
    }

    public void handleArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            File f = new File(args[i]);
            if (f.exists()) {
                m_window.getDocumentTabContainer().createEditorFromFile(f);
            }
        }
    }

    public static MenuBarPluginIF createMenuExtension(final String menu,
        final Object[] actions) {
        MenuBarPluginIF plugin = new MenuBarPluginIF() {
                public List getMenuNames() {
                    return Arrays.asList(new String[] { menu });
                }

                public Map getActionMap() {
                    Map m = new HashMap();
                    m.put(menu, Arrays.asList(actions));

                    return m;
                }
            };

        return plugin;
    }

    public static ToolBarPluginIF createToolbarExtension(final String tb,
        final Object[] actions) {
        // the toolbar extension
        ToolBarPluginIF toolbar = new ToolBarPluginIF() {
                public String[] getToolBarNames() {
                    return new String[] { tb };
                }

                public Map getActions() {
                    Map m = new HashMap();
                    m.put(tb, Arrays.asList(actions));

                    return m;
                }
            };

        return toolbar;
    }

    /**
     * Application's entry point
     * @param args  command line arguments
     * @throws java.lang.Exception An exception
     */
    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.manager", "");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        final JWindow splash = new SplashScreen();
        new Thread(new Runnable() {
                public void run() {
                    splash.setLocationRelativeTo(splash.getOwner());
                    splash.setVisible(true);
                }
            }).start();

        XPontusPluginManager controller = new XPontusPluginManager();

        controller.startApplication();

        XPontusTopComponentIF window = DefaultXPontusWindowImpl.getInstance();

        String[] identifiers = {
                IOCPlugin.PLUGIN_IDENTIFIER, ThemePlugin.PLUGIN_IDENTIFIER,
                MenuBarPlugin.PLUGIN_IDENTIFIER, LexerPlugin.PLUGIN_IDENTIFIER,
                ToolBarPlugin.PLUGIN_IDENTIFIER,
                IndentationPlugin.PLUGIN_IDENTIFIER,
                DocumentationPlugin.PLUGIN_IDENTIFIER,
                ScenarioPlugin.PLUGIN_IDENTIFIER,
                EvaluatorPlugin.PLUGIN_IDENTIFIER,
                CodeCompletionPlugin.PLUGIN_IDENTIFIER
            };

        // init plugins
        for (int i = 0; i < identifiers.length; i++) {
            XPontusPlugin plugin = (XPontusPlugin) controller.getPluginManager()
                                                             .getPlugin(identifiers[i]);
            plugin.init();
        }

        // get some plugins
        MenuBarPlugin menubarPlugin = (MenuBarPlugin) controller.getPluginManager()
                                                                .getPlugin(MenuBarPlugin.PLUGIN_IDENTIFIER);

        ToolBarPlugin toolbarPlugin = (ToolBarPlugin) controller.getPluginManager()
                                                                .getPlugin(ToolBarPlugin.PLUGIN_IDENTIFIER);

        final IOCPlugin iocPlugin = (IOCPlugin) controller.getPluginManager()
                                                          .getPlugin(IOCPlugin.PLUGIN_IDENTIFIER);

        if (iocPlugin.getContainer() != null) {
            final String[] actions = {
                    CreateNewFileActionImpl.BEAN_ALIAS,
                    OpenActionImpl.BEAN_ALIAS, SaveActionImpl.BEAN_ALIAS,
                    SaveAsActionImpl.BEAN_ALIAS, PrintActionImpl.BEAN_ALIAS,
                    ExitActionImpl.BEAN_ALIAS
                };

            final Object[] actionsList = new Object[actions.length];

            for (int j = 0; j < actions.length; j++) {
                actionsList[j] = iocPlugin.getBean(actions[j]);
            }

            final String[] editActions = {
                    SelectAllActionImpl.BEAN_ALIAS, CopyActionImpl.BEAN_ALIAS,
                    CutActionImpl.BEAN_ALIAS, PasteActionImpl.BEAN_ALIAS,
                    UndoActionImpl.BEAN_ALIAS, RedoActionImpl.BEAN_ALIAS,
                    "action.findreplace", "action.gotoline"
                };

            final String[] toolsActions = {
                    CheckXMLActionImpl.BEAN_ALIAS,
                    IndentContentActionImpl.BEAN_ALIAS, "action.docgen"
                };

            final String[] helpActions = { "action.about", "action.help" };
            final Object[] helpActionsList = new Object[helpActions.length];

            for (int i = 0; i < helpActions.length; i++) {
                helpActionsList[i] = iocPlugin.getBean(helpActions[i]);
            }

            final String[] scenariosActions = {
                    "action.scenariomanager", "action.scenariorunner"
                };
            final Object[] scenariosActionsList = new Object[scenariosActions.length];

            for (int i = 0; i < scenariosActionsList.length; i++) {
                scenariosActionsList[i] = iocPlugin.getBean(scenariosActions[i]);
            }

            final Object[] toolsActionsList = new Object[toolsActions.length];

            for (int i = 0; i < toolsActionsList.length; i++) {
                toolsActionsList[i] = iocPlugin.getBean(toolsActions[i]);
            }

            final Object[] editActionsList = new Object[editActions.length];

            for (int j = 0; j < editActions.length; j++) {
                editActionsList[j] = iocPlugin.getBean(editActions[j]);
            }

            // the menu extension
            MenuBarPluginIF fileMenuExt = createMenuExtension(XPontusMenuConstantsIF.FILE_MENU_ID,
                    actionsList);

            MenuBarPluginIF editMenuExt = createMenuExtension(XPontusMenuConstantsIF.EDIT_MENU_ID,
                    editActionsList);

            MenuBarPluginIF toolMenuExt = new MenuBarPluginIF() {
                    public List getMenuNames() {
                        return Arrays.asList(new String[] {
                                XPontusMenuConstantsIF.TOOLS_MENU_ID
                            });
                    }

                    public Map getActionMap() {
                        Map m = new HashMap();
                        String id = XPontusMenuConstantsIF.TOOLS_MENU_ID;
                        List li = new Vector();

                        for (int j = 0; j < toolsActionsList.length; j++) {
                            li.add(toolsActionsList[j]);
                        }

                        for (int j = 0; j < scenariosActionsList.length; j++) {
                            li.add(scenariosActionsList[j]);
                        }

                        m.put(id, li);

                        return m;
                    }
                };

            // the menu extension
            MenuBarPluginIF helpMenuExt = createMenuExtension(XPontusMenuConstantsIF.HELP_MENU_ID,
                    helpActionsList);

            menubarPlugin.initExtension(fileMenuExt);
            menubarPlugin.initExtension(helpMenuExt);
            menubarPlugin.initExtension(toolMenuExt);
            menubarPlugin.initExtension(editMenuExt);

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
            toolbarPlugin.initExtension(toolsToolbarExt);
            toolbarPlugin.initExtension(editToolbarExt);
            toolbarPlugin.initExtension(helpToolbarExt);
            toolbarPlugin.initExtension(scenariosToolbarExt);
            toolbarPlugin.getOrCreateToolBar("xpath")
                         .add(new ExpressionEvaluatorPanel());
        }

        DocumentAwareComponentHolder.getInstance()
                                    .notifyComponents(new DocumentContainerChangeEvent(
                null));

        new Thread(new Runnable() {
                public void run() {
                    splash.dispose();
                }
            }).start();
        System.out.println("Activating the main window");
        window.activateComponent();
        System.out.println("Done activating the main window");
    }
}

