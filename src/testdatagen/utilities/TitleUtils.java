package testdatagen.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class TitleUtils
{
	private static int lastTitleNumber =  19;
	private static final String COMMON_TITLE_COMPONENT = "Testtitel ";
	private static final String[] CURRENCIES = {"EUR", "GBP", "USD", "HKD", "CZK"};
	private static final String[] GENRES = {"Roman", "Gedichte", "Komödie", "Ratgeber"};
	private static final String[] FIRST_NAMES = {"Gesine", "Arnold", "Jascha", "Evelyne", "Horst", "Winfried", "Susanne", "Grazia", "Kasimir", "Kajetan", "Caille", "Nadine", "Fenja", "Robin", "Kalle", "Frieda", "Nanjiang", "Jianhuang", "René", "Jiří", "Joyce", "Jean", "Eleftheria", "Alessandro", "Alexander", "Alexandra"};
	private static final String[] LAST_NAMES = {"Schmidt", "Meier", "Tiedenhub", "Sully", "Prudhomme", "Mommsen", "Björnson", "Mistral", "Neruda", "Faulkner", "Hemingway", "Pynchon", "Joyce", "Zappa", "Frith", "Balcı", "Lodówka", "Lutosławski"};
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
	
	public static void loadLastTitleNumber()
	{
		File configDir = Utilities.getConfigDir();
		File ISBNFile = new File(configDir.getPath() + "/titleno.ebp");
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
	    	Utilities.showErrorPane("Error: could not read from title number configuration file", e);
	    }
	    catch (ClassNotFoundException e)
	    {
	    	Utilities.showErrorPane("Error: could not read class information from title number configuration file", e);
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
		File ISBNFile = new File(configDir.getPath() + "/titleno.ebp");
		ObjectOutputStream saveTNoStream = null;
		try
		{
			saveTNoStream = new ObjectOutputStream(new FileOutputStream(ISBNFile));
			saveTNoStream.writeObject(new Integer(lastTitleNumber));
			saveTNoStream.flush();
		}
		catch(IOException e)
		{
			Utilities.showErrorPane("Error: could not save title number to config file " + e.toString(), e);
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