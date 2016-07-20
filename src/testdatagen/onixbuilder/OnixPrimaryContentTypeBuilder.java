package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder takes care of the <PrimaryContentType> (Onix 3.0 only) element. For Onix 2.1 it will use a 
 * <ProductContentType> element instead.
 */
public class OnixPrimaryContentTypeBuilder extends OnixPartsBuilder
{
	private static final String[][] PRIMARY_CONTENTTYPE_DEFINITIONS = 
		{
			{"x416", "PrimaryContentType", "b385", "ProductContentType", "primarycontenttype", "10"},
		};
	private static final int SEQUENCE_NUMBER = 610;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixPrimaryContentTypeBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRIMARY_CONTENTTYPE_DEFINITIONS;
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