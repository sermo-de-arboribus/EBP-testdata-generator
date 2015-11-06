package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixProductFormBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PRODUCT_FORM_DEFINITIONS = 
		{
			{"b012", "ProductForm", "b012", "ProductForm", "productform", "BB"},
		};
	private static final int SEQUENCE_NUMBER = 400;

	public OnixProductFormBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRODUCT_FORM_DEFINITIONS;
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
	{
		Element prodForm = new Element(getTagName(0));
		prodForm.appendChild(new Text(determineElementContent(0)));
		return prodForm;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}
