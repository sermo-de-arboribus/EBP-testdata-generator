package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.*;

import org.apache.commons.io.FilenameUtils;

import testdatagen.GeneratorThread;
import testdatagen.TestDataGeneratorMain;
import testdatagen.model.*;
import testdatagen.utilities.Utilities;

/**
 * This listener handles clicks to the "Generate" button and updates the progress bar while the 
 * file generation is running. A FileChooser is displayed to let the user select a destination directory
 */
public class GenerateDataButtonListener implements ActionListener, PropertyChangeListener
{
	private TestDataGeneratorMain programWindow;
	private GeneratorThread genThread;
	private File destDir;
	
	/**
	 * Constructor
	 * @param programWindow The main program window
	 */
	public GenerateDataButtonListener(TestDataGeneratorMain programWindow)
	{
		this.programWindow = programWindow;
	}
	
	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		JTable scenarioTable = programWindow.getScenarioTable();
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
	    		// create output directory, using the scenario name as dir name
	    		destDir = new File(FilenameUtils.concat(parentOfDestDir.getPath(), selectedScenario.getName()));
	    		try
	    		{
	    			destDir.mkdir();
	    		}
	    		catch (SecurityException exc)
	    		{
	    			Utilities.showErrorPane("Error: could not create output directory", exc);
	    		}
	    		
	    		List<Title> titleList = selectedScenario.getTitleList();
	    		genThread = new GeneratorThread(titleList, destDir);
	    		genThread.addPropertyChangeListener(this);
	    		genThread.execute();
	    	}
    	}
	}
	
	@Override
	public void propertyChange(final PropertyChangeEvent evt)
	{
		JProgressBar progressBar = programWindow.getProgressBar();
		if(genThread.getProgress() == 0 && !evt.getNewValue().equals("DONE"))
		{
			progressBar.setIndeterminate(true);
		}
		else if(evt.getPropertyName().equals("progress"))
		{
			progressBar.setIndeterminate(false);
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
	        programWindow.getProgressLabel().setText(String.format("Generating files, %d%% completed.\n", genThread.getProgress()));
		}
		else //(evt.getNewValue().equals("DONE"))
		{
			programWindow.getProgressLabel().setText("Generating files completed. Files saved in " + destDir.getPath());
		}
	}
}