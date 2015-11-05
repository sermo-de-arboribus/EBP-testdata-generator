package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixNotificationTypeBuilder extends OnixPartsBuilder 
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] NOTIFY_TYPE_DEFINITIONS = 
		{
			{"a002", "NotificationType", "a002", "NotificationType", "notificationtype", "03"},
		};
	private static final int SEQUENCE_NUMBER = 300;
	
	public OnixNotificationTypeBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = NOTIFY_TYPE_DEFINITIONS;
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element notiType = new Element(getTagName(0));
		notiType.appendChild(new Text(determineElementContent(0)));
		return notiType;
	}
	
	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}