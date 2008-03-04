/*
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 */
package net.sf.xpontus.plugins.perspectives;

import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.properties.PropertiesHolder;

import java.util.List;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PerspectiveHelper {
    private PerspectiveHelper() {
    }

    /**
     *
     * @param mimeType
     * @return
     */
    public static IDocumentContainer createPerspective(String mimeType) {
        PerspectivePluginIF m_perspective = null;

        List<PerspectivePluginIF> availablePerspectives = (List<PerspectivePluginIF>) PropertiesHolder.getPropertyValue(XPontusPropertiesConstantsIF.XPONTUS_PERSPECTIVES);

        for (PerspectivePluginIF currentPerspective : availablePerspectives) {
            String m_mime = currentPerspective.getContentType();

            if (m_mime.equals(mimeType)) {
                m_perspective = currentPerspective;

                break;
            }
        }

        if (m_perspective == null) {
            m_perspective = new DefaultPerspectiveImpl();
        }

        return m_perspective.createDocumentContainer();
    }
}
