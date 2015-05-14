package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.ScenarioTableModel;
import testdatagen.utilities.Utilities;

public class SaveScenariosButtonListener implements ActionListener
{
	private ScenarioTableModel scenarios;

	public SaveScenariosButtonListener(final ScenarioTableModel scenarios)
	{
		this.scenarios = scenarios;
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
		    saveScenarioObjects = new ObjectOutputStream(new FileOutputStream(fileForSaving));
		    saveScenarioObjects.writeObject(new Long(Utilities.saveLastISBN()));
		    saveScenarioObjects.writeObject(scenarios);
		    saveScenarioObjects.flush();
		    JOptionPane.showMessageDialog(null, "" + scenarios.getRowCount() + " scenarios saved to file " + fileForSaving.getName(), "Info", JOptionPane.INFORMATION_MESSAGE);
	    }
	    catch (IOException e)
	    {
    		JOptionPane.showMessageDialog(null, "Error: could not save file " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
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