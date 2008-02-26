/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.utils;

/**
 *
 * @author mrcheeks
 */

import java.awt.Component;
import java.awt.GridBagLayout;

/**
 * An extension of GridBagLayout to allow describing constraints with a string.
 * Possible String formats are:
 * <pre>
 *   "x, y, w, h, wx, wy, anchor, fill, (insets), ipadx, ipady"
 * </pre>
 * Where x, and y are gridx and gridy; w, and h are gridwidth and gridheight
 * values, they can be int values or the REMAINDER or RELATIVE constants.
 * wx, and wy define weightx and weighty as a double.
 * Anchor can be any of the anchor constants and the same for fill.  Insets
 * describes an Insets object and can take the form of (tlbr), (tb, sides) or
 * (t, l, b, r).  If ipadx and ipady are desired, they must appear as the
 * last int values in the string, gridwidth and gridheight must be specified.
 * @author pfnguyen
 */
public class StringGridBagLayout extends GridBagLayout {
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof String) {
            String paramString = (String) constraints;
            constraints = new StringGridBagConstraints(paramString);
        }
        super.addLayoutComponent(comp, constraints);
    }
    
}