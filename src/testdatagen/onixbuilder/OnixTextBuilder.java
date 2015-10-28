package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Text;

public class OnixTextBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] TEXT_ELEMENT_DEFINITIONS = 
		{
			{"textcontent", "TextContent", "othertext", "OtherText", "", ""},	
			{"x426", "TextType", "d102", "TextTypeCode", "texttypecode", "02"},
			{"x427", "ContentAudience", "", "", "contentaudience", ""},		
			{"d104", "Text", "d104", "Text", "text", "Ich stehe hier und habe nichts zu sagen und das ist Poesie, wie ich sie brauche"}
		};
	
	public OnixTextBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = TEXT_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build() 
	{
		Element textNode = new Element(getTagName(0));
		
		Element textType = new Element(getTagName(1));
		textType.appendChild(new Text(determineElementContent(1)));
		textNode.appendChild(textType);
		
		if(hasArgument("contentaudience") && onixVersion.equals("3.0"))
		{
			Element contentAudience = new Element(getTagName(2));
			contentAudience.appendChild(determineElementContent(2));
			textNode.appendChild(contentAudience);
		}
		
		Element textContent = new Element(getTagName(3));
		textContent.addAttribute(new Attribute("textformat", "07"));
		textContent.appendChild(new Text(determineElementContent(3)));
		textNode.appendChild(textContent);
		
		return textNode;
	}

}