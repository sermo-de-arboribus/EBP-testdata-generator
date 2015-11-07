package testdatagen.gui.listeners;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import testdatagen.model.Subject;

public class AddSubjectsToTitleListener implements ActionListener
{
	private JTextArea subjectsField;
	private Set<Subject> subjectsSet;
	
	public AddSubjectsToTitleListener(final JTextArea subjectsField, final Set<Subject> subjectsSet)
	{
		this.subjectsField = subjectsField;
		this.subjectsSet = subjectsSet;
	}

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if(evt.getActionCommand().equals("open subjects dialog"))
		{
			new SubjectsDialog(this);
		}
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

class SubjectsDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JTextField subjectschemeidentifierField;
	private JTextField subjectschemeversionField;
	private JTextField subjectcodeField;
	private JTextField subjectheadingtextField;
	private JCheckBox mainsubjectCheckBox;
	
	public SubjectsDialog(AddSubjectsToTitleListener actionListener)
	{
		this.setTitle("Add a subject");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new GridLayout(0,2));
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
	
	public String getSubjectschemeidentifier()
	{
		return subjectschemeidentifierField.getText();
	}

	public String getSubjectschemeversion()
	{
		return subjectschemeversionField.getText();
	}

	public String getSubjectcode()
	{
		return subjectcodeField.getText();
	}

	public String getSubjectheadingtext()
	{
		return subjectheadingtextField.getText();
	}

	public boolean isMainSubject()
	{
		return mainsubjectCheckBox.isSelected();
	}
}