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
package net.sf.xpontus.plugins.validation.batchvalidation;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.vfs.FileSelectInfo;
import org.apache.commons.vfs.FileSelector;


/**
 * @version 0.0.1
 * Wildcard file filter for VFS
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class WildcardFileSelector implements FileSelector {
    private boolean recursive;
    private String[] wildcards;

    /**
     * Constructor
     * @param recursive Recursive match
     * @param wildcards Array for wilcards
     */
    public WildcardFileSelector(boolean recursive, String[] wildcards) {
        this.recursive = recursive;
        this.wildcards = wildcards;
    }

    /**
     * Indicate whether or not a file should be included
     * @param fsi The file selection
     * @return whether or not a file should be included
     * @throws java.lang.Exception
     */
    public boolean includeFile(FileSelectInfo fsi) throws Exception {
        for (int i = 0; i < wildcards.length; i++) {
            String name = fsi.getFile().getName().getBaseName();

            if (FilenameUtils.wildcardMatch(name, wildcards[i],
                        IOCase.INSENSITIVE)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicate whether or not the file matching is recursive
     * @param fsi The file selections
     * @return whether or not the file matching is recursive
     * @throws java.lang.Exception
     */
    public boolean traverseDescendents(FileSelectInfo fsi)
        throws Exception {
        return recursive;
    }
}
