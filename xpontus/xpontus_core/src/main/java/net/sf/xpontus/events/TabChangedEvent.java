/**
 * 
 */
package net.sf.xpontus.events;

import java.util.EventObject;

import net.sf.xpontus.modules.gui.components.IDocumentContainer;

/**
 * @author Yves Zoundi
 *
 */
public class TabChangedEvent extends EventObject{

	private final IDocumentContainer documentContainer;

	/**
	 * @param documentContainer
	 */
	public TabChangedEvent(final IDocumentContainer documentContainer) {
		super(documentContainer);
		this.documentContainer = documentContainer;
	}
	
	/* (non-Javadoc)
	 * @see java.util.EventObject#getSource()
	 */
	public IDocumentContainer getSource(){
		return documentContainer;
	}
	
}
