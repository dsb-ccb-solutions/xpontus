package net.sf.xpontus.view.editor.syntax.dtd;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;
import net.sf.xpontus.view.editor.syntax.StyleInfo;

import java.awt.Color;


public class DTDColorProvider extends DefaultColorProvider
  {
    public DTDColorProvider()
      {
        StyleInfo attr = new StyleInfo();
        attr.bold = true;
        attr.color = Color.BLUE;

        int[] attrs = 
            {
                DTDParserConstants.FIXED, DTDParserConstants.CDATA,
                DTDParserConstants.ENTITY, DTDParserConstants.EMPTY,
                DTDParserConstants.ID, DTDParserConstants.IDREF,
                DTDParserConstants.IDREFS, DTDParserConstants.REQUIRED,
                DTDParserConstants.ATTLIST, DTDParserConstants.ELEMENT,
                DTDParserConstants.ANY
            };
        addAll(attrs, attr);

        StyleInfo strStyle = new StyleInfo();
        strStyle.color = Color.RED;
        strStyle.bold = true;

        addStyle(DTDParserConstants.QUOTEDSTR, strStyle);
      }
  }
