package testdatagen.onixbuilder;

import nu.xom.Attribute;
import nu.xom.Element;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import testdatagen.model.Price;
import testdatagen.model.Subject;
import testdatagen.model.Title;
import testdatagen.utilities.ISBNUtils;
import testdatagen.utilities.TitleUtils;
import testdatagen.utilities.Utilities;

public class OnixPartsDirector
{
	// constants defined for ONIX tag types
	public static final int SHORTTAG = 1;
	public static final int REFERENCETAG = 2;
	
	private LinkedList<OnixPartsBuilder> requiredElements;
	private Title title;
	/*
	 * The OnixPartsDirector class maintains a pattern for an ONIX file to be generated. 
	 * When an object of the OnixPartsDirector class is instantiated, a default pattern for an ONIX file
	 * is created. After creation of the default file, the default settings can be modified by
	 * calling specific methods on the OnixPartsDirector object. 
	 */
	public OnixPartsDirector(Title title)
	{
		this.title = title;
		Random random = new Random();
		
		requiredElements = new LinkedList<OnixPartsBuilder>();
		
		// add default header
		requiredElements.add(new OnixHeaderBuilder(new HashMap<String, String>()));
		
		// add record reference
		HashMap<String, String> recRefArgs = new HashMap<String, String>();
		recRefArgs.put("recordreference", "test-" + title.getIsbn13());
		requiredElements.add(new OnixRecordReferenceBuilder(recRefArgs));
		
		// add notification Type
		requiredElements.add(new OnixNotificationTypeBuilder(new HashMap<String, String>()));
		
		// add product composition
		requiredElements.add(new OnixProductCompositionBuilder(new HashMap<String, String>()));
		
		// add default product Identifier (type 15: ISBN-13)
		addProductIdentifier("15");
		
		// product form should be handled by the onix 2.1 / onix 3 build methods
		
		// add product form detail
		addProductFormDetail(title.getEpubTypeForProductFormDetail());

		// add protection type
		HashMap<String, String> protectionArgs = new HashMap<>();
		protectionArgs.put("technicalprotection", title.getProtectionTypeForONIX());
		requiredElements.add(new OnixTechnicalProtectionBuilder(protectionArgs));
		
		// TODO: implement Series / Collection 
		
		// add a title
		addTitleWithSubtitle("01");
		
		// add (first / main) author
		HashMap<String, String> authorArgs = new HashMap<>();
		authorArgs.put("fullname", title.getAuthor());
		authorArgs.put("invertedname", title.getAuthorLastName() + ", " + title.getAuthorFirstName());
		authorArgs.put("namesbeforekey", title.getAuthorFirstName());
		authorArgs.put("keynames", title.getAuthorLastName());
		authorArgs.put("biographicalnote", title.getAuthorBlurb());
		authorArgs.put("birthdate", "19440704");
		authorArgs.put("deathdate", "20131228");
		requiredElements.add(new OnixContributorBuilder(authorArgs));
		
		// TODO: add a corporate contributor? Add the option of adding further authors?
		// TODO: do we want to implement EditionNumber / EditionStatement?
		
		// add a default language node
		requiredElements.add(new OnixLanguageBuilder(new HashMap<String, String>()));
		
		// add number of pages (in ONIX 2.1)
		HashMap<String, String> extentArgs = new HashMap<String, String>();
		extentArgs.put("numberofpages", "320");
		requiredElements.add(new OnixExtentBuilder(extentArgs));
		
		// add a default extent element
		requiredElements.add(new OnixExtentBuilder (new HashMap<String, String>()));
		
		// add main subject element
		HashMap<String, String> mainSubjectArgs = new HashMap<String, String>();
		mainSubjectArgs.put("subjectcode", TitleUtils.getRandomWarengruppeCode());
		mainSubjectArgs.put("subjectheadingtext", TitleUtils.getRandomTopic(new Locale("de")));
		requiredElements.add(new OnixSubjectBuilder(OnixSubjectBuilder.SUBJECTTYPE_MAIN, mainSubjectArgs));
		
		// add "normal" subject element
		HashMap<String, String> subjectArgs = new HashMap<String, String>();
		subjectArgs.put("subjectheadingtext", TitleUtils.getRandomTopic(new Locale("de")));
		requiredElements.add(new OnixSubjectBuilder(subjectArgs));
		
		// add descriptive / advertising text content
		HashMap<String, String> textArgs = new HashMap<String, String>();
		textArgs.put("texttypecode", "03");
		textArgs.put("contentaudience", "00");
		textArgs.put("text", title.getShortBlurb());
		requiredElements.add(new OnixTextBuilder(textArgs));
		
		// we don't need to set a default media resource, as the title's setMediaFileUrl() method is handling this
		
		// add an imprint node
		requiredElements.add(new OnixImprintBuilder(new HashMap<String,String>()));
		
		// add publisher information
		requiredElements.add(new OnixPublisherBuilder(new HashMap<String, String>()));
		
		// add place and country of publication
		requiredElements.add(new OnixCityOfPublicationBuilder(new HashMap<String, String>()));
		requiredElements.add(new OnixCountryOfPublicationBuilder(new HashMap<String, String>()));
		
		// add publishing status
		requiredElements.add(new OnixPublishingStatusBuilder(new HashMap<String, String>()));
		
		// add publication date
		HashMap<String, String> pubDateArgs = new HashMap<String, String>();
		long currentDateMillis = new Date().getTime();
		long randomPubDate = random.nextInt(10) * 86400000L;	
		randomPubDate = random.nextBoolean() ? randomPubDate : randomPubDate * -1;
		pubDateArgs.put("publishingdate", Utilities.getDateForONIX2(new Date(currentDateMillis + randomPubDate)).substring(0, 8));
		requiredElements.add(new OnixPublishingDateBuilder(pubDateArgs));
		
		// add sales rights
		HashMap<String, String> salesRightsArgs = new HashMap<String, String>();

		// build a random country list
		StringBuffer countryList = new StringBuffer("");
		int j = random.nextInt(6) + 1;
		for(int i = 0; i < j; i++)
		{
			countryList.append(Utilities.getCountryForONIX());
			if(i < j - 1)
			{		// Also note that we probably need an Extent builder to give us a b061 / NumberOfPages element
				countryList.append(" ");
			}
		}
		salesRightsArgs.put("salesrightstype", "0" + (random.nextInt(1) + 1));
		salesRightsArgs.put("countriesincluded", countryList.toString());
		requiredElements.add(new OnixSalesRightsBuilder(salesRightsArgs));
		
		// add some related product
		HashMap<String, String> relatedProductArgs = new HashMap<String, String>();
		// determine the ISBN to be used for a related product
		String ISBNString = Long.toString(title.getIsbn13());
		ISBNString = ISBNString.substring(0, 6) + "5" + ISBNString.substring(7, 12);
		String checkDigit = ISBNUtils.calculateCheckDigit(ISBNString);
		ISBNString = ISBNString + checkDigit;
		long relISBN = Long.parseLong(ISBNString);
		relatedProductArgs.put("productidvalue", Long.toString(relISBN));
		relatedProductArgs.put("productform", "BC");
		requiredElements.add(new OnixRelatedProductBuilder(relatedProductArgs));
		
		// add supply detail
		requiredElements.add(new OnixSupplierBuilder(new HashMap<String, String>()));
		
		// add product availability node
		requiredElements.add(new OnixProductAvailabilityBuilder(new HashMap<String, String>()));
		
		// add an expected ship date
		HashMap<String, String> expectedShipDateArgs = new HashMap<String, String>();
		// calculate a random expected ship date
		// Expected Ship Date, +/- 10 days from today
		currentDateMillis = new Date().getTime();
		long randomShipDate = random.nextInt(10) * 86400000L;
		randomShipDate = random.nextBoolean() ? randomShipDate : randomShipDate * -1;
		Date actualShipDate = new Date(currentDateMillis + randomShipDate);
		String shipDateString = Utilities.getDateForONIX2(actualShipDate).substring(0, 8);
		expectedShipDateArgs.put("supplydaterole", "08");
		expectedShipDateArgs.put("date", shipDateString);
		requiredElements.add(new OnixSupplyDateBuilder(expectedShipDateArgs));
		
		// add an on sale date
		HashMap<String, String> onSaleDateArgs = new HashMap<String, String>();
		long onSaleDate = random.nextInt(10) * 86400000L;
		onSaleDate = random.nextBoolean() ? onSaleDate : onSaleDate * -1;
		Date actualOnSaleDate = new Date(currentDateMillis + onSaleDate);
		shipDateString = Utilities.getDateForONIX2(actualOnSaleDate).substring(0, 8);
		onSaleDateArgs.put("supplydaterole", "02");
		onSaleDateArgs.put("date", shipDateString);
		requiredElements.add(new OnixSupplyDateBuilder(onSaleDateArgs));
		
		// add an 04-type price for Germany - further prices should become a configuration option
		// determine random base price
		Price basePrice = title.getBasePrice();
		HashMap<String, String> priceArgs = new HashMap<String, String>();
		basePrice.addPriceArguments(priceArgs);
		requiredElements.add(new OnixPriceBuilder(priceArgs));
	}
	
	public void addMediaResource(String url)
	{
		HashMap<String, String> mediaArgs = new HashMap<String, String>();
		mediaArgs.put("resourcelink", url);
		requiredElements.add(new OnixMediaResourceBuilder(mediaArgs));
	}
	
	public void addProductFormDetail(String code)
	{
		HashMap<String, String> productFormDetailArgs = new HashMap<>();
		productFormDetailArgs.put("productformdetail", code);
		requiredElements.add(new OnixProductFormDetailBuilder(productFormDetailArgs));
	}
	
	public void addProductIdentifier(String type)
	{
		String productIdValue;
		switch(type)
		{
			case "06": // DOI
				productIdValue = "10.2379/" + title.getIsbn13();
				break;
			case "22": // URN
				productIdValue = "urn:isbn:" + title.getIsbn13();
				break;
			default:
				productIdValue = Long.toString(title.getIsbn13());
		}
		
		HashMap<String, String> productIdArgs = new HashMap<>();
		productIdArgs.put("productidtype", type);
		productIdArgs.put("productidvalue", productIdValue);
		requiredElements.add(new OnixProductIdentifierBuilder(productIdArgs));
	}
	
	public void addSubject(Subject newSubject)
	{
		HashMap<String, String> subjectArgs = new HashMap<String, String>();
		subjectArgs.put("subjectcode", newSubject.getSubjectcode());
		subjectArgs.put("subjectheadingtext", newSubject.getSubjectheadingtext());
		subjectArgs.put("subjectschemeidentifier", newSubject.getSubjectschemeidentifier());
		subjectArgs.put("subjectheadingtext", newSubject.getSubjectheadingtext());
		if(newSubject.isMainSubject())
		{
			subjectArgs.put("mainsubjectschemeidentifier", newSubject.getSubjectschemeidentifier());
			requiredElements.add(new OnixSubjectBuilder(OnixSubjectBuilder.SUBJECTTYPE_MAIN, subjectArgs));
		}
		else
		{
			requiredElements.add(new OnixSubjectBuilder(OnixSubjectBuilder.SUBJECTTYPE_NORMAL, subjectArgs));
		}
		
	}
	
	public void addTitle(String titleType)
	{
		HashMap<String, String> titleArgs = new HashMap<>();
		titleArgs.put("titletext", determineTitleStringByType("01", title));
		OnixTitleBuilder otb = new OnixTitleBuilder(titleArgs);
		requiredElements.add(otb); 
	}
	
	public void addTitleWithSubtitle(String titleType)
	{
		HashMap<String, String> titleArgs = new HashMap<>();
		titleArgs.put("titletext", determineTitleStringByType("01", title));
		titleArgs.put("subtitle", determineTitleStringByType("subtitle", title));
		OnixTitleBuilder otb = new OnixTitleBuilder(titleArgs);
		requiredElements.add(otb); 
	}
	
	public Element buildOnix2(int tagType)
	{
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		Element parent = root;
		root.addAttribute(new Attribute("release", "2.1"));

		// first add specific ONIX 2.1 elements
		replaceProductForm("DG");
		
		// then sort the list of required elements
		Collections.sort(requiredElements);
		
		Iterator<OnixPartsBuilder> iterator = requiredElements.iterator();
		while(iterator.hasNext())
		{
			OnixPartsBuilder builder = iterator.next();
			
			// We need to distinguish between two styles of appending elements:
			// Elements created with direct subclasses of OnixPartsBuilder use the build() method,
			// elements created with subclasses of OnixSupplyDetailPartsBuilder use the appendElementsTo() method.
			// The first element that uses an OnixSupplyDetailPartsBuilder has the static sequence number 3000.
			if(builder.getSequenceNumber() < 3000)
			{
				Element nextElement = builder.build("2.1", tagType);
				// Builders that product elements that are only valid in ONIX 3 might return null
				// when called on with onixVersion 2.1
				if(nextElement != null)
				{
					parent.appendChild(nextElement);	
				}
				
				// if we have just built the header, then create a <product> node an make it the
				// parent element for all the following elements
				if(builder.getSequenceNumber() == 100)
				{
					Element productElement = createElement(tagType, "Product");
					parent.appendChild(productElement);
					parent = productElement;
				}
			}
			else if (builder.getSequenceNumber() == 3000)
			{
				Element supplyDetailNode = builder.build("2.1", tagType);
				parent.appendChild(supplyDetailNode);
				parent = supplyDetailNode;
			}
			else // here we handle the <supplydetail> child elements
			{
				OnixSupplyDetailPartsBuilder supplyDetailPartsBuilder = (OnixSupplyDetailPartsBuilder) builder;
				supplyDetailPartsBuilder.appendElementsTo(parent, "2.1", tagType);
			}
		}
		
		return root;
	}
	
	public Element buildOnix3(int tagType)
	{
		String rootElementName = getRootName(tagType);
		Element root = new Element(rootElementName);
		root.addAttribute(new Attribute("release", "3.0"));
		Element parent = root;
		Element product = null;
		
		// first add specific ONIX 3.0 element
		replaceProductForm("ED");
		
		// then sort the list of required elements
		Collections.sort(requiredElements);
		
		Iterator<OnixPartsBuilder> iterator = requiredElements.iterator();
		
		// when iterating through the OnixPartsBuilders, we use an integer flag to determine if we need to 
		// create a new parent node for the following elements.
		// 
		int flag = 0;
		
		while(iterator.hasNext())
		{
			OnixPartsBuilder builder = iterator.next();
			// we've reached the first element after the header, so instantiate a product node and increment the flag
			if(flag == 0 && builder.getSequenceNumber() > 100)
			{
				product = createElement(tagType, "Product");
				parent.appendChild(product);
				parent = product;
				flag++;
			}
			// we've reached the first element for the <descriptivedetail> composite
			if(flag == 1 && builder.getSequenceNumber() >= 450)
			{
				Element descriptiveDetail = createElement(tagType, "DescriptiveDetail");
				product.appendChild(descriptiveDetail);
				parent = descriptiveDetail;
				flag++;
			}
			// we've reached the first element for the <collateraldetail> composite
			if(flag == 2 && builder.getSequenceNumber() >= 1800)
			{
				Element collateralDetail = createElement(tagType, "CollateralDetail");
				product.appendChild(collateralDetail);
				parent = collateralDetail;
				flag++;
			}
			// we've reached the first element for the <publishingdetail> composite
			if(flag == 3 && builder.getSequenceNumber() >= 2000)
			{
				Element publishingDetail = createElement(tagType,"PublishingDetail");
				product.appendChild(publishingDetail);
				parent = publishingDetail;
				flag++;
			}
			// we've reached the first element for the <relatedmaterial> composite
			if(flag == 4 && builder.getSequenceNumber() > 2600)
			{
				Element relatedMaterial = createElement(tagType, "RelatedMaterial");
				product.appendChild(relatedMaterial);
				parent = relatedMaterial;
				flag++;
			}
			// we've reached the first element for the <productsupply> composite
			if(flag == 5 && builder.getSequenceNumber() > 2800)
			{
				Element productSupply = createElement(tagType, "ProductSupply");
				product.appendChild(productSupply);
				parent = productSupply;
				flag++;
			}
			
			// here we're adding the next element that is not a predecessor of <supplydetail>
			if(builder.getSequenceNumber() < 3000)
			{
				Element nextElement = builder.build("3.0", tagType);
				parent.appendChild(nextElement);
			}
			// this is the <supplydetail> node
			else if (builder.getSequenceNumber() == 3000)
			{
				Element supplyDetailNode = builder.build("3.0", tagType);
				parent.appendChild(supplyDetailNode);
				parent = supplyDetailNode;
			}
			else // here we handle the <supplydetail> child elements
			{
				OnixSupplyDetailPartsBuilder supplyDetailPartsBuilder = (OnixSupplyDetailPartsBuilder) builder;
				supplyDetailPartsBuilder.appendElementsTo(parent, "3.0", tagType);
			}
		}
		
		return root;
	}
	
	public void replaceMediaResource(String url)
	{
		Iterator<OnixPartsBuilder> iterator = requiredElements.iterator();
		while(iterator.hasNext())
		{
			OnixPartsBuilder nextBuilder = iterator.next();
			if(nextBuilder instanceof OnixMediaResourceBuilder)
			{
				iterator.remove();
			}
		}
		addMediaResource(url);
	}
	
	public void replaceProductForm(String productFormCode)
	{
		Iterator<OnixPartsBuilder> iterator = requiredElements.iterator();
		while(iterator.hasNext())
		{
			OnixPartsBuilder nextBuilder = iterator.next();
			if(nextBuilder instanceof OnixProductFormBuilder)
			{
				iterator.remove();
			}
		}
		HashMap<String, String> productFormArgs = new HashMap<String, String>();
		productFormArgs.put("productform", productFormCode);
		requiredElements.add(new OnixProductFormBuilder(productFormArgs));
	}
	
	private Element createElement(int tagType, String name)
	{
		if(tagType == OnixPartsBuilder.SHORTTAG)
		{
			name = name.toLowerCase();
		}
		return new Element(name);
	}
	
	/*
	 * Private helper method; returns the name of the ONIX file's root element based on the tag type (short tag / reference tag)
	 */
	private String getRootName(int tagType)
	{
		String rootElementName;
		switch(tagType)
		{
			case SHORTTAG:
				rootElementName = "ONIXmessage";
			case REFERENCETAG:
				rootElementName = "ONIXMessage";
			default:
				rootElementName = "ONIXmessage";
		}
		return rootElementName;
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