package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder generates <TitleDetail> (Onix 3.0) or <Title> (Onix 2.1) nodes and their child elements
 */
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
	private static final int SEQUENCE_NUMBER = 900;
	
	/**
	 * Constructor 
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixTitleBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = TITLE_DEFINITIONS;
	}
	
	@Override
	public Element build(final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element titleComposite = new Element(getTagName(0));
		
		Element titleType = new Element(getTagName(1));
		titleType.appendChild(new Text(determineElementContent(1)));
		titleComposite.appendChild(titleType);
		
		// from here on we need to distinguish between ONIX 2.1 and ONIX 3.0
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
		
		// write main title
		Element nextElement = new Element(getTagName(4));
		nextElement.appendChild(new Text(determineElementContent(4)));
		parentElement.appendChild(nextElement);
		
		// id a subtitle defined?
		if(hasArgument("subtitle"))
		{
			nextElement = new Element(getTagName(5));
			nextElement.appendChild(new Text(determineElementContent(5)));
			parentElement.appendChild(nextElement);
		}
		
		return titleComposite;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
	
}