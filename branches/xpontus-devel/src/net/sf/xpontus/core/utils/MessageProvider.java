/*
 * MessageProvider.java
 *
 * Created on 2 d�cembre 2006, 11:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.core.utils;

import java.util.Hashtable;
import java.util.ResourceBundle;


/**
 *
 * @author mrcheeks
 */
public class MessageProvider
  {
    private static MessageProvider provider;
    private Hashtable resList;
    private final String DEFAULT_BUNDLE_LOC = "net.sf.xpontus.i18n.application";

    /** Creates a new instance of MessageProvider */
    private MessageProvider()
      {
        resList = new Hashtable();

        ResourceBundle res = ResourceBundle.getBundle(DEFAULT_BUNDLE_LOC);
        resList.put("default", res);
      }

    public static MessageProvider getinstance()
      {
        if (provider == null)
          {
            provider = new MessageProvider();
          }

        return provider;
      }

    public void installBundle(String bundleName, String location)
      {
        ResourceBundle res = ResourceBundle.getBundle(location);

        if (resList.contains(bundleName))
          {
            StringBuilder msg = new StringBuilder();
            msg.append("The bundle name is already used");
            throw new IllegalArgumentException(msg.toString());
          }

        resList.put(bundleName, res);
      }

    public String getMessage(String bundleName, String key)
      {
        ResourceBundle res = (ResourceBundle) resList.get(bundleName);

        return res.getString(key);
      }

    public String getMessage(String key)
      {
        return getMessage("default", key);
      }
  }
