package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder takes care of the <AvailabiliyCode> (Onix 2.1 only) and <ProductAvailability> elements
 */
public class OnixProductAvailabilityBuilder extends OnixSupplyDetailPartsBuilder
{
	public static final String DEFAULT_AVAILABILITY_CODE = "IP";
	public static final String DEFAULT_PRODUCT_AVAILABILITY = "20";
	private static final String[][] PRODUCT_AVAILABILITY_DEFINITIONS = 
		{
			{"", "", "j141", "AvailabilityCode", "availabilitycode", DEFAULT_AVAILABILITY_CODE},
			{"j396", "ProductAvailability", "j396", "ProductAvailability", "productavailability", DEFAULT_PRODUCT_AVAILABILITY}
		};
	private static final int SEQUENCE_NUMBER = 3100;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
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