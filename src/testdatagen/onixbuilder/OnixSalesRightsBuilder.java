package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;
import testdatagen.onixbuilder.OnixPartsBuilder;

/**
 * This OnixPartsBuilder handles <SalesRights> nodes and its child elements
 */
public class OnixSalesRightsBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] SALES_RIGHTS_DEFINITIONS = 
		{
			{"salesrights", "SalesRights", "salesrights", "SalesRights", "", ""},
			{"b089", "SalesRightsType", "b089", "SalesRightsType", "salesrightstype", "02"},
			{"territory", "Territory", "", "", "", ""},
			{"x449", "CountriesIncluded", "b090", "RightsCountry", "countriesincluded", "DE AT CH"},
			{"x450", "RegionsIncluded", "b388", "RightsTerritory", "regionsincluded", "US-TX"}
		};
	private static final int SEQUENCE_NUMBER = 2600;

	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixSalesRightsBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = SALES_RIGHTS_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element salesRightsNode = new Element(getTagName(0));
		
		Element salesRightsType = new Element(getTagName(1));
		salesRightsType.appendChild(new Text(determineElementContent(1)));
		salesRightsNode.appendChild(salesRightsType);
		
		// In ONIX 3.0 the country/territory information is nested inside a <Territory> node, in ONIX 2.1 they're direct child elements of <SalesRights>
		Element territoriesParentNode;
		if(onixVersion.equals("3.0"))
		{
			territoriesParentNode = new Element(getTagName(2));
			salesRightsNode.appendChild(territoriesParentNode);
		}
		else
		{
			territoriesParentNode = salesRightsNode;
		}
		
		Element countriesNode = new Element(getTagName(3));
		countriesNode.appendChild(new Text(determineElementContent(3)));
		territoriesParentNode.appendChild(countriesNode);
		
		Element regionsNode = new Element(getTagName(4));
		regionsNode.appendChild(new Text(determineElementContent(4)));
		territoriesParentNode.appendChild(regionsNode);
		
		return salesRightsNode;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}