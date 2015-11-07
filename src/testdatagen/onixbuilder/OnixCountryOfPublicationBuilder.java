package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixCountryOfPublicationBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] COUNTRY_DEFINITIONS = 
		{
			{"b083", "CountryOfPublication", "b083", "CountryOfPublication", "countryofpublication", "DE"}
		};
	private static final int SEQUENCE_NUMBER = 2300;

	public OnixCountryOfPublicationBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = COUNTRY_DEFINITIONS;
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