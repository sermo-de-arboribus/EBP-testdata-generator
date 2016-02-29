package testdatagen.model.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

/**
 * Model class that represents and generates AudioZipFiles.
 * (Currently this class is not used, as the GUI only offers (Software)Zip products to be selected.
 * However, audio books in Zip containers are a product type of the E-Book-Plant and this product type
 * will be added to the test data generator in the future.
 */
public class AudioZipFile extends EBookFile
{
	private static final long serialVersionUID = 2L;
	
	/**
	 * Constructor.
	 * @param title The product object that this AudioZip file belongs to.
	 * @param demoFlag A flag to indicate if this file is a demo file (true) or a full product file (false).
	 */
	public AudioZipFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}

	@Override
	public java.io.File generate(final java.io.File destDir)
	{
		Random random = new Random();
		String destPath = FilenameUtils.concat(destDir.getPath(), buildFileName());
		ZipOutputStream out = null;
		
		java.io.File tempDir = Utilities.getTempDir();
		java.io.File bookletPath = null;
		
		// generate booklet
		if(random.nextBoolean())
		{
			// PDF booklet: simply use another PDFCoverFile as a PDF booklet
			PDFCoverFile PDFBooklet = new PDFCoverFile(title);
			bookletPath = PDFBooklet.generate(tempDir);
		}
		else
		{
			// EPUB booklet
			EpubFile EpubBooklet = new EpubFile(title, false, true);
			bookletPath = EpubBooklet.generate(tempDir);
		}
		
		try
		{
			// Write some random byte sequences into a random number of files with file extension .mp3
			out = new ZipOutputStream(new FileOutputStream(destPath));
			int numberOfIterations = random.nextInt(19) + 1;
			for(int i = 1; i <= numberOfIterations; i++ )
			{
				ZipEntry nextFile = new ZipEntry("" + i + "_dummyfile.mp3");
				nextFile.setMethod(ZipOutputStream.DEFLATED);
				out.putNextEntry(nextFile);
				int fileSize = random.nextInt(300000) + 1;
				byte[] randomBytes = new byte[fileSize];
				random.nextBytes(randomBytes);
				out.write(randomBytes);
				out.closeEntry();
			}
			// copy booklet into Zip container
			FileInputStream in = new FileInputStream(bookletPath);
			out.putNextEntry(new ZipEntry(bookletPath.getName()));
			int len;
			byte[] tmpBuf = new byte[1024];
			while ((len = in.read(tmpBuf)) > 0)
		    {
				out.write(tmpBuf, 0, len);
		    }
		    out.closeEntry();
		    in.close();
		    // create an audio sample, which has the naming convention that the file name must start with "v" and contain the ISBN
			ZipEntry audioSampleFile = new ZipEntry("v" + title.getIsbn13() + ".mp3");
			audioSampleFile.setMethod(ZipOutputStream.DEFLATED);
			out.putNextEntry(audioSampleFile);
			int fileSize = random.nextInt(300000) + 1;
			byte[] randomBytes = new byte[fileSize];
			random.nextBytes(randomBytes);
			out.write(randomBytes);
			out.closeEntry();
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
		
		// delete temp folder
		try
		{
			FileUtils.deleteDirectory(tempDir);
		}
		catch (IOException ex)
		{
			Utilities.showWarnPane("Could not delete temporary directory " + tempDir.getPath() + " for " + title.getIsbn13());
		}
		
		return new File(destPath);
	}

	@Override
	public String toString()
	{
		return "AudioZip["+ title.getIsbn13() +"]";
	}
	
	@Override
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + (isDemoFile() ? "_Extract" : "" ) + "_audio.zip";
	}
}