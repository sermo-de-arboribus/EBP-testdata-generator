package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixEditionNumberBuilder extends OnixPartsBuilder
{
	private static final String[][] EDITION_NUMBER_DEFINITIONS = 
	{
		{"b057", "EditionNumber", "b057", "EditionNumber", "editionnumber", "2"},
	};
	private static final int SEQUENCE_NUMBER = 1160;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixEditionNumberBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = EDITION_NUMBER_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element editionNumbers = new Element(getTagName(0));
		editionNumbers.appendChild(new Text(determineElementContent(0)));
		return editionNumbers;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}