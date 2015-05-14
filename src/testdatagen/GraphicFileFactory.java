package testdatagen;

import testdatagen.model.files.GraphicFile;
import testdatagen.model.files.JpegFile;
import testdatagen.model.files.PDFCoverFile;

public class GraphicFileFactory extends FileFactory
{
	private static final String[] validFormats = {"PDF", "JPEG", "jpg", "JPG", "JPeg"};
	private static GraphicFileFactory instance;
	
	// singleton: objects are only instantiated by getInstance() method
	private GraphicFileFactory()
	{
	}
	
	// return reference to the singleton
	public static GraphicFileFactory getInstance()
	{
		if(instance == null)
		{
			instance = new GraphicFileFactory();
		}
		return instance;
	}
	
	@Override
	public GraphicFile generateFile(String format, long ISBN)
	{
		// default graphic file type: Cover
		return generateFile(format, ISBN, GraphicFile.Type.COVER);
	}
	
	public GraphicFile generateFile(String format, long ISBN, GraphicFile.Type type)
	{
		GraphicFile newFile = null;
		validateFormat(format);
		switch(format)
		{
			case "PDF":
				newFile = new PDFCoverFile(ISBN);
				break;
			case "JPEG":
			case "jpg":
			case "JPG":
			case "JPeg":
				newFile = new JpegFile(ISBN, type);
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
		throw new IllegalArgumentException("Unknown file format name passed to Graphic file factory");
	}
}
