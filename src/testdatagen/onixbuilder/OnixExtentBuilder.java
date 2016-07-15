package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/**
 * This OnixPartsBuilder generates extent information: On the one hand this is represented in 
 * the <Extent> node, on the other hand (in Onix 2.1) by the explicit page number elements
 */
public class OnixExtentBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] EXTENT_ELEMENT_DEFINITIONS = 
		{
			{"extent", "Extent", "extent", "Extent", "", ""},
			{"b218", "ExtentType", "b218", "ExtentType", "extenttype", "11"},
			{"b219", "ExtentValue", "b219", "ExtentValue", "extentvalue", "192"},	
			{"x421", "ExtentValueRoman", "b254", "PagesRoman", "extentvalueroman", "IV"},
			{"b220", "ExtentUnit", "b220", "ExtentUnit", "extentunit", "03"},
			// elements that only exist in ONIX 2.1
			{"", "", "b061", "NumberOfPages", "numberofpages", "192"},
			{"", "", "b254", "PagesRoman", "pagesroman", "IV"},
			{"", "", "b255", "PagesArabic", "pagesarabic", "188"}
		};
	private static final int SEQUENCE_NUMBER = 1400;

	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixExtentBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = EXTENT_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		// if any of the arguments numberofpages, pagesroman or pagesarabic is defined, 
		// assume that the caller just wants to receive an ONIX 2.1 single element,
		// otherwise return a full extent node
		if(hasArgument("numberofpages") && onixVersion.equals("2.1"))
		{
			Element pageNum = new Element(getTagName(5));
			pageNum.appendChild(new Text(determineElementContent(5)));
			return pageNum;
		}
		else if(hasArgument("pagesroman") && onixVersion.equals("2.1"))
		{
			Element pageNum = new Element(getTagName(6));
			pageNum.appendChild(new Text(determineElementContent(6)));
			return pageNum;
		}
		else if(hasArgument("pagesarabic") && onixVersion.equals("2.1"))
		{
			Element pageNum = new Element(getTagName(7));
			pageNum.appendChild(new Text(determineElementContent(7)));
			return pageNum;
		}
		else
		{
			Element extent = new Element(getTagName(0));
			
			for(int i = 1; i < 5; i++)
			{
				// only include x421 / ExtentValueRoman, if argument extentvalueroman is defined
				if(i != 3 || hasArgument("extentvalueroman"))
				{
					Element nextElement = new Element(getTagName(i));
					nextElement.appendChild(new Text(determineElementContent(i)));
					extent.appendChild(nextElement);	
				}
			}
			
			return extent;
		}
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER + (hasArgument("numberOfPages") ? 0 : 1);
	}
}