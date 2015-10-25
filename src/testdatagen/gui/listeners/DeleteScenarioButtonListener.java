package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import testdatagen.TestDataGeneratorMain;
import testdatagen.model.ScenarioTableModel;
import testdatagen.model.TestScenario;
import testdatagen.utilities.Utilities;

public class DeleteScenarioButtonListener implements ActionListener
{
	private TestDataGeneratorMain programWindow;
	
	public DeleteScenarioButtonListener(final TestDataGeneratorMain programWindow)
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
    		int returnVal = JOptionPane.showConfirmDialog(programWindow, "Are you sure you want to delete this scenario?", "", JOptionPane.YES_NO_OPTION);
    		if (returnVal == JOptionPane.YES_OPTION)
    		{
            	ScenarioTableModel tableModel = (ScenarioTableModel) scenarioTable.getModel();
            	TestScenario selectedScenario = tableModel.getScenarioFromRow(row);
            	tableModel.removeScenario(selectedScenario);
    		}
    	}
	}
}
