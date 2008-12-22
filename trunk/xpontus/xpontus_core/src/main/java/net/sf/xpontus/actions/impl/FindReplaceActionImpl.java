/*
 *  soapUI, copyright (C) 2004-2007 eviware.com
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the
 *  terms of version 2.1 of the GNU Lesser General Public License as published by
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details at gnu.org.
 *
 *  Code adapted for xpontus by Yves Zoundi
 */
package net.sf.xpontus.actions.impl;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.SearchFormView;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.text.JTextComponent;


/**
 * Find/replace dialog (Unused)
 * @version 0.0.1
 * @author SOAP GUI, Yves Zoundi <yveszoundi at users dot sf dot net>
 * 
 */
public class FindReplaceActionImpl extends DefaultDocumentAwareActionImpl
{
    private static final long serialVersionUID = -679377056630905245L;
    private JTextComponent target;
    private JDialog dialog;
    private JCheckBox caseCheck;
    private JRadioButton allButton;
    private JRadioButton forwardButton;
    private JRadioButton backwardButton;
    private JCheckBox wholeWordCheck;
    private JButton findButton;
    private JButton replaceButton;
    private JButton replaceAllButton;
    private JComboBox findCombo;
    private JComboBox replaceCombo;
    private JCheckBox wrapCheck;
    private SearchFormView m_view;

    public FindReplaceActionImpl()
    {
    }

    public void run()
    {
        show2();
    }

    private void show2()
    {
        if (m_view == null)
        {
            m_view = new SearchFormView();
        }

        m_view.setLocationRelativeTo(DefaultXPontusWindowImpl.getInstance()
                                                             .getDisplayComponent());
        m_view.setVisible(true);
    }

    public void show()
    {
        if (dialog == null)
        {
            buildDialog();
        }

        target = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer()
                                         .getCurrentEditor();

        replaceCombo.setEnabled(target.isEditable());
        replaceAllButton.setEnabled(target.isEditable());
        replaceButton.setEnabled(target.isEditable());

        Frame f = (Frame) XPontusComponentsUtils.getTopComponent()
                                                .getDisplayComponent();
        dialog.setLocationRelativeTo(f);
        dialog.setVisible(true);

        if (target.getSelectedText() != null)
        {
            if (!target.getSelectedText().trim().equals(""))
            {
                findCombo.getEditor().setItem(target.getSelectedText());
            }
        }

        findCombo.getEditor().selectAll();
        findCombo.requestFocus();
    }

    private void buildDialog()
    {
        dialog = new JDialog((Frame) XPontusComponentsUtils.getTopComponent()
                                                           .getDisplayComponent(),
                "Find / Replace", false);

        JPanel panel = new JPanel(new BorderLayout());
        findCombo = new JComboBox();
        findCombo.setEditable(true);
        replaceCombo = new JComboBox();
        replaceCombo.setEditable(true);

        // create inputs
        GridLayout gridLayout = new GridLayout(2, 2);
        gridLayout.setVgap(5);

        JPanel inputPanel = new JPanel(gridLayout);
        inputPanel.add(new JLabel("Find:"));
        inputPanel.add(findCombo);
        inputPanel.add(new JLabel("Replace with:"));
        inputPanel.add(replaceCombo);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // create direction panel
        ButtonGroup directionGroup = new ButtonGroup();
        forwardButton = new JRadioButton("Forward", true);
        forwardButton.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        directionGroup.add(forwardButton);
        backwardButton = new JRadioButton("Backward");
        backwardButton.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        directionGroup.add(backwardButton);

        JPanel directionPanel = new JPanel(new GridLayout(2, 1));
        directionPanel.add(forwardButton);
        directionPanel.add(backwardButton);
        directionPanel.setBorder(BorderFactory.createTitledBorder("Direction"));

        // create scope panel
        ButtonGroup scopeGroup = new ButtonGroup();
        allButton = new JRadioButton("All", true);
        allButton.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        scopeGroup.add(allButton);

        JPanel scopePanel = new JPanel(new GridLayout(2, 1));
        scopePanel.add(allButton);
        scopePanel.setBorder(BorderFactory.createTitledBorder("Scope"));

        // create options
        caseCheck = new JCheckBox("Case Sensitive");
        caseCheck.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        wholeWordCheck = new JCheckBox("Whole Word");
        wholeWordCheck.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        wrapCheck = new JCheckBox("Wrap Search", true);
        wrapCheck.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JPanel optionsPanel = new JPanel(new GridLayout(3, 1));
        optionsPanel.add(caseCheck);
        optionsPanel.add(wholeWordCheck);
        optionsPanel.add(wrapCheck);
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        // create panel with options
        JPanel options = new JPanel(new GridLayout(1, 2));

        JPanel radios = new JPanel(new GridLayout(2, 1));
        radios.add(directionPanel);
        radios.add(scopePanel);

        options.add(optionsPanel);
        options.add(radios);
        options.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        // create buttons
        Box builder = Box.createHorizontalBox();
        findButton = new JButton(new FindAction());
        builder.add(findButton);
        builder.add(Box.createGlue());
        replaceButton = new JButton(new ReplaceAction());
        builder.add(replaceButton);
        builder.add(Box.createGlue());
        replaceAllButton = new JButton(new ReplaceAllAction());
        builder.add(replaceAllButton);
        builder.add(Box.createGlue());
        builder.add(new JButton(new CloseAction()));
        builder.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // tie it up!
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(options, BorderLayout.CENTER);
        panel.add(builder, BorderLayout.SOUTH);

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.getRootPane().setDefaultButton(findButton);
    }

    private int findNext(int pos, String txt, String value)
    {
        int ix = forwardButton.isSelected() ? txt.indexOf(value, pos + 1)
                                            : txt.lastIndexOf(value, pos - 1);

        if (wholeWordCheck.isSelected())
        {
            while (((ix != -1) &&
                    ((ix > 0) && Character.isLetterOrDigit(txt.charAt(ix - 1)))) ||
                    ((ix < (txt.length() - value.length() - 1)) &&
                    Character.isLetterOrDigit(txt.charAt(ix + value.length()))))
            {
                ix = findNext(ix, txt, value);
            }
        }

        if ((ix == -1) && wrapCheck.isSelected())
        {
            if (forwardButton.isSelected() && (pos > 0))
            {
                return findNext(0, txt, value);
            }
            else if (backwardButton.isSelected() && (pos < (txt.length() - 1)))
            {
                return findNext(txt.length() - 1, txt, value);
            }
        }

        return ix;
    }

    private class FindAction extends AbstractAction
    {
        private static final long serialVersionUID = 6119275100006266333L;

        public FindAction()
        {
            super("Find");
        }

        public void actionPerformed(ActionEvent e)
        {
            int pos = target.getCaretPosition();
            int selstart = target.getSelectionStart();

            if ((selstart < pos) && (selstart != -1))
            {
                pos = selstart;
            }

            String txt = target.getText();

            String value = findCombo.getSelectedItem().toString();

            if ((value.length() == 0) || (pos == txt.length()))
            {
                return;
            }

            if (!caseCheck.isSelected())
            {
                value = value.toUpperCase();
                txt = txt.toUpperCase();
            }

            int ix = findNext(pos, txt, value);

            if (ix != -1)
            {
                target.grabFocus();
                target.select(ix, ix + value.length());

                for (int c = 0; c < findCombo.getItemCount(); c++)
                {
                    if (findCombo.getItemAt(c).equals(value))
                    {
                        findCombo.removeItem(c);

                        break;
                    }
                }

                findCombo.insertItemAt(value, 0);
            }
            else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    private class ReplaceAction extends AbstractAction
    {
        private static final long serialVersionUID = -8766403165089665561L;

        public ReplaceAction()
        {
            super("Replace");
        }

        public void actionPerformed(ActionEvent e)
        {
            if (target.getSelectedText() == null)
            {
                return;
            }

            String value = replaceCombo.getSelectedItem().toString();
            int ix = target.getSelectionStart();
            //            target.setSelectedText(value);
            target.grabFocus();
            target.select(ix + value.length(), ix);

            for (int c = 0; c < replaceCombo.getItemCount(); c++)
            {
                if (replaceCombo.getItemAt(c).equals(value))
                {
                    replaceCombo.removeItem(c);

                    break;
                }
            }

            replaceCombo.insertItemAt(value, 0);
        }
    }

    private class ReplaceAllAction extends AbstractAction
    {
        private static final long serialVersionUID = 4312876915074536782L;

        public ReplaceAllAction()
        {
            super("Replace All");
        }

        public void actionPerformed(ActionEvent e)
        {
            int pos = target.getCaretPosition();
            String txt = target.getText();

            String value = findCombo.getSelectedItem().toString();

            if ((value.length() == 0) || (txt.length() == 0))
            {
                return;
            }

            String newValue = replaceCombo.getSelectedItem().toString();

            if (!caseCheck.isSelected())
            {
                if (newValue.equalsIgnoreCase(value))
                {
                    return;
                }

                value = value.toUpperCase();
                txt = txt.toUpperCase();
            }
            else if (newValue.equals(value))
            {
                return;
            }

            int ix = findNext(pos, txt, value);
            int firstIx = ix;
            int valueInNewValueIx = (!caseCheck.isSelected())
                ? newValue.toUpperCase().indexOf(value) : newValue.indexOf(value);

            while (ix != -1)
            {
                System.out.println("found match at " + ix + ", " + firstIx +
                    ", " + valueInNewValueIx);
                target.select(ix + value.length(), ix);

                //                target.setSelectedText(newValue);
                target.grabFocus();
                target.select(ix + newValue.length(), ix);

                // adjust firstix 
                if (ix < firstIx)
                {
                    firstIx += (newValue.length() - value.length());
                }

                txt = target.getText();

                if (!caseCheck.isSelected())
                {
                    txt = txt.toUpperCase();
                }

                ix = findNext(ix + newValue.length(), txt, value);

                if (wrapCheck.isSelected() && (valueInNewValueIx != -1) &&
                        (ix == (firstIx + valueInNewValueIx)))
                {
                    break;
                }
            }
        }
    }

    private class CloseAction extends AbstractAction
    {
        private static final long serialVersionUID = -8353890537642010316L;

        public CloseAction()
        {
            super("Close");
        }

        public void actionPerformed(ActionEvent e)
        {
            dialog.setVisible(false);
        }
    }
}
