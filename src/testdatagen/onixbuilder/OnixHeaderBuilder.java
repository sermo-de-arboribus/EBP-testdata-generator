package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixHeaderBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] HEADER_ELEMENT_DEFINITIONS = 
		{
			{"header", "Header", "header", "Header", "", ""},
			{"sender", "Sender", "", "", "", ""},
			{"x298", "SenderName", "m174", "FromCompany", "sender", "IT-E-Books-Verlag"},
			{"x299", "ContactName", "m175", "FromPerson", "person", "{$randomFullName}"},
			{"j172", "EmailAddress", "m283", "FromEmail", "email", "noreply@kno-va.de"},
			{"x307", "SentDateTime", "m182", "SentDate", "date", "{$currentDateTime}"},
			{"m184", "DefaultLanguageOfText", "m184", "DefaultLanguageOfText", "defaultlanguage", "ger"},
			{"x310", "DefaultPriceType", "m185", "DefaultPriceTypeCode", "defaultpricetype", "04"},
			{"m186", "DefaultCurrencyCode", "m186", "DefaultCurrencyCode", "defaultcurrency", "{$randomCurrencyCode}"}
		};
	private static final int SEQUENCE_NUMBER = 100;
	
	public OnixHeaderBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = HEADER_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element header = new Element(getTagName(0));
		
		Element senderNode;
		Element parentElement;
		if(onixVersion.equals("3.0"))
		{
			senderNode = new Element(getTagName(1));
			header.appendChild(senderNode);
			parentElement = senderNode;
		}
		else
		{
			parentElement = header;
		}

		for(int i = 2; i <= 4; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			parentElement.appendChild(nextElement);
		}
		
		parentElement = header;
		
		for(int i = 5; i <= 8; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			parentElement.appendChild(nextElement);
		}
		
		return header;
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}