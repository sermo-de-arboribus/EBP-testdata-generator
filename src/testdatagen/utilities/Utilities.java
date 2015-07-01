package testdatagen.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JOptionPane;

import testdatagen.config.ConfigurationRegistry;

public final class Utilities
{
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
		String configURI = appDirectory + "/config";
		System.out.println("appDirectory is: " + appDirectory + ", configURI is: " + configURI);
		File configDir = new File(configURI.replaceAll("%20", " "));
		if(!configDir.exists())
		{
			if(configDir.mkdirs())
			{
				showInfoPane("Created new config directory: " + configDir.getPath());
			}
			else
			{
				showErrorPane("Error: could not create or find a configuration directory\nFailed creating dir " + configDir.getPath(), new FileNotFoundException());
			}
		}
		return configDir;
	}
	
	public static String getCountryForONIX()
	{
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		Random random = new Random();
		String codeList = registry.getString("iso3166-1.countryCodes");
		int index = random.nextInt(codeList.length() / 3);
		return codeList.substring(index * 3, index * 3 + 2);
	}
	
	public static String getDateForONIX2(Date date)
	{
		String DATE_FORMAT = "yyyyMMddHHmm";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}
	
	// TODO: Refactor - this method belongs into the Title class
	public static String formatToFileType(String prodType)
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