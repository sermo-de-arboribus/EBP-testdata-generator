package testdatagen.model.files;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

public class MobiFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public MobiFile(long ISBN, boolean demoFlag)
	{
		super(Long.toString(ISBN) + (demoFlag ? "_Extract" : "" ) + ".mobi", demoFlag);
		this.ISBN = ISBN;
	}
	
	public String toString()
	{
		String fileString = "Mobi";
		if(this.isDemoFile())
		{
			fileString += "_extract";
		}
		return fileString + "[" +ISBN+ "]";
	}

	@Override
	public File generate(Title title, File destPath)
	{
		// first generate an EpubMobi file as the base
		EpubMobiFile epubMobi = new EpubMobiFile(ISBN, this.isDemoFile());
		File tempDir = new File(FilenameUtils.concat(destPath.getParent(), ConfigurationRegistry.getRegistry().getString("file.tempFolder")));
		tempDir.mkdirs();
		String epubMobiPath = FilenameUtils.concat(tempDir.getPath(), epubMobi.getName());
		String epubPath = epubMobiPath.replace("_EpubMobi", "");
		epubMobi.generate(title, new File(epubMobiPath));
		
		// rename EpubMobi file
		try
		{
			FileUtils.moveFile(FileUtils.getFile(epubMobiPath), FileUtils.getFile(epubPath));
		}
		catch (IOException exc)
		{
			Utilities.showErrorPane("Could not rename temporary EpubMobi file to " + epubPath + "\n", exc);
		}
		
		// determine path to kindlegen
		File configDir = Utilities.getConfigDir();
		File kindleGenPath = new File(configDir.toURI().getPath() + "/kindlegen.exe");
		
		// run kindlegen
		Runtime rt = Runtime.getRuntime();
		String executionStatement = kindleGenPath.getAbsolutePath() + " " + "\"" + epubPath + "\"";
		try
		{
			Process p = rt.exec(executionStatement);
			p.waitFor();
		}
		catch (IOException exc)
		{
			Utilities.showErrorPane("Could not execute KindleGen with statement " + executionStatement + "\n", exc);
		}
		catch (InterruptedException exc)
		{
			Utilities.showErrorPane("Interrupted while waiting for KindleGen to finish\n", exc);
		}

		// copy generated mobi file from temp to destination folder
		File mobiDestinationFile = new File(epubPath.replace(".epub", ".mobi"));
		destPath.getParentFile().mkdirs();
		try
		{
			FileUtils.copyFileToDirectory(mobiDestinationFile, destPath.getParentFile());
		}
		catch (IOException exc)
		{
			Utilities.showErrorPane("Could not move Mobi file from temp folder to destination folder.\n", exc);
		}
		
		// delete temp folder
		try
		{
			FileUtils.deleteDirectory(tempDir);
		}
		catch (IOException ex)
		{
			Utilities.showWarnPane("Could not delete temporary directory " + tempDir.getPath() + " for Mobi of " + title.getIsbn13());
		}

		return mobiDestinationFile;
	}
}