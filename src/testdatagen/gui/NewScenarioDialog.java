package testdatagen.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import testdatagen.TestDataGeneratorMain;
import testdatagen.gui.listeners.AddTitleToScenarioListener;
import testdatagen.gui.listeners.RemoveTitleFromScenarioListener;
import testdatagen.model.ScenarioTableModel;
import testdatagen.model.TestScenario;

/**
 * This class represents the modal dialog for adding a new scenario to the running program
 */
public class NewScenarioDialog extends JDialog
{
	private static final long serialVersionUID = 2L;
	private TestScenario scenario;
	private TestDataGeneratorMain mainWindow;
	private JTable titleTable;
	
	// some values for visual appearance of the dialog window
	// width and height of the dialog window
	private static final int DIALOG_WIDTH = 1024;
	private static final int DIALOG_HEIGHT = 800;
	
	/**
	 * Constructor of modal dialog window. Configures the visual appearance and displays the window.
	 * @param mainWindow The parent window, which is the main window of the program
	 * @param newScenario The new scenario object, which is to be configured and added
	 */
	public NewScenarioDialog(final TestDataGeneratorMain mainWindow, final TestScenario newScenario)
	{
		if(mainWindow == null || newScenario == null)
		{
			throw new IllegalArgumentException("NewProfileDialog constructor should not be given null paramater as an argument!");
		}
    	this.setTitle("create new test scenario");
    	this.scenario = newScenario;
    	this.mainWindow = mainWindow;
    	
    	// panel for displaying currently created test data sets
    	buildTestDataTable(scenario.getName());
    	
    	// panel for adding a test data item
    	buildTopButtonPanel();
    	
    	// add submit and cancel buttons
    	buildSubmitPanel();
    	
    	this.setModalityType(ModalityType.APPLICATION_MODAL);
    	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    	this.setLocation(50,50);
    	this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
	}
	
	// configure panel with submit button and an anonymous action listener
	private void buildSubmitPanel()
	{
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		JButton submitButton = new JButton("add scenario");
		submitButton.addActionListener(new ActionListener()
		{
    		public void actionPerformed(ActionEvent evt)
    		{
    			JButton button = (JButton) evt.getSource();
    			JDialog dialog = (JDialog) button.getTopLevelAncestor();
    			JTable scenarioTable = mainWindow.getScenarioTable();
    			ScenarioTableModel scenarioModel = (ScenarioTableModel) scenarioTable.getModel();
    			scenarioModel.addScenario(scenario);
    			scenarioTable.updateUI();
    			dialog.dispose();
    		}
		});
		buttonPanel.add(submitButton, BorderLayout.SOUTH);
		
    	JButton cancelButton = new JButton("cancel");
    	cancelButton.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent evt)
    		{
    			JButton button = (JButton) evt.getSource();
    			JDialog dialog = (JDialog) button.getTopLevelAncestor();
    			dialog.dispose();
    		}
    	});
    	buttonPanel.add(cancelButton, BorderLayout.SOUTH);
    	this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void buildTestDataTable(String title)
	{
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		
		JLabel scenarioTitle = new JLabel(title);
		centerPanel.add(scenarioTitle, BorderLayout.NORTH);
		
		titleTable = new TitleTable(scenario.getTitleTableModel());
		
	    JScrollPane scrollpane = new JScrollPane(titleTable);
	    centerPanel.add(scrollpane, BorderLayout.CENTER);
	    
	    this.add(centerPanel, BorderLayout.CENTER);
	}
	
	private void buildTopButtonPanel()
	{
		// create panel and buttons
		JPanel topButtonPanel = new JPanel();
		JButton addButton = new JButton("Add test title");
		addButton.addActionListener(new AddTitleToScenarioListener(this, scenario));
		JButton removeButton = new JButton("Remove selected test title");
		removeButton.addActionListener(new RemoveTitleFromScenarioListener(titleTable));
		
		// add buttons to panel
		topButtonPanel.add(addButton);
		topButtonPanel.add(removeButton);
		
		// add panel to dialog
		this.add(topButtonPanel, BorderLayout.NORTH);
	}
}