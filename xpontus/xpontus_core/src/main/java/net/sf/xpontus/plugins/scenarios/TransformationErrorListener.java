/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;


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

    //
    private void report(TransformerException e) {
        errors.append(e.getMessageAndLocation());
        errors.appendNewLine();
    }

    public void warning(TransformerException e) throws TransformerException {
        if (!hasWarnings) {
            hasWarnings = true;
        }

        warnings.append(e.getMessage());
        warnings.appendNewLine();
    }

    public void error(TransformerException e) throws TransformerException {
        report(e);
    }

    public void fatalError(TransformerException e) throws TransformerException {
        report(e);
    }
}
