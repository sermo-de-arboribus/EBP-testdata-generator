package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import testdatagen.TestDataGeneratorMain;
import testdatagen.model.ScenarioTableModel;
import testdatagen.utilities.Utilities;

public class LoadScenariosButtonListener implements ActionListener
{
	private TestDataGeneratorMain programWindow;
	
	public LoadScenariosButtonListener(final TestDataGeneratorMain mainFrame)
	{
		this.programWindow = mainFrame;
	}
	
	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("E-Book-Plant data files", "ebp");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(null);
	    File fileForOpening = null;
	    if(returnVal == JFileChooser.APPROVE_OPTION)
	    {
	    	fileForOpening = chooser.getSelectedFile();
	    }
	    ObjectInputStream loadScenarioObjects = null;
	    try
	    {
	    	loadScenarioObjects = new ObjectInputStream(new FileInputStream(fileForOpening));
	    	ScenarioTableModel scenarios = (ScenarioTableModel) loadScenarioObjects.readObject();
	    	programWindow.setScenarios(scenarios);
	    }
	    catch (IOException e)
	    {
	    	Utilities.showErrorPane("Error: could not open file", e);
	    }
	    catch (ClassNotFoundException e)
	    {
	    	Utilities.showErrorPane("Error: could not read object from File", e);
	    }
	    finally
	    {
	    	if(loadScenarioObjects != null)
	    	{
	    		Utilities.safeClose(loadScenarioObjects);
	    	}
	    }
	}
}