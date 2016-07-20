package testdatagen.gui;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

import testdatagen.gui.listeners.*;
import testdatagen.model.*;
import testdatagen.onixbuilder.OnixNotificationTypeBuilder;
import testdatagen.onixbuilder.OnixProductAvailabilityBuilder;

/**
 * This modal dialog represents the product details configuration window, which is displayed when a user
 * adds a new product to a scenario.
 */
public class TitleForm extends JDialog
{
	// Definitions of constants for selections
	private static final String[] COVER_OPTIONS = {"Cover upload", "Media file link"};
	private static final long serialVersionUID = 2L;
	
	// This form keeps the state of its combo and check boxes and makes the values available in some data structure
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
	// checkbox for collection / series information
	private JCheckBox requireCollectionBox, requireCorporateContributor;
	// availability and notification type elements
	private JTextField notificationTypeField, availabilityCodeField, productAvailabilityField, productContentTypeField;
	// set of configured subjects
	private Set<Subject> configuredSubjects;
	// additional price nodes
	private Set<Price> configuredPrices;
	// publisher elements
	private JTextField namecodetype, namecodevalue, publishername;
	
	/**
	 * Constructor, configures the components and visual elements and displays the form
	 * @param scenario The test scenario that a title is added to
	 */
	public TitleForm(final TestScenario scenario)
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
		Border purpleLineBorder = BorderFactory.createLineBorder(new Color(153, 0, 153));
		
		// Group form elements in left panel
		JPanel titleOptionsPanel = new JPanel();
		titleOptionsPanel.setLayout(new BoxLayout(titleOptionsPanel, BoxLayout.Y_AXIS));
		JPanel productTypeSelectionPanel = new JPanel(new FlowLayout());
		productTypeSelectionPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Product Type Selection"));
		JPanel coverSelectionPanel = new JPanel(new FlowLayout());
		coverSelectionPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Cover delivery method"));
		JPanel additionalAssetsPanel = new JPanel(new GridLayout(0,1));
		additionalAssetsPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional assets"));
		JPanel publisherPanel = new JPanel(new GridLayout(0,1));
		publisherPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Publisher information"));
		
		titleOptionsPanel.add(productTypeSelectionPanel);
		titleOptionsPanel.add(coverSelectionPanel);
		titleOptionsPanel.add(additionalAssetsPanel);
		titleOptionsPanel.add(publisherPanel);
		
		productTypeSelectionPanel.add(product);
		coverSelectionPanel.add(covertype);
		additionalAssetsPanel.add(scrBox);
		additionalAssetsPanel.add(packBox);
		additionalAssetsPanel.add(extrBox);
		additionalAssetsPanel.add(bkcBox);
		additionalAssetsPanel.add(epmoBox);
		
		JPanel publisherNameCodeTypePanel = new JPanel(new FlowLayout());
		publisherNameCodeTypePanel.setBorder(BorderFactory.createTitledBorder(purpleLineBorder, "Name Code Type"));
		publisherNameCodeTypePanel.add(namecodetype);
		publisherPanel.add(publisherNameCodeTypePanel);
		
		JPanel publisherNameCodeValuePanel = new JPanel(new FlowLayout());
		publisherNameCodeValuePanel.setBorder(BorderFactory.createTitledBorder(purpleLineBorder, "Name Code Value"));
		publisherNameCodeValuePanel.add(namecodevalue);
		publisherPanel.add(publisherNameCodeValuePanel);
		
		JPanel publisherNamePanel = new JPanel(new FlowLayout());
		publisherNamePanel.setBorder(BorderFactory.createTitledBorder(purpleLineBorder, "Publisher Name"));
		publisherNamePanel.add(publishername);
		publisherPanel.add(publisherNamePanel);
		
		// Group form elements in right panel
		JPanel detailOptionsPanel = new JPanel(new GridLayout(0,2));
		
		// Product ID options
		JPanel productIdPanel = new JPanel(new GridLayout(0,1));
		JPanel productIdPanelOptions = new JPanel(new GridLayout(0,1));
		detailOptionsPanel.add(productIdPanel);
		productIdPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional Product Identifiers"));
		productIdPanel.add(productIdPanelOptions);
		productIdPanelOptions.add(prodIdGtin13);
		productIdPanelOptions.add(prodIdDoi);
		productIdPanelOptions.add(prodIdUrn);
		
		// Product Form Detail options
		JPanel productFormDetailPanel = new JPanel(new GridLayout(0,1));
		JPanel productFormDetailPanelOptions = new JPanel(new GridLayout(0,1));
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
		JPanel titlePanelOptions = new JPanel(new GridLayout(0,1));
		detailOptionsPanel.add(titlePanel);
		titlePanel.add(titlePanelOptions);
		titlePanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional Titles (select Title Type)"));
		titlePanelOptions.add(tittyp03);
		titlePanelOptions.add(tittyp06);
		titlePanelOptions.add(tittyp08);
		titlePanelOptions.add(tittyp11);
		titlePanelOptions.add(tittyp13);
		
		// ProductContentType options
		JPanel productContentTypePanel = new JPanel(new GridLayout(0,1));
		productContentTypePanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional product content types"));
		
		JPanel numberOfProductContentTypes = new JPanel(new FlowLayout());
		numberOfProductContentTypes.setBorder(BorderFactory.createTitledBorder(purpleLineBorder, "Number of extra elements"));
		numberOfProductContentTypes.add(productContentTypeField);
		productContentTypePanel.add(numberOfProductContentTypes);
		
		// Collection / series options
		JPanel collectionPanel = new JPanel(new GridLayout(0,1));
		collectionPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Include series/collection"));
		collectionPanel.add(requireCollectionBox);
		
		// group ProductContentType and Collection options together
		JPanel contentTypeAndCollectionPanel = new JPanel(new GridLayout(0,1));
		contentTypeAndCollectionPanel.add(productContentTypePanel);
		contentTypeAndCollectionPanel.add(collectionPanel);

		detailOptionsPanel.add(contentTypeAndCollectionPanel);
		
		// contributor options
		JPanel contributorsPanel = new JPanel(new GridLayout(0,1));
		contributorsPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Contributor options"));
		contributorsPanel.add(requireCorporateContributor);
		detailOptionsPanel.add(contributorsPanel);
		
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
		JButton clearSubjectsButton = new JButton("clear subjects");
		clearSubjectsButton.setActionCommand("clear subjects");
		clearSubjectsButton.addActionListener(new ClearSubjectsFromTitleListener(configuredSubjectsField, configuredSubjects));
		
		JPanel addSubjectsButtonPanel = new JPanel(new FlowLayout());
		addSubjectsButtonPanel.add(addSubjectsButton);
		addSubjectsButtonPanel.add(clearSubjectsButton);
		subjectsPanel.add(configuredSubjectsField);
		subjectsPanel.add(addSubjectsButtonPanel);
		detailOptionsPanel.add(subjectsPanel);
		
		// availability and notification type options
		JPanel availabilityPanel = new JPanel(new GridLayout(0,1));
		availabilityPanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Availability settings"));
		
		JPanel notificationTypePanel = new JPanel(new FlowLayout());
		notificationTypePanel.setBorder(BorderFactory.createTitledBorder(purpleLineBorder, "Notification type"));
		notificationTypePanel.add(notificationTypeField);
		availabilityPanel.add(notificationTypePanel);
		
		JPanel availabilityCodePanel = new JPanel(new FlowLayout());
		availabilityCodePanel.setBorder(BorderFactory.createTitledBorder(purpleLineBorder, "Availability code"));
		availabilityCodePanel.add(availabilityCodeField);
		availabilityPanel.add(availabilityCodePanel);
		
		JPanel productAvailabilityPanel = new JPanel(new FlowLayout());
		productAvailabilityPanel.setBorder(BorderFactory.createTitledBorder(purpleLineBorder, "Product availability"));
		productAvailabilityPanel.add(productAvailabilityField);
		availabilityPanel.add(productAvailabilityPanel);
		
		detailOptionsPanel.add(availabilityPanel);
		
		// price options
		JPanel pricePanel = new JPanel(new GridLayout(1,0));
		pricePanel.setBorder(BorderFactory.createTitledBorder(blueLineBorder, "Additional prices"));
		pricePanel.setToolTipText("A 04-DE-EUR price is the default and will always be generated. Here you can add further prices");

		JTextArea configuredPricesField = new JTextArea();
		configuredPricesField.setLineWrap(true);
		configuredPricesField.setEditable(false);
		configuredPricesField.setColumns(40);
		JButton addPriceButton = new JButton("add price");
		addPriceButton.setActionCommand("open prices dialog");
		addPriceButton.addActionListener(new AddPricesToTitleListener(configuredPricesField, configuredPrices));
		JButton clearPriceButton = new JButton("clear prices");
		clearPriceButton.setActionCommand("clear prices");
		clearPriceButton.addActionListener(new ClearPricesFromTitleListener(configuredPricesField, configuredPrices));
		
		JPanel addPricesButtonPanel = new JPanel(new FlowLayout());
		addPricesButtonPanel.add(addPriceButton);
		addPricesButtonPanel.add(clearPriceButton);
		pricePanel.add(configuredPricesField);
		pricePanel.add(addPricesButtonPanel);
		detailOptionsPanel.add(pricePanel);
		
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
		this.setSize(1000, 800);
	}
	
	/**
	 * Get the prices that are configured for the new product
	 * @return A Set of Prices
	 */
	public Set<Price> getConfiguredPrices()
	{
		return configuredPrices;
	}
	
	/**
	 * Get the Onix subjects that are configures for the new product
	 * @return A Set of Subjects
	 */
	public Set<Subject> getConfiguredSubjects()
	{
		return configuredSubjects;
	}
	
	/**
	 * Get all the form data that is configured in the text fields, selection boxes, etc.
	 * @return A key-value map with the name of a swing component being the key to the value 
	 */
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
		formDataMap.put(requireCollectionBox.getName(), requireCollectionBox.isSelected() ? "true" : "false");
		formDataMap.put(requireCorporateContributor.getName(), requireCorporateContributor.isSelected() ? "true" : "false");
		formDataMap.put(namecodetype.getName(), namecodetype.getText());
		formDataMap.put(namecodevalue.getName(), namecodevalue.getText());
		formDataMap.put(publishername.getName(), publishername.getText());
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
		formDataMap.put(notificationTypeField.getName(), notificationTypeField.getText());
		formDataMap.put(availabilityCodeField.getName(), availabilityCodeField.getText());
		formDataMap.put(productAvailabilityField.getName(), productAvailabilityField.getText());
		formDataMap.put(productContentTypeField.getName(), productContentTypeField.getText());
		
		return formDataMap;
	}
	
	// Instantiate all the swing components and assign a name to each of them (The name being the key
	// in the form data map - see getFormDataMap() above
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
		
		requireCollectionBox = new JCheckBox("Include Series / Collection");
		requireCollectionBox.setName("collection");
		requireCollectionBox.setSelected(true);
		
		requireCorporateContributor = new JCheckBox("Include a corporate contributor");
		requireCorporateContributor.setName("corporatecontributor");
		requireCorporateContributor.setSelected(true);
		
		namecodetype = new JTextField("04");
		namecodetype.setName("namecodetype");
		namecodetype.setColumns(2);
		namecodevalue = new JTextField("56789");
		namecodevalue.setName("namecodevalue");
		namecodevalue.setColumns(10);
		publishername = new JTextField("IT-E-Books-Verlag");
		publishername.setName("publishername");
		publishername.setColumns(15);
		
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
		
		notificationTypeField = new JTextField(OnixNotificationTypeBuilder.DEFAULT_NOTIFICATION_TYPE);
		notificationTypeField.setName("notificationtype");
		notificationTypeField.setColumns(8);
		availabilityCodeField = new JTextField(OnixProductAvailabilityBuilder.DEFAULT_AVAILABILITY_CODE);
		availabilityCodeField.setName("availabilitycode");
		availabilityCodeField.setColumns(8);
		productAvailabilityField = new JTextField(OnixProductAvailabilityBuilder.DEFAULT_PRODUCT_AVAILABILITY);
		productAvailabilityField.setName("productavailability");
		productAvailabilityField.setColumns(8);
		productContentTypeField = new JTextField("0");
		productContentTypeField.setName("productcontenttypenumber");
		productContentTypeField.setColumns(2);
		
		configuredPrices = new HashSet<Price>();
	}
}