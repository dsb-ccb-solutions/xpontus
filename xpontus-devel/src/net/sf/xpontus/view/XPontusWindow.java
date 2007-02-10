/*
 * XPontusForm2.java
 *
 * Created on February 11, 2006, 9:48 PM
 */
package net.sf.xpontus.view;

 
import com.vlsolutions.swing.docking.*;
import com.vlsolutions.swing.toolbars.*;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import com.vlsolutions.swing.toolbars.VLToolBar;
import java.awt.event.ActionListener;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.controller.actions.dockables.ManageDockableAction;
import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.core.utils.BeanContainer;
import net.sf.xpontus.core.utils.MessageProvider;
import net.sf.xpontus.core.utils.WindowUtilities;
import net.sf.xpontus.view.components.JStatusBar;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Hashtable;

import javax.swing.*;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


/**
 *
 * @author Yves Zoundi
 */
public class XPontusWindow {
    private static XPontusWindow _instance;
    private EditorTabContainer tabContainer;
    public static boolean splashenabled = true;
    private SplashScreenView splash;
    private XPathToolBarComponent xpathTb;
    private Hashtable menuMap = new Hashtable();
    private BeanContainer applicationContext;
    private DockingDesktop desk = new DockingDesktop();
    private JMenu editMenu;
    private JMenu fileMenu;
    private JMenu formatMenu;
    private JFrame frame;
    private JMenu helpMenu;
    private JMenuBar menubar;
    private PaneForm pane;

    // private JScrollPane scrollPane;
    private net.sf.xpontus.view.components.JStatusBar statusbar;
    private ToolBarContainer toolbar;
    private JMenu toolsMenu;
    private MessageProvider messageSource;
    private ConsoleOutputWindow console; 
private OutlineViewDockable outlineDockable;


	public OutlineViewDockable getOutlineDockable(){
		return outlineDockable;
	}
    /** Creates new XPontusWindow */
    private XPontusWindow() {
        if (messageSource == null) {
            messageSource = MessageProvider.getinstance();
        }
    }
    
    public EditorTabContainer getTabContainer(){
    	return tabContainer;
    }

    public void maximizeDock(Dockable dockable) {
        int state = desk.getDockableState(dockable).getState();

        if (state != DockableState.STATE_MAXIMIZED) {
            desk.maximize(dockable);
        } else {
            desk.restore(dockable);
        }
    }

    /** Creates new XPontusWindow */
    public void init() {
        if (splashenabled) {
            splash = new SplashScreenView();
            WindowUtilities.centerOnScreen(splash);
            splash.setVisible(true);
        }
    }

    public void initWindow() {
        initComponents();
        initDock();
    }

    public DockingDesktop getDockingDesktop() {
        return desk;
    }

    public void initDock() {
        String[] mTitles = {
                getI18nMessage("dockwindow.editor.title"),
                getI18nMessage("dockwindow.output.title")
            };
        console = new ConsoleOutputWindow();
        tabContainer = new EditorTabContainer();
        //CompoundDockable compound = new CompoundDockable(new DockKey("Compound!"));
        // desk.addDockable(compound); 
        desk.addDockable(pane);

        outlineDockable = new OutlineViewDockable();
        
        desk.registerDockable(outlineDockable);
       desk.registerDockable(console.getDockables()[0]);
        desk.split(pane, outlineDockable, DockingConstants.SPLIT_LEFT);
        desk.split(pane, console.getDockables()[0],
            DockingConstants.SPLIT_BOTTOM);

        for (int i = 1; i < 3; i++) {
        	desk.registerDockable(console.getDockables()[i-1]);
            desk.createTab(console.getDockables()[i - 1],
                console.getDockables()[i], i);
        }

        //  desk.addDockable(compound , new JTreeDockable()) ;
        frame.getContentPane().add(desk, BorderLayout.CENTER);
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);
        frame.getContentPane().add(statusbar, BorderLayout.SOUTH);
    }

    /**
     *
     * @return
     */
    public static XPontusWindow getInstance() {
        if (_instance == null) {
            _instance = new XPontusWindow();
        }

        return _instance;
    }

    public ConsoleOutputWindow getConsole() {
        return console;
    }

    /**
     *
     */
    public void initActions() {
        configureMenu(fileMenu, XPontusConstants.fileActions);
        configureMenu(editMenu, XPontusConstants.editActions);
        configureMenu(helpMenu, XPontusConstants.helpActions);
        configureMenu(formatMenu, XPontusConstants.formatActions);
        configureMenu(toolsMenu, XPontusConstants.toolsActions);

        VLToolBar tb = createToolBar(new String[] {
                    "action.new", "action.open", "action.openremote",
                    "action.print", "action.save", "action.saveas",
                    "action.saveall"
                }, "File actions");

        toolbar.registerToolBar(tb);

        ToolBarPanel tbPanel = toolbar.getToolBarPanelAt(BorderLayout.NORTH);

        tbPanel.add(tb, new ToolBarConstraints(0, 0));

        tb = createToolBar(new String[] {
                    "action.cut", "action.copy", "action.paste", "action.undo",
                    "action.redo", "action.find", "action.gotoline"
                }, "Edit actions");

        toolbar.registerToolBar(tb);

        tbPanel.add(tb, new ToolBarConstraints(0, 1));

        tb = createToolBar(new String[] {
                    "action.preferences", "action.about", "action.help"
                }, "About actions");

        toolbar.registerToolBar(tb);

        tbPanel.add(tb, new ToolBarConstraints(0, 2));

        tb = createToolBar(new String[] {
                    "action.commentxml", "action.indentxml", "action.tidy"
                }, "XML/XHTML actions");

        toolbar.registerToolBar(tb);

        tbPanel.add(tb, new ToolBarConstraints(1, 0));

        tb = createToolBar(new String[] {
                    "action.checkxml", "action.validate",
                    "action.validateschema"
                }, "Validation actions");

        toolbar.registerToolBar(tb);

        tbPanel.add(tb, new ToolBarConstraints(1, 1));

        tb = createToolBar(new String[] {
                    "action.list_scenarios", "action.execute_scenarios"
                }, "Scenario actions");

        toolbar.registerToolBar(tb);

        tbPanel.add(tb, new ToolBarConstraints(1, 2));

        xpathTb = new XPathToolBarComponent();
        xpathTb.setXPathAction((Action) this.applicationContext.getBean(
                "action.xpath"));

        toolbar.registerToolBar(xpathTb);

        tbPanel.add(xpathTb, new ToolBarConstraints(1, 3));
    }

    public XPathToolBarComponent getXPathToolBar() {
        return this.xpathTb;
    }

    /**
     *
     * @param actions
     * @return
     */
    private VLToolBar createToolBar(String[] actions, String name) {
        VLToolBar tb = new VLToolBar(name); 

        for (int i = 0; i < actions.length; i++) {
            if (actions[i].equals("-")) {
                tb.addSeparator();
            } else {
                javax.swing.JButton button = new javax.swing.JButton((Action) applicationContext.getBean(
                            actions[i]));
                button.setText(null);
                tb.add(button);
            }
        }

        return tb;
    }

    public void updateLineInfo(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    statusbar.updateLineInfo(msg);
                }
            });
    }

    /**
     *
     * @param menu
     * @param actions
     */
    private void configureMenu(JMenu menu, String[] actions) {
        for (int i = 0; i < actions.length; i++) {
            if (actions[i].equals("-")) {
                menu.addSeparator();
            } else {
                menu.add((Action) applicationContext.getBean(actions[i]));
            }
        }
    }

    private void initComponents() {
        frame = new JFrame() {
                    public void setVisible(boolean b) {
                        if (splashenabled) {
                            if (splash.isShowing()) {
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception err) {
                                }

                                splash.setVisible(false);
                            }
                        }

                        super.setVisible(b);
                    }
                };
        toolbar = ToolBarContainer.createDefaultContainer(true, true, true, true);
        statusbar = new net.sf.xpontus.view.components.JStatusBar();
        menubar = new JMenuBar();
        fileMenu = new JMenu();
        editMenu = new JMenu();
        toolsMenu = new JMenu();
        formatMenu = new JMenu();
        helpMenu = new JMenu();

        JMenu viewMenu = new JMenu("View");
        viewMenu.add(new JCheckBoxMenuItem("Outline", true));
        viewMenu.add(new JCheckBoxMenuItem("Messages", true));
        viewMenu.add(new JCheckBoxMenuItem("XPath", true));
        viewMenu.add(new JCheckBoxMenuItem("Errors", true));
        
        ActionListener dockListener = new ManageDockableAction();
        
        for(int i=0;i<viewMenu.getItemCount();i++){
            viewMenu.getItem(i).addActionListener(dockListener);
        }
        pane = new PaneForm();
        // scrollPane = new JScrollPane();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    ((BaseAction) applicationContext.getBean("action.exit")).execute();
                }
            });

        fileMenu.setText(getI18nMessage("menu.file.name"));
        menubar.add(fileMenu);

        editMenu.setText(getI18nMessage("menu.edit.name"));
        menubar.add(editMenu);

        menubar.add(viewMenu);

        toolsMenu.setText(getI18nMessage("menu.tools.name"));
        menubar.add(toolsMenu);

        formatMenu.setText(getI18nMessage("menu.format.name"));
        menubar.add(formatMenu);

        helpMenu.setText(getI18nMessage("menu.help.name"));
        menubar.add(helpMenu);

        frame.setJMenuBar(menubar);

        // pane.setMinimumSize(new java.awt.Dimension(500, 350));
        // pane.setPreferredSize(new java.awt.Dimension(500, 350));
    }

    /**
     * return the editor's tab container
     *
     * @return The editor's tab container
     */
    public PaneForm getPane() {
        return (PaneForm) this.pane;
    }

    /**
     *
     * @return The application's statusbar
     */
//    public JComponent getStatusbar() {
//        return statusbar;
//    }

    /**
     *
     * @param msg
     */
    public void setMessage(String msg) {
        statusbar.setMessage(msg);
    }

    /**
     *
     * @return
     */
    public JEditorPane getCurrentEditor() {
        return tabContainer.getCurrentEditor();
    }

    /**
     *
     * @param msg
     */
    public void append(int consoleid, String msg) {
        console.println(consoleid, msg);
    }

    /**
     *
     * @return
     */
    public BeanContainer getApplicationContext() {
        return applicationContext;
    }

    /**
     *
     *
     * @param applicationContext
     */
    public void setApplicationContext(BeanContainer applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     *
     * @return
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     *
     * @return
     */
    public JStatusBar getStatusBar() {
        return this.statusbar;
    }

    public String getI18nMessage(String key) {
        return messageSource.getMessage(key);
    }

    public void setMessageSource(MessageProvider messageSource) {
        this.messageSource = messageSource;
    }

    public Hashtable getMenuMap() {
        return menuMap;
    }

    public void createMenuMap() {
        menuMap.put(XPontusConstants.FILE_MENU_ID, fileMenu);
        menuMap.put(XPontusConstants.EDIT_MENU_ID, editMenu);
        menuMap.put(XPontusConstants.FORMAT_MENU_ID, formatMenu);
        menuMap.put(XPontusConstants.TOOLS_MENU_ID, toolsMenu);
        menuMap.put(XPontusConstants.HELP_MENU_ID, helpMenu);
    }

    public JMenuBar getMenuBar() {
        return menubar;
    }

    public JComponent getToolBar() {
        return toolbar;
    }
}
