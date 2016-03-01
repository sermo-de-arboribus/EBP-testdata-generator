package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder handles <RecordReference>
 */
public class OnixRecordReferenceBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] recReferenceDefinitions = 
		{
			{"a001", "RecordReference", "a001", "RecordReference", "recordreference", "ERROR"},
		};
	private static final int SEQUENCE_NUMBER = 200;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixRecordReferenceBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = recReferenceDefinitions;
	}

	@Override
	public Element build(final String onixVersion, final int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element recRef = new Element(getTagName(0));
		recRef.appendChild(new Text(determineElementContent(0)));
		return recRef;
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}