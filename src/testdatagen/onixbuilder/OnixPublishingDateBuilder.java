package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * OnixPartsBuilder for generating <PublishingDate> (Onix 3.0) or <PublicationDate> (Onix 2.1) elements
 */
public class OnixPublishingDateBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PUBLISHING_DATE_DEFINITIONS = 
		{
			{"publishingdate", "PublishingDate", "", "", "", ""},
			{"x448", "PublishingDateRole", "", "", "publishingdaterole", "01"},
			{"b306", "Date", "b003", "PublicationDate", "publishingdate", "{$currentDateTime}"}
		};
	private static final int SEQUENCE_NUMBER = 2400;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixPublishingDateBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PUBLISHING_DATE_DEFINITIONS;
	}

	@Override
	public Element build(final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element publishingdate;
		
		if(onixVersion.equals("2.1"))
		{
			publishingdate = new Element(getTagName(2));
			publishingdate.appendChild(new Text(determineElementContent(2)));
		}
		else
		{
			publishingdate = new Element(getTagName(0));
			
			Element role = new Element(getTagName(1));
			role.appendChild(new Text(determineElementContent(1)));
			publishingdate.appendChild(role);
			
			Element date = new Element(getTagName(2));
			date.appendChild(new Text(determineElementContent(2)));
			publishingdate.appendChild(date);
		}
		
		return publishingdate;
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}