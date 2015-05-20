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