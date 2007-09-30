// Placed in public domain by Dmitry Olshansky, 2006
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import java.io.InputStreamReader;

import org.onemind.commons.java.util.FileUtils;
import org.onemind.jxp.CachedJxpPage;
import org.onemind.jxp.CachingPageSource;
import org.onemind.jxp.JxpPage;
import org.onemind.jxp.JxpPageNotFoundException;
import org.onemind.jxp.JxpPageParseException;
import org.onemind.jxp.parser.AstJxpDocument;
import org.onemind.jxp.parser.JxpParser;

/**
 * JXP page source configured to load templates from the classpath.
 * @version $Id: ClassPathPageSource.java,v 1.3 2006/02/23 16:44:05 ddimon Exp $
 */
final class ClassPathPageSource extends CachingPageSource {
    private final String base;
    private final ClassLoader cl;
    private final String encoding;

    ClassPathPageSource(final String basePath, final String enc) {
        super();
        this.base = basePath;
        this.encoding = enc;
        cl = getClass().getClassLoader();
    }

    /**
     * @see org.onemind.jxp.CachingPageSource#loadJxpPage(java.lang.String)
     */
    protected CachedJxpPage loadJxpPage(final String id)
            throws JxpPageNotFoundException {
        if (!hasJxpPage(id)) {
            throw new JxpPageNotFoundException("Page " + id + " not found");
        }
        return new CachedJxpPage(this, id);
    }

    /**
     * @see org.onemind.jxp.CachingPageSource#parseJxpDocument(
     *      org.onemind.jxp.JxpPage)
     */
    protected AstJxpDocument parseJxpDocument(final JxpPage page)
            throws JxpPageParseException {
        try {
            JxpParser parser;
            if (encoding == null) {
                parser = new JxpParser(cl.getResourceAsStream(
                        getStreamName(page.getName())));
            } else {
                parser = new JxpParser(new InputStreamReader(
                        cl.getResourceAsStream(getStreamName(page.getName())),
                        encoding));
            }
            return parser.JxpDocument();
        } catch (Exception e) {
            throw new JxpPageParseException("Problem parsing page "
                    + page.getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * @see org.onemind.jxp.JxpPageSource#hasJxpPage(java.lang.String)
     */
    public boolean hasJxpPage(final String id) {
        if (isJxpPageCached(id)) {
            return true;
        }
        return cl.getResource(getStreamName(id)) != null;
    }
    
    private String getStreamName(final String pageName) {
        return FileUtils.concatFilePath(base, pageName);
    }
}
