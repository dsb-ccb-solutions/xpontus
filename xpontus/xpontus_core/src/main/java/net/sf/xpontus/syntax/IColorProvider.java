/*
 * IColorProvider.java
 *
 * Created on 18-Jul-2007, 8:51:00 AM
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
package net.sf.xpontus.syntax;

import java.util.Map;

import javax.swing.text.MutableAttributeSet;


/**
 * @author Yves Zoundi
 *
 */
public interface IColorProvider
{
    /**
     *
     * @return
     */
    public Map<Integer, MutableAttributeSet> getStyles();
}
