package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JTable;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.ScenarioTableModel;
import testdatagen.model.TestScenario;
import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

public class GenerateDataButtonListener implements ActionListener
{
	private JTable scenarioTable;
	
	public GenerateDataButtonListener(JTable scenarioTable)
	{
		this.scenarioTable = scenarioTable;
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		// check if a row is selected
		int row = scenarioTable.getSelectedRow();
		// no row selected
    	if(row < 0)
    	{
    		Utilities.showWarnPane("No row selected");
    	}
    	// row selected
    	else
    	{
    		// get selected scenario
        	ScenarioTableModel tableModel = (ScenarioTableModel) scenarioTable.getModel();
        	TestScenario selectedScenario = tableModel.getScenarioFromRow(row);
        	
        	// let user select a destination directory
    	    JFileChooser chooser = new JFileChooser();
    	    chooser.setDialogTitle("Choose destination directory");
    	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	    int returnVal = chooser.showSaveDialog(null);
    	    File parentOfDestDir = null;
    		if(returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			parentOfDestDir = chooser.getSelectedFile();
    		}
    		
    		// create output directory, using the scenario name as dir name
    		File destDir = new File(FilenameUtils.concat(parentOfDestDir.getPath(), selectedScenario.getName()));
    		try
    		{
    			destDir.mkdir();
    		}
    		catch (SecurityException exc)
    		{
    			Utilities.showErrorPane("Error: could not create output directory", exc);
    		}
    		
    		List<Title> titleList = selectedScenario.getTitleList();
    		ListIterator<Title> titleIterator = titleList.listIterator();
    		while(titleIterator.hasNext())
    		{
    			Title title = titleIterator.next();
    			// TODO: generate cover
    			// TODO: check, if cover is handled by MediaFileLink
    			// TODO: generate ONIX file
    			// TODO: iterate over all file Objects and generate the files
    		}
    	}
	}
}