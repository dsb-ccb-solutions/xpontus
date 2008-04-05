/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.text.StrBuilder;


/**
 * Default transformation error listener
 * @version
 * @author Yves Zoundi
 */
public class TransformationErrorListener implements ErrorListener {
    private StrBuilder errors = new StrBuilder();
    private StrBuilder warnings = new StrBuilder();
    private boolean hasWarnings = false;

    /**
     *
     * @return
     */
    public String getErrors() {
        return errors.toString();
    }

    /**
     *
     * @return
     */
    public boolean warningsFound() {
        return hasWarnings;
    }

    /**
     *
     * @return
     */
    public String getWarnings() {
        return warnings.toString();
    }

    
    /**
     * @param e
     */
    private void report(TransformerException e) {
        errors.append(e.getMessageAndLocation());
        errors.appendNewLine();
    }

    /* (non-Javadoc)
     * @see javax.xml.transform.ErrorListener#warning(javax.xml.transform.TransformerException)
     */
    public void warning(TransformerException e) throws TransformerException {
        if (!hasWarnings) {
            hasWarnings = true;
        }

        warnings.append(e.getMessage());
        warnings.appendNewLine();
    }

    /* (non-Javadoc)
     * @see javax.xml.transform.ErrorListener#error(javax.xml.transform.TransformerException)
     */
    public void error(TransformerException e) throws TransformerException {
        report(e);
    }

    /* (non-Javadoc)
     * @see javax.xml.transform.ErrorListener#fatalError(javax.xml.transform.TransformerException)
     */
    public void fatalError(TransformerException e) throws TransformerException {
        report(e);
    }
}
