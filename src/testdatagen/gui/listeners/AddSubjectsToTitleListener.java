package testdatagen.gui.listeners;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import testdatagen.model.Subject;

/**
 * This listener handles Onix subject configurations that are added to the product configuration.
 */
public class AddSubjectsToTitleListener implements ActionListener
{
	private JTextArea subjectsField;
	private Set<Subject> subjectsSet;
	
	/**
	 * Constructor
	 * @param subjectsField The JTextArea that displays the configured Onix subjects
	 * @param subjectsSet The Set that contains the currently configured Onix subjects
	 */
	public AddSubjectsToTitleListener(final JTextArea subjectsField, final Set<Subject> subjectsSet)
	{
		this.subjectsField = subjectsField;
		this.subjectsSet = subjectsSet;
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		// display the modal subjects configuration dialog
		if(evt.getActionCommand().equals("open subjects dialog"))
		{
			new SubjectsDialog(this);
		}
		// handle the added subjects
		else if (evt.getActionCommand().equals("subject submitted"))
		{
			JComponent subjectsDialog = (JComponent) evt.getSource();
			Container subjectsContainer = subjectsDialog.getTopLevelAncestor();
			SubjectsDialog submittedDialog = (SubjectsDialog) subjectsContainer;
			
			String subjectschemeidentifier = submittedDialog.getSubjectschemeidentifier();
			String subjectschemeversion = submittedDialog.getSubjectschemeversion();
			String subjectcode = submittedDialog.getSubjectcode();
			String subjectheadingtext = submittedDialog.getSubjectheadingtext();
			boolean isMainSubject = submittedDialog.isMainSubject();
			
			Subject newSubject = new Subject(subjectschemeidentifier, subjectschemeversion, subjectcode,
					subjectheadingtext, isMainSubject);
			subjectsSet.add(newSubject);
			StringBuffer labelText = new StringBuffer("");
			Iterator<Subject> iterator = subjectsSet.iterator();
			while(iterator.hasNext())
			{
				labelText.append(iterator.next().toString());
				if(iterator.hasNext())
				{
					labelText.append(", ");
				}
			}
			subjectsField.setText(labelText.toString());
			subjectsContainer.setVisible(false);
		}
	}
}

/**
 * Helper class for displaying the modal subject configuration dialog window
 */
class SubjectsDialog extends JDialog
{
	private static final long serialVersionUID = 2L;
	private JTextField subjectschemeidentifierField;
	private JTextField subjectschemeversionField;
	private JTextField subjectcodeField;
	private JTextField subjectheadingtextField;
	private JCheckBox mainsubjectCheckBox;
	
	/**
	 * Constructor
	 * @param actionListener The AddSubjectsToTitleListener that handles clicks to the "Submit" button
	 */
	public SubjectsDialog(final AddSubjectsToTitleListener actionListener)
	{
		this.setTitle("Add a subject");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		// centerPanel is for entering data
		JPanel centerPanel = new JPanel(new GridLayout(0,2));
		// southPanel is for the submit button
		JPanel southPanel = new JPanel(new FlowLayout());
		
		JLabel subjectschemeidentifierLabel = new JLabel("Subject Scheme Identifier");
		JLabel subjectschemeversionLabel = new JLabel("Subject Scheme Version");
		JLabel subjectcodeLabel = new JLabel("Subject Code");
		JLabel subjectheadingtextLabel = new JLabel("Subject Heading Text");
		JLabel isMainSubjectLabel = new JLabel("Main Subject");
		
		subjectschemeidentifierField = new JTextField("");
		subjectschemeidentifierField.setName("subjectschemeidentifier");
		subjectschemeversionField = new JTextField("");
		subjectschemeversionField.setName("subjectschemeversion");
		subjectcodeField = new JTextField("");
		subjectcodeField.setName("subjectcode");
		subjectheadingtextField = new JTextField("");
		subjectheadingtextField.setName("subjectheadingtext");
		mainsubjectCheckBox = new JCheckBox();
		mainsubjectCheckBox.setName("mainsubject");
		JButton submitButton = new JButton("submit");
		submitButton.setActionCommand("subject submitted");
		submitButton.addActionListener(actionListener);
		
		subjectschemeidentifierLabel.setLabelFor(subjectschemeidentifierField);
		subjectschemeversionLabel.setLabelFor(subjectschemeversionField);
		subjectcodeLabel.setLabelFor(subjectcodeField);
		subjectheadingtextLabel.setLabelFor(subjectheadingtextField);
		isMainSubjectLabel.setLabelFor(mainsubjectCheckBox);
		
		centerPanel.add(subjectschemeidentifierLabel);
		centerPanel.add(subjectschemeidentifierField);
		centerPanel.add(subjectschemeversionLabel);
		centerPanel.add(subjectschemeversionField);
		centerPanel.add(subjectcodeLabel);
		centerPanel.add(subjectcodeField);
		centerPanel.add(subjectheadingtextLabel);
		centerPanel.add(subjectheadingtextField);
		centerPanel.add(isMainSubjectLabel);
		centerPanel.add(mainsubjectCheckBox);
		
		southPanel.add(submitButton);
		
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		setLocation(200, 200);
		setSize(500, 200);
		setVisible(true);
	}
	
	/**
	 * Get the Onix subject scheme identifier code that was entered in the form
	 * @return String representing the Onix subject scheme identifier
	 */
	public String getSubjectschemeidentifier()
	{
		return subjectschemeidentifierField.getText();
	}

	/**
	 * Get the Onix subject scheme version description that was enteref in the dialog
	 * @return String representing the Onix subject scheme version
	 */
	public String getSubjectschemeversion()
	{
		return subjectschemeversionField.getText();
	}

	/**
	 * Get the Onix subject code that was entered in the dialog's form
	 * @return String representing the Onix subject code
	 */
	public String getSubjectcode()
	{
		return subjectcodeField.getText();
	}

	/**
	 * Get the Onix subject heading text that was entered in the dialog's form
	 * @return String representing the Onix subject heading text
	 */
	public String getSubjectheadingtext()
	{
		return subjectheadingtextField.getText();
	}

	/**
	 * Checks whether the new subject is marked as a main subject
	 * @return boolean true, if subject is a main subject
	 */
	public boolean isMainSubject()
	{
		return mainsubjectCheckBox.isSelected();
	}
}