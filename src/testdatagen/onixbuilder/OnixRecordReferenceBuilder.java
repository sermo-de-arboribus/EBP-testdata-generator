package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixRecordReferenceBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] recReferenceDefinitions = 
		{
			{"a001", "RecordReference", "a001", "RecordReference", "recordreference", "ERROR"},
		};
	
	public OnixRecordReferenceBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = recReferenceDefinitions;
	}

	@Override
	public Element build()
	{
		Element recRef = new Element(getTagName(0));
		recRef.appendChild(new Text(determineElementContent(0)));
		return recRef;
	}
}