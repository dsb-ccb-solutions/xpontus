/*
 * ExpressionEvaluatorPanel.java
 *
 * Created on December 22, 2007, 2:38 PM
 */
package net.sf.xpontus.plugins.evaluator;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.DefaultComboBoxModel;
import net.sf.xpontus.actions.DocumentAwareComponentIF;
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.modules.gui.components.MemoryComboBox;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.DocumentAwareComponentHolder;
import net.sf.xpontus.utils.DocumentContainerChangeEvent;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import org.w3c.dom.NodeList;

/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class ExpressionEvaluatorPanel extends javax.swing.JPanel implements DocumentAwareComponentIF {

    /** Creates new form ExpressionEvaluatorPanel */
    public ExpressionEvaluatorPanel() {

        initComponents();
        DefaultComboBoxModel mm = (DefaultComboBoxModel) this.engineList.getModel();
        String engines[] = EvaluatorPluginConfiguration.getInstance().getEnginesNames();

        for (int i = 0; i < engines.length; i++) {
            mm.addElement(engines[i]);
        }
        if (mm.getSize() > 0) {
            this.engineList.setSelectedIndex(0);
        }
        registerComponent(); 
    }

    public String getExpression() {
        Object o = this.expressionList.getSelectedItem();
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        evaluateButton = new javax.swing.JButton();
        expressionList = new MemoryComboBox(10);
        engineList = new javax.swing.JComboBox();

        evaluateButton.setText("Evaluate");
        evaluateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evaluateButtonActionPerformed(evt);
            }
        });

        expressionList.setEditable(true);
        expressionList.setMinimumSize(new java.awt.Dimension(74, 20));
        expressionList.setPreferredSize(new java.awt.Dimension(74, 20));

        engineList.setModel(new DefaultComboBoxModel());
        engineList.setMinimumSize(new java.awt.Dimension(74, 20));
        engineList.setPreferredSize(new java.awt.Dimension(74, 20));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(evaluateButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(expressionList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 239, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(engineList, 0, 122, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(evaluateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(expressionList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(engineList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void evaluateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evaluateButtonActionPerformed

        Thread m_worker = new Thread() {

            public void run() {
                Hashtable t = (Hashtable) EvaluatorPluginConfiguration.getInstance().getEngines().get(engineList.getSelectedItem());

                if (t == null) {
                    XPontusComponentsUtils.showErrorMessage("No plugins installed for that action");
                    evaluateButton.setEnabled(false);
                    engineList.setEnabled(false);
                    expressionList.setEnabled(false);
                    return;
                }

                if (getExpression() == null || getExpression().trim().equals("")) {
                    XPontusComponentsUtils.showErrorMessage("No expression supplied!");
                    return;
                }

                ((MemoryComboBox) expressionList).addItem(getExpression());


                IDocumentContainer container = (IDocumentContainer) DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentDockable();

                container.getStatusBar().setMessage("Expression evaluation in progres...");
                ClassLoader loader = (ClassLoader) t.get(XPontusConstantsIF.CLASS_LOADER);
                String classname = t.get(XPontusConstantsIF.OBJECT_CLASSNAME).toString();
                try {
                    EvaluatorPluginIF plugin = (EvaluatorPluginIF) Class.forName(classname, true, loader).newInstance();
                    XPathResultDescriptor[] li = plugin.handle(getExpression());
                    XPathResultsDockable dockable = (XPathResultsDockable) DefaultXPontusWindowImpl.getInstance().getConsole().getDockableById(XPathResultsDockable.DOCKABLE_ID);
                    if (li != null) {
                        container.getStatusBar().setMessage("Expression evaluation finished...");
                        /*NodeList nl = (NodeList) li[0];
                        DOMAddLines dm = (DOMAddLines) li[1];     */
                        dockable.setResultsModel(new XPathResultsTableModel(li));
                        DefaultXPontusWindowImpl.getInstance().getConsole().setFocus(XPathResultsDockable.DOCKABLE_ID);
                    } else {
                        container.getStatusBar().setMessage("No results for expression....");
                        DefaultXPontusWindowImpl.getInstance().getConsole().getDockableById(MessagesWindowDockable.DOCKABLE_ID).println("No results");
                        DefaultXPontusWindowImpl.getInstance().getConsole().setFocus(MessagesWindowDockable.DOCKABLE_ID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    container.getStatusBar().setMessage("Error evaluating expression...");
                    DefaultXPontusWindowImpl.getInstance().getConsole().getDockableById(MessagesWindowDockable.DOCKABLE_ID).println("No results");
                    DefaultXPontusWindowImpl.getInstance().getConsole().getDockableById(MessagesWindowDockable.DOCKABLE_ID).println("Error:\n" + e.getMessage(), OutputDockable.RED_STYLE);
                    DefaultXPontusWindowImpl.getInstance().getConsole().setFocus(MessagesWindowDockable.DOCKABLE_ID);
                } finally {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        };
        m_worker.start();
        
    }//GEN-LAST:event_evaluateButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox engineList;
    private javax.swing.JButton evaluateButton;
    private javax.swing.JComboBox expressionList;
    // End of variables declaration//GEN-END:variables
    public void registerComponent() {
        DocumentAwareComponentHolder.getInstance().register(this);
    }

    public void onNotify(DocumentContainerChangeEvent evt) {
        boolean b = (evt.getSource() != null);
        evaluateButton.setEnabled(b);
        expressionList.setEnabled(b);
        engineList.setEnabled(b);
    }
}
