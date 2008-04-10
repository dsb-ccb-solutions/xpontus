package net.sf.xpontus.plugins.codecompletion.xml;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
 * User: mrcheeks
 * Date: Apr 10, 2008
 * Time: 1:02:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class XSDEntityResolver implements XMLEntityResolver {
    private final String baseid;

    public XSDEntityResolver(final String base) {
        baseid = base;
    }

    public XMLInputSource resolveEntity(
        XMLResourceIdentifier xmlResourceIdentifier)
        throws XNIException, IOException {
        System.out.println("baseid:" + baseid);
        xmlResourceIdentifier.setExpandedSystemId(baseid + "/" +
            xmlResourceIdentifier.getLiteralSystemId());
        System.out.println("getPublicId:" +
            xmlResourceIdentifier.getPublicId());
        System.out.println("getExpandedSystemId:" +
            xmlResourceIdentifier.getExpandedSystemId());
        System.out.println("getLiteralSystemId:" +
            xmlResourceIdentifier.getLiteralSystemId());
        System.out.println("getBaseSystemId:" +
            xmlResourceIdentifier.getBaseSystemId());

        return new XMLInputSource(xmlResourceIdentifier);
    }
}
