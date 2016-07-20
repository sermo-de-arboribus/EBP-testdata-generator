package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * OnixPartsBuilder for handling <Publisher> nodes
 */
public class OnixPublisherBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PUBLISHER_ELEMENT_DEFINITIONS = 
		{
			{"publisher", "Publisher", "publisher", "Publisher", "", ""}, 
			{"b291", "PublishingRole", "b291", "PublishingRole", "publishingrole", "01"},
			{"publisheridentifier", "PublisherIdentifier", "", "", "", ""},
			{"x447", "PublisherIDType", "b241", "NameCodeType", "publisheridtype", "04"},
			{"b244", "IDValue", "b243", "NameCodeValue", "idvalue", "56789"},
			{"b081", "PublisherName", "b081", "PublisherName", "publishername", "IT-E-Books-Verlag"},
			{"website", "Website", "website", "Website", "", ""},
			{"b367", "WebsiteRole", "b367", "WebsiteRole", "websiterole", "01"},
			{"b294", "WebsiteDescription", "b294", "WebsiteDescription", "websitedescription", "Die Verlagswebsite"},
			{"b295", "WebsiteLink", "b295", "WebsiteLink", "websitelink", "http://www.it-e-books-verlag.com"}
		};
	private static final int SEQUENCE_NUMBER = 2100;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixPublisherBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PUBLISHER_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element publisherNode = new Element(getTagName(0));
		
		Element pubRole = new Element(getTagName(1));
		pubRole.appendChild(new Text(determineElementContent(1)));
		publisherNode.appendChild(pubRole);
		
		Element identifierParentNode;
		if(onixVersion.equals("3.0"))
		{
			Element publisherIdentifier = new Element(getTagName(2));
			publisherNode.appendChild(publisherIdentifier);
			identifierParentNode = publisherIdentifier;
		}
		else
		{
			identifierParentNode = publisherNode;
		}
		
		for(int i = 3; i <= 4; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			identifierParentNode.appendChild(nextElement);
		}
		
		Element pubName = new Element(getTagName(5));
		pubName.appendChild(new Text(determineElementContent(5)));
		publisherNode.appendChild(pubName);
		
		Element website = new Element(getTagName(6));
		publisherNode.appendChild(website);
		
		for(int i = 7; i <= 9; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			website.appendChild(nextElement);
		}
		
		return publisherNode;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}
