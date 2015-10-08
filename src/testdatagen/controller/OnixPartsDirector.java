package testdatagen.controller;

import nu.xom.Element;

import java.util.ArrayList;

// implemented as a singleton with eager creation
public class OnixPartsDirector
{
	// constants defined for ONIX tag types
	public static final int SHORTTAG = 1;
	public static final int REFERENCETAG = 2;
	
	private ArrayList<OnixPartsBuilder> onix2HeaderPattern;
	private ArrayList<OnixPartsBuilder> onix3HeaderPattern;
	private ArrayList<OnixPartsBuilder> onix2ProductPattern;
	private ArrayList<OnixPartsBuilder> onix3ProductPattern;
	
	private static final OnixPartsDirector INSTANCE = new OnixPartsDirector();
	
	private OnixPartsDirector()
	{
		// build an ONIX 2.1 pattern and store it in an ArrayList
		onix2HeaderPattern = new ArrayList<OnixPartsBuilder>();
		onix2ProductPattern = new ArrayList<OnixPartsBuilder>();
		
		// build an ONIX 3.0 pattern and store it in an ArrayList
		onix3HeaderPattern = new ArrayList<OnixPartsBuilder>();
		onix3ProductPattern = new ArrayList<OnixPartsBuilder>();
	}
	
	public static OnixPartsDirector getInstance()
	{
		return INSTANCE;
	}
	
	public Element buildOnix2(int tagType)
	{
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);

		// TODO: build the header and product elements and append them to the root
		
		return root;
	}
	
	public Element buildOnix3(int tagType)
	{
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		
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