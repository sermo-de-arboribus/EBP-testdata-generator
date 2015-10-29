package testdatagen.model.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import nu.xom.Text;
import testdatagen.config.ConfigurationRegistry;
import testdatagen.model.Title;
import testdatagen.onixbuilder.*;
import testdatagen.utilities.ISBNUtils;
import testdatagen.utilities.TitleUtils;
import testdatagen.utilities.Utilities;

public class ONIXFile extends File
{
	private static Random random = new Random();
	private static ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
	private ArrayList<Element> prices;
	private HashMap<String, String> argumentsMap;
	private String version;
	private int tagType;
	
	public ONIXFile(long ISBN, int tagType, String version)
	{
		super(Long.toString(ISBN) + "_onix.xml");
		prices = new ArrayList<Element>();
		argumentsMap = new HashMap<String, String>();
		this.version = version;
		this.tagType = tagType;
	}
	
	public java.io.File generate(Title title, java.io.File destDir)
	{
		// configure the arguments for the OnixPartsBuilders
		argumentsMap.put("recordreference", title.getUid());
		// TODO: distinguish between reference and short tags; add release attribute
		Element ONIXroot = new Element("ONIXmessage");
		Element ONIXheader = buildHeader();
		Element ONIXproduct = buildProductNode(title);
		
		String tagTypeString = "";
		if(tagType == OnixPartsDirector.REFERENCETAG)
		{
			tagTypeString = "reference";
		}
		else
		{
			tagTypeString = "short";
		}
		ONIXroot.appendChild(ONIXheader);
		ONIXroot.appendChild(ONIXproduct);
		
		Document doc = new Document(ONIXroot);
		java.io.File outputFile = new java.io.File(FilenameUtils.concat(destDir.getPath(), title.getIsbn13() + "_onix" + version + "_" + tagTypeString + ".xml"));
		try
		{
			FileOutputStream fos = new FileOutputStream(outputFile);
			Serializer ser = new Serializer(fos);
			ser.setIndent(4);
			ser.write(doc);
			ser.flush();
			fos.close();
		}
		catch (IOException ex)
		{
			Utilities.showErrorPane("Could not save ONIX XML file", ex);
		}
		finally
		{
			// remove all arguments from arguments map
			argumentsMap.clear();
		}
		return outputFile;
	}
	
	/*
	 * The contributor node currently expects one title to have only one author. This needs to be modified when we allow
	 * for multiple authors and different author types per title
	 */
	private Element buildContributorNode(Title title)
	{
		OnixContributorBuilder ocb = new OnixContributorBuilder(version, tagType, argumentsMap);
		argumentsMap.put("fullname", title.getAuthor());
		argumentsMap.put("invertedname", title.getAuthorLastName() + ", " + title.getAuthorFirstName());
		argumentsMap.put("namesbeforekey", title.getAuthorFirstName());
		argumentsMap.put("keynames", title.getAuthorLastName());
		argumentsMap.put("biographicalnote", title.getAuthorBlurb());
		argumentsMap.put("birthdate", "19440704");
		argumentsMap.put("deathdate", "20131228");
		argumentsMap.put("professionalaffiliation", "");
		argumentsMap.put("websitelink", "http://www.purple.com/");
		Element contributorNode = ocb.build();
		argumentsMap.remove("fullname");
		argumentsMap.remove("invertedname");
		argumentsMap.remove("namesbeforekey");
		argumentsMap.remove("keynames");
		argumentsMap.remove("biographicalnote");
		argumentsMap.remove("birthdate");
		argumentsMap.remove("deathdate");
		argumentsMap.remove("professionalaffiliation");
		argumentsMap.remove("websitelink");
		return contributorNode;
	}
	
	private Element buildHeader()
	{
		OnixHeaderBuilder ohb = new OnixHeaderBuilder(version, tagType, new HashMap<String, String>());
		return ohb.build();
	}
	
	private Element buildLanguageNode(String langCode)
	{
		OnixLanguageBuilder olb = new OnixLanguageBuilder(version, tagType, argumentsMap);
		argumentsMap.put("languagecode", langCode);
		Element langNode = olb.build();
		argumentsMap.remove("languagecode");
		
		return langNode;
	}
	
	private Element buildMainSubjectNode()
	{
		OnixSubjectBuilder omainsubb = new OnixSubjectBuilder(version, tagType, OnixSubjectBuilder.SUBJECTTYPE_MAIN, argumentsMap);
		argumentsMap.put("subjectcode", TitleUtils.getRandomWarengruppeCode());
		argumentsMap.put("subjectheadingtext", TitleUtils.getRandomTopic(new Locale("de")));
		Element msn = omainsubb.build();
		argumentsMap.remove("subjectheadingtext");
		argumentsMap.remove("subjectcode");
		
		return msn;
	}
	
	private Element buildPriceNode(String nodeType, double basePrice, String countryCode, String currencyCode)
	{	
		double thisPrice = Math.round(basePrice * registry.getDoubleValue("currency.rate." + currencyCode) * 100) / 100.0;
		
		Element price = new Element("price");
		
		// Price Type Code
		Element j148 = new Element("j148");
		j148.appendChild(new Text(nodeType));
		price.appendChild(j148);
		
		Element j151 = new Element("j151");
		j151.appendChild(new Text("" + thisPrice));
		price.appendChild(j151);
		
		Element j152 = new Element("j152");
		j152.appendChild(new Text(currencyCode));
		price.appendChild(j152);
		
		Element b251 = new Element("b251");
		b251.appendChild(new Text(countryCode));
		price.appendChild(b251);
		
		Element j153 = new Element("j153");
		j153.appendChild(new Text("S"));
		price.appendChild(j153);
		
		// check if a price node with the same data already exists
		if(prices.isEmpty())
		{
			prices.add(price);
			return price;
		}
		else
		{
			Iterator<Element> it = prices.iterator();
			while(it.hasNext())
			{
				// if price node with the same data already exists, create a new one.
				Element compElement = it.next();
				if(compElement.getValue().equals(price.getValue()))
				{
					return buildPriceNode(nodeType, basePrice);
				}
			}
			prices.add(price);
			return price;
		}
	}
	
	private Element buildPriceNode(String nodeType, double basePrice)
	{
		// select a random currency code and calculate the price for this currency
		String priceCodeString = registry.getString("currency.codes");
		String[] priceCodes = priceCodeString.split(" ");
		String thisPriceCode = priceCodes[random.nextInt(priceCodes.length)];
		
		// select a country code for the selected currency
		String countryCodeString = registry.getString("currency.country." + thisPriceCode);
		String[] countryCodes = countryCodeString.split(" ");
		String thisCountryCode = countryCodes[random.nextInt(countryCodes.length)];
		
		return buildPriceNode(nodeType, basePrice, thisCountryCode, thisPriceCode);
	}
	
	private Element buildProductNode(Title title)
	{
		Element product = new Element("product");
		
		// Record Reference
		OnixRecordReferenceBuilder recrefb = new OnixRecordReferenceBuilder(version, tagType, argumentsMap); 
		Element recordReference = recrefb.build();
		product.appendChild(recordReference);
		
		// Notification Type
		OnixNotificationTypeBuilder notitypeb = new OnixNotificationTypeBuilder(version, tagType, argumentsMap);
		Element notiType = notitypeb.build();
		product.appendChild(notiType);
		
		// Product Identifier
		OnixProductIdentifierBuilder pidb = new OnixProductIdentifierBuilder(version, tagType, argumentsMap);
		
		argumentsMap.put("productidtype", "03");
		argumentsMap.put("productidvalue", Long.toString(title.getIsbn13()));
		Element prodId1 = pidb.build();
		argumentsMap.remove("productidvalue");
		argumentsMap.remove("productidtype");
		
		product.appendChild(prodId1);
		
		argumentsMap.put("productidvalue", Long.toString(title.getIsbn13()));
		Element prodId2 = pidb.build();
		argumentsMap.remove("productidvalue");
		
		product.appendChild(prodId2);
		
		argumentsMap.put("productidtype", "06");
		argumentsMap.put("productidvalue", "10.2379/" + title.getIsbn13());
		Element prodId3 = pidb.build();
		argumentsMap.remove("productidvalue");
		argumentsMap.remove("productidtype");
		
		product.appendChild(prodId3);
		
		argumentsMap.put("productidtype", "01");
		argumentsMap.put("productidvalue", Long.toString(title.getIsbn13()).substring(6, 12));
		argumentsMap.put("productidtypename", "Verlagsnummer");
		Element prodId4 = pidb.build();
		argumentsMap.remove("productidtypename");
		argumentsMap.remove("productidvalue");
		argumentsMap.remove("productidtype");
		
		product.appendChild(prodId4);
		
		// In ONIX 2.1 we need to append the following elements directly to the product node, in ONIX 3 they need to be appended to 
		// <descriptivedetail>
		Element parentNode;
		if(version.equals("2.1"))
		{
			parentNode = product;
			argumentsMap.put("productform", "DG"); // code represents a digital book in ONIX 2.1
		}
		else
		{
			// handle version 3, distinguish between long and short tag names
			if (tagType == OnixPartsBuilder.REFERENCETAG)
			{
				parentNode = new Element("DescriptiveDetail");
			}
			else
			{
				parentNode = new Element("descriptivedetail");
			}
			product.appendChild(parentNode);
			argumentsMap.put("productform", "ED"); // code represents a downloadable book in ONIX 3.0
		}
		
		// TODO: implement "AJ" products later
		// Product Form
		OnixProductFormBuilder pfb = new OnixProductFormBuilder(version, tagType, argumentsMap);
		parentNode.appendChild(pfb.build());

		// ProductFormDetail
		OnixProductFormDetailBuilder pfdb = new OnixProductFormDetailBuilder(version, tagType, argumentsMap); 

		// ProductFormDetail #1: is e-book reflowable or fixed layout?
		String layoutType = random.nextBoolean() ? "E200" : "E201";
		argumentsMap.put("productformdetail", layoutType);
		parentNode.appendChild(pfdb.build());
		argumentsMap.remove("productformdetail");
		// ProductFormDetail #2: give information about EpubType
		argumentsMap.put("productformdetail", title.getEpubTypeForProductFormDetail());
		parentNode.appendChild(pfdb.build());
		argumentsMap.remove("productformdetail");
		
		// output protection type
		OnixTechnicalProtectionBuilder tpb = new OnixTechnicalProtectionBuilder(version, tagType, argumentsMap);
		argumentsMap.put("technicalprotection", title.getProtectionTypeForONIX());
		parentNode.appendChild(tpb.build());
		argumentsMap.remove("technicalprotection");
		
		// output title
		OnixTitleBuilder otb = new OnixTitleBuilder(version, tagType, argumentsMap);
		argumentsMap.put("titletext", determineTitleStringByType("01", title));
		argumentsMap.put("subtitle", determineTitleStringByType("subtitle", title));
		parentNode.appendChild(otb.build());
		argumentsMap.remove("subtitle");
		argumentsMap.remove("titletext");
		
		argumentsMap.put("titletext", determineTitleStringByType("05", title));
		parentNode.appendChild(otb.build());
		argumentsMap.remove("titletext");
		
		// Contributor
		Element contributorNode = buildContributorNode(title);
		parentNode.appendChild(contributorNode);
		
		// Language
		Element languageNode = buildLanguageNode("ger");
		parentNode.appendChild(languageNode);
		
		// Extent / Page numbering information
		OnixExtentBuilder oextb = new OnixExtentBuilder(version, tagType, argumentsMap);
		if(version.equals("2.1"))
		{
			argumentsMap.put("numberofpages", "320");
			parentNode.appendChild(oextb.build());
			argumentsMap.remove("numberofpages");
			
			// we could also append some pagesroman or pagesarabic here, if we wanted to
		}
		if(version.equals("3.0"))
		{
			argumentsMap.put("extenttype", "07");
			argumentsMap.put("extentvalue", "320");
			argumentsMap.put("extentvalueroman", "CCCXX");
			argumentsMap.put("extentunit", "03");
			parentNode.appendChild(oextb.build());
			argumentsMap.remove("extenttype");
			argumentsMap.remove("extentvalue");
			argumentsMap.remove("extentvalueroman");
			argumentsMap.remove("extentunit");
		}
		// add an extent node for file size
		argumentsMap.put("extenttype", "22");
		argumentsMap.put("extentvalue", "1.3");
		argumentsMap.put("extentunit", "19");
		parentNode.appendChild(oextb.build());
		argumentsMap.remove("extenttype");
		argumentsMap.remove("extentvalue");
		argumentsMap.remove("extentunit");
		
		// Main subject
		Element mainsubject = buildMainSubjectNode();
		parentNode.appendChild(mainsubject);
		
		// Subjects
		Element subject = buildSubjectNode();
		parentNode.appendChild(subject);
		
		subject = buildSubjectNode();
		parentNode.appendChild(subject);
		
		// In ONIX 2.1 we need to append the following elements directly to the product node, in ONIX 3 they need to be appended to 
		// <collateraldetail>
		if(version.equals("3.0"))
		{
			// handle version 3, distinguish between long and short tag names
			if (tagType == OnixPartsBuilder.REFERENCETAG)
			{
				parentNode = new Element("CollateralDetail");
				product.appendChild(parentNode);
			}
			else
			{
				parentNode = new Element("collateraldetail");
				product.appendChild(parentNode);
			}
		}
		
		// descriptive / advertising text content 
		Element textNode = buildTextNode(title);
		parentNode.appendChild(textNode);
		
		// add a media resource node
		OnixMediaResourceBuilder omrb = new OnixMediaResourceBuilder(version, tagType, argumentsMap);
		parentNode.appendChild(omrb.build());
		
		// In ONIX 2.1 we need to append the following elements directly to the product node, in ONIX 3 they need to be appended to 
		// <publishingdetail>
		if(version.equals("3.0"))
		{
			// handle version 3, distinguish between long and short tag names
			if (tagType == OnixPartsBuilder.REFERENCETAG)
			{
				parentNode = new Element("PublishingDetail");
				product.appendChild(parentNode);
			}
			else
			{
				parentNode = new Element("publishingdetail");
				product.appendChild(parentNode);
			}
		}
		
		// add an imprint node
		OnixImprintBuilder impb = new OnixImprintBuilder(version, tagType, argumentsMap);
		parentNode.appendChild(impb.build());
		
		// Publisher information
		Element publisher = buildPublisherNode();
		product.appendChild(publisher);
		
		// Publishing Status
		Element pubStatus = new Element("b394");
		pubStatus.appendChild(new Text("04")); // TODO: would be nice to have some variation of the publishing status, but 04 ("active") is most common
		product.appendChild(pubStatus);
		
		// Publication date
		Element pubDate = new Element("b003");
		long currentDateMillis = new Date().getTime();
		long randomPubDate = random.nextInt(10) * 86400000L;
		randomPubDate = random.nextBoolean() ? randomPubDate : randomPubDate * -1;
		pubDate.appendChild(new Text(Utilities.getDateForONIX2(new Date(currentDateMillis + randomPubDate)).substring(0, 8)));
		product.appendChild(pubDate);

		// Sales Rights
		Element sr = buildSalesRightsNode();
		product.appendChild(sr);
		
		// Related products
		Element relProd = buildRelatedProductNode(title);
		product.appendChild(relProd);
		
		// Supply Detail, including prices
		Element suppDetail = buildSupplyDetailNode();
		product.appendChild(suppDetail);
		
		return product;
	}
	
	private Element buildPublisherNode()
	{
		OnixPublisherBuilder opubb = new OnixPublisherBuilder(version, tagType, argumentsMap);
		Element pubNode = opubb.build();
		
		return pubNode;
	}
	
	private Element buildRelatedProductNode(Title title)
	{
		Element relProd = new Element("relatedproduct");
		
		Element h208 = new Element("h208");
		h208.appendChild(new Text("13")); // TODO: a wider range of code list values could be implemented
		relProd.appendChild(h208);
		
		String ISBNString = Long.toString(title.getIsbn13());
		ISBNString = ISBNString.substring(0, 6) + "5" + ISBNString.substring(7, 12);
		String checkDigit = ISBNUtils.calculateCheckDigit(ISBNString);
		ISBNString = ISBNString + checkDigit;
		long relISBN = Long.parseLong(ISBNString);
		
		OnixProductIdentifierBuilder pidb = new OnixProductIdentifierBuilder(version, tagType, argumentsMap);
		
		argumentsMap.put("productidvalue", Long.toString(relISBN));
		Element identifier = pidb.build();
		argumentsMap.remove("productidvalue");
		
		relProd.appendChild(identifier);
		
		Element b012 = new Element("b012");
		b012.appendChild(new Text("BC"));
		relProd.appendChild(b012);
		
		return relProd;
	}
	
	private Element buildSalesRightsNode()
	{
		Element salesRights = new Element("salesrights");
		
		Element b089 = new Element("b089");
		b089.appendChild(new Text("0" + (random.nextInt(1) + 1)));
		salesRights.appendChild(b089);
		
		Element rightsRegion = random.nextBoolean() ? new Element("b090") : new Element("b388");
		if(rightsRegion.getLocalName().equals("b090"))
		{
			int j = random.nextInt(6) + 1;
			for(int i = 0; i < j; i++)
			{
				rightsRegion.appendChild(new Text(Utilities.getCountryForONIX()));
				if(i < j - 1)
				{
					rightsRegion.appendChild(new Text(" "));
				}
			}
		}
		else
		{
			rightsRegion.appendChild(new Text("WORLD"));
		}
		salesRights.appendChild(rightsRegion);
		
		return salesRights;
	}
	
	private Element buildSubjectNode()
	{
		OnixSubjectBuilder osubb = new OnixSubjectBuilder(version, tagType, argumentsMap);
		argumentsMap.put("subjectheadingtext", TitleUtils.getRandomTopic(new Locale("de")));
		Element subjectNode = osubb.build();
		argumentsMap.remove("subjectheadingtext");
		
		return subjectNode;
	}
	
	private Element buildSupplyDetailNode()
	{	
		Element suppDet = new Element("supplydetail");
		
		// Supplier Name
		Element j137 = new Element("j137");
		j137.appendChild(new Text("KNV IT E-Books-Verlag"));
		suppDet.appendChild(j137);
		
		// Supplier's website
		Element website = new Element("website");
		Element b295 = new Element("b295");
		b295.appendChild(new Text("http://www.buchkatalog.de/"));
		website.appendChild(b295);
		suppDet.appendChild(website);
		
		// Supplier Role
		Element j292 = new Element("j292");
		j292.appendChild(new Text("01"));
		suppDet.appendChild(j292);
		
		// Availability Code
		Element j141 = new Element("j141");
		j141.appendChild(new Text("IP"));
		suppDet.appendChild(j141);
		
		// Product Availability
		Element j396 = new Element("j396");
		j396.appendChild(new Text("20"));
		suppDet.appendChild(j396);
		
		// Expected Ship Date, +/- 10 days from today
		long currentDateMillis = new Date().getTime();
		long randomShipDate = random.nextInt(10) * 86400000L;
		randomShipDate = random.nextBoolean() ? randomShipDate : randomShipDate * -1;
		Element j142 = new Element("j142");
		j142.appendChild(new Text(Utilities.getDateForONIX2(new Date(currentDateMillis + randomShipDate)).substring(0, 8)));
		suppDet.appendChild(j142);
		
		// TODO: refactor price node handling. There are currently two ArrayLists kept with price node elements, one locally, one as an instance variable. That's overdoing it!
		
		// price nodes
		double basePrice = Math.round((random.nextDouble() * 28.99 + 1) * 100) / 100.0;	
		// create a list of price nodes
		ArrayList<Element> priceNodeList = new ArrayList<>();
		
		Element germanFixedPrice = buildPriceNode("04", basePrice, "DE", "EUR");
		priceNodeList.add(germanFixedPrice);
		
		// random number of additional price nodes
		int nodes = random.nextInt(registry.getIntValue("onix.maxNumberOfPriceNodes") - registry.getIntValue("onix.minNumberOfPriceNodes")) + registry.getIntValue("onix.minNumberOfPriceNodes") - 1;
		for(int i = 0; i < nodes; i++)
		{
			int nodeNumber = random.nextInt(5);
			String nodeType;
			switch(nodeNumber)
			{
			case(1):
				nodeType = "24";
				break;
			case(2):
				nodeType = "02";
				break;
			case(3):
				nodeType = "22";
				break;
			case(4):
				nodeType = "42";
				break;
			case(0):
			default:
				nodeType = "04";
			}
			Element priceNode = buildPriceNode(nodeType, basePrice);
			priceNodeList.add(priceNode);
		}
		
		// sort list of price nodes
		Collections.sort(priceNodeList, new ONIXPriceComparator());
		
		// add sorted list of price nodes to the supply detail node
		Iterator<Element> iterator = priceNodeList.iterator();
		while(iterator.hasNext())
		{
			Element element = iterator.next();
			suppDet.appendChild(element);
		}
		
		return suppDet;
	}
	
	private Element buildTextNode(Title title)
	{
		OnixTextBuilder otextb = new OnixTextBuilder(version, tagType, argumentsMap);
		argumentsMap.put("texttypecode", "03");
		if(version.equals("3.0"))
		{
			argumentsMap.put("contentaudience", "00");
		}
		argumentsMap.put("text", title.getShortBlurb());
		
		Element otNode = otextb.build();
		
		argumentsMap.remove("texttypecode");
		argumentsMap.remove("contentaudience");
		argumentsMap.remove("text");
		
		return otNode;
	}
	
	private String determineTitleStringByType(String titleType, Title title)
	{
		// split titleString into main title and subtitle, if titleString contains a full-stop.
		String titleString = title.getName();
		String mainTitle = "";
		String subTitle = "";
		String abbrevTitle = "";
		if(titleString.contains("."))
		{
			mainTitle = titleString.substring(0, titleString.lastIndexOf('.'));
			subTitle = titleString.substring(titleString.lastIndexOf('.') + 1).trim();
		}
		else 
		{
			mainTitle = titleString;
		}
		// if titleType is "05", this is meant to be a shortened title
		if(titleType.equals("05"))
		{
			abbrevTitle = mainTitle;
			if(mainTitle.length() > 12)
			{
				abbrevTitle = mainTitle.substring(0, 10);
			}
		}
		if(subTitle.equals(""))
		{
			subTitle = "Kein Untertitel";
		}
		switch(titleType)
		{
			case "subtitle": return subTitle;
			case "01":
			case "title":
			case "distinctivetitle": return mainTitle;
			case "05":
			case "abbreviatedtitle": return abbrevTitle;
			default: return "";
		}
	}
}

class ONIXPriceComparator implements Comparator<Element>
{
	@Override
	public int compare(Element price1, Element price2)
	{
		String codeType1 = price1.getFirstChildElement("j148").getValue();
		String codeType2 = price2.getFirstChildElement("j148").getValue();
		int compareValue = codeType1.compareTo(codeType2);
		if(compareValue > 0)
		{
			return 1;
		}
		else if(compareValue == 0)
		{
			String countryCode1 = price1.getFirstChildElement("b251").getValue();
			String countryCode2 = price2.getFirstChildElement("b251").getValue();
			return countryCode1.compareTo(countryCode2);
		}
		else
		{
			return -1;
		}
	}
}
