package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder takes care of the <ProductForm> element
 */
public class OnixProductFormBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PRODUCT_FORM_DEFINITIONS = 
		{
			{"b012", "ProductForm", "b012", "ProductForm", "productform", "BB"},
		};
	private static final int SEQUENCE_NUMBER = 500;

	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixProductFormBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRODUCT_FORM_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
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
