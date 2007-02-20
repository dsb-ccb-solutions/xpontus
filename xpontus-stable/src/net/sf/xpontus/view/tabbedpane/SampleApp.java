/*
 * Created on Aug 16, 2004
 *
 */
package net.sf.xpontus.view.tabbedpane;


//import com.incors.plaf.alloy.AlloyLookAndFeel;
//import com.incors.plaf.alloy.themes.glass.GlassTheme;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * @author KRISHNAKUMARP
 *
 */
public class SampleApp extends JFrame {
    public SampleApp(String title) {
        super(title);
        addComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 200);
        setVisible(true);
    }

    protected void addComponents() {
        CloseTabbedPane tabbedPane = new CloseTabbedPane();
        JButton button1 = new JButton("This is Tab 1");
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.add(button1);

        //ImageIcon img = null;
        //try{
        //    img = new ImageIcon("file:/Users/Adam/Eclipse/ActivityPlan/source/com/mri/activityplan/home.png");
        //}catch(Exception e){
        //    e.printStackTrace();
        //}
        tabbedPane.addTab("Buy Plan", buttonPanel1);

        JButton button2 = new JButton("This is Tab 2");
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.add(button2);
        tabbedPane.addTab("Dash Board", buttonPanel2);

        JButton button3 = new JButton("This is Tab 3");
        JPanel buttonPanel3 = new JPanel();
        buttonPanel3.add(button3);
        tabbedPane.addTab("Tab 3", buttonPanel3);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            //AlloyLookAndFeel.setProperty("alloy.licenseCode","");		     //$NON-NLS-1$
            //GlassTheme theme = new GlassTheme();
            //AlloyLookAndFeel laf = new AlloyLookAndFeel(theme);
            //UIManager.setLookAndFeel(laf);
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SampleApp fe = new SampleApp("EnhancedTabbedPane Sample App");
    }
}
