package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * The OnixSupplierBuilder generates <Supplier> nodes (Onix 3.0) or appends the supplier information 
 * directly to the <SupplyDetail> parent node (Onix 2.1)
 */
public class OnixSupplierBuilder extends OnixSupplyDetailPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] SUPPLIER_ELEMENTS_DEFINITIONS = 
		{
			{"supplier", "Supplier", "", "", "", ""},
			{"j292", "SupplierRole", "j292", "SupplierRole", "supplierrole", "01"},
			{"supplieridentifier", "SupplierIdentifier", "supplieridentifier", "SupplierIdentifier", "", ""},
			{"j345", "SupplierIDType", "j345", "SupplierIDType", "supplieridtype", "04"},
			{"b233", "IDTypeName", "b233", "IDTypeName", "supplieridtypename", "Börsenverein Verkehrsnummer"},
			{"b244", "IDValue", "b244", "IDValue", "productidvalue", "No Supplier ID Value given"},
			{"j137", "SupplierName", "j137", "SupplierName", "suppliername", "IT E-Books-Supllier"}
		};
	private static final int SEQUENCE_NUMBER = 3000;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixSupplierBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = SUPPLIER_ELEMENTS_DEFINITIONS;
	}
	
	@Override
	public void appendElementsTo(Element parentNode, final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		if(onixVersion.equals("3.0"))
		{
			Element supplier = new Element(getTagName(0));
			parentNode.appendChild(supplier);
			parentNode = supplier;
			
			// Supplier Role
			Element supplierRole = new Element(getTagName(1));
			supplierRole.appendChild(new Text(determineElementContent(1)));
			parentNode.appendChild(supplierRole);
		}
		
		// Supplier Identifier
		Element supplierIdentifier = new Element(getTagName(2));
		parentNode.appendChild(supplierIdentifier);
		for (int i = 3; i <= 5; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			supplierIdentifier.appendChild(nextElement);	
		}
		
		// Supplier Name
		Element supplierName = new Element(getTagName(6));
		supplierName.appendChild(new Text(determineElementContent(6)));
		parentNode.appendChild(supplierName);
		
		if(onixVersion.equals("2.1"))
		{
			// Supplier Role
			Element supplierRole = new Element(getTagName(1));
			supplierRole.appendChild(new Text(determineElementContent(1)));
			parentNode.appendChild(supplierRole);
		}
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}