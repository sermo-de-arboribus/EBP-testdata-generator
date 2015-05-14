package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;

import testdatagen.TestDataGeneratorMain;
import testdatagen.gui.EditScenarioDialog;

public class EditScenarioButtonListener implements ActionListener 
{
	private EditScenarioDialog newProfileDialog;
	private JTable scenarioTable;
	
	public EditScenarioButtonListener(JTable scenarioTable)
	{
		this.scenarioTable = scenarioTable;
	}

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		// get the source of the event
		JButton source = (JButton) evt.getSource();
		TestDataGeneratorMain mainWindow = (TestDataGeneratorMain) source.getTopLevelAncestor();
		
	    newProfileDialog = new EditScenarioDialog(mainWindow, scenarioTable);
	}
}