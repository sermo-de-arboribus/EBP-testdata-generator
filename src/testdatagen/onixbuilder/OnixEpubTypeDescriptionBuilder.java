package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixEpubTypeDescriptionBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] EPUB_TYPE_DEFINITIONS = 
		{
			{"", "", "b213", "EpubTypeDescription", "epubtypedescription", "Zip(Win)"}
		};
	private static final int SEQUENCE_NUMBER = 610;
	
	public OnixEpubTypeDescriptionBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = EPUB_TYPE_DEFINITIONS;
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
	{
		initialize(onixVersion, tagType);
		Element returnElement = null;
		
		// this element only exists in ONIX 2.1
		if(onixVersion.equals("2.1"))
		{
			returnElement = new Element(getTagName(0));
			returnElement.appendChild(new Text(determineElementContent(0)));
		}
		return returnElement;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}

}
