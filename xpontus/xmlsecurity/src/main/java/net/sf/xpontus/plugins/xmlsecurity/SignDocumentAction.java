/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.xmlsecurity;

import net.sf.xpontus.actions.impl.XPontusThreadedActionImpl;
import net.sf.xpontus.utils.XPontusComponentsUtils;


/**
 *
 * @author Proprietary
 */
public class SignDocumentAction extends XPontusThreadedActionImpl {
    public SignDocumentAction() {
        setName("Sign document");
        setDescription("Sign document");
    }

    public void run() {
        XPontusComponentsUtils.showErrorMessage("Not yet implemented");
    }
}
