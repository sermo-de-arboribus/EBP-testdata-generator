package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JTextArea;

import testdatagen.model.Subject;

/**
 * Listener to clear the configured subjects field in the product configuration dialog.
 */
public class ClearSubjectsFromTitleListener implements ActionListener
{
	private JTextArea subjectsField;
	private Set<Subject> subjectsSet;
	
	/**
	 * Constructor
	 * @param subjectsField The JTextArea that displays the configured subjects
	 * @param subjectsSet The Set of subjects that are currently configured for this product
	 */
	public ClearSubjectsFromTitleListener(final JTextArea subjectsField, final Set<Subject> subjectsSet)
	{
		this.subjectsField = subjectsField;
		this.subjectsSet = subjectsSet;
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		if (evt.getActionCommand().equals("clear subjects"))
		{
			subjectsSet.clear();
			subjectsField.setText("");
			subjectsField.repaint();
		}
	}
}