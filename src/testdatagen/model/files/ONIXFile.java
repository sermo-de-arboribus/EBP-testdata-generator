package testdatagen.model.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import nu.xom.Text;
import testdatagen.model.Title;
import testdatagen.utilities.TitleUtils;
import testdatagen.utilities.Utilities;

// This class depends on the XOM library: Currently project directory contains version XOM 1.1;
// TODO: better replace this with newer version XOM 1.2.10;
public class ONIXFile extends File
{

	public ONIXFile(long ISBN)
	{
		super(Long.toString(ISBN) + "_onix.xml");
	}
	
	public java.io.File generate(Title title, java.io.File destDir)
	{
		Element ONIXroot = new Element("ONIXmessage");
		Element ONIXheader = buildHeader();
		Element ONIXproduct = buildProductNode(title);
		
		ONIXroot.appendChild(ONIXheader);
		ONIXroot.appendChild(ONIXproduct);
		
		Document doc = new Document(ONIXroot);
		java.io.File outputFile = new java.io.File(FilenameUtils.concat(destDir.getPath(), title.getIsbn13() + "_onix.xml"));
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
		return outputFile;
	}
	
	/*
	 * The contributor node currently expects one title to have only one author. This needs to be modified when we allow
	 * for multiple authors and different author types per title
	 */
	private Element buildContributorNode(Title title)
	{
		Element contributorNode = new Element("contributor");
		
		// Contributor Role
		Element b035 = new Element("b035");
		b035.appendChild(new Text("A01"));
		contributorNode.appendChild(b035);
		
		// Sequence Number within role
		Element b340 = new Element("b340");
		b340.appendChild(new Text("1"));
		contributorNode.appendChild(b340);
		
		// complete author name
		Element b036 = new Element("b036");
		b036.appendChild(new Text(title.getAuthor()));
		contributorNode.appendChild(b036);
		
		// complete author name inverted
		Element b037 = new Element("b037");
		b037.appendChild(new Text(title.getAuthorLastName() + ", " + title.getAuthorFirstName()));
		contributorNode.appendChild(b037);
		
		// author's first name(s)
		Element b039 = new Element("b039");
		b039.appendChild(new Text(title.getAuthorFirstName()));
		contributorNode.appendChild(b039);
		
		// author's last name
		Element b040 = new Element("b040");
		b040.appendChild(new Text(title.getAuthorLastName()));
		contributorNode.appendChild(b040);
		
		// author blurb
		Element b044 = new Element("b044");
		b044.appendChild(new Text(title.getAuthorBlurb()));
		contributorNode.appendChild(b044);
		
		return contributorNode;
	}
	
	private Element buildHeader()
	{
		Element header = new Element("header");
		
		Element m174 = new Element("m174");
		m174.appendChild(new Text("IT-E-Books-Verlag"));
		header.appendChild(m174);
		
		Element m175 = new Element("m175");
		m175.appendChild(new Text(TitleUtils.getRandomFullName()));
		header.appendChild(m175);
		
		Element m283 = new Element("m283");
		m283.appendChild(new Text("noreply@kno-va.de"));
		header.appendChild(m283);
		
		Element m182 = new Element("m182");
		m182.appendChild(new Text(Utilities.getDateForONIX2(new Date())));
		header.appendChild(m182);
		
		Element m184 = new Element("m184");
		m184.appendChild(new Text("ger"));
		header.appendChild(m184);
		
		Element m185 = new Element("m185");
		m185.appendChild(new Text("04"));
		header.appendChild(m185);
		
		Element m186 = new Element("m186");
		m186.appendChild(new Text(TitleUtils.getRandomCurrencyCode()));
		header.appendChild(m186);
		
		return header;
	}
	
	private Element buildLanguageNode(String langCode)
	{
		Element langNode = new Element("language");
		
		Element b253 = new Element("b253");
		b253.appendChild(new Text("01"));
		langNode.appendChild(b253);
		
		Element b252 = new Element("b252");
		b252.appendChild(new Text(langCode));
		langNode.appendChild(b252);
		
		return langNode;
	}
	
	private Element buildMainSubjectNode()
	{
		Element msn = new Element("mainsubject");
		
		Element b191 = new Element("b191");
		b191.appendChild(new Text("26"));
		msn.appendChild(b191);
		
		Element b068 = new Element("b068");
		b068.appendChild(new Text("2.0"));
		msn.appendChild(b068);
		
		Element b069 = new Element("b069");
		b069.appendChild(new Text(TitleUtils.getRandomWarengruppeCode()));
		msn.appendChild(b069);
		
		return msn;
	}
	
	private Element buildOtherTextNode(Title title)
	{
		Element otNode = new Element("othertext");
		
		// TODO: add some variety to the generated othertext types
		Element d102 = new Element("d102");
		d102.appendChild(new Text("01"));
		otNode.appendChild(d102);
		
		Element d104 = new Element("d104");
		d104.addAttribute(new Attribute("textformat", "07"));
		d104.appendChild(new Text(title.getShortBlurb()));
		otNode.appendChild(d104);
		
		return otNode;
	}
	
	private Element buildProductIdentifier(String ProdIDType, long ISBN)
	{
		Element productIdNode = new Element("productidentifier");
		
		Element b221 = new Element("b221");
		b221.appendChild(new Text(ProdIDType)); // ProdIDTypes are not validated. Should we?
		productIdNode.appendChild(b221);
		
		Element b244 = new Element("b244");
		b244.appendChild(new Text(String.valueOf(ISBN)));
		productIdNode.appendChild(b244);
		
		return productIdNode;
	}
	
	private Element buildProductNode(Title title)
	{
		Element product = new Element("product");
		
		// Record Reference
		Element a001 = new Element("a001");
		a001.appendChild(new Text(title.getUid()));
		product.appendChild(a001);
		
		// Notification Type
		Element a002 = new Element("a002");
		a002.appendChild(new Text("03"));
		product.appendChild(a002);
		
		// Product Identifier
		Element prodId1 = buildProductIdentifier("03", title.getIsbn13());
		product.appendChild(prodId1);
		
		Element prodId2 = buildProductIdentifier("15", title.getIsbn13());
		product.appendChild(prodId2);
		
		// Product Form
		Element b012 = new Element("b012");
		b012.appendChild(new Text("DG")); // TODO: implement "AJ" products later
		product.appendChild(b012);

		// Epub Type
		Element b211 = new Element("b211");
		b211.appendChild(new Text(title.getEpubTypeForONIX()));
		product.appendChild(b211);
		
		// Epub Type Note (with protection information)
		Element b277 = new Element("b277");
		b277.appendChild(new Text(title.getProtectionTypeForONIX()));
		product.appendChild(b277);
		
		// Title
		Element titleNode = buildTitleNode("01", title);
		product.appendChild(titleNode);
		
		Element shortTitleNode = buildTitleNode("05", title);
		product.appendChild(shortTitleNode);
		
		// Contributor
		Element contributorNode = buildContributorNode(title);
		product.appendChild(contributorNode);
		
		// Language
		Element languageNode = buildLanguageNode("ger");
		product.appendChild(languageNode);
		
		// Number of Pages
		Element b061 = new Element("b061");
		b061.appendChild(new Text("320"));
		product.appendChild(b061);
		
		// Main subject
		Element mainsubject = buildMainSubjectNode();
		product.appendChild(mainsubject);
		
		// Subjects
		Element subject = buildSubjectNode();
		product.appendChild(subject);
		
		// Othertext
		Element ot = buildOtherTextNode(title);
		product.appendChild(ot);
		
		// TODO: Add handling of mediafile nodes
		
		// Publisher information
		Element publisher = buildPublisherNode();
		product.appendChild(publisher);
		
		// Publishing Status
		Element pubStatus = new Element("b394");
		pubStatus.appendChild(new Text("04")); // TODO: would be nice to have some variation of the publishing status, but 04 ("active") is most common
		product.appendChild(pubStatus);
		
		// Publication date
		Random random = new Random();
		Element pubDate = new Element("b003");
		long currentDateMillis = new Date().getTime();
		long randomPubDate = random.nextInt(10) * 86400000L;
		randomPubDate = random.nextBoolean() ? randomPubDate : randomPubDate * -1;
		pubDate.appendChild(new Text(Utilities.getDateForONIX2(new Date(currentDateMillis + randomPubDate)).substring(0, 8)));
		product.appendChild(pubDate);

		// Sales Rights
		Element sr = buildSalesRightsNode();
		product.appendChild(sr);
		
		return product;
	}
	
	private Element buildPublisherNode()
	{
		Element pubNode = new Element("publisher");
		
		Element b291 = new Element("b291");
		b291.appendChild(new Text("01"));
		pubNode.appendChild(b291);
		
		Element b241 = new Element("b241");
		b241.appendChild(new Text("04"));
		pubNode.appendChild(b241);
		
		Element b243 = new Element("b243");
		b243.appendChild(new Text("56789"));
		pubNode.appendChild(b243);
		
		Element b081 = new Element("b081");
		b081.appendChild(new Text("IT E-Books-Verlag"));
		pubNode.appendChild(b081);
		
		return pubNode;
	}
	
	private Element buildSalesRightsNode()
	{
		Random random = new Random();
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
		Element subjectNode = new Element("subject");
		
		Element b067 = new Element("b067");
		b067.appendChild(new Text("20"));
		subjectNode.appendChild(b067);
		
		Element b069 = new Element("b069");
		b069.appendChild(new Text(TitleUtils.getRandomTopic(new Locale("de"))));
		subjectNode.appendChild(b069);
		
		return subjectNode;
	}
	private Element buildTitleNode(String titleType, Title title)
	{
		Element titleNode = new Element("title");
		Element b202 = new Element("b202");
		b202.appendChild(new Text(titleType)); // do we need to validate titleTypes?
		titleNode.appendChild(b202);
		// split titleString into main title and subtitle, if titleString contains a full-stop.
		String titleString = title.getName();
		String mainTitle = "";
		String subTitle = "";
		if(titleString.contains("."))
		{
			mainTitle = titleString.substring(0, titleString.lastIndexOf('.'));
			subTitle = titleString.substring(titleString.lastIndexOf('.') + 1).trim();
		}
		else 
		{
			mainTitle = titleString;
		}
		Element b203 = new Element("b203");
		// if titleType is "05", this is meant to be a shortened title
		if(titleType.equals("05"))
		{
			String abbrevTitle = mainTitle;
			if(mainTitle.length() > 12)
			{
				abbrevTitle = mainTitle.substring(0, 10);
			}
			b203.appendChild(new Text(title.getAuthorLastName() + ", " + abbrevTitle + "..."));
			titleNode.appendChild(b203);
		}
		else
		{
			b203.appendChild(new Text(mainTitle));
			titleNode.appendChild(b203);
			if(subTitle != "")
			{
				Element b029 = new Element("b029");
				b029.appendChild(new Text(subTitle));
				titleNode.appendChild(b029);
			}
		}
		return titleNode;
	}
}