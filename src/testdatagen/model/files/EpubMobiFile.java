package testdatagen.model.files;

import java.io.IOException;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

public class EpubMobiFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public EpubMobiFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	public java.io.File generate(final java.io.File destFolder)
	{
		// create an Epub file in a temporary folder
		java.io.File tempDir = Utilities.getTempDir();
		EpubFile epubFile = new EpubFile(title, this.isDemoFile());
		java.io.File epubMobiTempFile = epubFile.generate(tempDir);
		
		java.io.File epubMobiDestFile = new java.io.File(FilenameUtils.concat(destFolder.getAbsolutePath(), buildFileName()));
		// rename Epub file and move it to destination folder
		try
		{
			FileUtils.moveFile(epubMobiTempFile, epubMobiDestFile);
		}
		catch (FileExistsException exc)
		{
			Utilities.showErrorPane("File " + epubMobiDestFile.getAbsolutePath() + "already exists!\n", exc);
		}
		catch (IOException exc)
		{
			Utilities.showErrorPane("Could not move temporary EpubMobi file to " + destFolder.getAbsolutePath() + "\n", exc);
		}
		
		try
		{
			FileUtils.deleteDirectory(tempDir);
		}
		catch (IOException ex)
		{
			Utilities.showWarnPane("Could not delete temporary directory " + tempDir.getPath() + " for " + title.getIsbn13());
		}
		return FileUtils.getFile(epubMobiDestFile);
	}
	
	public String toString()
	{
		String fileString = "EpubMobi";
		if(this.isDemoFile())
		{
			fileString += "_extract";
		}
		return fileString + "[" + title.getIsbn13() + "]";
	}
	
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + (isDemoFile() ? "_Extract" : "" ) + "_EpubMobi.epub";
	}
}