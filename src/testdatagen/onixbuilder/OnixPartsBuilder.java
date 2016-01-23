package testdatagen.onixbuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import testdatagen.utilities.TitleUtils;
import testdatagen.utilities.Utilities;
import nu.xom.Element;

public abstract class OnixPartsBuilder implements Comparable<OnixPartsBuilder>, Serializable
{
	protected static final long serialVersionUID = 1L;
	// constants defined for ONIX tag types
	public static final int SHORTTAG = 1;
	public static final int REFERENCETAG = 2;
	
	protected String onixVersion = null;
	protected int tagType = 0;
	protected HashMap<String, String> arguments;
	
	/* This String array contains information on how to create ONIX elements.
	 * Every row represents one ONIX element.
	 * The columns represent: ONIX 3 short name | ONIX 3 ref name | ONIX 2.1 short name | ONIX 2.1 ref name | argument name | default value
	 * "argument name" is the key name in the arguments HashMap, which is passed to the constructor of OnixHeaderBuilder
	 * "default value" is the value to be used, if no argument with the listed name is available.
	 * If the default value is encapsulated by {$ ... } then a random value generator is supposed to be used.
	 * Otherwise the string is used literally 
	 */
	protected String[][] elementDefinitions;
	
	/*
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
	
	/*
	 * build() is the main worker method to create a chunk of ONIX elements.
	 */
	public abstract Element build(String onixVersion, int tagType);
	
	/*
	 * To enforce an order on the sequence of ONIX elements, every subclass needs to 
	 * be able to return a (statically determined) sequence number.
	 */
	public abstract int getSequenceNumber();
	
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
	
	public static String getTagTypeString(int tagType)
	{
		switch(tagType)
		{
			case SHORTTAG: return "short";
			case REFERENCETAG: return "reference";
			default: return "unknownTagType";
		}
	}
	
	/*
	 * gets an argument value from the arguments HashMap that was passed to the constructor
	 * @arg String: key to the hash map.
	 */
	protected String getArgument(String arg)
	{
		return arguments.get(arg);
	}
	
	protected boolean hasArgument(String arg)
	{
		return arguments.containsKey(arg);
	}
	
	protected void validateTagType(int tagType)
	{
		if(!(tagType == SHORTTAG || tagType == REFERENCETAG))
		{
			throw new IllegalArgumentException("OnixPartsBuilder received an invalid ONIX tag type as argument. Use symbolic constants SHORTTAG or REFERENCETAG!");
		}
	}
	
	protected void validateOnixVersion(String onixVersion)
	{
		if(!(onixVersion.equals("2.1") || onixVersion.equals("3.0")))
		{
			throw new IllegalArgumentException("OnixPartsBuilder received an invalid ONIX version as argument. Only '2.1' and '3.0' are allowed!");
		}
	}
	
	/*
	 * The text content for an element is determined in three steps:
	 * 1. If the OnixPartsBuilder object's argument hash map has a value matching the current element's key, then this argument value is used.
	 * 2. If the elementDefinitions dictionary indicates that an element content shall be calculated by some function, the function is determined and called.
	 *    (A function call is indicated by a default value from elementDefinitions, which is enclosed in curly brackets and starts with a dollar: {$...}.)
	 * 3. If neither an argument is given nor a function to be called, then the default value from the elementDefinitions array is used literally.
	 */
	protected String determineElementContent(int row)
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
	
	protected String getTagName(int row)
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
	
	protected void initialize(String onixVersion, int tagType)
	{
		validateTagType(tagType);
		validateOnixVersion(onixVersion);
		
		this.onixVersion = onixVersion;
		this.tagType = tagType;
	}
}