package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixProductFormDetailBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] PRODUCT_FORM_DETAIL_DEFINITIONS = 
		{
			{"b333", "ProductFormDetail", "b333", "ProductFormDetail", "productformdetail", "E200"},
			{"", "", "b211", "EpubType", "", "029"}
		};
	private static final int SEQUENCE_NUMBER = 600;
	
	public OnixProductFormDetailBuilder(HashMap<String, String> args)
	{
		super(args);
		elementDefinitions = PRODUCT_FORM_DETAIL_DEFINITIONS;
	}
	
	@Override
	public Element build(String onixVersion, int tagType)
	{
		initialize(onixVersion, tagType);
		
		Element returnElement;
		// In ONIX 2.1 we need to produce <b211> / <EpubType> for some information,
		// In ONIX 3.0 all information is handled by <b333> / <ProductFormDetail>
		if(onixVersion.equals("2.1"))
		{
			if(hasArgument(elementDefinitions[0][4]))
			{
				String pfdArg = getArgument(elementDefinitions[0][4]);
				String epubType = null;
				switch(pfdArg)
				{
					case "E107": epubType = "002"; break; // PDF
					case "E101": epubType = "029"; break; // EPUB
					case "E141": epubType = "044"; break; // IBOOK
					case "E116":
					case "E127": epubType = "022"; break; // MOBI
					default: break;
				}
				// we need to return a <b211> / <EpubType> element instead of <b333> / <ProductFormDetail>
				if(epubType != null)
				{
					returnElement = new Element(getTagName(1));
					returnElement.appendChild(new Text(epubType));
					return returnElement;
				}
				// "normal" case: output <b333> / <ProductFormDetail>
				else
				{
					returnElement = new Element(getTagName(0));
					returnElement.appendChild(new Text(pfdArg));
					return returnElement;
				}
			}
			else // no argument for ONIX 2.1 ProductFormType given, use default value
			{
				returnElement = new Element(getTagName(0));
				returnElement.appendChild(new Text(elementDefinitions[0][5]));
				return returnElement;
			}
		}
		else // ONIX 3.0
		{
			returnElement = new Element(getTagName(0));
			returnElement.appendChild(new Text(determineElementContent(0)));
			return returnElement;
		}
	}

	@Override
	public int getSequenceNumber()
	{
		return SEQUENCE_NUMBER;
	}

}