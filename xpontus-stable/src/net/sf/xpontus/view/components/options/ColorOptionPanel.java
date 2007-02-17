/*
 * ColorOptionPanel.java
 *
 * Created on February 16, 2007, 8:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.view.components.options;



import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.sf.xpontus.model.options.TokenColorsOptionModel;
import org.syntax.jedit.tokenmarker.Token;


public class ColorOptionPanel extends JScrollPane implements IOptionPanel{
    private TokenColorsOptionModel colorModel = new TokenColorsOptionModel();
    private JTable table;
    
    public ColorOptionPanel() {
        
        table = new JTable(new MyTableModel());
        
        
        //Set up renderer and editor for the Favorite Color column.
        table.setDefaultRenderer(Color.class,
                new ColorRenderer(true));
        table.setDefaultEditor(Color.class,
                new ColorEditor());
        
        //Add the scroll pane to this panel.
        getViewport().add(table);
        
        read();
    }
    
    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"Token ID", "Color"};
        private Object[][] data = {
            {"COMMENT1", new Color(0, 102, 102)},
            {"COMMENT2", new Color(Integer.parseInt("727675", 16))},
            {"COMMENT3", new Color(Integer.parseInt("dd25af", 16))},
            {"KEYWORD1", new Color(Integer.parseInt("1723aa", 16))},
            {"KEYWORD2", new Color(0, 124, 0)},
            {"KEYWORD3" , Color.MAGENTA},
            {"PRIMITIVE", Color.blue},
            {"LITERAL1",new Color(153, 0, 107)},
            {"LABEL", new Color(0x990000)},
            {"OPERATOR", new Color(0xcc9900)},
            {"INVALID", new Color(0xff3300)}
        };
        
        public int getColumnCount() {
            return columnNames.length;
        }
        
        public int getRowCount() {
            return data.length;
        }
        
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
        
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }
        
        public void setValueAt(Object value, int row, int col) {
            
            data[row][col] = value;
            fireTableCellUpdated(row, col);
            
        }
        
    }
    
    public JComponent getControl() {
        return this;
    }
    
    public String getDisplayName() {
        return "Colors";
    }
    
    public void save() {
        MyTableModel tableModel = (MyTableModel)table.getModel();
        TokenColorsOptionModel model1 = (TokenColorsOptionModel)new TokenColorsOptionModel().load();
        Color[] colors = model1.getColors();
        for(int i=0;i<tableModel.getRowCount();i++){
            try {
                String tokenId = (String) tableModel.getValueAt(i, 0);
                int val = Token.class.getField(tokenId).getInt(null);
                colorModel.getColors()[val] = (Color)tableModel.getValueAt(i, 1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.colorModel.save();
    }
    
    public void read() {
        TokenColorsOptionModel model1 = (TokenColorsOptionModel)new TokenColorsOptionModel().load();
        if(model1==null){ 
            model1 = new TokenColorsOptionModel();
        } 
        this.colorModel.setColors(model1.getColors());
        
        MyTableModel tableModel = (MyTableModel)table.getModel();
        
        for(int i=0;i<tableModel.getRowCount();i++){
            try {
                String tokenId = (String) tableModel.getValueAt(i, 0);
                int val = Token.class.getField(tokenId).getInt(null);
                table.setValueAt(colorModel.getColors()[val], i, 1); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        table.revalidate();
        table.repaint();
        
        
        
        
    }
}



class ColorRenderer extends JLabel
        implements TableCellRenderer {
    Border unselectedBorder = null;
    Border selectedBorder = null;
    boolean isBordered = true;
    
    public ColorRenderer(boolean isBordered) {
        this.isBordered = isBordered;
        setOpaque(true); //MUST do this for background to show up.
    }
    
    public Component getTableCellRendererComponent(
            JTable table, Object color,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        Color newColor = (Color)color;
        setBackground(newColor);
        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                            table.getSelectionBackground());
                }
                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                            table.getBackground());
                }
                setBorder(unselectedBorder);
            }
        }
        
        setToolTipText("RGB value: " + newColor.getRed() + ", "
                + newColor.getGreen() + ", "
                + newColor.getBlue());
        return this;
    }
}

/*
 * ColorEditor.java (compiles with releases 1.3 and 1.4) is used by
 * ColorOptionPanel.java.
 */

class ColorEditor extends AbstractCellEditor
        implements TableCellEditor,
        ActionListener {
    Color currentColor;
    JButton button;
    JColorChooser colorChooser;
    JDialog dialog;
    protected static final String EDIT = "edit";
    
    public ColorEditor() {
        //Set up the editor (from the table's point of view),
        //which is a button.
        //This button brings up the color chooser dialog,
        //which is the editor from the user's point of view.
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        
        //Set up the dialog that the button brings up.
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button,
                "Pick a Color",
                true,  //modal
                colorChooser,
                this,  //OK button handler
                null); //no CANCEL button handler
    }
    
    /**
     * Handles events from the editor button and from
     * the dialog's OK button.
     */
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            button.setBackground(currentColor);
            colorChooser.setColor(currentColor);
            dialog.setVisible(true);
            
            //Make the renderer reappear.
            fireEditingStopped();
            
        } else { //User pressed dialog's "OK" button.
            currentColor = colorChooser.getColor();
        }
    }
    
    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return currentColor;
    }
    
    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
        currentColor = (Color)value;
        return button;
    }
}
