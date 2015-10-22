package testdatagen.controller;

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

	public OnixProductFormBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = PRODUCT_FORM_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		Element prodForm = new Element(getTagName(0));
		prodForm.appendChild(new Text(determineElementContent(0)));
		return prodForm;
	}
}
