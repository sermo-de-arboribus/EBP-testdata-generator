package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;

/**
 * The OnixSupplyDetailPartsBuilder is an abstract class that extends the OnixPartsBuilder. A "normal" OnixPartsBuilder is 
 * used through its build() method and returns the root XML element of the elements being built. 
 * However, for the <SupplyDetail> node of Onix there is a complication with this approach, because of 
 * nesting differences between Onix 2.1 and Onix 3.0. Therefore the concrete classes derived from 
 * OnixSupplyDetailPartsBuilder use a different strategy: The parent node is passed in as an argument and 
 * the builder appends the required elements to this parent node. It is the OnixPartsDirector's job to call
 * appendElementsTo() instead of build() for the Onix elements inside the <SupplyDetail> node.
 */
public abstract class OnixSupplyDetailPartsBuilder extends OnixPartsBuilder
{
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixSupplyDetailPartsBuilder(final HashMap<String, String> args)
	{
		super(args);
	}
	
	/**
	 * build() just returns an empty SupplyDetail node.
	 */
	@Override
	public Element build()
	{
		if(tagType == OnixPartsBuilder.REFERENCETAG)
		{
			return new Element("SupplyDetail");
		}
		else
		{
			return new Element("supplydetail");
		}
	}
	
	/**
	 * This method instantiates the required child elements and appends them to the parentNode which is passed in 
	 * as an argument
	 * @param parentNode The parent node to append to
	 * @param onixVersion The Onix version to be used (Onix 2.1 / 3.0)
	 * @param tagType The tag type (SHORTTAG / REFERENCETAG)
	 */
	public abstract void appendElementsTo(Element parentNode, String onixVersion, int tagType);
}