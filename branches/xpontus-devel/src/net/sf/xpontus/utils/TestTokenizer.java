/*
 * TestTokenizer.java
 *
 * Created on July 28, 2006, 6:50 PM
 *
 *
 *  Copyright (C) 2005 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.utils;

import java.util.StringTokenizer;


/**
 *
 * @author Yves Zoundi
 */
public class TestTokenizer
  {
    /** Creates a new instance of TestTokenizer */
    public TestTokenizer()
      {
      }

    public static void main(String[] argv)
      {
        String s = " beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\"";
        StringTokenizer st = new StringTokenizer(s);
        String str = null;

        while (st.hasMoreTokens())
          {
            str = st.nextToken().toString();
          }

        System.out.println(str.substring(1, str.length() - 1));
      }
  }
