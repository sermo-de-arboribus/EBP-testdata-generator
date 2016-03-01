package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder takes care of the <ProductComposition> (Onix 3.0 only) element
 */
public class OnixProductCompositionBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PRODUCT_COMPOSITION_DEFINITIONS = 
		{
			{"x314", "ProductComposition", "", "", "productcomposition", "00"},
		};
	private static final int SEQUENCE_NUMBER = 450;

	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixProductCompositionBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRODUCT_COMPOSITION_DEFINITIONS;
	}
	
	@Override
	public Element build(final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		if(onixVersion.equals("3.0"))
		{
			Element prodForm = new Element(getTagName(0));
			prodForm.appendChild(new Text(determineElementContent(0)));
			return prodForm;
		}
		else
		{
			return null;
		}
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}