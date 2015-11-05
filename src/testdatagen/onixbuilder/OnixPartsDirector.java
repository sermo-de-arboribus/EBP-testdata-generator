package testdatagen.onixbuilder;

import nu.xom.Attribute;
import nu.xom.Element;

import java.util.HashMap;
import java.util.LinkedList;

import testdatagen.model.Title;

public class OnixPartsDirector
{
	// constants defined for ONIX tag types
	public static final int SHORTTAG = 1;
	public static final int REFERENCETAG = 2;
	
	private LinkedList<OnixPartsBuilder> requiredElements;
	private Title title;
	/*
	 * The OnixPartsDirector class maintains a pattern for an ONIX file to be generated. 
	 * When an object of the OnixPartsDirector class is instantiated, a default pattern for an ONIX file
	 * is created. After creation of the default file, the default settings can be modified by
	 * calling specific methods on the OnixPartsDirector object. 
	 */
	public OnixPartsDirector(Title title)
	{
		this.title = title;
		
		requiredElements = new LinkedList<OnixPartsBuilder>();
		
		// add default header
		requiredElements.add(new OnixHeaderBuilder(new HashMap<String, String>()));
		
		// add record reference
		requiredElements.add(new OnixRecordReferenceBuilder(new HashMap<String, String>()));
		
		// add notification Type
		requiredElements.add(new OnixNotificationTypeBuilder(new HashMap<String, String>()));
		
		// add default product Identifier (type 15: ISBN-13)
		HashMap<String, String> isbnArgs = new HashMap<>();
		isbnArgs.put("productidvalue", Long.toString(title.getIsbn13()));
		requiredElements.add(new OnixProductIdentifierBuilder(isbnArgs));
		
	}
	
	public void addProductIdentifier(String type)
	{
		String productIdValue;
		switch(type)
		{
			case "06": // DOI
				productIdValue = "10.2379/" + title.getIsbn13();
				break;
			case "22": // URN
				productIdValue = "urn:isbn:" + title.getIsbn13();
				break;
			default:
				productIdValue = Long.toString(title.getIsbn13());
		}
		
		HashMap<String, String> productIdArgs = new HashMap<>();
		productIdArgs.put("productidtype", type);
		productIdArgs.put("productidvalue", productIdValue);
		requiredElements.add(new OnixProductIdentifierBuilder(productIdArgs));
	}
	
	public Element buildOnix2(int tagType)
	{
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		root.addAttribute(new Attribute("release", "2.1"));

		// TODO: build the header and product elements and append them to the root
		
		return root;
	}
	
	public Element buildOnix3(int tagType)
	{
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		root.addAttribute(new Attribute("release", "3.0"));
		
		// TODO: build the header and product elements and append them to the root
		
		return root;
	}
	
	/*
	 * Private helper method; returns the name of the ONIX file's root element based on the tag type (short tag / reference tag)
	 */
	private String getRootName(int tagType)
	{
		String rootElementName;
		switch(tagType)
		{
			case SHORTTAG:
				rootElementName = "ONIXmessage";
			case REFERENCETAG:
				rootElementName = "ONIXMessage";
			default:
				rootElementName = "ONIXmessage";
		}
		return rootElementName;
	}
}