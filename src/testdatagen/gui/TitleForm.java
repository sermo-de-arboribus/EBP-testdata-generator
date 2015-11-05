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
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import testdatagen.gui.listeners.AddTitleToScenarioListener;
import testdatagen.model.ProductType;
import testdatagen.model.TestScenario;

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
		JLabel coverSelectionLabel = new JLabel("Choose cover delivery method");
		coverSelectionLabel.setForeground(Color.BLUE);
		JComboBox<String> covertype = new JComboBox<String>(coverOptions);
		covertype.setName("covertype");
		JLabel productTypeSelectionLabel = new JLabel("Choose product type");
		productTypeSelectionLabel.setForeground(Color.BLUE);
		JComboBox<String> product = new JComboBox<String>(ProductType.productTypeNames());
		product.setName("product");
		JLabel additionalAssetsLabel = new JLabel("Choose additional assets");
		additionalAssetsLabel.setForeground(Color.BLUE);
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
		JLabel productIdentifierSelectionLabel = new JLabel("Additional product identifiers");
		productIdentifierSelectionLabel.setForeground(Color.BLUE);
		JCheckBox prodIdGtin13 = new JCheckBox("GTIN-13");
		prodIdGtin13.setName("GTIN-13");
		JCheckBox prodIdDoi = new JCheckBox("DOI");
		prodIdDoi.setName("DOI");
		JCheckBox prodIdUrn = new JCheckBox("DOI");
		prodIdUrn.setName("URN");
		
		// Group form elements in left panel
		JPanel titleOptionsPanel = new JPanel(new GridLayout(0,1));
		titleOptionsPanel.setName("titleOptionsPanel");
		titleOptionsPanel.add(productTypeSelectionLabel);
		titleOptionsPanel.add(product);
		titleOptionsPanel.add(coverSelectionLabel);
		titleOptionsPanel.add(covertype);
		titleOptionsPanel.add(additionalAssetsLabel);
		titleOptionsPanel.add(scrBox);
		titleOptionsPanel.add(packBox);
		titleOptionsPanel.add(extrBox);
		titleOptionsPanel.add(bkcBox);
		titleOptionsPanel.add(epmoBox);
		
		// Group form elements in right panel
		JPanel detailOptionsPanel = new JPanel(new GridLayout(0,1));
		detailOptionsPanel.add(productIdentifierSelectionLabel);
		detailOptionsPanel.add(prodIdGtin13);
		detailOptionsPanel.add(prodIdDoi);
		detailOptionsPanel.add(prodIdUrn);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, titleOptionsPanel, detailOptionsPanel);
		
		// submit button
		JButton submitbtn = new JButton("submit");
		submitbtn.setActionCommand("title submitted");
		submitbtn.addActionListener(new AddTitleToScenarioListener(this,scenario));
		
		// add form elements to dialog and define dimensions
		this.add(header, BorderLayout.NORTH);
		this.add(splitPane, BorderLayout.CENTER);
		this.add(submitbtn, BorderLayout.SOUTH);
		this.setLocation(100,100);
		this.setSize(600, 500);
	}
}