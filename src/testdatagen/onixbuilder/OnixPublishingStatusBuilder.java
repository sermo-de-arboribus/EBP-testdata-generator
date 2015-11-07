package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixPublishingStatusBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PUBLISHING_STATUS_DEFINITIONS = 
		{
			{"b394", "PublishingStatus", "b394", "PublishingStatus", "publishingstatus", "04"}
		};
	private static final int SEQUENCE_NUMBER = 2400;
	
	public OnixPublishingStatusBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PUBLISHING_STATUS_DEFINITIONS;
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element country = new Element(getTagName(0));
		country.appendChild(new Text(determineElementContent(0)));
		return country;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}