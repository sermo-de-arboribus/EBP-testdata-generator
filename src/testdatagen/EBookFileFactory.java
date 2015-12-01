package testdatagen;

import testdatagen.model.Title;
import testdatagen.model.files.AudioZipFile;
import testdatagen.model.files.EBookFile;
import testdatagen.model.files.EpubFile;
import testdatagen.model.files.EpubMobiFile;
import testdatagen.model.files.IBookFile;
import testdatagen.model.files.MobiFile;
import testdatagen.model.files.PDFFile;
import testdatagen.model.files.SoftwareZipFile;

public class EBookFileFactory extends FileFactory
{
	private static final String[] validFormats = {"PDF", "Epub", "EpubMobi", "IBook", "Mobi", "AudioZip", "SoftwareZip"};
	private static EBookFileFactory instance;
	
	// singleton: objects are only instantiated by getInstance() method
	private EBookFileFactory()
	{
	}
	
	// return reference to the singleton
	public static EBookFileFactory getInstance()
	{
		if(instance == null)
		{
			instance = new EBookFileFactory();
		}
		return instance;
	}
	
	@Override
	public EBookFile generateFile(final String format, final Title title)
	{
		return instantiateEbookFile(format, title, false);
	}
	
	public EBookFile generateDemoFile(final String format, final Title title)
	{
		return instantiateEbookFile(format, title, true);
	}
	
	private EBookFile instantiateEbookFile(String format, Title title, boolean demoFlag)
	{
		EBookFile newFile = null;
		validateFormat(format);
		switch(format)
		{
			case "PDF":
				newFile = new PDFFile(title, demoFlag);
				break;
			case "Epub":
				newFile = new EpubFile(title, demoFlag);
				break;
			case "EpubMobi":
				newFile = new EpubMobiFile(title, demoFlag);
				break;
			case "IBook":
				newFile = new IBookFile(title, demoFlag);
				break;
			case "Mobi":
				newFile = new MobiFile(title, demoFlag);
				break;
			case "AudioZip":
				newFile = new AudioZipFile(title, demoFlag);
				break;
			case "SoftwareZip":
				newFile = new SoftwareZipFile(title, demoFlag);
				break;
			default:
				break;
		}
		return newFile;
	}
	
	private void validateFormat(String format)
	{
		for(String validFormat : validFormats)
		{
			if(format.equals(validFormat))
			{
				return;
			}
		}
		throw new IllegalArgumentException("Unknown file format name passed to EBook file factory");
	}
}