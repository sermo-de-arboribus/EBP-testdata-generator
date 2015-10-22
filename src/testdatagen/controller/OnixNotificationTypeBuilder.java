package testdatagen.controller;

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
	
	public OnixNotificationTypeBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = NOTIFY_TYPE_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		Element notiType = new Element(getTagName(0));
		notiType.appendChild(new Text(determineElementContent(0)));
		return notiType;
	}
}