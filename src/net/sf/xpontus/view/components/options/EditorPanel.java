/*
 * EditorPanel.java
 *
 * Created on February 16, 2006, 8:06 PM
 */

package net.sf.xpontus.view.components.options;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.text.NumberFormat;
import javax.swing.JComponent;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SpinnerModel;
import net.sf.xpontus.model.options.EditorOptionModel;

/**
 *
 * @author  Owner
 */
public class EditorPanel extends javax.swing.JPanel implements IOptionPanel{
    
    private BeanAdapter adapter;
    private ComboBoxAdapter fontFamilyModel, fontSizeModel;
    
    /** Creates new form EditorPanel */
    public EditorPanel() {
        model = new EditorOptionModel();
        adapter = new BeanAdapter(model, true);
        initFont();
        initComponents();
        read();
    }
    
    public void initFont(){
        ValueModel stringModel = adapter.getValueModel("fontName");
        List possibleValues = new ArrayList();
        
        String fnames[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        for(int i=0;i<fnames.length;i++){
            possibleValues.add(fnames[i]);
        }
        
        
        fontFamilyModel = new ComboBoxAdapter(possibleValues, stringModel);
        
        ValueModel _stringModel = adapter.getValueModel("fontSize");
        List _possibleValues = new ArrayList();
        
        for(int i=8;i<20;i++){
            _possibleValues.add("" + i);
        }
        
        fontSizeModel = new ComboBoxAdapter(_possibleValues, _stringModel);
    }
    
    public void read(){
        EditorOptionModel m = new EditorOptionModel();
        EditorOptionModel obj = (EditorOptionModel)m.load();
        model.setShowLineNumbers(obj.isShowLineNumbers());
        model.setTabSize(obj.getTabSize());
        model.setFontName(obj.getFontName());
        model.setFontSize(obj.getFontSize());
        model.setCursorRate(obj.getCursorRate());
    }
    
    /**
     *Save the model which will be stored in the preferences directory
     *
     */
    
    public void save(){
        model.save();
    }
    
    /**
     *
     * @return
     */
    public JComponent getControl() {
        return this;
    }
    
    
    
    /**
     *
     * @return
     */
    public String getDisplayName() {
        return "Editor";
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        lineNumbersOption = new javax.swing.JCheckBox();
        cursorBlinkRateLabel = new javax.swing.JLabel();
        spinnerModel = SpinnerAdapterFactory.createNumberAdapter(
            adapter.getValueModel("cursorRate"), 500, 300, 900, 100);
        cursorRateValues = new javax.swing.JSpinner();
        tabSizeLabel = new javax.swing.JLabel();
        tabSizeTF = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fontSizeList = new javax.swing.JComboBox();
        fontFamilyList = new javax.swing.JComboBox();

        lineNumbersOption.setSelected(true);
        lineNumbersOption.setText("Display line numbers");
        lineNumbersOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lineNumbersOption.setMargin(new java.awt.Insets(0, 0, 0, 0));
        Bindings.bind(lineNumbersOption, adapter.getValueModel("showLineNumbers"));

        cursorBlinkRateLabel.setText("Cursor blink rate");

        cursorRateValues.setModel(spinnerModel);

        tabSizeLabel.setText("Tab size");

        tabSizeTF.setValue(new Integer(4));
        Bindings.bind(tabSizeTF, adapter.getValueModel("tabSize"));

        jLabel1.setText("Font family");

        jLabel2.setText("Font size");

        fontSizeList.setModel(fontSizeModel);

        fontFamilyList.setModel(fontFamilyModel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lineNumbersOption)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cursorBlinkRateLabel)
                            .add(tabSizeLabel)
                            .add(jLabel2)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(31, 31, 31)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(fontFamilyList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(fontSizeList, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(tabSizeTF, 0, 0, Short.MAX_VALUE)
                                .add(cursorRateValues, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)))))
                .addContainerGap(210, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lineNumbersOption)
                .add(22, 22, 22)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(cursorBlinkRateLabel)
                    .add(cursorRateValues, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(tabSizeLabel)
                    .add(tabSizeTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(23, 23, 23)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(fontFamilyList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(fontSizeList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
           
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cursorBlinkRateLabel;
    private javax.swing.JSpinner cursorRateValues;
    private javax.swing.JComboBox fontFamilyList;
    private javax.swing.JComboBox fontSizeList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox lineNumbersOption;
    private javax.swing.JLabel tabSizeLabel;
    private javax.swing.JFormattedTextField tabSizeTF;
    // End of variables declaration//GEN-END:variables
    private EditorOptionModel model;
    private SpinnerModel spinnerModel;
}
