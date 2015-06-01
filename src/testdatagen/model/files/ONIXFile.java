package testdatagen.model.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

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
		
		return product;
	}
}