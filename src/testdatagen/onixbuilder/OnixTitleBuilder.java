package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixTitleBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] TITLE_DEFINITIONS = 
		{
			{"titledetail", "TitleDetail", "title", "Title"},
			{"b202", "TitleType", "b202", "TitleType", "titletype", "01"},
			{"titleelement", "TitleElement", "", "", "", ""},
			{"x409", "TitleElementLevel", "", "", "titleelementlevel", "01"},
			{"b203", "TitleText", "b203", "TitleText", "titletext", "Buchtitel"},
			{"b029", "Subtitle", "b029", "Subtitle", "subtitle", "Untertitel"}
		};
	
	public OnixTitleBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = TITLE_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		Element titleComposite = new Element(getTagName(0));
		
		Element titleType = new Element(getTagName(1));
		titleType.appendChild(new Text(determineElementContent(1)));
		titleComposite.appendChild(titleType);
		
		// from here we need to distinguish between ONIX 2.1 and ONIX 3.0
		Element parentElement;
		if(onixVersion.equals("2.1"))
		{
			parentElement = titleComposite;
		}
		else
		{
			parentElement = new Element(getTagName(2));
			titleComposite.appendChild(parentElement);
			
			Element titleElementLevel = new Element(getTagName(3));
			titleElementLevel.appendChild(new Text(determineElementContent(3)));
			parentElement.appendChild(titleElementLevel);
		}
		
		for(int i = 4; i < elementDefinitions.length; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			parentElement.appendChild(nextElement);
		}
		
		return titleComposite;
	}

}