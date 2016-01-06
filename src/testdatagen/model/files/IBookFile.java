package testdatagen.model.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

public class IBookFile extends EBookFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IBookFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	public String toString()
	{
		return "iBook[" + title.getIsbn13() + "]";
	}

	@Override
	public File generate(final File destDir)
	{
		Random random = new Random();
		String destPath = FilenameUtils.concat(destDir.getPath(), buildFileName());
		FileOutputStream out = null;
		
		try
		{
			out = new FileOutputStream(destPath);
			int fileSize = random.nextInt(600000) + 1;
			byte[] randomBytes = new byte[fileSize];
			random.nextBytes(randomBytes);
			out.write(randomBytes);
			
			return new File(destPath);
		}
		catch (IOException exc)
		{
			Utilities.showErrorPane("Error saving iBooks file " + buildFileName(), exc);
			return null;
		}
		finally
		{
			Utilities.safeClose(out);
		}
	}
	
	@Override
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + (isDemoFile() ? "_Extract" : "" ) + ".ibook";
	}
}