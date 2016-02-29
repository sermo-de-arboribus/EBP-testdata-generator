package testdatagen.model.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

/**
 * A file that represents an e-book in Apple's proprietary iBook format. As this format is proprietary,
 * the Testdata Generator does not generate real iBook files. It just writes a random byte sequence with 
 * a file extension of .ibook
 */
public class IBookFile extends EBookFile
{
	private static final long serialVersionUID = 2L;
	
	/**
	 * Constructor
	 * @param title The product Title object that this iBook file belongs to.
	 * @param demoFlag This boolean flag indicates, if the product is meant as a demo or sample file (which has an influence on file naming).
	 */
	public IBookFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	@Override
	public String toString()
	{
		return "iBook[" + title.getIsbn13() + "]";
	}

	@Override
	public java.io.File generate(final java.io.File destDir)
	{
		Random random = new Random();
		String destPath = FilenameUtils.concat(destDir.getPath(), buildFileName());
		FileOutputStream out = null;
		
		// write random byte sequence to disk
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