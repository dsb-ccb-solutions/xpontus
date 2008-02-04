/*
 * InstalledPluginsPanel.java
 *
 * Created on December 26, 2007, 10:41 PM
 */
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import org.java.plugin.registry.PluginDescriptor; 
/**
 *
 * @author  mrcheeks
 */
public class InstalledPluginsPanel extends javax.swing.JPanel {
    private DefaultTableModel tableModel;
    private Map<String, Map> pluginsMap = new HashMap<String, Map>();

    public int getNbPlugins(){
        return tableModel.getRowCount();
    }
    
    /** Creates new form InstalledPluginsPanel */
    public InstalledPluginsPanel() { 

        Object[] descriptors = PBPlugin.r.getPluginDescriptors().toArray();

        java.util.Vector columns = new java.util.Vector(3);
        columns.add("Identifier");
        columns.add("Category");
        columns.add("Built-in"); 

        java.util.Vector rows = new java.util.Vector();


        for (int i = 0; i < descriptors.length; i++) {
            PluginDescriptor pds = (PluginDescriptor) descriptors[i];
            String id = pds.getId().toString(); 
            System.out.println("id:" + id);
            String category = pds.getAttribute("Category").getValue().toString();
            String homepage = pds.getAttribute("Homepage").getValue().toString();
            String builtin = pds.getAttribute("Built-in").getValue().toString();
            String displayname = pds.getAttribute("DisplayName").getValue().toString();
            String description = pds.getAttribute("Description").getValue().toString();
            String version = pds.getVersion().toString();

            java.util.Vector m_row = new java.util.Vector(3);
            m_row.add(id);
            m_row.add(category);
            m_row.add(builtin);
            
            Map<String, String> m = new HashMap<String, String>();
            
            m.put("category", category);
            m.put("built-in", builtin);
            m.put("id", id);
            m.put("homepage", homepage); 
            m.put("displayname", displayname);
            m.put("description", description);
            m.put("version", version);
            
            pluginsMap.put(id, m);

            rows.add(m_row); 
        }

        tableModel = new DefaultTableModel(rows, columns) {

            public boolean isCellEditable(int row,
                    int column) {
                return false;
            }
        };
        
         

        initComponents();

         this.jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    int row = jTable1.getSelectedRow();
                    if(row != -1){
                        String id = tableModel.getValueAt(row, 0).toString();
                        Map<String, String> m = pluginsMap.get(id);
                        readMap(m);
                    }
                }

            
        });
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       jTable1.setRowSelectionInterval(0, 0);


    }
    
    private void readMap(Map<String, String> m) {
               jEditorPane1.setText("");
               Document doc = jEditorPane1.getDocument();
               
//                m.put("category", category);
//            m.put("builtin", builtin);
//            m.put("id", id);
//            m.put("homepage", homepage); 
//            m.put("displayname", displayname);
//            m.put("description", description);
//            m.put("version", version);
            
               StringBuffer str = new StringBuffer();
               str.append("<html><head></head><body>");
               try{
                   Iterator<String> it = m.keySet().iterator();
                   while(it.hasNext()){
                       String key = it.next();
                       String value = m.get(key);
                       
                       str.append("<p>");
                       str.append("<b>");
                       str.append(key + ":");
                       str.append("</b> ");
                       str.append(value);
                       str.append("</p>");
                       
                   }
                   str.append("</body></html>");
                   jEditorPane1.setText(str.toString());  
                   
               }
               catch(Exception e){
                   
               }
               
            }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setOneTouchExpandable(true);

        jTable1.setModel(tableModel);
        jScrollPane1.setViewportView(jTable1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jEditorPane1.setContentType("text/html");
        jEditorPane1.setEditable(false);
        jEditorPane1.setEditorKit(new HTMLEditorKit());
        jScrollPane2.setViewportView(jEditorPane1);

        jSplitPane1.setRightComponent(jScrollPane2);

        add(jSplitPane1, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
