package testdatagen.model.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
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
	private HashMap<String, String> argumentsMap;
	private String version;
	private int tagType;
	
	public ONIXFile(long ISBN, int tagType, String version)
	{
		super(Long.toString(ISBN) + "_onix.xml");
		argumentsMap = new HashMap<String, String>();
		this.version = version;
		this.tagType = tagType;
	}
	
	public java.io.File generate(Title title, java.io.File destDir)
	{
		// configure the arguments for the OnixPartsBuilders
		argumentsMap.put("recordreference", title.getUid());
		// TODO: distinguish between reference and short tags; add release attribute
		String rootElementName;
		if(tagType == OnixPartsBuilder.REFERENCETAG)
		{
			rootElementName = "ONIXMessage";
		}
		else
		{
			rootElementName = "ONIXmessage";
		}

		Element ONIXroot = new Element(rootElementName);
		ONIXroot.addAttribute(new Attribute("release", version));
		
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
	private Element buildContributorNode(Title title, boolean corporateContributor)
	{
		if(corporateContributor)
		{
			argumentsMap.put("sequencenumber", "2");
			argumentsMap.put("contributorrole", "D01");
			argumentsMap.put("corporatename", "KNO DiVA corporation");
		}
		else
		{
			argumentsMap.put("fullname", title.getAuthor());
			argumentsMap.put("invertedname", title.getAuthorLastName() + ", " + title.getAuthorFirstName());
			argumentsMap.put("namesbeforekey", title.getAuthorFirstName());
			argumentsMap.put("keynames", title.getAuthorLastName());
			argumentsMap.put("biographicalnote", title.getAuthorBlurb());
			argumentsMap.put("birthdate", "19440704");
			argumentsMap.put("deathdate", "20131228");
		}
		argumentsMap.put("professionalaffiliation", "");
		argumentsMap.put("websitelink", "http://www.purple.com/");
		OnixContributorBuilder ocb = new OnixContributorBuilder(version, tagType, argumentsMap);
		Element contributorNode = ocb.build();
		argumentsMap.remove("sequencenumber");
		argumentsMap.remove("contributorrole");
		argumentsMap.remove("contributorname");
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
	
	private Element buildProductNode(Title title)
	{
		String productTagName;
		if(tagType == OnixPartsBuilder.REFERENCETAG)
		{
			productTagName = "Product";
		}
		else
		{
			productTagName = "product";
		}
		Element product = new Element(productTagName);
		
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
		
		// ONIX 3.0 requires a mandatory ProductComposition element
		if(version.equals("3.0"))
		{
			OnixProductCompositionBuilder oprocob = new OnixProductCompositionBuilder(version, tagType, argumentsMap);
			parentNode.appendChild(oprocob.build());
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
		
		// person name contributor
		Element contributorNode = buildContributorNode(title, false);
		parentNode.appendChild(contributorNode);
		
		// add a corporate contributor
		Element corporateContributorNode = buildContributorNode(title, true);
		parentNode.appendChild(corporateContributorNode);
		
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
		String resourceUrl = title.getMediaFileUrl();
		if(resourceUrl != null)
		{
			argumentsMap.put("resourcelink", resourceUrl);
		}
		parentNode.appendChild(omrb.build());
		argumentsMap.remove("resourcelink");
		
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
		parentNode.appendChild(publisher);
		
		// Place and country of publication
		OnixCityOfPublicationBuilder ocopb = new OnixCityOfPublicationBuilder(version, tagType, argumentsMap);
		parentNode.appendChild(ocopb.build());
		
		OnixCountryOfPublicationBuilder ocountpb = new OnixCountryOfPublicationBuilder(version, tagType, argumentsMap);
		parentNode.appendChild(ocountpb.build());
		
		// Publishing Status
		OnixPublishingStatusBuilder opubstatb = new OnixPublishingStatusBuilder(version, tagType, argumentsMap);
		parentNode.appendChild(opubstatb.build());
		
		// Publication date
		OnixPublishingDateBuilder odateb = new OnixPublishingDateBuilder(version, tagType, argumentsMap);
		long currentDateMillis = new Date().getTime();
		long randomPubDate = random.nextInt(10) * 86400000L;	
		randomPubDate = random.nextBoolean() ? randomPubDate : randomPubDate * -1;
		argumentsMap.put("publishingdate", Utilities.getDateForONIX2(new Date(currentDateMillis + randomPubDate)).substring(0, 8));
		parentNode.appendChild(odateb.build());
		argumentsMap.remove("publishingdate");

		// Sales Rights
		parentNode.appendChild(buildSalesRightsNode());
		
		// In ONIX 2.1 we need to append the following elements directly to the product node, in ONIX 3 they need to be appended to 
		// <relatedmaterial>
		if(version.equals("3.0"))
		{
			// handle version 3, distinguish between long and short tag names
			if (tagType == OnixPartsBuilder.REFERENCETAG)
			{
				parentNode = new Element("RelatedMaterial");
				product.appendChild(parentNode);
			}
			else
			{
				parentNode = new Element("relatedmaterial");
				product.appendChild(parentNode);
			}
		}
		
		// Related products
		Element relProd = buildRelatedProductNode(title);
		parentNode.appendChild(relProd);
		
		// In ONIX 2.1 we need to append the following elements directly to the product node, in ONIX 3 they need to be appended to 
		// <productsupply>
		if(version.equals("3.0"))
		{
			// handle version 3, distinguish between long and short tag names
			if (tagType == OnixPartsBuilder.REFERENCETAG)
			{
				parentNode = new Element("ProductSupply");
				product.appendChild(parentNode);
			}
			else
			{
				parentNode = new Element("productsupply");
				product.appendChild(parentNode);
			}
		}
		
		// Supply Detail, including prices
		Element suppDetail = buildSupplyDetailNode();
		parentNode.appendChild(suppDetail);
		
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
		// determine the ISBN to be used for a related product
		String ISBNString = Long.toString(title.getIsbn13());
		ISBNString = ISBNString.substring(0, 6) + "5" + ISBNString.substring(7, 12);
		String checkDigit = ISBNUtils.calculateCheckDigit(ISBNString);
		ISBNString = ISBNString + checkDigit;
		long relISBN = Long.parseLong(ISBNString);
		
		argumentsMap.put("productidvalue", Long.toString(relISBN));
		argumentsMap.put("productform", "BC");
		OnixRelatedProductBuilder orelpb = new OnixRelatedProductBuilder(version, tagType, argumentsMap);
		Element relProd = orelpb.build();
		argumentsMap.remove("productform");
		argumentsMap.remove("productidvalue");
		
		return relProd;
	}
	
	private Element buildSalesRightsNode()
	{
		OnixSalesRightsBuilder osalrb = new OnixSalesRightsBuilder(version, tagType, argumentsMap);
		argumentsMap.put("salesrightstype", "0" + (random.nextInt(1) + 1));
		
		StringBuffer countryList = new StringBuffer("");
		int j = random.nextInt(6) + 1;
		for(int i = 0; i < j; i++)
		{
			countryList.append(Utilities.getCountryForONIX());
			if(i < j - 1)
			{
				countryList.append(" ");
			}
		}
		
		argumentsMap.put("countriesincluded", countryList.toString());
		
		Element salesRights = osalrb.build();
		
		argumentsMap.remove("salesrightstype");
		argumentsMap.remove("countriesincluded");
		
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
		OnixSupplierBuilder osupb = new OnixSupplierBuilder(version, tagType, argumentsMap);
		Element supplyDetailNode = osupb.build();
		
		// add supplier information
		osupb.appendElementsTo(supplyDetailNode);
		
		OnixProductAvailabilityBuilder oprodavb = new OnixProductAvailabilityBuilder(version, tagType, argumentsMap);
		oprodavb.appendElementsTo(supplyDetailNode);
		
		OnixSupplyDateBuilder osdateb = new OnixSupplyDateBuilder(version, tagType, argumentsMap);
		
		// calculate a random expected ship date
		// Expected Ship Date, +/- 10 days from today
		long currentDateMillis = new Date().getTime();
		long randomShipDate = random.nextInt(10) * 86400000L;
		randomShipDate = random.nextBoolean() ? randomShipDate : randomShipDate * -1;
		Date actualShipDate = new Date(currentDateMillis + randomShipDate);
		String shipDateString = Utilities.getDateForONIX2(actualShipDate).substring(0, 8);
		
		argumentsMap.put("supplydaterole", "08");
		argumentsMap.put("date", shipDateString);
		
		osdateb.appendElementsTo(supplyDetailNode);
		
		argumentsMap.remove("supplydaterole");
		argumentsMap.remove("date");
		
		// Create an OnSaleDate / Embargo date in the same way as an Expected Ship Date
		randomShipDate = random.nextInt(10) * 86400000L;
		randomShipDate = random.nextBoolean() ? randomShipDate : randomShipDate * -1;
		actualShipDate = new Date(currentDateMillis + randomShipDate);
		shipDateString = Utilities.getDateForONIX2(actualShipDate).substring(0, 8);
		
		argumentsMap.put("supplydaterole", "02");
		argumentsMap.put("date", shipDateString);
		
		osdateb.appendElementsTo(supplyDetailNode);
		
		argumentsMap.remove("supplydaterole");
		argumentsMap.remove("date");

		// determine random base price
		double basePrice = Math.round((random.nextDouble() * 28.99 + 1) * 100) / 100.0;
		
		// create a set of prices
		HashSet<Price> priceSet = new HashSet<>();
		
		Price germanFixedPrice = new Price("04", "" + basePrice, "EUR", "DE");
		priceSet.add(germanFixedPrice);
		
		// random number of additional price nodes
		int numberOfPrices = random.nextInt(registry.getIntValue("onix.maxNumberOfPriceNodes") - registry.getIntValue("onix.minNumberOfPriceNodes")) + registry.getIntValue("onix.minNumberOfPriceNodes") - 1;
		for(int i = 0; i < numberOfPrices; i++)
		{
			int randomPriceType = random.nextInt(5);
			String priceType;
			switch(randomPriceType)
			{
			case(1):
				priceType = "24";
				break;
			case(2):
				priceType = "02";
				break;
			case(3):
				priceType = "22";
				break;
			case(4):
				priceType = "42";
				break;
			case(0):
			default:
				priceType = "04";
			}
			Price nextPrice = buildPriceObject(priceType, basePrice);
			priceSet.add(nextPrice);
		}
		
		// generate a price element for every price in the set
		OnixPriceBuilder oprb = new OnixPriceBuilder(version, tagType, argumentsMap);
		Iterator<Price> iterator = priceSet.iterator();
		while(iterator.hasNext())
		{
			Price price = iterator.next();
			price.addPriceArguments(argumentsMap);
			oprb.appendElementsTo(supplyDetailNode);
			price.removePriceArguments(argumentsMap);
		}
		
		return supplyDetailNode;
	}
	
	private Price buildPriceObject(String nodeType, double basePrice)
	{
		// select a random currency code and calculate the price for this currency
		String priceCodeString = registry.getString("currency.codes");
		String[] priceCodes = priceCodeString.split(" ");
		String thisPriceCode = priceCodes[random.nextInt(priceCodes.length)];
		
		// select a country code for the selected currency
		String countryCodeString = registry.getString("currency.country." + thisPriceCode);
		String[] countryCodes = countryCodeString.split(" ");
		String thisCountryCode = countryCodes[random.nextInt(countryCodes.length)];
		
		return new Price(nodeType, "" + basePrice, thisPriceCode, thisCountryCode);
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

class PriceComparator implements Comparator<Price>
{
	@Override
	public int compare(Price price1, Price price2)
	{
		int compareValue = price1.type.compareTo(price2.type);
		if(compareValue > 0)
		{
			return 1;
		}
		else if(compareValue == 0)
		{
			return price1.country.compareTo(price2.country);
		}
		else
		{
			return -1;
		}
	}
}

class Price
{
	String type, amount, currency, country;
	
	Price(String type, String amount, String currency, String country)
	{
		this.type = type;
		this.amount = amount;
		this.currency = currency;
		this.country = country;
	}
	
	void addPriceArguments(HashMap<String, String> argumentsMap)
	{
		argumentsMap.put("pricetype", type);
		argumentsMap.put("priceamount", amount);
		argumentsMap.put("currencycode", currency);
		argumentsMap.put("countriesincluded", country);
	}
	
	void removePriceArguments(HashMap<String, String> argumentsMap)
	{
		argumentsMap.remove("pricetype");
		argumentsMap.remove("priceamount");
		argumentsMap.remove("currencycode");
		argumentsMap.remove("countriesincluded");
	}
	
	public boolean equals(Price otherPrice)
	{
		return type.equals(otherPrice.type) && country.equals(otherPrice.country);
	}
	
	public int hashCode()
	{
		String concatenatedValues = type + amount + currency + country;
		return concatenatedValues.hashCode();
	}
}