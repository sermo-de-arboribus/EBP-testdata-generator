package testdatagen;

import testdatagen.model.Title;
import testdatagen.model.files.*;

/**
 * The EBookFileFactory is a singleton that instantiates file objects from the testdatagen.model.files package.
 * These objects represent e-book files.
 */
public class EBookFileFactory implements FileFactory
{
	private static final String[] validFormats = {"PDF", "Epub", "EpubMobi", "IBook", "Mobi", "AudioZip", "SoftwareZip"};
	private static final EBookFileFactory instance = new EBookFileFactory();
	
	// singleton: object is only instantiated when the class is loaded by the JVM, so the 
	// constructor is not meant to be accessed
	private EBookFileFactory()
	{
	}
	
	/**
	 * Returns the reference to the singleton object
	 * @return Singleton instance of an EBookFileFactory
	 */
	public static EBookFileFactory getInstance()
	{
		return instance;
	}
	
	/**
	 * (non-Javadoc)
	 * @see testdatagen.FileFactory#generateFile(java.lang.String, testdatagen.model.Title)
	 */
	@Override
	public EBookFile generateFile(final String format, final Title title)
	{
		return instantiateEbookFile(format, title, false);
	}

	/**
	 * This method generates a demo version of an e-book file (in fact it is just a normal e-book file
	 * with a different file naming convention).
	 * @param format The format of the e-book file (e.g. PDF, Epub) as a String.
	 * @param title The product Title object that the e-book file belongs to. 
	 * @return instantiated EBookFile object.
	 */
	public EBookFile generateDemoFile(final String format, final Title title)
	{
		return instantiateEbookFile(format, title, true);
	}
	
	/**
	 * Private helper method for instantiating an e-book file object of the required sub-class
	 * @param format The format of the e-book file (e.g. PDF, Epub) as a String.
	 * @param title The product Title object that the e-book file belongs to. 
	 * @param demoFlag A boolean flag to indicate if the e-book file serves as a demo file.
	 * @return Instantiated EBookFile object of the requested sub-class.
	 */
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