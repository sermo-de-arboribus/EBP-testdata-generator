package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder handles the <CityOfPublication> node of Onix files
 */
public class OnixCityOfPublicationBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] CITY_DEFINITIONS = 
	{
		{"b209", "CityOfPublication", "b209", "CityOfPublication", "cityofpublication", "Stuttgart"}
	};
	private static final int SEQUENCE_NUMBER = 2200;

	/**
	 * Constructor
	 * @param args HashMap with arguments
	 */
	public OnixCityOfPublicationBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = CITY_DEFINITIONS;
	}
	
	@Override
	public Element build(final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element city = new Element(getTagName(0));
		city.appendChild(new Text(determineElementContent(0)));
		return city;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}