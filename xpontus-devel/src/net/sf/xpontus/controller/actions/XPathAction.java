package net.sf.xpontus.controller.actions;

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

import java.util.List;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;


/**
 * @author Yves Zoundi
 *
 */
public class XPathAction extends ThreadedAction
  {
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
            InputSource mInputSource = new InputSource(mReader);
            Source mSource = new SAXSource(mInputSource);
            xpath.setSource(mSource);

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
            console.setFocus(ConsoleOutputWindow.ERRORS_WINDOW);
            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.ERRORS_WINDOW,
                e.getMessage());
            XPontusWindow.getInstance().getStatusBar()
                         .setOperationMessage("Error");
          }
      }
  }
