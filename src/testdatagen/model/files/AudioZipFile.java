package testdatagen.model.files;

import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.model.Title;
import testdatagen.utilities.Utilities;


public class AudioZipFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public AudioZipFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) + "_audio.zip", demoFlag);
		this.ISBN = ISBN;
	}
	
	public String toString()
	{
		return "AudioZip["+ISBN+"]";
	}

	@Override
	public java.io.File generate(Title title, java.io.File destDir)
	{
		Random random = new Random();
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		java.io.File tempDir = new java.io.File(FilenameUtils.concat(Utilities.getConfigDir().getPath(), registry.getString("file.tempFolder") + "/" + title.getIsbn13()));
		// generate booklet
		if(random.nextBoolean())
		{
			// PDF booklet: simply use another PDFCoverFile as a PDF booklet
			PDFCoverFile PDFBooklet = new PDFCoverFile(title.getIsbn13());
			PDFBooklet.generate(title, tempDir);
		}
		else
		{
			// EPUB booklet
			EpubFile EpubBooklet = new EpubFile(title.getIsbn13(), false, true);
			EpubBooklet.generate(title, tempDir);
			
		}
		
		// generate sample file
		
		// generate audio file container
		
		// Zip it up
		return null;
	}
}