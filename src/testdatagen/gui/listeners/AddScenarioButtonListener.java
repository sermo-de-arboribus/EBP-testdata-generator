package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import testdatagen.TestDataGeneratorMain;
import testdatagen.gui.NewScenarioDialog;
import testdatagen.model.TestScenario;

public class AddScenarioButtonListener implements ActionListener
{
	TestDataGeneratorMain programWindow;
	JDialog newProfileDialog;
	TestScenario newScenario;
	
	public AddScenarioButtonListener(TestDataGeneratorMain programWindow)
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
        		// generate a new scenario object
        		newScenario = new TestScenario(scenarioTitle);
        		
        		// set up the main dialog page for the new scenario
    	    	newProfileDialog = new NewScenarioDialog(programWindow, newScenario);
    	    	
    	    	// set dialog visible
    	    	newProfileDialog.setVisible(true);	
    		}
	}
	
	// this method shows a small dialog for entering a title and return the title
	private String askForTitle()
	{
		String s = (String)JOptionPane.showInputDialog(null, 
				"Enter a scenario title: ", 
				"Enter title", 
				JOptionPane.PLAIN_MESSAGE);
		return s;
	}
}