/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.modules.gui.components;

import net.sf.xpontus.controllers.impl.GotoLineController;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.EventHandler;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


/**
 *
 * @author yvzou
 */
public class GotoLineView extends JDialog
{
    private JPanel bottomPanel;
    private JButton closeButton;
    private JLabel columnLabel;
    private JFormattedTextField columnNumberTF;
    private JButton findButton;
    private JFormattedTextField lineNumberTF;
    private GotoLineController controller;
    private JComponent centerPanel;
    private JComponent detailsPanel;
    private JButton detailsButton;
    private JComponent compPanel;

    /** Creates new form GotoLineView
     * @param parent
     * @param modal
     */
    public GotoLineView(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        controller = new GotoLineController(this);
        initComponents();

        this.lineNumberTF.setValue(new Integer(1));
        this.columnNumberTF.setValue(new Integer(1));
    }

    public GotoLineView()
    {
        this((Frame) XPontusComponentsUtils.getTopComponent()
                                           .getDisplayComponent(), false);
    }

    public int getLine()
    {
        return Integer.parseInt(this.lineNumberTF.getValue().toString());
    }

    public int getColumn()
    {
        return Integer.parseInt(this.columnNumberTF.getValue().toString());
    }

    private void initComponents()
    {
        setLayout(new BorderLayout());

        detailsPanel = new JPanel(new GridBagLayout());

        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        Dimension m_dimension = new Dimension(200, 100);
        centerPanel.setMinimumSize(m_dimension);
        centerPanel.setPreferredSize(m_dimension);

        GridBagConstraints gbc = null;

        findButton = new JButton();
        detailsButton = new JButton("Details >>");

        lineNumberTF = new JFormattedTextField();
        bottomPanel = new JPanel();
        closeButton = new JButton();
        columnLabel = new JLabel();
        columnNumberTF = new JFormattedTextField();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Go to line");

        findButton.setText("Find line");
        findButton.addActionListener((ActionListener) EventHandler.create(
                ActionListener.class, controller,
                GotoLineController.GOTO_LINE_METHOD));

        lineNumberTF.setToolTipText("The line number");

        closeButton.setText("Close");
        closeButton.addActionListener((ActionListener) EventHandler.create(
                ActionListener.class, controller,
                GotoLineController.CLOSE_WINDOW_METHOD));
        bottomPanel.add(closeButton);

        columnLabel.setText("  Column");

        columnNumberTF.setToolTipText(
            "The column is mandatory, if not found it will be reset to 1");

        gbc = new GridBagConstraints(0, 0, 1, 1, 0, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0);

        centerPanel.add(findButton, gbc);

        gbc = new GridBagConstraints(1, 0, 1, 2, 0.5, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0);

        centerPanel.add(lineNumberTF, gbc);

        gbc = new GridBagConstraints(0, 1, 1, 1, 0, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0);

        centerPanel.add(detailsButton, gbc);

        detailsPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints(0, 0, 1, 1, 0, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0);

        detailsPanel.add(columnLabel, gbc);

        gbc = new GridBagConstraints(1, 0, 1, 2, 0.5, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0);

        detailsPanel.add(columnNumberTF, gbc);

        compPanel = new JPanel(new BorderLayout());

        compPanel.add(centerPanel, BorderLayout.CENTER);
        compPanel.add(detailsPanel, BorderLayout.SOUTH);

        add(compPanel, BorderLayout.CENTER);
        detailsPanel.setVisible(false);

        detailsButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    detailsPanel.setVisible(!detailsPanel.isVisible());
                }
            });
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
    }
}
