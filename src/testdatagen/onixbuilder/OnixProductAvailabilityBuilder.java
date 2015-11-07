package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixProductAvailabilityBuilder extends OnixSupplyDetailPartsBuilder
{
	private static final String[][] PRODUCT_AVAILABILITY_DEFINITIONS = 
		{
			{"", "", "j141", "AvailabilityCode", "availabilitycode", "IP"},
			{"j396", "ProductAvailability", "j396", "ProductAvailability", "productavailability", "20"}
		};
	private static final int SEQUENCE_NUMBER = 3100;
	
	public OnixProductAvailabilityBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRODUCT_AVAILABILITY_DEFINITIONS;
	}
	
	@Override
	public void appendElementsTo(final Element parentNode, final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		// only ONIX 2.1 allows the Availability Code element
		if(onixVersion.equals("2.1"))
		{
			Element availCode = new Element(getTagName(0));
			availCode.appendChild(new Text(determineElementContent(0)));
			parentNode.appendChild(availCode);
		}
		
		Element productAvail = new Element(getTagName(1));
		productAvail.appendChild(new Text(determineElementContent(1)));
		parentNode.appendChild(productAvail);
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}