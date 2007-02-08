package test.completion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.*;

/**
 * Test program for {@link CalltipWindow}
 * 
 * @author ddjohnson
 * @version 1.0
 */
public class TestProgram
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame owner = new JFrame("Owner");
				owner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				owner.setSize(420,420);

				JMenuBar bar = new JMenuBar();
				owner.setJMenuBar(bar);
				JMenu menu = bar.add(new JMenu("File"));
				menu.setMnemonic('F');

				JMenuItem item = menu.add(new JMenuItem(new OpenAction(owner, "Open")));
				item.setMnemonic('O');

				owner.setVisible(true);
			}
		});
	}


	static class OpenAction extends AbstractAction
	{
		Frame owner;

		OpenAction(Frame owner, String name)
		{
			super(name);
			this.owner = owner;
		}

		public void actionPerformed(ActionEvent e)
		{
			CalltipWindow window = new CalltipWindow(owner);
			window.setListContents(Arrays.asList(new String[] {
				"one",
				"two",
				"three"
			}));
		}
	}

}
