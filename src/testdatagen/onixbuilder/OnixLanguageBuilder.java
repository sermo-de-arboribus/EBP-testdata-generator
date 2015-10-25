package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

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

	public OnixLanguageBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = LANGUAGE_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		Element language = new Element(getTagName(0));
		
		for(int i = 1; i < elementDefinitions.length; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			language.appendChild(nextElement);
		}
		
		return language;
	}

}