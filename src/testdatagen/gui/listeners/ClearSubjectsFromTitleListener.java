package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JTextArea;

import testdatagen.model.Subject;

public class ClearSubjectsFromTitleListener implements ActionListener
{
	private JTextArea subjectsField;
	private Set<Subject> subjectsSet;
	
	public ClearSubjectsFromTitleListener(final JTextArea subjectsField, final Set<Subject> subjectsSet)
	{
		this.subjectsField = subjectsField;
		this.subjectsSet = subjectsSet;
	}

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getActionCommand().equals("clear subjects"))
		{
			subjectsSet.clear();
			subjectsField.setText("");
			subjectsField.repaint();
		}
	}
}