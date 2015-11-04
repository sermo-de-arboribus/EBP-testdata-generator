package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import testdatagen.TestDataGeneratorMain;
import testdatagen.gui.EditScenarioDialog;
import testdatagen.model.ScenarioTableModel;
import testdatagen.model.TestScenario;
import testdatagen.utilities.Utilities;

public class EditScenarioButtonListener implements ActionListener 
{
	private TestDataGeneratorMain programWindow;
	
	public EditScenarioButtonListener(final TestDataGeneratorMain programWindow)
	{
		this.programWindow = programWindow;
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		JTable scenarioTable = programWindow.getScenarioTable();
		
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
    	    new EditScenarioDialog(programWindow, selectedScenario);
    	}
	}
}
