package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixTechnicalProtectionBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] TECHNICAL_PROTECTION_DEFINITIONS = 
		{
			{"x317", "EpubTechnicalProtection", "b277", "EpubTypeNote", "technicalprotection", "03"}
		};

	public OnixTechnicalProtectionBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = TECHNICAL_PROTECTION_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		Element protectionType = new Element(getTagName(0));
		protectionType.appendChild(new Text(determineElementContent(0)));
		return protectionType;
	}

}
