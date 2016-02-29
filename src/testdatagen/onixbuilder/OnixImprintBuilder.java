package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder takes care of the Onix file's <Imprint> node
 */
public class OnixImprintBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] IMPRINT_ELEMENT_DEFINITIONS = 
		{
			{"imprint", "Imprint", "imprint", "Imprint", "", ""},
			{"imprintidentifier", "ImprintIdentifier", "", "", "", ""},
			{"x445", "ImprintIDType", "b241", "NameCodeType", "imprintidtype", "01"},
			{"b233", "IDTypeName", "b242", "NameCodeTypeName", "idtypename", "Proprietary"},
			{"b244", "IDValue", "b243", "NameCodeValue", "idvalue", "ABC"},
			{"b079", "ImprintName", "b079", "ImprintName", "imprintname", "Imprint Name"}
		};
	private static final int SEQUENCE_NUMBER = 2000;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */	
	public OnixImprintBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = IMPRINT_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build(final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element imprintNode = new Element(getTagName(0));
		Element identifierParentNode;
		
		if(onixVersion.equals("3.0"))
		{
			Element imprintIdentifier = new Element(getTagName(1));
			imprintNode.appendChild(imprintIdentifier);
			identifierParentNode = imprintIdentifier;
		}
		else
		{
			identifierParentNode = imprintNode;
		}
		
		for(int i = 2; i <= 4; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			identifierParentNode.appendChild(nextElement);
		}
		
		Element imprintName = new Element(getTagName(5));
		imprintName.appendChild(new Text(determineElementContent(5)));
		imprintNode.appendChild(imprintName);
		
		return imprintNode;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}