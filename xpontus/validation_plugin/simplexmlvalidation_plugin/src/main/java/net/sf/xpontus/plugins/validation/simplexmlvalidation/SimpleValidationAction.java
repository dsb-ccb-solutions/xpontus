/*
 * SimpleValidationAction.java
 * 
 * Created on 2007-07-26, 14:19:47
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.validation.simplexmlvalidation;

import javax.swing.JOptionPane;
import net.sf.xpontus.actions.impl.XPontusThreadedActionImpl;

/**
 *
 * @author YVZOU
 */
public class SimpleValidationAction extends XPontusThreadedActionImpl{

    public SimpleValidationAction() {
        setName("Validate XML");
        setDescription("XML Validation");
    }

    public void run() {
        JOptionPane.showMessageDialog(null, "Not done!");
    }

}
