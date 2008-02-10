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

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.FileObject;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
class BatchValidationErrorHandler implements ErrorHandler {
    private StrBuilder errors = new StrBuilder();
    private FileObject fo;
    private int total = 0;
    
    public int getNumberOfErrors(){
        return total;
    }

    public BatchValidationErrorHandler() {
    }

    public String getErrorMessages(){
        return errors.toString();
    }
    
    public void reset() {
        errors.clear();
        total = 0;
    }

    public void setCurrentFile(FileObject fo) {
        this.fo = fo;
    }

    private void report(SAXParseException e) {
        total++;
        
        int line = e.getLineNumber();
        int column = e.getColumnNumber();
        String message = e.getLocalizedMessage();

        errors.append("**** File " + fo.getName().getURI() + " is invalid");
        errors.append("Error around line:" + line);
        errors.append(",column:" + column);
        errors.appendNewLine();
        errors.append(message);
        errors.appendNewLine();
    }

    public void warning(SAXParseException e) throws SAXException {
        report(e);
    }

    public void error(SAXParseException e) throws SAXException {
        report(e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        report(e);
    }
}
