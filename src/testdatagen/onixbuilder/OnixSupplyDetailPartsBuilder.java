package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;

public abstract class OnixSupplyDetailPartsBuilder extends OnixPartsBuilder
{
	/*
	 * This abstract class extends OnixPartsBuilder, because for the ONIX elements under the <SupplyDetail> node
	 * the build() function works a bit differently. The build() function of OnixPartsBuilder has no side effects,
	 * but just returns a freshly built node. The build() function of OnixSupplyDetailPartBuilder works on
	 * a supplyDetail node which is passed in as an argument. 
	 */
	public OnixSupplyDetailPartsBuilder(HashMap<String, String> args)
	{
		super(args);
	}
	
	/*
	 * build() just returns an empty SupplyDetail node.
	 */
	public Element build(String onixVersion, int tagType)
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
	
	public abstract void appendElementsTo(Element parentNode, String onixVersion, int tagType);

}