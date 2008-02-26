/*
 * IOptionPanel.java
 *
 * Created on February 16, 2006, 8:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.components.options;

import javax.swing.JComponent;


/**
 *
 * @author Yves Zoundi
 */
public interface IOptionPanel
  {
    /**
     *
     * @return
     */
    public JComponent getControl();

    /**
     *
     * @return
     */
    public String getDisplayName();

    /**
     *
     */
    public void save();

    /**
     *
     */
    public void read();
  }
