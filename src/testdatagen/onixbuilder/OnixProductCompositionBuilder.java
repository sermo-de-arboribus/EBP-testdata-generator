package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixProductCompositionBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PRODUCT_COMPOSITION_DEFINITIONS = 
		{
			{"x314", "ProductComposition", "", "", "productcomposition", "00"},
		};
	private static final int SEQUENCE_NUMBER = 400;

	public OnixProductCompositionBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRODUCT_COMPOSITION_DEFINITIONS;
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
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