package testdatagen.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import testdatagen.config.ConfigurationRegistry;

/**
 * General utility class with uncategorized static helper methods.
 */
public final class Utilities
{
	/**
	 * Get the screen dimensions of the environment the test data generator is running in
	 * @param component The awt Component to determine the screen dimensions with 
	 * @return A Dimension object for the screen dimensions, minus the task bar
	 */
	public static Dimension getScreenDimensions(final Component component)
	{
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(component.getGraphicsConfiguration());
    	int taskBarSize = scnMax.bottom;
    	
    	return new Dimension((int) (screenSize.width * 0.8), (int) ((screenSize.height - taskBarSize) * 0.6));
	}
	
	/**
	 * Get the File path to the configuration directory
	 * @return A java.file.IO object representing the configuration directory
	 */
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

	/**
	 * Get a random ISO country code
	 * @return A string with an ISO country code
	 */
	public static String getCountryForONIX()
	{
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		Random random = new Random();
		String codeList = registry.getString("iso3166-1.countryCodes");
		int index = random.nextInt(codeList.length() / 3);
		return codeList.substring(index * 3, index * 3 + 2);
	}
	
	/**
	 * Format a date in the yyyyMMddHHmm format
	 * @param date The date that needs to be formatted
	 * @return The formatted date String
	 */
	public static String getDateForONIX2(final Date date)
	{
		String DATE_FORMAT = "yyyyMMddHHmm";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}

	/**
	 * Format a date in the yyyyMMdd'T'HHmm format
	 * @param date The date that needs to be formatted
	 * @return The formatted date String
	 */
	public static String getDateTimeForONIX3(Date date)
	{
		String DATE_FORMAT = "yyyyMMdd'T'HHmm";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}
	
	/**
	 * Get a temporary working directory with a unique filename
	 * @return A java.io.File representing the unique temp directory
	 */
	public static File getTempDir()
	{
		Random random = new Random();
		long timestamp = System.currentTimeMillis();
		File tempDir = new File(FilenameUtils.concat(Utilities.getConfigDir().getPath(), "temp" + timestamp  + "-" + random.nextInt(1000) + "/"));
		tempDir.mkdirs();
		return tempDir;
	}
	
	/**
	 * Helper method to compare two objects with a check for null values
	 * @param obj1 The first object to be compared
	 * @param obj2 The second object to be compared
	 * @return Returns true, if both objects are equal, otherwise false
	 */
	public static boolean nullSafeEquals(final Object obj1, final Object obj2)
	{
		return (obj1 == obj2) || (obj1 != null && obj1.equals(obj2));
	}
	
	/**
	 * Close an output stream safely, within a try/catch blok
	 * @param str The Stream to be closed
	 */
	public static void safeClose(final Closeable str)
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

	/**
	 * Display an error message in a Swing pop-up window
	 * @param errorMessage The error message to be displayed
	 * @param e The exception object associated with the error 
	 */
	public static void showErrorPane(final String errorMessage, final Exception e)
	{
		JOptionPane.showMessageDialog(null, errorMessage + " " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Display an info message in a Swing pop-up window
	 * @param infoMessage The info message to be displayed
	 */
	public static void showInfoPane(final String infoMessage)
	{
		JOptionPane.showMessageDialog(null, infoMessage, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Display an warning message in a Swing pop-up window
	 * @param warnMessage The warning message to be displayed
	 */
	public static void showWarnPane(String warnMessage)
	{
		JOptionPane.showMessageDialog(null, warnMessage, "Warning", JOptionPane.WARNING_MESSAGE);
	}
}