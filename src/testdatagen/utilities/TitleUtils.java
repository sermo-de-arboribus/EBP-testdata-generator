package testdatagen.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import testdatagen.config.ConfigurationRegistry;

/**
 * A class with static helper methods for product Title objects
 */
public final class TitleUtils
{
	private static int lastTitleNumber =  101;
	private static final String COMMON_TITLE_COMPONENT = "Testtitel ";
	private static final String[] CURRENCIES = {"EUR", "GBP", "USD", "HKD", "CZK"};
	private static final String[] GENRES = {"Roman", "Gedichte", "Komödie", "Ratgeber", "Erzählungen", "Reiseführer"};
	private static final String[] FIRST_NAMES = {"Gesine", "Arnold", "Jascha", "Evelyne", "Horst", "Winfried", "Susanne", "Grazia", "Kasimir", "Kajetan", "Caille", "Nadine", "Fenja", "Robin", "Kalle", "Frieda", "Nanjiang", "Jianhuang", "René", "Jiří", "Joyce", "Jean", "Eleftheria", "Alessandro", "Alexander", "Alexandra", "Mari", "Amy", "Oskar", "Carl", "Hongyun"};
	private static final String[] LAST_NAMES = {"Schmidt", "Meier", "Tiedenhub", "Sully", "Prudhomme", "Mommsen", "Björnson", "Mistral", "Neruda", "Faulkner", "Hemingway", "Pynchon", "Joyce", "Zappa", "Frith", "Balcı", "Lodówka", "Lutosławski", "Einstein", "Ramanujan", "Sigurdsen", "Flintstone", "Rindfleisch", "Karsch", "Juppen", "Kloth", "Wagner", "Boole"};
	private static Random random = new Random();

	/**
	 * Determine the file type from the E-Book-Plant's product type String
	 * @param prodType The product type String
	 * @return The file type String
	 */
	public static String formatToFileType(final String prodType)
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
	
	/**
	 * Get a random author name
	 * @return A String with a random author name
	 */
	public static String getNewAuthor()
	{
		return getRandomFirstName() + " Testautor-" + getRandomLastName();
	}

	/**
	 * Get a random book title
	 * @return A String with a random book title
	 */
	public static String getNewTitle()
	{
		int titleNumber = ++lastTitleNumber;
		saveLastTitleNumber();
		return COMMON_TITLE_COMPONENT + titleNumber + ". " + GENRES[random.nextInt(GENRES.length)];
	}

	/**
	 * Get a random currency code for a product price
	 * @return A String with a random currency code
	 */
	public static String getRandomCurrencyCode()
	{
		return CURRENCIES[random.nextInt(CURRENCIES.length)];
	}
	
	/**
	 * Get a random first name for an author
	 * @return A string with a random first name
	 */
	public static String getRandomFirstName()
	{
		return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
	}

	/**
	 * Get a random full name for an author
	 * @return A string with a random full name
	 */
	public static String getRandomFullName()
	{
		return getRandomFirstName() + " " + getRandomLastName();
	}

	/**
	 * Get a random family name for an author
	 * @return A string with a random last name
	 */
	public static String getRandomLastName()
	{
		return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
	}
	
	/**
	 * Gets a random topic description of a product (which can be used in Onix <subject> elements)
	 * @param loc The Locale object to be used
	 * @return A String with a random topic.
	 */
	public static String getRandomTopic(final Locale loc)
	{
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		String[] topics = registry.getLocalizedText(loc, "topic");
		String topic = topics[random.nextInt(topics.length)];
		topic = topic.substring(0, 1).toUpperCase() + topic.substring(1);
		// if topic starts with an article, cut it off
		if(topic.startsWith("Der ") || topic.startsWith("Den ") || topic.startsWith("Das ") || topic.startsWith("Die ") || topic.startsWith("The "))
		{
			topic = topic.substring(4);
		}
		return topic;
	}

	/**
	 * Get a random VLB Warengruppe code (a standardized code issued by Börsenverein des Deutschen Buchhandels
	 * @return A String with a random Warengruppen code (prepended with "9" to indicate a digital product)
	 */
	public static String getRandomWarengruppeCode()
	{
		String code = "9";
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		StringTokenizer tokenizer = new StringTokenizer(registry.getString("onix.WarengruppeCodes"));
		String[] codelist = new String[tokenizer.countTokens()];
		int i = 0;
		while(tokenizer.hasMoreTokens())
		{
			codelist[i] = tokenizer.nextToken();
			i++;
		}
		code += codelist[random.nextInt(codelist.length)];
		return code;
	}
	
	/**
	 * Load the most recently used title number from a configuration file in the config directory
	 */
	public static void loadLastTitleNumber()
	{
		File configDir = Utilities.getConfigDir();
		File ISBNFile = new File(configDir.toURI().getPath() + "/titleno.ebp");
		ObjectInputStream loadTNoStream = null;
	    try
	    {
	    	loadTNoStream = new ObjectInputStream(new FileInputStream(ISBNFile));
	    	lastTitleNumber = (Integer) loadTNoStream.readObject();
	    }
	    catch (FileNotFoundException e)
	    {
	    	// if file doesn't exist, use the default value, no action necessary
	    }
	    catch (IOException e)
	    {
	    	Utilities.showErrorPane("Error: could not read from title number configuration file\n", e);
	    }
	    catch (ClassNotFoundException e)
	    {
	    	Utilities.showErrorPane("Error: could not read class information from title number configuration file\n", e);
	    }
	    finally
	    {
	    	if(loadTNoStream != null)
	    	{
	    		Utilities.safeClose(loadTNoStream);
	    	}
	    }
	}
	
	/**
	 * Save the most recently used title number to a file in the configuration directory
	 */
	public static void saveLastTitleNumber()
	{
		File configDir = Utilities.getConfigDir();
		File ISBNFile = new File(configDir.toURI().getPath() + "/titleno.ebp");
		ObjectOutputStream saveTNoStream = null;
		try
		{
			saveTNoStream = new ObjectOutputStream(new FileOutputStream(ISBNFile));
			saveTNoStream.writeObject(new Integer(lastTitleNumber));
			saveTNoStream.flush();
		}
		catch(IOException e)
		{
			Utilities.showErrorPane("Error: could not save title number to config file " + e.toString() + "\n", e);
		}
		finally
		{
			if(saveTNoStream != null)
			{
				Utilities.safeClose(saveTNoStream);
			}
		}
	}
}