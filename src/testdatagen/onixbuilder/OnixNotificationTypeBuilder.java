package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * OnixBuilder class to handle <NotificationType> elements in Onix messages
 */
public class OnixNotificationTypeBuilder extends OnixPartsBuilder 
{
	public static final String DEFAULT_NOTIFICATION_TYPE = "03";
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] NOTIFY_TYPE_DEFINITIONS = 
		{
			{"a002", "NotificationType", "a002", "NotificationType", "notificationtype", DEFAULT_NOTIFICATION_TYPE},
		};
	private static final int SEQUENCE_NUMBER = 300;

	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixNotificationTypeBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = NOTIFY_TYPE_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
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