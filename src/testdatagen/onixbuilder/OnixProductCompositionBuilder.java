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

	public OnixProductCompositionBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = PRODUCT_COMPOSITION_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		Element prodForm = new Element(getTagName(0));
		prodForm.appendChild(new Text(determineElementContent(0)));
		return prodForm;
	}
}