/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;


/**
 *
 * @author Propriétaire
 */
public class TransformationErrorListener implements ErrorListener {
    private StringBuffer errors = new StringBuffer();

    public String getErrors() {
        return errors.toString();
    }

    private void report(TransformerException e) {
        errors.append(e.getMessageAndLocation() + "\n");
    }

    public void warning(TransformerException e) throws TransformerException {
        report(e);
    }

    public void error(TransformerException e) throws TransformerException {
        report(e);
    }

    public void fatalError(TransformerException e) throws TransformerException {
        report(e);
    }
}
