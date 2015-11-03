package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixPriceBuilder extends OnixSupplyDetailPartsBuilder
{
	private static final String[][] PRICE_ELEMENT_DEFINITIONS = 
		{
	/*0*/	{"price", "Price", "price", "Price", "", ""},
			{"x462", "PriceType", "j148", "PriceTypeCode", "pricetype", "04"},
			{"discountcoded", "DiscountCoded", "discountcoded", "DiscountCoded", "", ""},
			{"j363", "DiscountCodeType", "j363", "DiscountCodeType", "discountcodetype", "02"},
			{"j378", "DiscountCodeTypeName", "j378", "DiscountCodeTypeName", "discountcodetypename", "Proprietary discount code"},
	/*5*/	{"j364", "DiscountCode", "j364", "DiscountCode", "discountcode", "00"},
			{"j266", "PriceStatus", "j266", "PriceStatus", "pricestatus", "02"},
			{"j151", "PriceAmount", "j151", "PriceAmount", "priceamount", "9.99"},
			{"tax", "Tax", "", "", "", ""},
			{"x470", "TaxType", "", "", "taxtype", "01"},
	/*10*/	{"x471", "TaxRateCode", "j153", "TaxRateCode1", "taxratecode", "S"},
			{"x472", "TaxRatePercent", "j154", "TaxRatePercent1", "taxratepercent", "19.0"},
			{"x473", "TaxableAmount", "j155", "TaxableAmount1", "taxableamount", "8.40"},
			{"j152", "CurrencyCode", "j152", "CurrencyCode", "currencycode", "EUR"},
			{"territory", "Territory", "", "", "", ""},
	/*15*/	{"x449", "CountriesIncluded", "b251", "CountryCode", "countriesincluded", "DE"},
			{"x450", "RegionsIncluded", "j303", "Territory", "regionsincluded", "WORLD"},
			{"pricedate", "PriceDate", "", "", "", ""},
			{"x476", "PriceDateRole", "", "", "pricedaterole", "14"},
			{"b306", "Date", "", "", "date", "{$currentDate"},
	/*20*/	{"", "", "j161", "PriceEffectiveFrom", "fromdate", "{$currentDate}"},
			{"", "", "j162", "PriceEffectiveUntil", "untildate", "{$currentDate}"}
		};

	public OnixPriceBuilder(final String onixVersion, final int tagType, final HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = PRICE_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public void appendElementsTo(Element parentNode)
	{
		Element priceNode = new Element(getTagName(0));
		parentNode.appendChild(priceNode);
		
		Element typeCode = new Element(getTagName(1));
		typeCode.appendChild(new Text(determineElementContent(1)));
		priceNode.appendChild(typeCode);
		
		// build discountcode information
		Element temporaryParentNode;
		if(onixVersion.equals("3.0"))
		{
			temporaryParentNode = new Element(getTagName(2));
			priceNode.appendChild(temporaryParentNode);
		}
		else
		{
			temporaryParentNode = priceNode;
		}
		appendElementsFromTo(temporaryParentNode, 3, 5);
		
		// add price status and price amount
		appendElementsFromTo(priceNode, 6, 7);
		
		// handle tax information
		if(onixVersion.equals("3.0"))
		{
			temporaryParentNode = new Element(getTagName(8));
			priceNode.appendChild(temporaryParentNode);
		}
		appendElementsFromTo(temporaryParentNode, 9, 12);
		
		// add currency code
		appendElementsFromTo(priceNode, 13, 13);
		
		// add territorial information
		if(onixVersion.equals("3.0"))
		{
			temporaryParentNode = new Element(getTagName(14));
			priceNode.appendChild(temporaryParentNode);
		}
		appendElementsFromTo(temporaryParentNode, 15, 16);
		
		// add price dates
		if(onixVersion.equals("3.0"))
		{
			if(hasArgument("pricedaterole"))
			{
				Element priceDateNode = new Element(getTagName(17));
				priceNode.appendChild(priceDateNode);
				
				appendElementsFromTo(priceDateNode, 18, 19);	
			}
		}
		else
		{
			if(hasArgument("fromdate"))
			{
				appendElementsFromTo(priceNode, 20, 20);
			}
			if(hasArgument("untildate"))
			{
				appendElementsFromTo(priceNode, 21, 21);
			}
		}
		
	}

	private void appendElementsFromTo(Element parent, int from, int to)
	{
		for(int i = from; i <= to; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			parent.appendChild(nextElement);
		}
	}
}
