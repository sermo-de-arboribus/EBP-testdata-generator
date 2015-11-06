package testdatagen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

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
		Border blueLineBorder = BorderFactory.createLineBorder(Color.BLUE);
		
		// Group form elements in left panel
		JPanel titleOptionsPanel = new JPanel(new GridLayout(0,1));
		JPanel productTypeSelectionPanel = new JPanel(new GridLayout(0,1));
		productTypeSelectionPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Product Type Selection"));
		JPanel coverSelectionPanel = new JPanel(new GridLayout(0,1));
		coverSelectionPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Cover delivery method"));
		JPanel additionalAssetsPanel = new JPanel(new GridLayout(0,1));
		additionalAssetsPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional assets"));
		
		titleOptionsPanel.add(productTypeSelectionPanel);
		titleOptionsPanel.add(coverSelectionPanel);
		titleOptionsPanel.add(additionalAssetsPanel);
		
		productTypeSelectionPanel.add(product);
		coverSelectionPanel.add(covertype);
		additionalAssetsPanel.add(scrBox);
		additionalAssetsPanel.add(packBox);
		additionalAssetsPanel.add(extrBox);
		additionalAssetsPanel.add(bkcBox);
		additionalAssetsPanel.add(epmoBox);
		
		// Group form elements in right panel
		JPanel detailOptionsPanel = new JPanel(new GridLayout(0,1));
		JPanel productIdPanel = new JPanel(new GridLayout(0,1));
		JPanel productIdPanelOptions = new JPanel(new GridLayout(1,0));
		detailOptionsPanel.add(productIdPanel);
		detailOptionsPanel.add(Box.createHorizontalGlue());
		productIdPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional product identifiers"));
		productIdPanel.add(productIdPanelOptions);
		productIdPanelOptions.add(prodIdGtin13);
		productIdPanelOptions.add(prodIdDoi);
		productIdPanelOptions.add(prodIdUrn);
		
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