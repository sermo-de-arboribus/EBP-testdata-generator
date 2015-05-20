package testdatagen.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

public final class Utilities
{
	private static Random random = new Random();

	public static Dimension getScreenDimensions(Component component)
	{
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(component.getGraphicsConfiguration());
    	int taskBarSize = scnMax.bottom;
    	
    	return new Dimension(screenSize.width, screenSize.height - taskBarSize);
	}
	
	public static File getConfigDir()
	{
		// get the directory where the executable file is located
		String appDirectory = ISBNUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		if(appDirectory.endsWith(".exe") || appDirectory.endsWith(".jar")) // when running program from executable
		{
			appDirectory = new File(appDirectory).getParent();	
		}
		else // when running from IDE during development
		{
			appDirectory += ISBNUtils.class.getName().replace('.', '/');
			appDirectory = new File(appDirectory).getParent();
		}
		File configDir = new File(appDirectory + "/config");
		try
		{
			configDir.mkdir();
		}
		catch (SecurityException exc)
		{
			showErrorPane("Error: could not create or find configuration directory",exc);
		}
		return configDir;
	}
	
	public static String getRandomCoverFormat()
	{
		float rnd = random.nextFloat();
		if(rnd < 0.8)
		{
			return "JPEG";
		}
		else
		{
			return "PDF";
		}
	}
	
	public static String productTypeToFileType(String prodType)
	{
		switch(prodType)
		{
			case "PDF":
			case "WMPDF":
			case "NDPDF":
				return "PDF";
			case "EPUB":
			case "WMEPUB":
			case "NDEPUB":
				return "Epub";
			case "iBOOK":
				return "IBook";
			case "ZIP":
				return "SoftwareZip";
			case "AUDIO":
				return "AudioZip";
			case "WMMOBI":
			case "NDMOBI":
				return "Mobi";
			default:
				throw new IllegalArgumentException("Invalid product type given: " + prodType);
		}
	}
	
	public static void safeClose(Closeable str)
	{
		try
		{
			str.close();
		}
	    catch (IOException e)
	    {
    		showErrorPane("Error: could not save file\n",e);
	    }
	}
	
	public static void showErrorPane(String errorMessage, Exception e)
	{
		JOptionPane.showMessageDialog(null, errorMessage + " " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showInfoPane(String infoMessage)
	{
		JOptionPane.showMessageDialog(null, infoMessage, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showWarnPane(String warnMessage)
	{
		JOptionPane.showMessageDialog(null, warnMessage, "Warning", JOptionPane.WARNING_MESSAGE);
	}
}