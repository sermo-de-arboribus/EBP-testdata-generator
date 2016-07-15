package testdatagen.onixbuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import testdatagen.utilities.TitleUtils;
import testdatagen.utilities.Utilities;
import nu.xom.Element;

/**
 * OnixPartsBuilder is the abstract base class for all concrete OnixBuilder classes. Every OnixPartsBuilder 
 * subclass takes an arguments HashMap in its constructor, which allows key/value pairs for passing
 * configuration values. A concrete OnixPartsBuilder also knows which Onix elements it has to handle
 * and how to generate them. (These Onix elements are stored in the two-dimensional String array 
 * elementDefinitions.)
 * This abstract class provides implementations for determining Onix element names, based on an index into
 * elementDefinitions (see getTageName() method) and for filling the element's content (see method
 * determineElementContent()). It also forces sub-classes to return a sequence number, which is used 
 * to sort the OnixBuilder objects in the order they are supposed to appear in an Onix file.
 */
public abstract class OnixPartsBuilder implements Comparable<OnixPartsBuilder>, Serializable
{
	protected static final long serialVersionUID = 1L;
	// constants defined for ONIX tag types
	public static final int SHORTTAG = 1;
	public static final int REFERENCETAG = 2;
	
	protected String onixVersion = null;
	protected int tagType = 0;
	protected HashMap<String, String> arguments;
	
	/** This String array contains information on how to create ONIX elements.
	 * Every row represents one ONIX element.
	 * The columns represent: ONIX 3 short name | ONIX 3 ref name | ONIX 2.1 short name | ONIX 2.1 ref name | argument name | default value
	 * "argument name" is the key name in the arguments HashMap, which is passed to the constructor of OnixHeaderBuilder
	 * "default value" is the value to be used, if no argument with the listed name is available.
	 * If the default value is encapsulated by {$ ... } then a random value generator is supposed to be used.
	 * Otherwise the string is used literally 
	 */
	protected String[][] elementDefinitions;
	
	/**
	 * Constructor validates onix version and tag type arguments
	 * @onixVersion String: accepted version strings are "2.1" or "3.0"
	 * @tagType int: accepted values are SHORTTAG and REFERENCETAG
	 * @args HashMap<String, String>: this hash map may contain key-value pairs for providing certain ONIX elements' text content.
	 * 		The key names to be used have to match the 5th column (index 4) of the elementDefinitions array.
	 */
	public OnixPartsBuilder(final HashMap<String, String> args)
	{
		arguments = args;
	}
	
	/**
	 * build() is the main worker method to create a chunk of ONIX elements.
	 * Before calling build(), the caller is expected to have called initialize() to set the required OnixType and tagType
	 * @tagType The tag type to be used (SHORT / REFERENCE)
	 * @return An XML element
	 */
	public abstract Element build();
	
	/**
	 * To enforce an order on the sequence of ONIX elements, every subclass needs to 
	 * be able to return a (statically determined) sequence number.
	 * @return An integer value that indicates the position where built ONIX elements are
	 * to be put into DOM tree
	 */
	public abstract int getSequenceNumber();

	/**
	 * Compares OnixBuilders based on their sequence numbers, and thus orders them according to the Onix schema
	 */
	@Override
	public int compareTo(final OnixPartsBuilder otherBuilder)
	{
		if(this.getSequenceNumber() < otherBuilder.getSequenceNumber())
		{
			return -1;
		}
		if(this.getSequenceNumber() > otherBuilder.getSequenceNumber())
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Returns a String with the tag type
	 * @param tagType The tag type to give a string for
	 * @return A String for the tag type
	 */
	public static String getTagTypeString(final int tagType)
	{
		switch(tagType)
		{
			case SHORTTAG: return "short";
			case REFERENCETAG: return "reference";
			default: return "unknownTagType";
		}
	}
	
	/**
	 * gets an argument value from the arguments HashMap that was passed to the constructor
	 * @arg String: key to the hash map.
	 */
	protected String getArgument(final String arg)
	{
		return arguments.get(arg);
	}

	/**
	 * Checks whether this OnixPartsBuilder has an argument key in its arguments map. This is used to 
	 * determine if the argument or the default value is supposed to be used for an Onix element's content.
	 * @param arg
	 * @return
	 */
	protected boolean hasArgument(final String arg)
	{
		return arguments.containsKey(arg);
	}
	
	protected boolean isInitialized()
	{
		if(tagType == 0 || onixVersion == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Validates the tag type, only the constant integer values SHORTTAG / REFERENCETAG are allowed
	 * @param tagType
	 */
	protected void validateTagType(final int tagType)
	{
		if(!(tagType == SHORTTAG || tagType == REFERENCETAG))
		{
			throw new IllegalArgumentException("OnixPartsBuilder received an invalid ONIX tag type as argument. Use symbolic constants SHORTTAG or REFERENCETAG!");
		}
	}
	
	/**
	 * Validates the Onix version string that is passed in as an argument. Only "2.1" and "3.0" are allowed
	 * @param onixVersion The String representing the Onix version
	 */
	protected void validateOnixVersion(final String onixVersion)
	{
		if(!(onixVersion.equals("2.1") || onixVersion.equals("3.0")))
		{
			throw new IllegalArgumentException("OnixPartsBuilder received an invalid ONIX version as argument. Only '2.1' and '3.0' are allowed!");
		}
	}
	
	/**
	 * The text content for an element is determined in three steps:
	 * 1. If the OnixPartsBuilder object's argument hash map has a value matching the current element's key, then this argument value is used.
	 * 2. If the elementDefinitions dictionary indicates that an element content shall be calculated by some function, the function is determined and called.
	 *    (A function call is indicated by a default value from elementDefinitions, which is enclosed in curly brackets and starts with a dollar: {$...}.)
	 * 3. If neither an argument is given nor a function to be called, then the default value from the elementDefinitions array is used literally.
	 */
	protected String determineElementContent(final int row)
	{
		String argName = elementDefinitions[row][4];
		String defValue = elementDefinitions[row][5];
		String elementContent = "";
		if(hasArgument(argName))
		{
			elementContent = getArgument(argName);
		}
		else if(defValue.matches("\\{\\$.+\\}"))
		{
			switch(defValue)
			{
				case "{$randomFullName}":
					elementContent = TitleUtils.getRandomFullName();
					break;
				case "{$currentDateTime}":
					if(onixVersion.equals("2.1"))
					{
						elementContent = Utilities.getDateForONIX2(new Date());
					}
					else if (onixVersion.equals("3.0"))
					{
						elementContent = Utilities.getDateTimeForONIX3(new Date());
					}
					else
					{
						elementContent = "ONIX version unknown";
					}
					break;
				case "{$randomCurrencyCode}":
					elementContent = TitleUtils.getRandomCurrencyCode();
					break;
				case "{$randomDate}":
					Random random = new Random();
					elementContent = "" + (random.nextInt(100) + 1950) + "0" + (random.nextInt(8) + 1) + (random.nextInt(18) + 10);
					break;
				default:
					elementContent = "ERROR! Could not create element content for " + defValue;
			}
		}
		else
		{
			elementContent = defValue;
		}
		return elementContent;
	}

	/**
	 * Uses the integer row index to the two-dimensional elementsDefinitions variable to get the 
	 * Onix element's name.
	 * @param row The row index for the two-dimensional elementsDefinitions variable
	 * @return A String representing the element name
	 */
	protected String getTagName(final int row)
	{
		int col = onixVersion.equals("2.1") ? 1 : -1;
		col += tagType;
		if(row <= elementDefinitions.length)
		{
			return elementDefinitions[row][col];
		}
		else
		{
			throw new IndexOutOfBoundsException("OnixPartsBuilder tried to read a tagname outside of elementDefinitions' array bounds!");
		}
	}

	/**
	 * Initializes this OnixPartsBuilder by setting up the onixVersion and tagType.
	 * It should be called by the OnixPartsDirector before calling "build"
	 * @param onixVersion String of the Onix version ("2.1" / "3.0")
	 * @param tagType The tag type (integer constants SHORTTAG / REFERENCETAG)
	 */
	public void initialize(String onixVersion, int tagType)
	{
		validateTagType(tagType);
		validateOnixVersion(onixVersion);
		
		this.onixVersion = onixVersion;
		this.tagType = tagType;
	}
}