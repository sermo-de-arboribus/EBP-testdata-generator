package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixEditionTypeBuilder extends OnixPartsBuilder
{
	private static final String[][] EDITION_TYPE_DEFINITIONS = 
	{
		{"x419", "EditionType", "b056", "EditionTypeCode", "editiontype", "REV"},
	};
	private static final int SEQUENCE_NUMBER = 1080;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixEditionTypeBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = EDITION_TYPE_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element editionType = new Element(getTagName(0));
		editionType.appendChild(new Text(determineElementContent(0)));
		return editionType;
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}

}
