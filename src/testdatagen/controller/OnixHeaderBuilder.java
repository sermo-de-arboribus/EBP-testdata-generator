package testdatagen.controller;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixHeaderBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] headerElementDefinitions = 
		{
			{"header", "Header", "header", "Header", "", ""},
			{"x298", "SenderName", "m174", "FromCompany", "sender", "IT-E-Books-Verlag"},
			{"x299", "ContactName", "m175", "FromPerson", "person", "{$randomFullName}"},
			{"j172", "EmailAddress", "m283", "FromEmail", "email", "noreply@kno-va.de"},
			{"x307", "SentDateTime", "m182", "SentDate", "date", "{$currentDateTime}"},
			{"m184", "DefaultLanguageOfText", "m184", "DefaultLanguageOfText", "defaultlanguage", "ger"},
			{"x310", "DefaultPriceType", "m185", "DefaultPriceTypeCode", "defaultpricetype", "04"},
			{"m186", "DefaultCurrencyCode", "m186", "DefaultCurrencyCode", "defaultcurrency", "{$randomCurrencyCode}"},
		};
	
	public OnixHeaderBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = headerElementDefinitions;
	}
	
	@Override
	public Element build()
	{
		Element header = new Element(getTagName(0));
		
		for(int i = 1; i < elementDefinitions.length; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			header.appendChild(nextElement);
		}
		
		return header;
	}
}
