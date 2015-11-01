package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

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

	public OnixPublishingDateBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = PUBLISHING_DATE_DEFINITIONS;
	}

	@Override
	public Element build()
	{
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
}