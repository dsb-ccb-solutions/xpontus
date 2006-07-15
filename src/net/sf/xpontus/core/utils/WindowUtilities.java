/*
 * EnvironmentModel.java
 *
 * Created on 2 octobre 2005, 16:25
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
package net.sf.xpontus.core.utils;

import java.awt.*;


public class WindowUtilities {
    private WindowUtilities() {
    }

    /**
     *
     * @param window
     */
    public static final void centerOnScreen(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();

        if (windowSize.height > screenSize.height) {
            windowSize.height = screenSize.height;
        }

        if (windowSize.width > screenSize.width) {
            windowSize.width = screenSize.width;
        }

        window.setLocation((screenSize.width - windowSize.width) / 2,
            (screenSize.height - windowSize.height) / 2);
    }
}
