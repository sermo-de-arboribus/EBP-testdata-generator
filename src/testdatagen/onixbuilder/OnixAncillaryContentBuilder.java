package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixAncillaryContentBuilder extends OnixPartsBuilder
{
	private static final String[][] ANCILLARY_CONTENT_DEFINITIONS = 
	{
		{"ancillarycontent", "AncillaryContent", "illustrations", "Illustrations", "", ""},
		{"x423", "AncillaryContentType", "b256", "IllustrationType", "contenttype", "02"},
		{"x424", "AncillaryContentDescription", "b361", "IllustrationTypeDescription", "contentdescription", "Colour illustrations"},
		{"b257", "Number", "b257", "Number", "number", "64"}
	};
	private static final int SEQUENCE_NUMBER = 1475;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixAncillaryContentBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = ANCILLARY_CONTENT_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element ancillaryContent = new Element(getTagName(0));
		
		Element type = new Element(getTagName(1));
		type.appendChild(new Text(determineElementContent(1)));
		ancillaryContent.appendChild(type);
		
		Element description = new Element(getTagName(2));
		description.appendChild(new Text(determineElementContent(2)));
		ancillaryContent.appendChild(description);
		
		Element number = new Element(getTagName(3));
		number.appendChild(new Text(determineElementContent(3)));
		ancillaryContent.appendChild(number);
		
		return ancillaryContent;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}