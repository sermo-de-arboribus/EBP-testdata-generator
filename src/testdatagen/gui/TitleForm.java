package testdatagen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

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
	/*
	 * Definitions of constants for selections
	 */
	private static final String[] COVER_OPTIONS = {"Media file link", "Cover upload"};
	private static final long serialVersionUID = 1L;
	
	/*
	 * This form keeps the state of its combo and check boxes and makes the values available in some data structure
	 */
	private JComboBox<String> covertype;
	private JComboBox<String> product;
	private JCheckBox scrBox;
	private JCheckBox packBox;
	private JCheckBox extrBox;
	private JCheckBox bkcBox;
	private JCheckBox epmoBox;
	private JCheckBox prodIdGtin13;
	private JCheckBox prodIdDoi;
	private JCheckBox prodIdUrn;
	
	public TitleForm(TestScenario scenario)
	{
		initializeSelectionElements();
		
		this.setTitle("New title settings");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		// Header of the dialog
		JLabel header = new JLabel("Define the new title", SwingConstants.CENTER);
		header.setForeground(Color.RED);
		header.setFont(header.getFont().deriveFont(16.0f));
		
		// The form elements
		
		// instantiate labels for different sections
		JLabel productTypeSelectionLabel = new JLabel("Choose product type");
		productTypeSelectionLabel.setForeground(Color.BLUE);
		
		JLabel coverSelectionLabel = new JLabel("Choose cover delivery method");
		coverSelectionLabel.setForeground(Color.BLUE);
		
		JLabel additionalAssetsLabel = new JLabel("Choose additional assets");
		additionalAssetsLabel.setForeground(Color.BLUE);

		JLabel productIdentifierSelectionLabel = new JLabel("Additional product identifiers");
		productIdentifierSelectionLabel.setForeground(Color.BLUE);
		
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
	
	public Map<String, String> getFormDataMap()
	{
		HashMap<String, String> formDataMap = new HashMap<>();
		
		formDataMap.put(covertype.getName(), (String) covertype.getSelectedItem());
		formDataMap.put(product.getName(), (String) product.getSelectedItem());
		formDataMap.put(scrBox.getName(), scrBox.isSelected() ? "true" : "false");
		formDataMap.put(packBox.getName(), packBox.isSelected() ? "true" : "false");
		formDataMap.put(extrBox.getName(), extrBox.isSelected() ? "true" : "false");
		formDataMap.put(bkcBox.getName(), bkcBox.isSelected() ? "true" : "false");
		formDataMap.put(epmoBox.getName(), epmoBox.isSelected() ? "true" : "false");
		formDataMap.put(prodIdGtin13.getName(), prodIdGtin13.isSelected() ? "true" : "false");
		formDataMap.put(prodIdDoi.getName(), prodIdDoi.isSelected() ? "true" : "false");
		formDataMap.put(prodIdUrn.getName(), prodIdUrn.isSelected() ? "true" : "false");
		
		return formDataMap;
	}
	
	private void initializeSelectionElements()
	{
		covertype = new JComboBox<String>(COVER_OPTIONS);
		covertype.setName("covertype");
		
		product = new JComboBox<String>(ProductType.productTypeNames());
		product.setName("product");
		
		scrBox = new JCheckBox("Add screenshot assets");
		scrBox.setName("screenshot");
		
		packBox = new JCheckBox("Add 3D packshot assets");
		packBox.setName("packshot");
		
		extrBox = new JCheckBox("Add extract / demo asset");
		extrBox.setName("extracts");
		
		bkcBox = new JCheckBox("Add back cover");
		bkcBox.setName("backcover");
		
		epmoBox = new JCheckBox("Add EpubMobi");
		epmoBox.setName("epubmobi");
		
		prodIdGtin13 = new JCheckBox("GTIN-13");
		prodIdGtin13.setName("GTIN-13");
		
		prodIdDoi = new JCheckBox("DOI");
		prodIdDoi.setName("DOI");
		
		prodIdUrn = new JCheckBox("DOI");
		prodIdUrn.setName("URN");
	}
}