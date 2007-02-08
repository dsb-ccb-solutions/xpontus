package test.completion;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.*;

/**
 * A simple window for displaying a list of strings.  The window
 * disposes itself when you press ESCAPE.
 *
 * TODO: x/y
 * TODO: width/height
 * TODO: addActionListener() method
 *
 * @author ddjohnson
 * @version 1.0
 */
public class CalltipWindow extends JWindow
{
	private JList list;
	private StringListModel listModel;

	/**
	 * Constructor.
	 *
	 * @see JWindow#JWindow(Frame)
	 *
	 * @param owner
	 */
	public CalltipWindow(Frame owner)
	{
		super(owner);
		initComponents();
		initWindow();
	}

	private void initComponents()
	{
		listModel = new StringListModel();
		list = new TipList(listModel);

		Container panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(list),BorderLayout.CENTER);

		setContentPane(panel);
	}

	private void initWindow()
	{
		setSize(120,90);
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	/**
	 * Returns the {@link StringListModel}
	 *
	 * @return listModel
	 */
	public StringListModel getListModel()
	{
		return listModel;
	}

	/**
	 * Sets the contents of the window to the given collection of
	 * strings.
	 *
	 * @param strings
	 */
	public void setListContents(Collection strings)
	{
		listModel.clear();
		listModel.addAll(strings);
	}

	/**
	 * Disposes the window and contents.
	 *
	 */
	public void closeWindow()
	{
		dispose();
	}

	class TipList extends JList
	{
		TipList(ListModel model)
		{
			super(model);
			enableEvents(AWTEvent.KEY_EVENT_MASK);
		}

		protected void processKeyEvent(KeyEvent e)
		{
			if (e.getID() == KeyEvent.KEY_PRESSED)
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_ESCAPE:
						closeWindow();
						break;
				}
			}
			super.processKeyEvent(e);
		}
	}
}



