package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;

import testdatagen.TestDataGeneratorMain;
import testdatagen.gui.EditScenarioDialog;
import testdatagen.model.ScenarioTableModel;
import testdatagen.model.TestScenario;
import testdatagen.utilities.Utilities;

public class EditScenarioButtonListener implements ActionListener 
{
	TestDataGeneratorMain programWindow;
	
	public EditScenarioButtonListener(final TestDataGeneratorMain programWindow)
	{
		this.programWindow = programWindow;
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		JTable scenarioTable = programWindow.getScenarioTable();
		// get the source of the event
		JButton source = (JButton) evt.getSource();
		TestDataGeneratorMain mainWindow = (TestDataGeneratorMain) source.getTopLevelAncestor();
		
		// check if a row is selected
		int row = scenarioTable.getSelectedRow();
    	if(row < 0)
    	{
    		Utilities.showWarnPane("No row selected");
    	}
    	else
    	{
        	ScenarioTableModel tableModel = (ScenarioTableModel) scenarioTable.getModel();
        	TestScenario selectedScenario = tableModel.getScenarioFromRow(row);
    	    new EditScenarioDialog(mainWindow, selectedScenario);
    	}
	}
}