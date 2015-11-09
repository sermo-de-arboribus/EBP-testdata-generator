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

public class TitleUtils
{
	private static int lastTitleNumber =  101;
	private static final String COMMON_TITLE_COMPONENT = "Testtitel ";
	private static final String[] CURRENCIES = {"EUR", "GBP", "USD", "HKD", "CZK"};
	private static final String[] GENRES = {"Roman", "Gedichte", "Komödie", "Ratgeber", "Erzählungen"};
	private static final String[] FIRST_NAMES = {"Gesine", "Arnold", "Jascha", "Evelyne", "Horst", "Winfried", "Susanne", "Grazia", "Kasimir", "Kajetan", "Caille", "Nadine", "Fenja", "Robin", "Kalle", "Frieda", "Nanjiang", "Jianhuang", "René", "Jiří", "Joyce", "Jean", "Eleftheria", "Alessandro", "Alexander", "Alexandra", "Mari", "Amy", "Oskar", "Carl"};
	private static final String[] LAST_NAMES = {"Schmidt", "Meier", "Tiedenhub", "Sully", "Prudhomme", "Mommsen", "Björnson", "Mistral", "Neruda", "Faulkner", "Hemingway", "Pynchon", "Joyce", "Zappa", "Frith", "Balcı", "Lodówka", "Lutosławski", "Einstein", "Ramanujan", "Sigurdsen"};
	private static Random random = new Random();
	
	public static String getNewAuthor()
	{
		return getRandomFirstName() + " Testautor-" + getRandomLastName();
	}
	
	public static String getNewTitle()
	{
		int titleNumber = ++lastTitleNumber;
		saveLastTitleNumber();
		return COMMON_TITLE_COMPONENT + titleNumber + ". " + GENRES[random.nextInt(GENRES.length)];
	}
	
	public static String getRandomCurrencyCode()
	{
		return CURRENCIES[random.nextInt(CURRENCIES.length)];
	}
	
	public static String getRandomFirstName()
	{
		return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
	}
	
	public static String getRandomFullName()
	{
		return getRandomFirstName() + " " + getRandomLastName();
	}
	
	public static String getRandomLastName()
	{
		return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
	}
	
	public static String getRandomTopic(Locale loc)
	{
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		String[] topics = registry.getLocalizedText(loc, "topic");
		String topic = topics[random.nextInt(topics.length)];
		topic = topic.substring(0, 1).toUpperCase() + topic.substring(1);
		return topic;
	}
	
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