/*
 * ColorProvider.java
 *
 * Created on December 17, 2006, 2:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor.syntax;


/**
 *
 * @author Yves Zoundi
 */
public class DefaultColorProvider implements IColorProvider
  {
    protected java.util.Map styles = new java.util.HashMap();

    /** Creates a new instance of ColorProvider */
    public DefaultColorProvider()
      {
      }

    public void addAll(int[] tokenIds, StyleInfo style)
      {
        for (int i = 0; i < tokenIds.length; i++)
          {
            addStyle(tokenIds[i], style);
          }
      }

    public void addStyle(int tokenId, StyleInfo style)
      {
        this.styles.put(new Integer(tokenId), style);
      }

    public java.util.Map getStyles()
      {
        return styles;
      }
  }
