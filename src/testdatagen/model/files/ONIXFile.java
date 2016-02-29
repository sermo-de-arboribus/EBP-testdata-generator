package testdatagen.model.files;

import java.io.FileOutputStream;
import java.io.IOException;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.onixbuilder.*;
import testdatagen.utilities.Utilities;

/**
 * Class for representing Onix files. The actual generation of Onix XML elements is handled by the classes
 * in testdatagen.onixbuilder. This ONIXFile class only saves the generated XML tree to disk.
 */
public class ONIXFile extends File
{
	private String version;
	private int tagType;
	
	/**
	 * Constructor
	 * @param title The product Title object that this ONIXFile belongs to
	 * @param tagType The tagtype of the generated Onix file (SHORTTAG / REFERENCETAG)
	 * @param version The Onix version to be used (2.1 / 3.0)
	 */
	public ONIXFile(final Title title, final int tagType, final String version)
	{
		super(title);
		this.version = version;
		this.tagType = tagType;
	}
	
	@Override
	public java.io.File generate(final java.io.File destDir)
	{
		Element ONIXroot;
		if(version.equals("2.1"))
		{
			ONIXroot = title.getOnixPartsDirector().buildOnix2(tagType);
		}
		else
		{
			ONIXroot = title.getOnixPartsDirector().buildOnix3(tagType);
		}
		
		String tagTypeString = "";
		if(tagType == OnixPartsBuilder.REFERENCETAG)
		{
			tagTypeString = "reference";
		}
		else
		{
			tagTypeString = "short";
		}
		
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
		return outputFile;
	}
	
	@Override
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + "_onix" + version + OnixPartsBuilder.getTagTypeString(tagType)  + ".xml";
	}
}