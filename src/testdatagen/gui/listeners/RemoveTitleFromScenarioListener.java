package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import testdatagen.model.TitleTableModel;

public class RemoveTitleFromScenarioListener implements ActionListener 
{
	private JTable titleTable;
	
	public RemoveTitleFromScenarioListener(JTable titleTable)
	{
		this.titleTable = titleTable;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt)
	{
    //	ScenarioTableModel tableModel = (ScenarioTableModel) scenarioTable.getModel();
    	
    	int row = titleTable.getSelectedRow();
    	if(row < 0)
    	{
    		JOptionPane.showMessageDialog(null, "No row selected", "Warning",
    			JOptionPane.WARNING_MESSAGE);
    	}
    	else
    	{
    		TitleTableModel tableModel = (TitleTableModel) titleTable.getModel();
    		tableModel.removeTitle(row);
    	}
	}
}