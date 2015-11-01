package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixCityOfPublicationBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] CITY_DEFINITIONS = 
		{
			{"b209", "CityOfPublication", "b209", "CityOfPublication", "cityofpublication", "Stuttgart"}
		};

	public OnixCityOfPublicationBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = CITY_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		Element city = new Element(getTagName(0));
		city.appendChild(new Text(determineElementContent(0)));
		return city;
	}

}