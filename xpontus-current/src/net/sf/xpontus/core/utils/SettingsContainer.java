/*
 * SettingsContainer.java
 *
 * Created on 2 d�cembre 2006, 17:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.core.utils;


/**
 *
 * @author mrcheeks
 */
public class SettingsContainer
  {
    private static SettingsContainer instance;

    /** Creates a new instance of SettingsContainer */
    private SettingsContainer()
      {
      }

    public static SettingsContainer getInstance()
      {
        if (instance == null)
          {
            instance = new SettingsContainer();
          }

        return instance;
      }

    /**
     *
     * @param object
     */
    public void save(Object object)
      {
      }

    public void shutdown()
      {
      }

    /**
     *
     * @param clazz
     * @return
     */
    public Object get(Object obj)
      {
        return null;
      }
  }
