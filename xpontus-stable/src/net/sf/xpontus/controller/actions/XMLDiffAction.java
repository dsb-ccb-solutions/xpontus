/*
 * XMLDiffAction.java
 *
 * Created on May 29, 2006, 9:08 PM
 *
 *
 *  Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.controller.actions;

import java.awt.FlowLayout;
import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.view.XPontusWindow;
import oracle.xml.differ.XMLDiff;
import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import net.sf.xpontus.utils.EncodingHelper;
import org.xml.sax.InputSource;


/**
 *
 * @author Yves Zoundi
 */
public class XMLDiffAction extends ThreadedAction {
    private XMLDocument document1;
    private XMLDocument document2;
    private Log log = LogFactory.getLog(XMLDiffAction.class);
    
    /** Creates a new instance of XMLDiffAction */
    public XMLDiffAction() {
    }
    
    public void execute() {
        XMLDiff xmlDiff = new XMLDiff();
        xmlDiff.setDocuments(document1, document2);
        xmlDiff.diff();
        
        JTextPane pane1 = xmlDiff.getDiffPane1();
        JTextPane pane2 = xmlDiff.getDiffPane2();
        
        pane1.setEditable(false);
        pane2.setEditable(false);
        
        Dimension dim = new Dimension(300, 300);
        pane1.setPreferredSize(dim);
        pane1.setMinimumSize(dim);
        
        pane2.setPreferredSize(dim);
        pane2.setMinimumSize(dim);
        
        JFrame frame = XPontusWindow.getInstance().getFrame();
        final JDialog dialog = new JDialog(frame, true);
        
        JScrollPane sp1;
        JScrollPane sp2;
        sp1 = new JScrollPane(pane1);
        sp2 = new JScrollPane(pane2);
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setLeftComponent(sp1);
        split.setRightComponent(sp2);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(split, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(closeButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.setContentPane(panel);
        dialog.setTitle("XMLDiff results");
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    
    public void setFile1(File file) {
        try {
            DOMParser parser = new DOMParser();
            InputStream is = new FileInputStream(file);
            parser.parse(new InputSource(EncodingHelper.getReader(is)));
            this.document1 = parser.getDocument();
            log.info("Preparing file " + file.getAbsolutePath() +
                    " For xmldiff");
            log.info("I18n tests:" +
                    XPontusWindow.getInstance().getI18nMessage("close.key"));
        } catch (XMLParseException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setFile2(File file) {
        try {
            DOMParser parser = new DOMParser();
            InputStream is = new FileInputStream(file);
            parser.parse(new InputSource(EncodingHelper.getReader(is)));
            this.document2 = parser.getDocument();
            log.info("Preparing file " + file.getAbsolutePath() +
                    " For xmldiff");
        } catch (XMLParseException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }
    }
    
    public XMLDocument getDocument1() {
        return document1;
    }
    
    public void setDocument1(XMLDocument document1) {
        this.document1 = document1;
    }
    
    public XMLDocument getDocument2() {
        return document2;
    }
    
    public void setDocument2(XMLDocument document2) {
        this.document2 = document2;
    }
}
