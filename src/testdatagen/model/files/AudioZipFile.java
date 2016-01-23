package testdatagen.model.files;

import java.util.Random;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;


public class AudioZipFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public AudioZipFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	public String toString()
	{
		return "AudioZip["+ title.getIsbn13() +"]";
	}

	@Override
	public java.io.File generate(java.io.File destFolder)
	{
		Random random = new Random();
		java.io.File tempDir = Utilities.getTempDir();
		// generate booklet
		if(random.nextBoolean())
		{
			// PDF booklet: simply use another PDFCoverFile as a PDF booklet
			PDFCoverFile PDFBooklet = new PDFCoverFile(title);
			PDFBooklet.generate(tempDir);
		}
		else
		{
			// EPUB booklet
			EpubFile EpubBooklet = new EpubFile(title, false, true);
			EpubBooklet.generate(tempDir);
		}
		
		// TODO: generate sample file
		
		// TODO: generate audio file container
		
		// TODO: Zip it up
		return null;
	}
	
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + (isDemoFile() ? "_Extract" : "" ) + "_audio.zip";
	}
}