package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import testdatagen.TestDataGeneratorMain;
import testdatagen.model.ScenarioTableModel;
import testdatagen.utilities.Utilities;

public class SaveScenariosButtonListener implements ActionListener
{
	// private ScenarioTableModel scenarios;
	private TestDataGeneratorMain programWindow;

	public SaveScenariosButtonListener(final TestDataGeneratorMain programWindow)
	{
		this.programWindow = programWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt)
	{
	    JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("E-Book-Plant data files", "ebp");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(null);
	    File fileForSaving = null;
	    while(fileForSaving == null)
	    {
		    if(returnVal == JFileChooser.APPROVE_OPTION)
		    {
		    	fileForSaving = chooser.getSelectedFile();
		    }
	    }
	    // add desired file extension, if not entered by user
	    if (FilenameUtils.getExtension(fileForSaving.getName()).equalsIgnoreCase("ebp"))
	    {
	        // filename is OK as-is
	    }
	    else
	    {
	    	fileForSaving = new File(fileForSaving.toString() + ".ebp");
	    }
	    ObjectOutputStream saveScenarioObjects = null;
	    try
	    {
	    	ScenarioTableModel stm = programWindow.getScenarioTableModel();
		    saveScenarioObjects = new ObjectOutputStream(new FileOutputStream(fileForSaving));
		    saveScenarioObjects.writeObject(stm);
		    saveScenarioObjects.flush();
		    Utilities.showInfoPane("" + stm.getRowCount() + " scenarios saved to file " + fileForSaving.getName());
	    }
	    catch (IOException e)
	    {
	    	Utilities.showErrorPane("Error: could not save file", e);
    		e.printStackTrace();
	    }
	    finally
	    {
	    	if(saveScenarioObjects != null)
	    	{
	    		Utilities.safeClose(saveScenarioObjects);
	    	}
	    }
	}
}
