package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixNumberOfIllustrationsBuilder extends OnixPartsBuilder
{
	private static final String[][] NUMBER_OF_ILLUSTRATIONS_DEFINITIONS = 
	{
		{"b125", "NumberOfIllustrations", "b125", "NumberOfIllustrations", "numberofillustrations", "64"},
	};
	private static final int SEQUENCE_NUMBER = 1425;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixNumberOfIllustrationsBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = NUMBER_OF_ILLUSTRATIONS_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element numberOfIllus = new Element(getTagName(0));
		numberOfIllus.appendChild(new Text(determineElementContent(0)));
		return numberOfIllus;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}