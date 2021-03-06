/*
 * DocumentContainer.java
 *
 * Created on July 1, 2007, 10:44 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
package net.sf.xpontus.modules.gui.components;

import com.ibm.icu.text.CharsetDetector;

import com.jidesoft.swing.Searchable;
import com.jidesoft.swing.SearchableBar;
import com.jidesoft.swing.SearchableUtils;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableActionCustomizer;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.TabbedContainerActions;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusFileConstantsIF;
import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.controllers.impl.ModificationHandler;
import net.sf.xpontus.controllers.impl.PopupHandler;
import net.sf.xpontus.controllers.impl.XPontusUndoManager;
import net.sf.xpontus.plugins.quicktoolbar.DefaultQuickToolbarPluginImpl;
import net.sf.xpontus.plugins.quicktoolbar.QuickToolBarPluginIF;
import net.sf.xpontus.properties.PropertiesHolder;
import net.sf.xpontus.syntax.LineView;
import net.sf.xpontus.syntax.SyntaxDocument;
import net.sf.xpontus.utils.GUIUtils;
import net.sf.xpontus.utils.MimeTypesProvider;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;


/**
 * Document container
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class DocumentContainer implements IDocumentContainer
{
    private static final Log LOG = LogFactory.getLog(DocumentContainer.class);
    private JComponent documentPanel;
    private JStatusBar status;
    private JScrollPane scrollPane;
    private Action closeAllInTab;
    private Action closeAllOtherInTab;
    private JTextComponent editor;
    private DockKey key;
    private final String image = "/net/sf/xpontus/icons/file.gif";
    private JComponent bottomPanel;

    /**
     * Creates a new instance of EditorContainer
     */
    public DocumentContainer()
    {
        documentPanel = new JPanel();
        documentPanel.setLayout(new BorderLayout());
        documentPanel.setFocusable(true);
        documentPanel.setRequestFocusEnabled(true);

        editor = new JEditorPane();
        editor.setFont((Font) XPontusConfig.getValue("EditorPane.Font"));
        editor.setFocusable(true);
        editor.setRequestFocusEnabled(true);

        GUIUtils.installDragAndDropSupport(editor);

        status = new JStatusBar();
        editor.setCaretPosition(0);

        scrollPane = new JScrollPane(editor);

        boolean lineProp = Boolean.parseBoolean(XPontusConfig.getValue(
                    "displayLineNumbers").toString());

        if (lineProp)
        {
            scrollPane.setRowHeaderView(new LineView(editor));
        }

        documentPanel.add(scrollPane, BorderLayout.CENTER);

        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(status, BorderLayout.CENTER);

        Searchable searchable = SearchableUtils.installSearchable(editor);
        searchable.setRepeats(true);
        SearchableBar.install(searchable,
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK),
            new SearchableBar.Installer()
            {
                public void openSearchBar(SearchableBar searchableBar)
                {
                    bottomPanel.add(searchableBar, BorderLayout.NORTH);
                    bottomPanel.invalidate();
                    bottomPanel.revalidate();
                }

                public void closeSearchBar(SearchableBar searchableBar)
                {
                    bottomPanel.remove(searchableBar);
                    bottomPanel.invalidate();
                    bottomPanel.revalidate();
                }
            });

        documentPanel.add(bottomPanel, BorderLayout.SOUTH);

        GUIUtils.installWindowSwitcher(editor);
    }

    public JComponent getDocumentPanel()
    {
        return documentPanel;
    }

    /**
     *
     * @return
     */
    public JStatusBar getStatusBar()
    {
        return status;
    }

    /**
     *
     */
    public void completeSetup()
    {
        Dimension dim = new Dimension(600, 400);
        this.getComponent().setMinimumSize(dim);
        this.getComponent().setPreferredSize(dim);

        key.setResizeWeight(0.7f);
        key.setDockGroup(DocumentTabContainer.group);

        ImageIcon icon = new ImageIcon(getClass().getResource(image));
        key.setIcon(icon);

        init();

        new ModificationHandler(this);
        editor.addMouseListener(new PopupHandler());
    }

    /**
     *
     * @param fileURL
     */
    public void setup(URL fileURL)
    {
        try
        {
            FileObject fileObject = VFS.getManager()
                                       .resolveFile(fileURL.toExternalForm());
            setup(fileObject, fileURL.toExternalForm(), null);
        }
        catch (IOException ex)
        {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public void setup()
    {
        documentPanel.add(new DefaultQuickToolbarPluginImpl().getComponent(),
            BorderLayout.NORTH);

        String mm = MimeTypesProvider.getInstance().getMimeType("file.xml");

        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);

        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, null);

        editor.setUI(new XPontusEditorUI(editor, "file.xml"));

        SyntaxDocument doc = (SyntaxDocument) editor.getDocument();
        doc.setLoading(true);

        key = new DockKey("Untitled" + this.hashCode() + "",
                "Untitled" + this.hashCode() + "");

        try
        {
            CharsetDetector detector = new CharsetDetector();
            InputStream is = getClass()
                                 .getResourceAsStream("/net/sf/xpontus/templates/template.xml");
            detector.setText(new BufferedInputStream(is));

            try
            {
                editor.read(detector.detect().getReader(), null);
            }
            catch (Exception ioe)
            {
                editor.read(new InputStreamReader(is), null);
            }

            XPontusUndoManager _undo = new XPontusUndoManager();
            editor.putClientProperty(XPontusConstantsIF.UNDO_MANAGER, _undo);

            editor.getDocument().addUndoableEditListener(new UndoableEditListener()
                {
                    public void undoableEditHappened(UndoableEditEvent event)
                    {
                        ((XPontusUndoManager) editor.getClientProperty(XPontusConstantsIF.UNDO_MANAGER)).addEdit(event.getEdit());
                    }
                });

            editor.putClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED,
                Boolean.FALSE);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
    }

    public void setupFromTemplate(String templateFileName, String templatePath)
    {
        documentPanel.add(new DefaultQuickToolbarPluginImpl().getComponent(),
            BorderLayout.NORTH);

        String mm = MimeTypesProvider.getInstance().getMimeType(templateFileName);

        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);

        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, null);

        editor.setUI(new XPontusEditorUI(editor, templateFileName));

        SyntaxDocument doc = (SyntaxDocument) editor.getDocument();
        doc.setLoading(true);

        key = new DockKey("Untitled" + this.hashCode() + "",
                "Untitled" + this.hashCode() + "");

        try
        {
            CharsetDetector detector = new CharsetDetector();
            InputStream is = getClass().getResourceAsStream(templatePath);
            detector.setText(new BufferedInputStream(is));

            try
            {
                editor.read(detector.detect().getReader(), null);
            }
            catch (Exception ioe)
            {
                editor.read(new InputStreamReader(is), null);
            }

            XPontusUndoManager _undo = new XPontusUndoManager();
            editor.putClientProperty(XPontusConstantsIF.UNDO_MANAGER, _undo);

            editor.getDocument().addUndoableEditListener(new UndoableEditListener()
                {
                    public void undoableEditHappened(UndoableEditEvent event)
                    {
                        ((XPontusUndoManager) editor.getClientProperty(XPontusConstantsIF.UNDO_MANAGER)).addEdit(event.getEdit());
                    }
                });

            editor.putClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED,
                Boolean.FALSE);

            editor.setCaretPosition(0);

            String m_ext = templateFileName.substring(templateFileName.indexOf(
                        ".") + 1);

            if (m_ext != null)
            {
                m_ext = m_ext.toLowerCase();

                // no good but in a hurry
                if (m_ext.endsWith("xsl") || (m_ext.endsWith("xslt")))
                {
                    doc.putProperty("BUILTIN_COMPLETION", "XSL");
                }
                else if (m_ext.endsWith("xsd"))
                {
                    doc.putProperty("BUILTIN_COMPLETION", "XSD");
                }
                else if (m_ext.endsWith("html") || m_ext.endsWith("htm"))
                {
                    doc.putProperty("BUILTIN_COMPLETION", "HTML");
                }
                else if (m_ext.toLowerCase().endsWith("rng"))
                {
                    doc.putProperty("BUILTIN_COMPLETION", "RELAXNG");
                }
            }
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     *
     * @param file
     */
    public void setup(java.io.File file)
    {
        try
        {
            FileObject fo = VFS.getManager().toFileObject(file);
            setup(fo, file.getAbsolutePath(), file.getName());
            editor.setEditable(file.canWrite());
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     *
     * @param fo
     * @param ext
     * @param fileinfo
     */
    public void setup(FileObject fo, String ext, String fileinfo)
    {
        if (ext == null)
        {
            ext = "file.txt";
        }

        String m_ext = FilenameUtils.getExtension(ext);

        if ((m_ext == null) || m_ext.trim().equals(""))
        {
            ext = "file.txt";
        }

        if (m_ext != null)
        {
            m_ext = m_ext.toLowerCase();
        }

        String mm = MimeTypesProvider.getInstance().getMimeType(ext);

        ////////////////////////////////
        Map qtb = (Map) PropertiesHolder.getPropertyValue(XPontusPropertiesConstantsIF.XPONTUS_QUICKTOOLBAR_PROPERTY);

        if (qtb.size() > 0)
        {
            Object obj = qtb.get(mm);

            if (obj != null)
            {
                Hashtable t = (Hashtable) obj;
                ClassLoader cl = (ClassLoader) t.get(XPontusConstantsIF.CLASS_LOADER);
                String className = (String) t.get(XPontusConstantsIF.OBJECT_CLASSNAME);

                try
                {
                    QuickToolBarPluginIF qbp = (QuickToolBarPluginIF) Class.forName(className,
                            true, cl).newInstance();
                    documentPanel.add(qbp.getComponent(), BorderLayout.NORTH);
                }
                catch (Exception err)
                {
                    err.printStackTrace();
                }
            }
        }

        ////////////////////////////////
        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);

        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, fo);

        try
        {
            editor.putClientProperty(XPontusFileConstantsIF.FILE_LAST_MODIFIED_DATE,
                "" + fo.getContent().getLastModifiedTime());
        }
        catch (Exception err)
        {
        }

        editor.setUI(new XPontusEditorUI(editor, ext));

        SyntaxDocument doc = (SyntaxDocument) editor.getDocument();

        if (m_ext != null)
        {
            if (m_ext.endsWith("xsl") || (m_ext.endsWith("xslt")))
            {
                doc.putProperty("BUILTIN_COMPLETION", "XSL");
            }
            else if (m_ext.endsWith("xsd"))
            {
                doc.putProperty("BUILTIN_COMPLETION", "XSD");
            }
            else if (m_ext.endsWith("html") || m_ext.endsWith("htm"))
            {
                doc.putProperty("BUILTIN_COMPLETION", "HTML");
            }
            else if (m_ext.toLowerCase().endsWith("rng"))
            {
                doc.putProperty("BUILTIN_COMPLETION", "RELAXNG");
            }
        }

        doc.setLoading(true);

        if ((fileinfo != null) && !fileinfo.trim().equals(""))
        {
            key = new DockKey(this.hashCode() + "", fo.getName().getBaseName());
        }
        else
        {
            key = new DockKey("Untitled" + this.hashCode() + "",
                    "Untitled" + this.hashCode() + "");
        }

        if (fo != null)
        {
            try
            {
                key.setTooltip(fo.getURL().toExternalForm());
            }
            catch (FileSystemException ex)
            {
                ex.printStackTrace();
            }
        }

        try
        {
            if (fo != null)
            {
                CharsetDetector detector = new CharsetDetector();

                byte[] b = IOUtils.toByteArray(fo.getContent().getInputStream());
                detector.setText(b);

                try
                {
                    editor.read(detector.detect().getReader(), null);
                }
                catch (Exception ioe)
                {
                    editor.read(new InputStreamReader(
                            new ByteArrayInputStream(b)), null);
                }

                XPontusUndoManager _undo = new XPontusUndoManager();
                editor.putClientProperty(XPontusConstantsIF.UNDO_MANAGER, _undo);

                editor.getDocument().addUndoableEditListener(new UndoableEditListener()
                    {
                        public void undoableEditHappened(
                            UndoableEditEvent event)
                        {
                            ((XPontusUndoManager) editor.getClientProperty(XPontusConstantsIF.UNDO_MANAGER)).addEdit(event.getEdit());
                        }
                    });
            }

            editor.putClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED,
                Boolean.FALSE);

            editor.setCaretPosition(0);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Init the component
     */
    public void init()
    {
        DockingDesktop desk = ((DefaultXPontusWindowImpl) XPontusComponentsUtils.getTopComponent()).getDesktop();

        closeAllInTab = TabbedContainerActions.createCloseAllAction(this, desk);
        closeAllOtherInTab = TabbedContainerActions.createCloseAllOtherAction(this,
                desk);

        DockableActionCustomizer customizer = new DockableActionCustomizer()
            {
                public void visitTabSelectorPopUp(JPopupMenu popUpMenu,
                    Dockable dockable)
                {
                    popUpMenu.add(closeAllInTab);
                    popUpMenu.add(closeAllOtherInTab);
                    popUpMenu.revalidate();
                }
            };

        customizer.setSingleDockableTitleBarPopUpCustomizer(true);
        customizer.setTabSelectorPopUpCustomizer(true);
        key.setActionCustomizer(customizer);
    }

    /**
     * The unique key of this component
     * @return The unique key of this dockable
     */
    public DockKey getDockKey()
    {
        return key;
    }

    /**
     * Returns the component to display
     * @return The component to display
     */
    public Component getComponent()
    {
        return documentPanel;
    }

    /**
     *
     * @return The text editor of this component
     */
    public JTextComponent getEditorComponent()
    {
        return editor;
    }

    public void setup(FileObject fo)
    {
        try
        {
            setup(fo, fo.getName().getURI(), fo.getName().getBaseName());
            editor.setEditable(fo.isWriteable());
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
    }
}
