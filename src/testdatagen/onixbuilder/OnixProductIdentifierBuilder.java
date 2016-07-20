package testdatagen.onixbuilder;

import java.util.HashMap;

import testdatagen.utilities.OnixUtils;
import nu.xom.Element;
import nu.xom.Text;

/**
 * OnixPartsBuilder class to handle <ProductIdentifier> elements
 */
public class OnixProductIdentifierBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PRODUCT_IDENTIFIER_DEFINITIONS = 
		{
			{"productidentifier", "ProductIdentifier", "productidentifier", "ProductIdentifier", "", ""},
			{"b221", "ProductIDType", "b221", "ProductIDType", "productidtype", "15"},
			{"b233", "IDTypeName", "b233", "IDTypeName", "productidtypename", "{$productidtypename}"},
			{"b244", "IDValue", "b244", "IDValue", "productidvalue", "WARNING! NO PRODUCT ID ARGUMENT PASSED"}
		};
	private static final int SEQUENCE_NUMBER = 400;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixProductIdentifierBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRODUCT_IDENTIFIER_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element productidentifier = new Element(getTagName(0));
		
		for(int i = 1; i < elementDefinitions.length; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			productidentifier.appendChild(nextElement);
		}
		
		return productidentifier;
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
	
	@Override
	protected String determineElementContent(final int row)
	{
		String argName = elementDefinitions[row][4];
		String defValue = elementDefinitions[row][5];
		String elementContent = "";
		if(hasArgument(argName))
		{
			elementContent = getArgument(argName);
		}
		else if(defValue.matches("\\{\\$.+\\}"))
		{
			switch(defValue)
			{
				case "{$productidtypename}":
					// determine productidtype
					elementContent = getNameForIDType(getProductIDType());
					break;
				default:
					elementContent = "ERROR! Could not create element content for " + defValue;
			}
		}
		else
		{
			elementContent = defValue;
		}
		
		return elementContent;
	}
	
	// helper method to get a name String for a given ProductIDType
	private String getNameForIDType(String idType)
	{
		// names for ID types can be found in ONIX code list 5
		return OnixUtils.getCodeListDescription(5, idType);
	}
	
	// helper method to determine the ProductIDType
	private String getProductIDType()
	{
		String idType = "";
		String argName = elementDefinitions[1][4];
		if(hasArgument(argName))
		{
			idType = getArgument(argName);
		}
		else
		{
			// return default ID Type
			idType = elementDefinitions[1][5];
		}
		
		return idType;
	}
}