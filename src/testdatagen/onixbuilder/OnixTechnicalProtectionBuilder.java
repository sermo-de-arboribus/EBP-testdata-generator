package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder handles information about technical protection (like watermarking or digital rights management)
 */
public class OnixTechnicalProtectionBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] TECHNICAL_PROTECTION_DEFINITIONS = 
		{
			{"x317", "EpubTechnicalProtection", "b277", "EpubTypeNote", "technicalprotection", "03"}
		};
	private static final int SEQUENCE_NUMBER = 700;

	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixTechnicalProtectionBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = TECHNICAL_PROTECTION_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element protectionType = new Element(getTagName(0));
		protectionType.appendChild(new Text(determineElementContent(0)));
		return protectionType;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}
