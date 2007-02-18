/*
 * PreferencesModel.java
 *
 *
 * Created on 1 août 2005, 17:46
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
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
package net.sf.xpontus.model;

import net.sf.xpontus.core.model.ObservableModel;


/**
 * A class to describe user preferences
 * @author Yves Zoundi
 */
public class PreferencesModel extends ObservableModel {
    private boolean showLineNumbers = true;
    private boolean showSplashScreen = true;
    private String xsltProcessor = "Saxon 6.5.4";
    private String xmlEncoding = "UTF-8";
    private String rate = "500";
    private String theme = "Plastic";

    /** Creates a new instance of PreferencesModel */
    public PreferencesModel() {
    }

    /**
     *
     * @return If the editor should display line numbers
     */
    public boolean isShowLineNumbers() {
        return showLineNumbers;
    }

    /**
     *
     * @param showLineNumbers Boolean value to indicate if the editor should display line numbers
     */
    public void setShowLineNumbers(boolean showLineNumbers) {
        this.showLineNumbers = showLineNumbers;
        updateView();
    }

    /**
     *
     * @return If the splash screen should be displayed at startup
     */
    public boolean isShowSplashScreen() {
        return showSplashScreen;
    }

    /**
     *
     * @param showSplashScreen A boolean value to specify if the splash screen should be display at startup
     */
    public void setShowSplashScreen(boolean showSplashScreen) {
        this.showSplashScreen = showSplashScreen;
        updateView();
    }

    /**
     *
     * @return The xslt processor to use for XSLT transformations
     */
    public String getXsltProcessor() {
        return xsltProcessor;
    }

    /**
     *
     * @param xsltProcessor The XSLT processor to use
     */
    public void setXsltProcessor(String xsltProcessor) {
        this.xsltProcessor = xsltProcessor;
        updateView();
    }

    /**
     *
     * @return The encoding of the output file of an XSLT transformation
     */
    public String getXmlEncoding() {
        return xmlEncoding;
    }

    /**
     *
     * @param xmlEncoding The XML encoding to use for the result of XSLT transformations
     */
    public void setXmlEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
        updateView();
    }

    /**
     *
     * @return The editor's caret blink reate
     */
    public String getRate() {
        return rate;
    }

    /**
     *
     * @param rate The editor's caret blink rate
     */
    public void setRate(String rate) {
        this.rate = rate;

        updateView();
    }

    /**
     *
     * @return The java look and feel to use
     */
    public String getTheme() {
        return theme;
    }

    /**
     *
     * @param theme The java look and feel to use
     */
    public void setTheme(String theme) {
        this.theme = theme;
        updateView();
    }
}
