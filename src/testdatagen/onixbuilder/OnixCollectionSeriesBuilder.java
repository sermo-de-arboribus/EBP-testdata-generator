package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

/***
 * This class represents <Collection> (ONIX 3.0) and <Series> (ONIX 2.1) information
 * @author Weber, Kai
 *
 */
public class OnixCollectionSeriesBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] COLLECTION_DEFINITIONS = 
	{
		/*  0 */ {"collection", "Collection", "series", "Series", "", ""},
		/*  1 */ {"x329", "CollectionType", "", "", "collectiontype", "10"},
		/*  2 */ {"collectionidentifier", "CollectionIdentifier", "seriesidentifier", "SeriesIdentifier", "", ""},
		/*  3 */ {"x344", "CollectionIDType", "b273", "SeriesIDType", "collectionidtype", "01"},
		/*  4 */ {"b233", "IDTypeName", "b233", "IDTypeName", "idtypename", "EBP Collection Code"},
		/*  5 */ {"b244", "IDValue", "b244", "IDValue", "idvalue", "15376"},
		/*  6 */ {"collectionsequence", "CollectionSequence", "", "", "", ""},
		/*  7 */ {"x479", "CollectionSequenceType", "", "", "collectionsequencetype", "02"},
		/*  8 */ {"x480", "CollectionSequenceTypeName", "", "", "collectionsequencetypename", "Title Order"},
		/*  9 */ {"x481", "CollectionSequenceNumber", "b019", "NumberWithinSeries", "collectionsequencenumber", "1"},
		/* 10 */ {"titledetail", "TitleDetail", "", "", "", ""},
		/* 11 */ {"b202", "TitleType", "", "", "titletype", "01"},
		/* 12 */ {"titleelement", "TitleElement", "", "", "", ""},
		/* 13 */ {"b034", "SequenceNumber", "", "", "sequencenumber", "1"},
		/* 14 */ {"x409", "TitleElementLevel", "", "", "titleelementlevel", "02"},
		/* 15 */ {"b203", "TitleText", "", "", "titletext", "A Series / Collection title"},
		/* 16 */ {"x478", "TitleStatement", "b018", "TitleOfSeries", "titlestatement", "A Series / Collection title statement"}
	};
	private static final int SEQUENCE_NUMBER = 800;
	
	/**
	 * Constructor
	 * @param args The arguments as a key-value HashMap
	 */
	public OnixCollectionSeriesBuilder(final HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = COLLECTION_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		if(!isInitialized())
		{
			throw new IllegalStateException("This builder object is not initialized. Please call initialize() before calling build().");
		}
		
		Element collectionRoot = new Element(getTagName(0));
		
		if(onixVersion.equals("3.0"))
		{
			Element collectionType = new Element(getTagName(1));
			collectionType.appendChild(new Text(determineElementContent(1)));
			collectionRoot.appendChild(collectionType);
		}
		
		Element collectionIdentifier = new Element(getTagName(2));
		collectionRoot.appendChild(collectionIdentifier);
		
		for(int i = 3; i < 6; i++)
		{
			Element identifierElement = new Element(getTagName(i));
			identifierElement.appendChild(new Text(determineElementContent(i)));
			collectionIdentifier.appendChild(identifierElement);
		}
		
		if(onixVersion.equals("3.0"))
		{
			Element collectionSequence = new Element(getTagName(6));
			collectionRoot.appendChild(collectionSequence);
			
			for(int i = 7; i < 10; i++)
			{
				Element sequenceElement = new Element(getTagName(i));
				sequenceElement.appendChild(new Text(determineElementContent(i)));
				collectionSequence.appendChild(sequenceElement);
			}
		}
		
		Element titleDetail = null;
		if(onixVersion.equals("3.0"))
		{
			titleDetail = new Element(getTagName(10));
			collectionRoot.appendChild(titleDetail);
			
			Element titleType = new Element(getTagName(11));
			titleType.appendChild(new Text(determineElementContent(11)));
			titleDetail.appendChild(titleType);
			
			Element titleElement = new Element(getTagName(12));
			titleDetail.appendChild(titleElement);
			
			for(int i = 13; i < 16; i++)
			{
				Element titleElementElement = new Element(getTagName(i));
				titleElementElement.appendChild(new Text(determineElementContent(i)));
				titleElement.appendChild(titleElementElement);
			}
		}
		
		Element titleOfSeries = new Element(getTagName(16));
		titleOfSeries.appendChild(new Text(determineElementContent(16)));
		if(onixVersion.equals("3.0"))
		{
			titleDetail.appendChild(titleOfSeries);
		}
		else
		{
			collectionRoot.appendChild(titleOfSeries);
		}
		
		if(onixVersion.equals("2.1"))
		{
			Element numberWithinSeries = new Element(getTagName(9));
			numberWithinSeries.appendChild(new Text(determineElementContent(9)));
			collectionRoot.appendChild(numberWithinSeries);
		}
		
		return collectionRoot;
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}
}