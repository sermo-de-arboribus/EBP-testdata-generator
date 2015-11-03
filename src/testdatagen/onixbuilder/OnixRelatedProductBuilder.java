package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixRelatedProductBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] RELATED_PRODUCT_DEFINITIONS = 
		{
			{"relatedproduct", "RelatedProduct", "relatedproduct", "RelatedProduct", "", ""},
			{"x455", "ProductRelationCode", "h208", "RelationCode", "productrelationcode", "13"},
			// the following definitions are shown here for reference reasons. The generation of the actual XML code 
			// for those elements will be handled by the classes OnixProductIdentifierBuilder, OnixProductFormBuilder and OnixProductFormDetailBuilder
			{"productidentifier", "ProductIdentifier", "productidentifier", "ProductIdentifier", "", ""},
			{"b221", "ProductIDType", "b221", "ProductIDType", "productidtype", "15"},
			{"b233", "IDTypeName", "b233", "IDTypeName", "productidtypename", "{$productidtypename}"},
			{"b244", "IDValue", "b244", "IDValue", "productidvalue", "WARNING! NO PRODUCT ID ARGUMENT PASSED"},
			{"b012", "ProductForm", "b012", "ProductForm", "productform", "BB"},
			{"b333", "ProductFormDetail", "b333", "ProductFormDetail", "productformdetail", "E200"},
		};
	
	public OnixRelatedProductBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = RELATED_PRODUCT_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		OnixProductIdentifierBuilder opib = new OnixProductIdentifierBuilder(onixVersion, tagType, arguments);
		OnixProductFormBuilder opfb = new OnixProductFormBuilder(onixVersion, tagType, arguments);
		OnixProductFormDetailBuilder opfdb = new OnixProductFormDetailBuilder(onixVersion, tagType, arguments);
		
		Element relatedProductNode = new Element(getTagName(0));
		
		Element productRelationCode = new Element(getTagName(1));
		productRelationCode.appendChild(new Text(determineElementContent(1)));
		relatedProductNode.appendChild(productRelationCode);
		
		relatedProductNode.appendChild(opib.build());
		relatedProductNode.appendChild(opfb.build());
		relatedProductNode.appendChild(opfdb.build());
		
		return relatedProductNode;
	}

}