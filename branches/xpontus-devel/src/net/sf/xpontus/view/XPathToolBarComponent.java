package net.sf.xpontus.view;

import com.vlsolutions.swing.toolbars.VLToolBar;

import net.sf.xpontus.controller.actions.XPathAction;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;


public class XPathToolBarComponent extends VLToolBar
{
    private JButton mExecuteXPathButton;
    private JComboBox mXPathExpressionsList;

    public XPathToolBarComponent()
    {
        super("xpath");
        initComponents();
    }

    public void setXPathAction(Action action)
    {
        mExecuteXPathButton.setAction(action);
    }

    public void initComponents()
    {
        mXPathExpressionsList = new MemoryComboBox();

        Dimension dimension = new Dimension(230, 20);

        mXPathExpressionsList.setMinimumSize(dimension);
        mXPathExpressionsList.setPreferredSize(dimension);

        mExecuteXPathButton = new JButton();

        this.add(mExecuteXPathButton);

        this.add(mXPathExpressionsList);
    }

    public Object getXPathExpression()
    {
        return mXPathExpressionsList.getSelectedItem();
    }
}
