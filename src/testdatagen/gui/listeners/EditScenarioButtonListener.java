package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import testdatagen.TestDataGeneratorMain;
import testdatagen.gui.EditScenarioDialog;
import testdatagen.model.ScenarioTableModel;
import testdatagen.model.TestScenario;
import testdatagen.utilities.Utilities;

/**
 * A listener to trigger edits of test scenarios. Delegates the editing to the EditScenarioDialog class
 */
public class EditScenarioButtonListener implements ActionListener 
{
	private TestDataGeneratorMain programWindow;

	/**
	 * Constructor
	 * @param programWindow The main window of the program
	 */
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
