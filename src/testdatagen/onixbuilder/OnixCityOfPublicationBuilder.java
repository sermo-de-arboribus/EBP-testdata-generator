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

	public OnixCityOfPublicationBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = CITY_DEFINITIONS;
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element city = new Element(getTagName(0));
		city.appendChild(new Text(determineElementContent(0)));
		return city;
	}

}