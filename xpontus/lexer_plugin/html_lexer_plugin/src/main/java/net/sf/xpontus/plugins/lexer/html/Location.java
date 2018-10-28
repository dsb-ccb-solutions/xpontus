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
package net.sf.xpontus.plugins.lexer.html;


/**
 *
 * @author Yves Zoundi
 */
public class Location implements Comparable {
    public int line = 0;
    public int column = 0;

    public Location() {
    }

    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int compareTo(Object o) {
        Location loc = (Location) o;

        if (line < loc.line) {
            return -1;
        }

        if (line > loc.line) {
            return 1;
        }

        if (column < loc.column) {
            return -1;
        }

        if (column > loc.column) {
            return 1;
        }

        return 0;
    }

    public String toString() {
        return "Location:[line=" + line + ",column=" + column + "]";
    }
}
