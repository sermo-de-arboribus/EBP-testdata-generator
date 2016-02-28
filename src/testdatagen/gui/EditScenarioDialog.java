package testdatagen.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import testdatagen.TestDataGeneratorMain;
import testdatagen.gui.listeners.AddTitleToScenarioListener;
import testdatagen.gui.listeners.RemoveTitleFromScenarioListener;
import testdatagen.model.TestScenario;

/**
 * This class represents the modal dialog window that allows editing of a single scenario (add or remove
 * product titles).
 */
public class EditScenarioDialog extends JDialog
{
	private static final long serialVersionUID = 2L;
	// some values for visual appearance of the dialog window
	// width and height of the dialog window
	private static final int DIALOG_WIDTH = 1024;
	private static final int DIALOG_HEIGHT = 800;

	private JTable titleTable;
	
	/**
	 * Constructor. Initializes appearance and displays the dialog window
	 * @param mainWindow The parent window, which is the program's main window
	 * @param selectedScenario The scenario that was selected for editing in the main window
	 */
	public EditScenarioDialog(final TestDataGeneratorMain mainWindow, final TestScenario selectedScenario)
	{
		this.setTitle("edit a test scenario");

    	// panel for displaying currently created test data sets
    	buildTestDataTable(selectedScenario);
    	
    	// panel for adding a set of test data
    	// note: has to be built after the test data table
    	buildTopButtonPanel(selectedScenario);
    	
    	// add submit button
    	JButton submitButton = new JButton("submit");
    	submitButton.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent evt)
    		{
    			JButton button = (JButton) evt.getSource();
    			JDialog dialog = (JDialog) button.getTopLevelAncestor();
    			mainWindow.repaint();
    			dialog.dispose();
    		}
    	});
    	this.add(submitButton, BorderLayout.SOUTH);
    	
    	// set dialog visible
    	this.setModalityType(ModalityType.APPLICATION_MODAL);
    	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    	this.setLocation(50,50);
    	this.setSize(DIALOG_WIDTH,DIALOG_HEIGHT);
	    this.setVisible(true);
	}
	
	private void buildTopButtonPanel(final TestScenario scenario)
	{
		// create panel and buttons
		JPanel topButtonPanel = new JPanel();
		JButton addButton = new JButton("Add title to scenario");
		addButton.addActionListener(new AddTitleToScenarioListener(this, scenario));
		JButton removeButton = new JButton("Remove selected title(s) from scenario");
		removeButton.addActionListener(new RemoveTitleFromScenarioListener(titleTable));

		// add buttons to panel
		topButtonPanel.add(addButton);
		topButtonPanel.add(removeButton);
		
		// add panel to dialog
		this.add(topButtonPanel, BorderLayout.NORTH);
	}
	
	private void buildTestDataTable(final TestScenario scenario)
	{
	    titleTable = new TitleTable(scenario.getTitleTableModel());
	    JScrollPane scrollpane = new JScrollPane(titleTable);
	    this.add(scrollpane, BorderLayout.CENTER);
	}
}