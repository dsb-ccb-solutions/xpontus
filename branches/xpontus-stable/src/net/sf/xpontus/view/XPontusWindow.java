/*
 * XPontusForm2.java
 *
 * Created on February 11, 2006, 9:48 PM
 */

package net.sf.xpontus.view;


import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import com.vlsolutions.swing.toolbars.VLToolBar;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.controller.handlers.RecentFileListActionListener;
import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.core.controller.handlers.PopupListener;
import net.sf.xpontus.core.utils.L10nHelper;
import net.sf.xpontus.core.utils.WindowUtilities;
import net.sf.xpontus.model.options.GeneralOptionModel;
import net.sf.xpontus.view.components.JStatusBar;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;

/**
 *
 * @author  Yves Zoundi
 */
public class XPontusWindow implements ApplicationContextAware{
    
    private static XPontusWindow _instance;
    private boolean splashenabled = true;
    private SplashScreen splash;
    
    /** Creates new XPontusWindow */
    public XPontusWindow() {
        GeneralOptionModel model1 = (GeneralOptionModel)new GeneralOptionModel().load();
        if(model1==null){
            model1 = new GeneralOptionModel();
        }
        splashenabled = model1.isShowSplashScreen();
        if (splashenabled) {
            splash = new SplashScreen();
            WindowUtilities.centerOnScreen(splash);
            splash.setVisible(true);
        }
        recentFileMenu = new JMenu("Recent files");
        
        initComponents();
        
        initDock();
    }
    
    // get the recent files opened in the editor
    public JMenu getRecentFileMenu(){
        return recentFileMenu;
    }
    
    /**
     *
     * @return
     */
    public DockingDesktop getDesktop(){
        return desk;
    }
    
    /**
     *
     */
    public void initDock(){
        desk.addDockable((DockablePaneForm)pane);
        desk.split((DockablePaneForm)pane, (DockableMessageWindow)scrollPane, DockingConstants.SPLIT_BOTTOM);
        frame.getContentPane().add(desk, BorderLayout.CENTER);
    }
    
    /**
     *
     * @return
     */
    public static XPontusWindow getInstance(){
        if(_instance == null){
            _instance = new XPontusWindow();
        }
        return _instance;
    }
    
    /**
     *
     */
    public void initActions(){
        configureMenu(fileMenu, XPontusConstants.fileActions);
        fileMenu.add(recentFileMenu);
        RecentFileListActionListener listener = (RecentFileListActionListener)XPontusWindow.getInstance().getApplicationContext().getBean("recentFilesListener");
        try {
            listener.loadList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        configureMenu(fileMenu, XPontusConstants.fileActions2);
        configureMenu(editMenu, XPontusConstants.editActions);
        configureMenu(helpMenu, XPontusConstants.helpActions);
        configureMenu(formatMenu, XPontusConstants.formatActions);
        configureMenu(toolsMenu, XPontusConstants.toolsActions);
        
        toolbar = ToolBarContainer.createDefaultContainer(true, true, true, true);
        
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);
        
        
        ToolBarPanel tbPanel = toolbar.getToolBarPanelAt(BorderLayout.NORTH);
        
        VLToolBar tb = null;
        
        tb = createToolBar(new String[]{
            "action.new",
            "action.open",
            "action.openremote",
            "action.print",
            "action.save",
            "action.saveas",
            "action.saveall"}
        , "General");
        toolbar.registerToolBar(tb);
        tbPanel.add(tb, new ToolBarConstraints(0, 0));
        
        
        tb = createToolBar(new String[]{
            "action.cut",
            "action.copy",
            "action.paste",
            "action.undo",
            "action.redo",
            "action.find",
            "action.gotoline"}
        , "Edit");
        toolbar.registerToolBar(tb);
        tbPanel.add(tb, new ToolBarConstraints(0, 1));
        
        tb = createToolBar(new String[]{
            "action.preferences",
            "action.about",
            "action.help"}
        , "About");
        toolbar.registerToolBar(tb);
        tbPanel.add(tb, new ToolBarConstraints(0, 2));
        
        
        tb = createToolBar(new String[]{
            "action.commentxml",
            "action.indentxml",
            "action.tidy"}
        , "Utilities");
        toolbar.registerToolBar(tb);
        tbPanel.add(tb, new ToolBarConstraints(1, 0));
        
        tb = createToolBar(new String[]{
            "action.checkxml",
            "action.validate",
            "action.validateschema"}
        , "Validation");
        toolbar.registerToolBar(tb);
        tbPanel.add(tb, new ToolBarConstraints(1, 1));
        
        
        tb = createToolBar(new String[]{
            "action.list_scenarios",
            "action.execute_scenarios"}
        , "Scenarios");
        toolbar.registerToolBar(tb);
        tbPanel.add(tb, new ToolBarConstraints(1, 2));
        
        
        createMessagesPopupListener();
        
        configureDragAndDrop(this.pane);
        configureDragAndDrop(this.frame);
        
    }
    
    public void configureDragAndDrop(Component component){
        new DropTarget(component, new DropTargetAdapter() {
            public void drop(DropTargetDropEvent e) {
                try {
                    Transferable t = e.getTransferable();
                    e.acceptDrop(e.getDropAction());
                    
                    DataFlavor []flavors = e.getCurrentDataFlavors();
                    URL fileURL = null;
                    java.util.List fileList = null;
                    String  dndString = null;
                    DataFlavor urlFlavor = null;
                    DataFlavor listFlavor = null;
                    DataFlavor stringFlavor = null;
                    if(flavors != null){
                        for(int i = 0; i < flavors.length; i++){
                            // System.out.println(flavors[i]);
                            if(flavors[i].getRepresentationClass() == java.net.URL.class){
                                urlFlavor = flavors[i];
                            }else if(flavors[i].getRepresentationClass() == java.util.List.class){
                                listFlavor = flavors[i];
                            }else if(flavors[i].getRepresentationClass() == java.lang.String.class){
                                stringFlavor = flavors[i];
                            }
                        }
                    }else{
                        System.out.println("flavors == null");
                        return;
                    }
                    if(stringFlavor != null){
                        try{
                            Object obj = e.getTransferable().getTransferData(stringFlavor);
                            if(obj instanceof String){
                                dndString = (String)obj;
                            }
                        }catch(Throwable tr){
                        }
                        if(dndString != null) {
                            StringReader reader = new StringReader(dndString.trim());
                            BufferedReader mReader = new BufferedReader(reader);
                            
                            String line = null;
                            
                            while( (line = mReader.readLine())!=null){
                                URL url = new URL(line.trim());
                                System.out.println("String");
                                if(url.getProtocol().equals("file")){
                                    ((DockablePaneForm)pane).createEditorFromFile(new File(url.getPath()));
                                } else{
                                    ((DockablePaneForm)pane).createEditorFromStream(url.openStream(), FilenameUtils.getExtension(dndString));
                                }
                            }
                            
                        }
                    }
                    try{
                        Object obj = e.getTransferable().getTransferData(urlFlavor);
                        if(obj instanceof URL){
                            fileURL = (URL)obj;
                        }
                    }catch(Throwable tr){
                    }
                    if(fileURL != null) {
                        URL url = fileURL;
                        System.out.println("file");
                        ((DockablePaneForm)pane).createEditorFromStream(url.openStream(), FilenameUtils.getExtension(dndString));
                    }
                    
                    try{
                        Object obj = e.getTransferable().getTransferData(listFlavor);
                        if(obj instanceof java.util.List){
                            fileList = (java.util.List)obj;
                        }
                    }catch(Throwable tr){
                    }
                    if(fileList != null){
                        Iterator it = fileList.iterator();
                        while(it.hasNext()){
                            Object file = it.next();
                            if(file instanceof File){
                                ((DockablePaneForm)pane).createEditorFromFile((File)file);
                            }
                        }
                    }
                    try{
                        e.dropComplete(true);
                    }catch(Throwable tr){
                        System.out.println("???? "+t);
                    }
                    
                    
                    
                    
                    
                    
                } catch (Exception ex) {
                    //                    ex.printStackTrace();
                }
            }
        }
        
        );
    }
    
    private void createMessagesPopupListener() {
        final L10nHelper helper = L10nHelper.getInstance();
        javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
        javax.swing.Action action;
        javax.swing.ActionMap map = messagesTA.getActionMap();
        
        action = map.get(javax.swing.text.DefaultEditorKit.copyAction);
        action.putValue(javax.swing.Action.NAME, helper.getValue("action.copy.name"));
        popup.add(action);
        
        action = map.get(javax.swing.text.DefaultEditorKit.selectAllAction);
        action.putValue(javax.swing.Action.NAME, helper.getValue("action.selectall.name"));
        popup.add(action);
        
        action = new javax.swing.AbstractAction(helper.getValue("clear.key")) {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messagesTA.setText("");
            }
        };
        popup.add(action);
        
        PopupListener listener = new PopupListener(popup);
        messagesTA.addMouseListener(listener);
    }
    
    private VLToolBar createToolBar(String[] actions, String name) {
        VLToolBar tb = new VLToolBar(name);
        
        for (int i = 0; i < actions.length; i++) {
            if (actions[i].equals("-")) {
                tb.addSeparator();
            } else {
                Object bean = applicationContext.getBean(actions[i]);
                JButton button = new JButton((Action) bean);
                button.setText(null);
                tb.add(button);
            }
        }
        
        return tb;
    }
    
    
    
    /**
     *
     * @param menu
     * @param actions
     */
    private void configureMenu(javax.swing.JMenu menu, String actions[]){
        for(int i=0;i<actions.length;i++){
            if(actions[i].equals("-")){
                menu.addSeparator();
            } else{
                menu.add((Action)applicationContext.getBean(actions[i]));
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        frame = new javax.swing.JFrame() {
            public void setVisible(boolean b) {
                if (splashenabled) {
                    if (splash.isShowing()) {
                        try{
                            Thread.sleep(2000);
                        }
                        catch(Exception err){}
                        splash.setVisible(false);
                    }
                }
                super.setVisible(b);
            }
        };
        statusbar = new net.sf.xpontus.view.components.JStatusBar();
        menubar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();
        toolsMenu = new javax.swing.JMenu();
        formatMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        pane = new DockablePaneForm();
        scrollPane = new DockableMessageWindow();
        messagesTA = new javax.swing.JTextArea(){
            public void append(String s){
                super.append(s);
                setCaretPosition(getDocument().getLength());
            }
        };

        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onWindowClosing(evt);
            }
        });

        frame.getContentPane().add(statusbar, java.awt.BorderLayout.SOUTH);

        fileMenu.setText(L10nHelper.getInstance().getValue("menu.file.name"));
        menubar.add(fileMenu);

        editMenu.setText(L10nHelper.getInstance().getValue("menu.edit.name"));
        menubar.add(editMenu);

        toolsMenu.setText(L10nHelper.getInstance().getValue("menu.tools.name"));
        menubar.add(toolsMenu);

        formatMenu.setText(L10nHelper.getInstance().getValue("menu.format.name"));
        menubar.add(formatMenu);

        helpMenu.setText(L10nHelper.getInstance().getValue("menu.help.name"));
        menubar.add(helpMenu);

        frame.setJMenuBar(menubar);

        pane.setMinimumSize(new java.awt.Dimension(700, 350));
        pane.setPreferredSize(new java.awt.Dimension(700, 350));
        scrollPane.setMinimumSize(new java.awt.Dimension(400, 120));
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 120));
        messagesTA.setColumns(20);
        messagesTA.setEditable(false);
        messagesTA.setLineWrap(true);
        messagesTA.setRows(5);
        messagesTA.setWrapStyleWord(true);
        scrollPane.setViewportView(messagesTA);

    }// </editor-fold>//GEN-END:initComponents
    
    private void onWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWindowClosing
        ((BaseAction) applicationContext.getBean("action.exit")).execute();
    }//GEN-LAST:event_onWindowClosing
    
    /**
     * return the editor's tab container
     * @return The editor's tab container
     */
    public PaneForm getPane() {
        return (PaneForm)this.pane;
    }
    
    /**
     *
     */
    public void ajustFrameMessages() {
        
    }
    
    /**
     *
     * @return The application's statusbar
     */
    public javax.swing.JComponent getStatusbar() {
        return statusbar;
    }
    
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
    public javax.swing.JEditorPane getCurrentEditor() {
        return ((PaneForm)pane).getCurrentEditor();
    }
    
    /**
     *
     * @param msg
     */
    public void append(String msg) {
        messagesTA.append(msg + "\n");
    }
    
    /**
     *
     * @return
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /**
     *
     *
     * @param applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        getPane().setApplicationContext(applicationContext);
    }
    
    /**
     *
     * @return
     */
    public javax.swing.JFrame getFrame(){
        return frame;
    }
    
    /**
     *
     */
    public class DockablePaneForm extends PaneForm implements Dockable {
        DockKey key = new DockKey("Editor window");
        
        /**
         *
         */
        public DockablePaneForm() {
            key.setCloseEnabled(false);
            key.setAutoHideEnabled(false);
            key.setResizeWeight(1.0f); // takes all resizing
        }
        
        /** implement Dockable  */
        public DockKey getDockKey() {
            return key;
        }
        
        /** implement Dockable  */
        public Component getComponent() {
            return this;
        }
    }
    
    class DockableMessageWindow extends JScrollPane implements Dockable{
        DockKey key = new DockKey("Output Window  ");
        
        public DockableMessageWindow(){
            key.setCloseEnabled(false);
            setMinimumSize(new java.awt.Dimension(400, 120));
            setPreferredSize(new java.awt.Dimension(400, 120));
            key.setResizeWeight(0.2f);
        }
        
        public DockKey getDockKey() {
            return key;
        }
        public Component getComponent() {
            return this;
        }
    }
    
    /**
     *
     * @return
     */
    public JStatusBar getStatusBar(){
        return this.statusbar;
    }
    
    
    /**
     *
     * @param key
     * @return
     */
    public String getI18nMessage(String key){
        MessageSource src = (MessageSource)applicationContext.getBean("messageSource");
        return src.getMessage(key, null, Locale.getDefault());
    }
    
    public JMenuBar getApplicationMenuBar(){
        return this.menubar;
    }
    
    
    private JMenu recentFileMenu;
    private ApplicationContext applicationContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu formatMenu;
    private javax.swing.JFrame frame;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JTextArea messagesTA;
    private javax.swing.JTabbedPane pane;
    private javax.swing.JScrollPane scrollPane;
    private net.sf.xpontus.view.components.JStatusBar statusbar;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables
    DockingDesktop desk = new DockingDesktop();
    private ToolBarContainer toolbar;
}
