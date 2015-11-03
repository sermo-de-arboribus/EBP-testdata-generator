package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixSupplyDateBuilder extends OnixSupplyDetailPartsBuilder
{
	private static final String[][] SUPPLY_DATE_DEFINITIONS = 
		{
			{"supplydate", "SupplyDate", "supplydate", "SupplyDate", "", ""},
			{"x461", "SupplyDateRole", "", "", "supplydaterole", "08"},
			{"b306", "Date", "b306", "Date", "date", "{$currentDateTime}"},
			{"", "", "j143", "OnSaleDate", "date", "{$currentDateTime}"},
			{"", "", "j142", "ExpectedShipDate", "date", "{$currentDateTime}"},
		};

	public OnixSupplyDateBuilder(final String onixVersion, final int tagType, final HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = SUPPLY_DATE_DEFINITIONS;
	}
	
	@Override
	public void appendElementsTo(Element parentNode)
	{
		// we have to build different date elements for ONIX 2.1 and ONIX 3
		if(onixVersion.equals("2.1"))
		{
			String supplyDateRole = determineElementContent(1);
			switch(supplyDateRole)
			{
				case "02": 
					Element onSaleDate = new Element(getTagName(3));
					onSaleDate.appendChild(new Text(determineElementContent(3)));
					parentNode.appendChild(onSaleDate);
					break;
				case "08":
					Element expectedShipDate = new Element(getTagName(4));
					expectedShipDate.appendChild(new Text(determineElementContent(4)));
					parentNode.appendChild(expectedShipDate);
					break;
				default: // ignore dates with other type codes		
			}
		}
		else // handle ONIX 3
		{
			Element supplyDateNode = new Element(getTagName(0));
			
			Element supplyDateRole = new Element(getTagName(1));
			supplyDateRole.appendChild(new Text(determineElementContent(1)));
			supplyDateNode.appendChild(supplyDateRole);
			
			Element dateNode = new Element(getTagName(2));
			supplyDateRole.appendChild(new Text(determineElementContent(2)));
			supplyDateNode.appendChild(dateNode);
			
			parentNode.appendChild(supplyDateNode);
		}
	}

}
