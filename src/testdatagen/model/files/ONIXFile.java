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

public class ONIXFile extends File
{
	private String version;
	private int tagType;
	
	public ONIXFile(long ISBN, int tagType, String version)
	{
		super(Long.toString(ISBN) + "_onix.xml");
		this.version = version;
		this.tagType = tagType;
	}
	
	public java.io.File generate(Title title, java.io.File destDir)
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
		if(tagType == OnixPartsDirector.REFERENCETAG)
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
}