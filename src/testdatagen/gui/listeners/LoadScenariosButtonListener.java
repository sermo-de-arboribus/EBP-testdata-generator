package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import testdatagen.TestDataGeneratorMain;
import testdatagen.TestScenario;
import testdatagen.model.ScenarioTableModel;
import testdatagen.utilities.Utilities;

public class LoadScenariosButtonListener implements ActionListener
{
	private TestDataGeneratorMain mainFrame;
	
	public LoadScenariosButtonListener(TestDataGeneratorMain mainFrame)
	{
		this.mainFrame = mainFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt)
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
	    	Long lastIsbn = (Long) loadScenarioObjects.readObject();
	    	Utilities.loadLastISBN(lastIsbn);
	    	ScenarioTableModel scenarios = (ScenarioTableModel) loadScenarioObjects.readObject();
	    	mainFrame.setScenarios(scenarios);
	    }
	    catch (IOException e)
	    {
    		JOptionPane.showMessageDialog(null, "Error: could not save file " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    catch (ClassNotFoundException e)
	    {
	    	JOptionPane.showMessageDialog(null, "Error: could not read object from File " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
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