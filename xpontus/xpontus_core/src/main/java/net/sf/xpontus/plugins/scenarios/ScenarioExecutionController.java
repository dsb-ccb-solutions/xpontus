/*
 * ScenarioExecutionController.java
 *
 * Created on Aug 26, 2007, 9:34:46 AM
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
 */
package net.sf.xpontus.plugins.scenarios;

import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Toolkit;


/**
 * Controller to handle scenario profile execution
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class ScenarioExecutionController {
    public static final String CLOSE_METHOD = "closeWindow";
    public static final String EXECUTE_METHOD = "execute";
    private Log logger = LogFactory.getLog(ScenarioExecutionController.class);
    private ScenarioExecutionView view;

    /**
     *
     * @param view
     */
    public ScenarioExecutionController(ScenarioExecutionView view) {
        this.view = view;
    }

    /**
     *
     */
    public void closeWindow() {
        view.setVisible(false);
    }

    
    /**
     * 
     */
    public void execute() {
        if (view.getScenarioListModel().getSize() == 0) {
            XPontusComponentsUtils.showWarningMessage(
                "No transformation profile to run");
        } else if (view.getScenarioList().getSelectedIndex() == -1) {
            XPontusComponentsUtils.showWarningMessage(
                "Please select a transformation profile to run");
        } else {
            new Thread(new Runnable() {
                    public void run() {
                        view.setVisible(false);

                        DetachableScenarioModel dsm = (DetachableScenarioModel) view.getScenarioListModel()
                                                                                    .getSelectedItem();

                        String proc = dsm.getProcessor();

                        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                              .getConsole();

                        OutputDockable odk = (OutputDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);

                        try {
                            logger.info("Beginning the transformation");

                            // get the plugin for this transformation
                            ScenarioPluginIF m_plugin = ScenarioPluginsConfiguration.getInstance()
                                                                                    .getEngineForName(proc);

                            logger.info("Transformation the profile");

                            // run the transformation profile
                            m_plugin.handleScenario(dsm);

                            // preview the result of the transformation
                            if (dsm.isPreview()) {
                                logger.info("Previewing the transformation");
                            }

                            logger.info("Transformation done");
                        } catch (Exception e) {
                            logger.fatal("An error occured...");

                            // print the error
                            logger.fatal(e.getMessage());
                            odk.println(e.getMessage(), OutputDockable.RED_STYLE);
                        } finally {
                            console.setFocus(MessagesWindowDockable.DOCKABLE_ID);
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                }).start();
        }
    }
}
