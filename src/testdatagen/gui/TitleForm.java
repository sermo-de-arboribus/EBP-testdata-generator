package testdatagen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import testdatagen.TestScenario;
import testdatagen.gui.listeners.AddTitleToScenarioListener;
import testdatagen.model.ProductType;

public class TitleForm extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TitleForm(TestScenario scenario)
	{
		this.setTitle("New title settings");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		// Header of the dialog
		JLabel header = new JLabel("Define the new title", SwingConstants.CENTER);
		header.setForeground(Color.RED);
		header.setFont(header.getFont().deriveFont(16.0f));
		
		// The form elements
		String[] coverOptions = {"Media file link", "Cover upload"};
		JLabel coverSelection = new JLabel("Choose cover delivery method");
		JComboBox covertype = new JComboBox(coverOptions);
		covertype.setName("covertype");
		JLabel productTypeSelection = new JLabel("Choose product type");
		JComboBox product = new JComboBox(ProductType.productTypeNames());
		product.setName("product");
		JCheckBox scrBox = new JCheckBox("Add screenshot assets");
		scrBox.setName("screenshot");
		JCheckBox packBox = new JCheckBox("Add 3D packshot assets");
		packBox.setName("packshot");
		JCheckBox extrBox = new JCheckBox("Add extract / demo asset");
		extrBox.setName("extracts");
		JCheckBox bkcBox = new JCheckBox("Add back cover");
		bkcBox.setName("backcover");
		JCheckBox epmoBox = new JCheckBox("Add EpubMobi");
		epmoBox.setName("epubmobi");
		
		// Group form elements in panel
		JPanel titleOptionsPanel = new JPanel(new GridLayout(0,1));
		titleOptionsPanel.setName("titleOptionsPanel");
		titleOptionsPanel.add(coverSelection);
		titleOptionsPanel.add(covertype);
		titleOptionsPanel.add(productTypeSelection);
		titleOptionsPanel.add(product);
		titleOptionsPanel.add(scrBox);
		titleOptionsPanel.add(packBox);
		titleOptionsPanel.add(extrBox);
		titleOptionsPanel.add(bkcBox);
		titleOptionsPanel.add(epmoBox);
		
		// submit button
		JButton submitbtn = new JButton("submit");
		submitbtn.setActionCommand("title submitted");
		submitbtn.addActionListener(new AddTitleToScenarioListener(this,scenario));
		
		// add form elements to dialog and define dimensions
		this.add(header, BorderLayout.NORTH);
		this.add(titleOptionsPanel, BorderLayout.CENTER);
		this.add(submitbtn, BorderLayout.SOUTH);
		this.setLocation(100,100);
		this.setSize(300, 600);
	}
}