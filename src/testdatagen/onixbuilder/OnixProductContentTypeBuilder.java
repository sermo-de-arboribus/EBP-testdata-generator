package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder takes care of the <ProductContentType> element. Conceptually it should only be used for additional content types, 
 * as the primary content type, irrespective of the ONIX output format, should be used first.
 */
public class OnixProductContentTypeBuilder extends OnixPartsBuilder
{
	public static final String PRODUCT_CONTENTTYPE_DEFAULT_VALUE = "20";
	private static final String[][] PRODUCT_CONTENTTYPE_DEFINITIONS = 
		{
			{"b385", "ProductContentType", "b385", "ProductContentType", "productcontenttype", PRODUCT_CONTENTTYPE_DEFAULT_VALUE},
		};
	private static final int SEQUENCE_NUMBER = 620;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixProductContentTypeBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRODUCT_CONTENTTYPE_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element primContType = new Element(getTagName(0));
		primContType.appendChild(new Text(determineElementContent(0)));
		return primContType;
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}