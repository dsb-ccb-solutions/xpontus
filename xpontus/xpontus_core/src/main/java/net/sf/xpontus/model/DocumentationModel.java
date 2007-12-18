/*
 *
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
 */
package net.sf.xpontus.model;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DocumentationModel {
    private String type;
    private String title;
    private String input;
    private String output;
    private String header;
    private String footer;
    private String css;

    /**
     * 
     * @return
     */
    public String getCss() {
        return css;
    }

    /**
     * 
     * @param css
     */
    public void setCss(String css) {
        this.css = css;
    }

    /**
     * 
     * @return
     */
    public String getFooter() {
        return footer;
    }

    /**
     * 
     * @param footer
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

    /**
     * 
     * @return
     */
    public String getHeader() {
        return header;
    }

    /**
     * 
     * @param header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * 
     * @return
     */
    public String getInput() {
        return input;
    }

    /**
     * 
     * @param input
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * 
     * @return
     */
    public String getOutput() {
        return output;
    }

    /**
     * 
     * @param output
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * 
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
}
