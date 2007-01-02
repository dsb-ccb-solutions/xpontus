package net.sf.xpontus.view.editor.syntax.properties;

public class Property implements Comparable
  {
    private String key = null;
    private String value = null;
    private Location startLocation = new Location();
    private Location endLocation = new Location();

    public Property()
      {
      }

    public Property(String key, String value)
      {
        this.key = key;
        this.value = value;
      }

    public void setKey(String key)
      {
        this.key = key;
      }

    public String getKey()
      {
        return key;
      }

    public void setValue(String value)
      {
        this.value = value;
      }

    public String getValue()
      {
        return value;
      }

    public void setStartLocation(Location start)
      {
        startLocation = start;
      }

    public Location getStartLocation()
      {
        return startLocation;
      }

    public void setEndLocation(Location end)
      {
        endLocation = end;
      }

    public Location getEndLocation()
      {
        return endLocation;
      }

    public String toString()
      {
        String k = (key == null) ? "" : key;
        String v = (value == null) ? "" : value;
        String p = k + " = " + v;

        return p.equals(" = ") ? "" : p;
      }

    public int compareTo(Object o)
      {
        return toString().compareToIgnoreCase(o.toString());
      }
  }
