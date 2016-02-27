package testdatagen;

import testdatagen.model.Title;
import testdatagen.model.files.GraphicFile;
import testdatagen.model.files.JpegFile;
import testdatagen.model.files.PDFCoverFile;

/**
 * The GraphicFileFactory is a singleton that instantiates file objects from the testdatagen.model.files package.
 * These objects represent image files (JPG, PDF used as a cover image).
 */
public class GraphicFileFactory implements FileFactory
{
	private static final String[] validFormats = {"PDF", "JPEG", "jpg", "JPG", "JPeg"};
	// no lazy initialization needed, a GraphicFileFactory will be required anyway
	private static GraphicFileFactory instance = new GraphicFileFactory();
	
	// singleton: objects are only passed out by getInstance() method
	private GraphicFileFactory()
	{
	}
	
	/**
	 * Returns the reference to the singleton object
	 * @return Singleton instance of an GraphicFileFactory
	 */
	public static GraphicFileFactory getInstance()
	{
		return instance;
	}
	
	/**
	 * (non-Javadoc)
	 * @see testdatagen.FileFactory#generateFile(java.lang.String, testdatagen.model.Title)
	 */
	@Override
	public GraphicFile generateFile(final String format, final Title title)
	{
		// default graphic file type: Cover
		return generateFile(format, title, GraphicFile.Type.COVER);
	}
	
	/**
	 * Instantiates an object that represents an image file.
	 * @param format File format (e.g. JPG, PDF).
	 * @param title The product Title object that this image file belongs to.
	 * @param type The type of graphic file (cover, back cover, screenshot, etc...)
	 * @return The instantiated object from the required sub-class of GraphicFile.
	 */
	public GraphicFile generateFile(final String format, final Title title, final GraphicFile.Type type)
	{
		GraphicFile newFile = null;
		validateFormat(format);
		switch(format)
		{
			case "PDF":
				newFile = new PDFCoverFile(title);
				break;
			case "JPEG":
			case "jpg":
			case "JPG":
			case "JPeg":
				newFile = new JpegFile(title, type);
				break;
			default:
				break;
		}
		return newFile;
	}
	
	// constructor for screenshot and packshot files that should have sequence numbers
	/**
	 * While there can be only one cover and back cover file for a product, a product can have 
	 * several screenshot or packshot images. Therefore packshot images require a sequence number
	 * in their file names. This constructor takes care of such file names.
	 * @param format File format (e.g. JPG, PDF).
	 * @param title The product Title object that this image file belongs to.
	 * @param type The type of graphic file (cover, back cover, screenshot, etc...)
	 * @param sequenceNumber The sequence number to be used in the file name.
	 * @return The instantiated object from the required sub-class of GraphicFile.
	 */
	public GraphicFile generateFile(final String format, final Title title, final GraphicFile.Type type, final int sequenceNumber)
	{
		GraphicFile newFile = null;
		validateFormat(format);
		switch(format)
		{
			case "JPEG":
			case "jpg":
			case "JPG":
			case "JPeg":
				newFile = new JpegFile(title, type, sequenceNumber);
				break;
			default:
				break;
		}
		return newFile;
	}
	
	private void validateFormat(final String format)
	{
		for(String validFormat : validFormats)
		{
			if(format.equals(validFormat))
			{
				return;
			}
		}
		throw new IllegalArgumentException("Unknown file format name passed to Graphic file factory");
	}
}
