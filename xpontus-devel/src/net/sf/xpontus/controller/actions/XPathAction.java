package net.sf.xpontus.controller.actions;

import java.io.BufferedReader;
import net.sf.saxon.Configuration;
import net.sf.saxon.xpath.XPathEvaluator;
import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.model.XPathResultsTableModel;
import net.sf.xpontus.view.ConsoleOutputWindow;
import net.sf.xpontus.view.XPontusWindow;
import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.xpath.NamespaceContextImpl;
import net.sf.saxon.xpath.StandaloneContext;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.w3c.dom.Document;
import org.w3c.dom.xpath.XPathNSResolver;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author Yves Zoundi
 *
 */
public class XPathAction extends ThreadedAction
  {
    
    private Map nsMap = new HashMap();
    
    class NamespaceURIResolv extends DefaultHandler2{
        
        public NamespaceURIResolv(){
            nsMap.clear();
        }
        
        public void startPrefixMapping(String prefix,
                               String uri)
                        throws SAXException{
            nsMap.put(prefix, uri);
            
        }
    }
    /**
     *
     */
    private static final long serialVersionUID = 2628764338166215003L;

    /**
     *
     */
    public XPathAction()
      {
        this.putValue(Action.NAME, "XPATH");
      }

    /* (non-Javadoc)
     * @see net.sf.xpontus.core.controller.actions.BaseAction#execute()
     */
    public void execute()
      {
        Object xpathExpr = XPontusWindow.getInstance().getXPathToolBar()
                                        .getXPathExpression();

        if (xpathExpr == null)
          {
            JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
                "Please enter an xpath expression");

            return;
          }

        String mExpression = xpathExpr.toString();
        ConsoleOutputWindow console = XPontusWindow.getInstance().getConsole();

        try
          {
            Configuration config = new Configuration();
            config.setLineNumbering(true); 
            JEditorPane edit = XPontusWindow.getInstance().getCurrentEditor();
            XPathEvaluator xpath = new XPathEvaluator(config);
            
            String mText = edit.getText();
            Reader mReader = new StringReader(mText);
            
            BufferedReader isr = new BufferedReader(mReader);
            
            SAXParser parser = new SAXParser();
            
            parser.setContentHandler(new NamespaceURIResolv());
            
                        InputSource mInputSource = new InputSource(isr);
                        
            parser.parse(mInputSource);
            

//            
//            Document domDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(new StringReader(mText))));
//            
        Source mSource = new SAXSource(new InputSource(new BufferedReader(new StringReader(mText))));
             
            NodeInfo info = xpath.setSource(mSource); 
            
            StandaloneContext std = new StandaloneContext(info);
            
            
            
            Iterator it = nsMap.keySet().iterator();
            
            while(it.hasNext()){
                String key = it.next().toString();
                String val = nsMap.get(key).toString();
                std.declareNamespace(key, val);
            }
            NamespaceContextImpl contextImpl = new NamespaceContextImpl(std);
            
            xpath.setNamespaceContext(contextImpl);
            List list = xpath.evaluate(mExpression);

            console.setFocus(ConsoleOutputWindow.XPATH_WINDOW);

            if (list.size() > 0)
              {
                XPontusWindow.getInstance().getStatusBar()
                             .setOperationMessage("Found " + list.size() +
                    " results");
                console.setResultsModel(new XPathResultsTableModel(list));
              }
            else
              {
                XPontusWindow.getInstance().getStatusBar()
                             .setOperationMessage("No results");
              }

            IOUtils.closeQuietly(mReader);
          }
        catch (Exception e)
          {
            e.printStackTrace();
            console.setFocus(ConsoleOutputWindow.ERRORS_WINDOW);
            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.ERRORS_WINDOW,
                e.getMessage());
            XPontusWindow.getInstance().getStatusBar()
                         .setOperationMessage("Error");
          }
      }
  }
