package testdatagen.model.files;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;

import javax.swing.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

/**
 * Class that represents a Mobi file. Mobi is a proprietary format of Amazon (see https://www.amazon.com/gp/feature.html?docId=1000765211).
 * The Testdata Generator uses an EpubMobi internally (see testdatagen.model.files.EpubMobiFile).
 * For licence reasons the KindleGen tool of Amazon is not bundled with the Testdata Generator.
 * However, if the TDG user downloads the KindleGen tool herself, the TDG can generate Mobi files out of
 * EpubMobiFiles. The user will be prompted with a FileChooser to tell the TDG where the KindleGen.exe tool can be found.
 */
public class MobiFile extends EBookFile
{
	private static final long serialVersionUID = 2L;
	
	/**
	 * Constructor
	 * @param title The product Title object that this iBook file belongs to.
	 * @param demoFlag This boolean flag indicates, if the product is meant as a demo or sample file (which has an influence on file naming).
	 */
	public MobiFile(final Title title, boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	@Override
	public String toString()
	{
		String fileString = "Mobi";
		if(this.isDemoFile())
		{
			fileString += "_extract";
		}
		return fileString + "[" + title.getIsbn13() + "]";
	}

	@Override
	public java.io.File generate(final java.io.File destFolder)
	{
		// first generate an EpubMobi file as the base
		EpubMobiFile epubMobi = new EpubMobiFile(title, this.isDemoFile());
		File tempDir = Utilities.getTempDir();
		epubMobi.generate(tempDir);
		
		// rename EpubMobi file
		String epubMobiPath = FilenameUtils.concat(tempDir.getPath(), epubMobi.buildFileName());
		String epubPath = epubMobiPath.replace("_EpubMobi", "");
		
		try
		{
			FileUtils.moveFile(FileUtils.getFile(epubMobiPath), FileUtils.getFile(epubPath));
		}
		catch (IOException exc)
		{
			Utilities.showErrorPane("Could not rename temporary EpubMobi file to " + epubPath + "\n", exc);
		}
		
		// determine path to kindlegen
		// first look for kindlegen.exe in config directory
		File configDir = Utilities.getConfigDir();
		File kindleGenPath = new File(configDir.toURI().getPath() + "/kindlegen.exe");
		if(!kindleGenPath.exists())
		{
			// Do we have a KindleGenPath in the registry?
			ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
			String kindleGenPathString = registry.getString("kindleGenPath");
			kindleGenPath = new File(kindleGenPathString);
			
			// If we still don't have a valid file representing a KindleGen, then prompt the user to list one
			if(!kindleGenPath.exists())
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Path to KindleGen");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				JTextArea textarea = new JTextArea(10, 30);
				textarea.setLineWrap(true);
				textarea.setEditable(false);
				textarea.setWrapStyleWord(true);
				textarea.setText("For licence reasons, KindleGen cannot be bundled directly with this TestDataGenerator. If you consent with Amazon's EULA, you can place the KindleGen.exe "
						+ "file into the TDG's config directory or choose the KindleGen.exe in this file dialog below. Only if KindleGen.exe is present, TDG can directly generate "
						+ "Mobi files. Otherwise you can select the file option \"EpubMobi\" and the EBook Plant will generate the Mobi for you on import.");
				textarea.setBorder(BorderFactory.createTitledBorder("Note"));
				chooser.setAccessory(textarea);
				
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					kindleGenPath = chooser.getSelectedFile();
					registry.put("kindleGenPath", kindleGenPath.getAbsolutePath());
				}
			}
		}
		
		// Here we still might not have a valid KindleGen reference. If so, we skip the Mobi generation with a warning (see else branch).
		if(kindleGenPath.exists())
		{
			Writer kindleGenOutputWriter = new StringWriter();
			String kindleGenOutput = "";
			try
			{
				ProcessBuilder pb = new ProcessBuilder(kindleGenPath.getAbsolutePath(), "\"" + epubPath + "\"");
				pb.redirectOutput(Redirect.PIPE);
				pb.redirectError(Redirect.PIPE);
				Process p = pb.start();
				p.waitFor();
				IOUtils.copy(p.getInputStream(), kindleGenOutputWriter, "UTF-8");
				kindleGenOutput = kindleGenOutputWriter.toString();
			}
			catch (IOException exc)
			{
				Utilities.showErrorPane("Could not execute KindleGen with kindlePath " + kindleGenPath.getAbsolutePath() + "\n", exc);
			}
			catch (InterruptedException exc)
			{
				Utilities.showErrorPane("Interrupted while waiting for KindleGen to finish\n", exc);
			}
			// TODO: debug output, should be put into a logging framework later
			System.out.println(kindleGenOutput);

			// copy generated mobi file from temp to destination folder
			File mobiDestinationFile = new File(epubPath.replace(".epub", ".mobi"));
			destFolder.mkdirs();
			try
			{
				FileUtils.copyFileToDirectory(mobiDestinationFile, destFolder);
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
		else
		{
			Utilities.showWarnPane("Could not generate Mobi file for " + title.getIsbn13() + ", KindleGen path is missing or incorrect.");
			return null;
		}
		
	}
	
	@Override
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + (isDemoFile() ? "_Extract" : "" ) + ".mobi";
	}
	
	// helper class for selecting the filepath to the KindleGen.exe tool
	class KindleGenChooser extends JFileChooser
	{
        public JDialog createDialog(Component parent) throws HeadlessException
        {
            JDialog dialog = super.createDialog(parent);
            dialog.setLocation(300, 200);
            dialog.setResizable(false);
            return dialog;
        }
	}
}