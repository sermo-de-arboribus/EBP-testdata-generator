package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder handles the <Language> node of Onix files
 */
public class OnixLanguageBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] LANGUAGE_ELEMENT_DEFINITIONS = 
		{
			{"language", "Language", "language", "Language", "", ""},
			{"b253", "LanguageRole", "b253", "LanguageRole", "languagerole", "01"},
			{"b252", "LanguageCode", "b252", "LanguageCode", "languagecode", "ger"}
		};
	private static final int SEQUENCE_NUMBER = 1300;

	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixLanguageBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = LANGUAGE_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build(final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element language = new Element(getTagName(0));
		
		for(int i = 1; i < elementDefinitions.length; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			language.appendChild(nextElement);
		}
		
		return language;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}