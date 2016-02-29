package testdatagen.model.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

/**
 * SoftwareZipFile represents and generates zip files with dummy content that conceptually represent 
 * downloadable software products 
 */
public class SoftwareZipFile extends EBookFile
{
	private static final long serialVersionUID = 2L;
	
	/**
	 * Constructor
	 * @param title The product Title object that this file belongs to
	 * @param demoFlag This boolean flag indicates, if the product is meant as a demo or sample file (which has an influence on file naming).
	 */
	public SoftwareZipFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	@Override
	public String toString()
	{
		return "SoftwareZip["+ title.getIsbn13() +"]";
	}

	@Override
	public java.io.File generate(final java.io.File destDir) 
	{
		String destPath = FilenameUtils.concat(destDir.getPath(), buildFileName());
		Random random = new Random();
		ZipOutputStream out = null;
		
		try
		{
			out = new ZipOutputStream(new FileOutputStream(destPath));
			int numberOfIterations = random.nextInt(19) + 1;
			for(int i = 1; i <= numberOfIterations; i++ )
			{
				ZipEntry nextFile = new ZipEntry("dummyfile_" + i + ".exe");
				nextFile.setMethod(ZipOutputStream.DEFLATED);
				out.putNextEntry(nextFile);
				int fileSize = random.nextInt(300000) + 1;
				byte[] randomBytes = new byte[fileSize];
				random.nextBytes(randomBytes);
				out.write(randomBytes);
				out.closeEntry();
			}
			return new java.io.File(destPath);
		}
		catch(IOException exc)
		{
			Utilities.showErrorPane("Error zipping SoftwareZip file " + buildFileName(), exc);
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
		return Long.toString(title.getIsbn13())  + (isDemoFile() ? "_demosoftware" : "" ) + ".zip";
	}
}