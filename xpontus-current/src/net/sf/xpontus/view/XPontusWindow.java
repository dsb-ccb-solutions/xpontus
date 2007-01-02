/*
 * XPontusForm2.java
 *
 * Created on February 11, 2006, 9:48 PM
 */
package net.sf.xpontus.view;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.ViewMap;

import net.infonode.util.Direction;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.core.utils.BeanContainer;
import net.sf.xpontus.core.utils.MessageProvider;
import net.sf.xpontus.core.utils.WindowUtilities;
import net.sf.xpontus.view.components.JStatusBar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
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
public class XPontusWindow
  {
    private static XPontusWindow _instance;
    public static boolean splashenabled = true;
    private SplashScreenView splash;
    private RootWindow rootWindow;
    private View[] views = new View[2];
    private ViewMap viewMap = new ViewMap();
    private XPathToolBarComponent xpathTb;
    private Hashtable menuMap = new Hashtable();
    private BeanContainer applicationContext;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JMenu editMenu;
    private JMenu fileMenu;
    private JMenu formatMenu;
    private JFrame frame;
    private JMenu helpMenu;
    private JMenuBar menubar;
    private JTabbedPane pane;

    // private JScrollPane scrollPane;
    private net.sf.xpontus.view.components.JStatusBar statusbar;
    private JPanel toolbar;
    private JMenu toolsMenu;

    // End of variables declaration//GEN-END:variables
    private MessageProvider messageSource;
    private ConsoleOutputWindow console;

    /** Creates new XPontusWindow */
    private XPontusWindow()
      {
        if (messageSource == null)
          {
            messageSource = MessageProvider.getinstance();
          }
      }

    /** Creates new XPontusWindow */
    public void init()
      {
        if (splashenabled)
          {
            splash = new SplashScreenView();
            WindowUtilities.centerOnScreen(splash);
            splash.setVisible(true);
          }
      }

    public void initWindow()
      {
        initComponents();
        initDock();
      }

    private View createView(String mTitle, Component mComponent)
      {
        View mView = new View(mTitle, null, mComponent)
              {
                public boolean isClosable()
                  {
                    return false;
                  }

                public boolean isUndockable()
                  {
                    return false;
                  }
              };

        return mView;
      }

    public void initDock()
      {
        String[] mTitles = 
            {
                getI18nMessage("dockwindow.editor.title"),
                getI18nMessage("dockwindow.output.title")
            };
        console = new ConsoleOutputWindow();

        Dimension dim = new Dimension(100, 50);
        console.setPreferredSize(dim);
        console.setMinimumSize(dim);

        Component[] mComponents = { pane, console };

        for (int i = 0; i < views.length; i++)
          {
            views[i] = createView(mTitles[i], mComponents[i]);
            viewMap.addView(i, views[i]);
          }

        MixedViewHandler handler = new MixedViewHandler(viewMap,
                new ViewSerializer()
                  {
                    public void writeView(View view, ObjectOutputStream out)
                        throws IOException
                      {
                        out.writeBytes(view.getName());
                      }

                    public View readView(ObjectInputStream in)
                        throws IOException
                      {
                        try
                          {
                            return (View) in.readObject();
                          }
                        catch (ClassNotFoundException e)
                          {
                            // TODO Auto-generated catch block
                            return null;
                          }
                      }
                  });
        rootWindow = DockingUtil.createRootWindow(viewMap, handler, true);

        rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
        // TabWindow tabWindow = new TabWindow(views);
        rootWindow.setWindow(createSplitWinddow(views[0], views[1]));

        frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
      }

    public DockingWindow createSplitWinddow(View view1, View view2)
      {
        return new SplitWindow(false, 0.7f, view1, view2)
              {
                public boolean isClosable()
                  {
                    return false;
                  }

                public boolean isUndockable()
                  {
                    return false;
                  }
              };
      }

    /**
     *
     * @return
     */
    public static XPontusWindow getInstance()
      {
        if (_instance == null)
          {
            _instance = new XPontusWindow();
          }

        return _instance;
      }

    public ConsoleOutputWindow getConsole()
      {
        return console;
      }

    /**
     *
     */
    public void initActions()
      {
        configureMenu(fileMenu, XPontusConstants.fileActions);
        configureMenu(editMenu, XPontusConstants.editActions);
        configureMenu(helpMenu, XPontusConstants.helpActions);
        configureMenu(formatMenu, XPontusConstants.formatActions);
        configureMenu(toolsMenu, XPontusConstants.toolsActions);

        JPanel p1;
        JPanel p2;
        p1 = new JPanel();
        p2 = new JPanel();

        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));

        java.awt.LayoutManager l = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);

        p1.setLayout(l);
        p2.setLayout(l);

        p1.add(createToolBar(
                new String[]
                {
                    "action.new", "action.open", "action.openremote",
                    "action.print", "action.save", "action.saveas",
                    "action.saveall"
                }));
        p1.add(createToolBar(
                new String[]
                {
                    "action.cut", "action.copy", "action.paste", "action.undo",
                    "action.redo", "action.find", "action.gotoline"
                }));
        p1.add(createToolBar(
                new String[] { "action.preferences", "action.about", "action.help" }));
        p2.add(createToolBar(
                new String[]
                {
                    "action.commentxml", "action.indentxml", "action.tidy"
                }));
        p2.add(createToolBar(
                new String[]
                {
                    "action.checkxml", "action.validate",
                    "action.validateschema"
                }));
        p2.add(createToolBar(
                new String[] { "action.list_scenarios", "action.execute_scenarios" }));
        xpathTb = new XPathToolBarComponent();
        xpathTb.setXPathAction((Action) this.applicationContext.getBean(
                "action.xpath"));
        p2.add(xpathTb);
        toolbar.add(p1);
        toolbar.add(p2);
      }

    public XPathToolBarComponent getXPathToolBar()
      {
        return this.xpathTb;
      }

    /**
     *
     * @param actions
     * @return
     */
    private JToolBar createToolBar(String[] actions)
      {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setRollover(true);

        for (int i = 0; i < actions.length; i++)
          {
            if (actions[i].equals("-"))
              {
                tb.addSeparator();
              }
            else
              {
                tb.add((Action) applicationContext.getBean(actions[i]));
              }
          }

        return tb;
      }

    public void updateLineInfo(final String msg)
      {
        SwingUtilities.invokeLater(new Runnable()
              {
                public void run()
                  {
                    statusbar.updateLineInfo(msg);
                  }
              });
      }

    /**
     *
     * @param menu
     * @param actions
     */
    private void configureMenu(JMenu menu, String[] actions)
      {
        for (int i = 0; i < actions.length; i++)
          {
            if (actions[i].equals("-"))
              {
                menu.addSeparator();
              }
            else
              {
                menu.add((Action) applicationContext.getBean(actions[i]));
              }
          }
      }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // ">//GEN-BEGIN:initComponents
    private void initComponents()
      {
        frame = new JFrame()
                  {
                    public void setVisible(boolean b)
                      {
                        if (splashenabled)
                          {
                            if (splash.isShowing())
                              {
                                try
                                  {
                                    Thread.sleep(2000);
                                  }
                                catch (Exception err)
                                  {
                                  }

                                splash.setVisible(false);
                              }
                          }

                        super.setVisible(b);
                      }
                  };
        toolbar = new JPanel();
        statusbar = new net.sf.xpontus.view.components.JStatusBar();
        menubar = new JMenuBar();
        fileMenu = new JMenu();
        editMenu = new JMenu();
        toolsMenu = new JMenu();
        formatMenu = new JMenu();
        helpMenu = new JMenu();
        pane = new PaneForm();
        // scrollPane = new JScrollPane();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter()
              {
                public void windowClosing(java.awt.event.WindowEvent evt)
                  {
                    onWindowClosing(evt);
                  }
              });

        frame.getContentPane().add(toolbar, java.awt.BorderLayout.NORTH);

        frame.getContentPane().add(statusbar, java.awt.BorderLayout.SOUTH);

        fileMenu.setText(getI18nMessage("menu.file.name"));
        menubar.add(fileMenu);

        editMenu.setText(getI18nMessage("menu.edit.name"));
        menubar.add(editMenu);

        toolsMenu.setText(getI18nMessage("menu.tools.name"));
        menubar.add(toolsMenu);

        formatMenu.setText(getI18nMessage("menu.format.name"));
        menubar.add(formatMenu);

        helpMenu.setText(getI18nMessage("menu.help.name"));
        menubar.add(helpMenu);

        frame.setJMenuBar(menubar);

        pane.setMinimumSize(new java.awt.Dimension(700, 350));
        pane.setPreferredSize(new java.awt.Dimension(700, 350));
      } // </editor-fold>//GEN-END:initComponents

    private void onWindowClosing(java.awt.event.WindowEvent evt)
      { // GEN-FIRST:event_onWindowClosing
        ((BaseAction) applicationContext.getBean("action.exit")).execute();
      } // GEN-LAST:event_onWindowClosing

    /**
     * return the editor's tab container
     *
     * @return The editor's tab container
     */
    public PaneForm getPane()
      {
        return (PaneForm) this.pane;
      }

    /**
     *
     * @return The application's statusbar
     */
    public JComponent getStatusbar()
      {
        return statusbar;
      }

    /**
     *
     * @param msg
     */
    public void setMessage(String msg)
      {
        statusbar.setMessage(msg);
      }

    /**
     *
     * @return
     */
    public JEditorPane getCurrentEditor()
      {
        return ((PaneForm) pane).getCurrentEditor();
      }

    /**
     *
     * @param msg
     */
    public void append(int consoleid, String msg)
      {
        console.println(consoleid, msg);
      }

    /**
     *
     * @return
     */
    public BeanContainer getApplicationContext()
      {
        return applicationContext;
      }

    /**
     *
     *
     * @param applicationContext
     */
    public void setApplicationContext(BeanContainer applicationContext)
      {
        this.applicationContext = applicationContext;
      }

    /**
     *
     * @return
     */
    public JFrame getFrame()
      {
        return frame;
      }

    /**
     *
     * @return
     */
    public JStatusBar getStatusBar()
      {
        return this.statusbar;
      }

    public String getI18nMessage(String key)
      {
        return messageSource.getMessage(key);
      }

    public void setMessageSource(MessageProvider messageSource)
      {
        this.messageSource = messageSource;
      }

    public Hashtable getMenuMap()
      {
        return menuMap;
      }

    public void createMenuMap()
      {
        menuMap.put(XPontusConstants.FILE_MENU_ID, fileMenu);
        menuMap.put(XPontusConstants.EDIT_MENU_ID, editMenu);
        menuMap.put(XPontusConstants.FORMAT_MENU_ID, formatMenu);
        menuMap.put(XPontusConstants.TOOLS_MENU_ID, toolsMenu);
        menuMap.put(XPontusConstants.HELP_MENU_ID, helpMenu);
      }

    public JMenuBar getMenuBar()
      {
        return menubar;
      }

    public JComponent getToolBar()
      {
        return toolbar;
      }
  }
