package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import testdatagen.TestDataGeneratorMain;
import testdatagen.gui.NewScenarioDialog;
import testdatagen.model.TestScenario;

/**
 * Listener close to handle adding new test scenarios to the program
 */
public class AddScenarioButtonListener implements ActionListener
{
	TestDataGeneratorMain programWindow;

	/**
	 * Constructor. 
	 * @param programWindow The main window JFrame of the program
	 */
	public AddScenarioButtonListener(final TestDataGeneratorMain programWindow)
	{
		this.programWindow = programWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt)
	{
    		// ask user to enter a new scenario title
    		String scenarioTitle = askForTitle();
    	
    		// check if user entered a value
    		if((scenarioTitle != null) && scenarioTitle.length() > 0)
    		{	
        		// set up the main dialog page for the new scenario
    			JDialog newProfileDialog = new NewScenarioDialog(programWindow, new TestScenario(scenarioTitle));
    	    	
    	    	// set dialog visible
    	    	newProfileDialog.setVisible(true);	
    		}
	}
	
	// this method shows a small dialog for entering a title and returns the title
	private String askForTitle()
	{
		String s = (String)JOptionPane.showInputDialog(null, 
				"Enter a scenario title: ", 
				"Enter title", 
				JOptionPane.PLAIN_MESSAGE);
		return s;
	}
}
