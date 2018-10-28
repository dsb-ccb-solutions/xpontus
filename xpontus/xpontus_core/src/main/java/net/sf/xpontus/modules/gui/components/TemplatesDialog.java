/*
 * TemplatesDialog.java
 *
 * Created on 28-Jul-2007, 12:39:41 PM
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
package net.sf.xpontus.modules.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


/**
 * Templates dialog
 * User: Yves Zoundi
 * Date: Apr 5, 2008
 * Time: 1:59:18 PM
 */
public class TemplatesDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 1373552411828211904L;
    private JComponent buttonsPanel;
    private JComponent templatesPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JScrollPane scrollPane;
    private JList templatesList;
    private DefaultListModel templatesListModel;
    private final String[] TEMPLATE_TITLES = 
        {
            "HTML Document", "Docbook XML", "Ant build file", "XSL stylesheet",
            "XML Schema", "XSL FO", "Relax NG"
        };
    private final String[] TEMPLATES_FILES = 
        {
            "/net/sf/xpontus/templates/template.html",
            "/net/sf/xpontus/templates/docbook.xml",
            "/net/sf/xpontus/templates/build.xml",
            "/net/sf/xpontus/templates/template.xsl",
            "/net/sf/xpontus/templates/template.xsd",
            "/net/sf/xpontus/templates/template.fo",
            "/net/sf/xpontus/templates/template.rng"
        };

    /**
     * Constructor TemplatesDialog creates a new TemplatesDialog instance.
     *
     * @param owner of type Frame
     * @param modal of type boolean
     */
    public TemplatesDialog(Frame owner, boolean modal)
    {
        super(owner, modal);

        initComponents();
        initListeners();
        pack();
    }

    /**
     * Constructor TemplatesDialog creates a new TemplatesDialog instance.
     */
    public TemplatesDialog()
    {
        this((Frame) net.sf.xpontus.utils.XPontusComponentsUtils.getTopComponent()
                                                                .getDisplayComponent(),
            true);
    }

    private void initComponents()
    {
        setLayout(new BorderLayout());

        setTitle("New file from templates");

        templatesPanel = new JPanel(new GridLayout());

        Border outsideBorder = new EmptyBorder(2, 2, 2, 2);
        Border insideBorder = new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
                new TitledBorder("Templates"));
        templatesPanel.setBorder(new CompoundBorder(insideBorder, outsideBorder));

        templatesListModel = new DefaultListModel();

        for (String alias : TEMPLATE_TITLES)
        {
            templatesListModel.addElement(alias);
        }

        templatesList = new JList(templatesListModel);

        scrollPane = new JScrollPane(templatesList);

        templatesPanel.add(scrollPane);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");

        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        Dimension dim = new Dimension(300, 200);

        templatesPanel.setPreferredSize(dim);
        templatesPanel.setMinimumSize(dim);

        getContentPane().add(templatesPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void initListeners()
    {
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        templatesList.setSelectedIndex(0);
        templatesList.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() == 2)
                    {
                        insertTemplate();
                    }
                }
            });

        templatesList.addKeyListener(new KeyAdapter()
            {
                public void keyReleased(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        insertTemplate();
                    }
                }
            });
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(cancelButton))
        {
            setVisible(false);
        }
        else if (e.getSource().equals(okButton))
        {
            insertTemplate();
        }
    }

    private void insertTemplate()
    {
        final int index = templatesList.getSelectedIndex();
        setVisible(false);
        SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    String templatePath = TEMPLATES_FILES[index];
                    int pos = templatePath.lastIndexOf("/") + 1;
                    String templateName = templatePath.substring(pos);
                    DocumentTabContainer dtc = DefaultXPontusWindowImpl.getInstance()
                                                                       .getDocumentTabContainer();
                    dtc.createEditorForTemplate(templateName, templatePath);
                }
            });
    }
}
