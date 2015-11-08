package testdatagen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import testdatagen.gui.listeners.AddSubjectsToTitleListener;
import testdatagen.gui.listeners.AddTitleToScenarioListener;
import testdatagen.model.ProductType;
import testdatagen.model.Subject;
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
	// additional asset checkboxes
	private JCheckBox scrBox, packBox, extrBox, bkcBox, epmoBox;
	// additional product identifier checkboxes
	private JCheckBox prodIdGtin13, prodIdDoi, prodIdUrn;
	// additional product form detail checkboxes
	private JCheckBox a103, e200, e201, e202, e203;
	// additional title type checkboxes
	private JCheckBox tittyp03, tittyp06, tittyp08, tittyp11, tittyp13;
	// set of configured subjects
	private Set<Subject> configuredSubjects;
	
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
		
		// Product ID options
		JPanel productIdPanel = new JPanel(new GridLayout(0,1));
		JPanel productIdPanelOptions = new JPanel(new GridLayout(1,0));
		detailOptionsPanel.add(productIdPanel);
		productIdPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional Product Identifiers"));
		productIdPanel.add(productIdPanelOptions);
		productIdPanelOptions.add(prodIdGtin13);
		productIdPanelOptions.add(prodIdDoi);
		productIdPanelOptions.add(prodIdUrn);
		
		// Product Form Detail options
		JPanel productFormDetailPanel = new JPanel(new GridLayout(0,1));
		JPanel productFormDetailPanelOptions = new JPanel(new GridLayout(1,0));
		detailOptionsPanel.add(productFormDetailPanel);
		productFormDetailPanel.add(productFormDetailPanelOptions);
		productFormDetailPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional Product Form Detail"));
		productFormDetailPanelOptions.add(a103);
		productFormDetailPanelOptions.add(e200);
		productFormDetailPanelOptions.add(e201);
		productFormDetailPanelOptions.add(e202);
		productFormDetailPanelOptions.add(e203);
		
		// Title options
		JPanel titlePanel = new JPanel(new GridLayout(0,1));
		JPanel titlePanelOptions = new JPanel(new GridLayout(1,0));
		detailOptionsPanel.add(titlePanel);
		titlePanel.add(titlePanelOptions);
		titlePanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional Titles (select Title Type)"));
		titlePanelOptions.add(tittyp03);
		titlePanelOptions.add(tittyp06);
		titlePanelOptions.add(tittyp08);
		titlePanelOptions.add(tittyp11);
		titlePanelOptions.add(tittyp13);
		
		// Subject options
		JPanel subjectsPanel = new JPanel(new GridLayout(1,0));
		subjectsPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional subjects"));
		JTextArea configuredSubjectsField = new JTextArea();
		configuredSubjectsField.setLineWrap(true);
		configuredSubjectsField.setEditable(false);
		configuredSubjectsField.setColumns(40);
		JButton addSubjectsButton = new JButton("add subject");
		addSubjectsButton.setActionCommand("open subjects dialog");
		addSubjectsButton.addActionListener(new AddSubjectsToTitleListener(configuredSubjectsField, configuredSubjects));
		JPanel addSubjectsButtonPanel = new JPanel(new FlowLayout());
		addSubjectsButtonPanel.add(addSubjectsButton);
		subjectsPanel.add(configuredSubjectsField);
		subjectsPanel.add(addSubjectsButtonPanel);
		detailOptionsPanel.add(subjectsPanel);
		
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
	
	public Set<Subject> getConfiguredSubjects()
	{
		return configuredSubjects;
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
		formDataMap.put(a103.getName(), a103.isSelected() ? "true" : "false");
		formDataMap.put(e200.getName(), e200.isSelected() ? "true" : "false");
		formDataMap.put(e201.getName(), e201.isSelected() ? "true" : "false");
		formDataMap.put(e202.getName(), e202.isSelected() ? "true" : "false");
		formDataMap.put(e203.getName(), e203.isSelected() ? "true" : "false");
		formDataMap.put(tittyp03.getName(), tittyp03.isSelected() ? "true" : "false");
		formDataMap.put(tittyp06.getName(), tittyp06.isSelected() ? "true" : "false");
		formDataMap.put(tittyp08.getName(), tittyp08.isSelected() ? "true" : "false");
		formDataMap.put(tittyp11.getName(), tittyp11.isSelected() ? "true" : "false");
		formDataMap.put(tittyp13.getName(), tittyp13.isSelected() ? "true" : "false");
		
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
		prodIdGtin13.setName("gtin13");
		
		prodIdDoi = new JCheckBox("DOI");
		prodIdDoi.setName("doi");
		
		prodIdUrn = new JCheckBox("URN");
		prodIdUrn.setName("urn");
		
		a103 = new JCheckBox("A103");
		a103.setName("pfd-a103");
		a103.setToolTipText("MP3");
		
		e200 = new JCheckBox("E200");
		e200.setName("pfd-e200");
		e200.setToolTipText("reflowable");
		
		e201 = new JCheckBox("E201");
		e201.setName("pfd-e201");
		e201.setToolTipText("fixed layout");
		
		e202 = new JCheckBox("E202");
		e202.setName("pfd-e202");
		e202.setToolTipText("readable offline");
		
		e203 = new JCheckBox("E203");
		e203.setName("pfd-e203");
		e203.setToolTipText("requires network connection");
		
		tittyp03 = new JCheckBox("03");
		tittyp03.setName("tittyp03");
		tittyp03.setToolTipText("Title in original language");
		
		tittyp06 = new JCheckBox("06");
		tittyp06.setName("tittyp06");
		tittyp06.setToolTipText("Title in other language");
		
		tittyp08 = new JCheckBox("08");
		tittyp08.setName("tittyp08");
		tittyp08.setToolTipText("Former title");
		
		tittyp11 = new JCheckBox("11");
		tittyp11.setName("tittyp11");
		tittyp11.setToolTipText("Alternative title on cover");
		
		tittyp13 = new JCheckBox("13");
		tittyp13.setName("tittyp13");
		tittyp13.setToolTipText("Expanded title");
		
		configuredSubjects = new HashSet<Subject>();
	}
}