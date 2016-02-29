package testdatagen.model.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

public class SoftwareZipFile extends EBookFile
{
	private static final long serialVersionUID = 2L;
	
	public SoftwareZipFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	public String toString()
	{
		return "SoftwareZip["+ title.getIsbn13() +"]";
	}

	@Override
	public File generate(final File destDir) 
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
			return new File(destPath);
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