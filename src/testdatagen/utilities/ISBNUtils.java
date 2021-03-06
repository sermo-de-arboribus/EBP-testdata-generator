package testdatagen.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class contains static helper methods around ISBN (International Standard Book Number).
 * An ISBN is a 13-digit number, where the first 12 are the assigned numbers and number 13 is a check-digit.
 * It also keeps an internal ISBN counter which is increased with every ISBN used. This is a simple
 * mechanism to ensure that every test product has a unique ISBN. 
 * The ISBN numbering sequence is designed to make sure there are no clashes with real ISBN numbers
 * that are used in the E-Book-Plant production system.
 */
public class ISBNUtils
{
	private static long lastISBN = 978721000001L;
	
	/**
	 * Returns the next ISBN and increases the ISBN counter
	 * @return ISBN as long value
	 */
	public static long getNextISBN()
	{
		long nextISBN = ++lastISBN;
		String checkDigit = calculateCheckDigit(String.valueOf(nextISBN));
		String nextISBNWithCheckDigit = String.valueOf(nextISBN) + checkDigit;
		saveLastISBN();
		return Long.parseLong(nextISBNWithCheckDigit);
	}

	/**
	 * An ISBN can be displayed as a pure 13-digit number string or with hyphens, to separate
	 * different elements, e.g. 978-3-497-012345-6. This method hyphenates ISBNs in a likely way.
	 * @param isbn The 13-digit ISBN value as a long parameter
	 * @return A hyphenated ISBN string
	 */
	public static String hyphenateISBN(final long isbn)
	{
		String simpleString = String.valueOf(isbn);
		char[] isbnChars = simpleString.toCharArray();
		StringBuffer isbnBuffer = new StringBuffer();
		for(int i = 0; i < isbnChars.length; i++)
		{
			isbnBuffer.append(isbnChars[i]);
			if(i == 2 || i == 3 || i == 6 || i == 11)
			{
				isbnBuffer.append('-');
			}
		}
		return isbnBuffer.toString();
	}
	
	/**
	 * Loads the most recently used ISBN from a configuration file from the disk
	 */
	public static void loadLastISBN()
	{
		File configDir = Utilities.getConfigDir();
		File ISBNFile = new File(configDir.toURI().getPath() + "/isbn.ebp");
		ObjectInputStream loadISBNStream = null;
	    try
	    {
	    	loadISBNStream = new ObjectInputStream(new FileInputStream(ISBNFile));
	    	lastISBN = (Long) loadISBNStream.readObject();
	    }
	    catch (FileNotFoundException e)
	    {
	    	// if file doesn't exist, use the default value, no action necessary
	    }
	    catch (IOException e)
	    {
	    	Utilities.showErrorPane("Error: could not read from config from File\n", e);
	    }
	    catch (ClassNotFoundException e)
	    {
	    	Utilities.showErrorPane("Error: could not read from config from File\n", e);
	    }
	    finally
	    {
	    	if(loadISBNStream != null)
	    	{
	    		Utilities.safeClose(loadISBNStream);
	    	}
	    }
	}

	/**
	 * Saves the most recently used ISBN to a file in the configuration directory on the disk
	 */
	public static void saveLastISBN()
	{
		File configDir = Utilities.getConfigDir();
		File ISBNFile = new File(configDir.getPath() + "/isbn.ebp");
		ObjectOutputStream saveISBNStream = null;
		try
		{
			saveISBNStream = new ObjectOutputStream(new FileOutputStream(ISBNFile));
			saveISBNStream.writeObject(new Long(lastISBN));
			saveISBNStream.flush();
		}
		catch(IOException e)
		{
			Utilities.showErrorPane("Error: could not save ISBN to config file " + e.toString() + "\n", e);
		}
		finally
		{
			if(saveISBNStream != null)
			{
				Utilities.safeClose(saveISBNStream);
			}
		}
	}
	
	/**
	 * Helper method that calculates the check-digit of the ISBN - see http://www.arndt-bruenner.de/mathe/scripts/pruefziffern.htm for the algorithm
	 * @param code The 12-digit ISBN string
	 * @return The check-digit as a String
	 */
    public static String calculateCheckDigit(String code)
    {
        if (code == null || code.length() == 0)
        {
            throw new IllegalArgumentException("Code is missing");
        }
        int modulusResult = calculateModulus(code, false);
        int charValue = (10 - modulusResult) % 10;
        return toCheckDigit(charValue);
    }

    // private helper method for calculating a check digit
    private static int calculateModulus(String code, boolean includesCheckDigit)
    {
        int total = 0;
        for (int i = 0; i < code.length(); i++) {
            int lth = code.length() + (includesCheckDigit ? 0 : 1);
            int leftPos  = i + 1;
            int rightPos = lth - i;
            int charValue = toInt(code.charAt(i), leftPos, rightPos);
            total += weightedValue(charValue, leftPos, rightPos);
        }
        if (total == 0) {
            throw new IllegalArgumentException("Invalid code, sum is zero");
        }
        return total % 10;
    }
    
    // private helper method for calculating a check digit
    private static String toCheckDigit(int charValue)
    {
        if (charValue >= 0 && charValue <= 9)
        {
            return Integer.toString(charValue);
        }
        throw new IllegalArgumentException("Invalid Check Digit Value =" +
                + charValue);
    }
    
    // private helper method for calculating a check digit
    private static int toInt(char character, int leftPos, int rightPos)
    {
        if (Character.isDigit(character))
        {
            return Character.getNumericValue(character);
        }
        throw new IllegalArgumentException("Invalid Character[" +
                leftPos + "] = '" + character + "'");
    }
    
    // private helper method for calculating a check digit
    private static int weightedValue(int charValue, int leftPos, int rightPos)
    {
    	final int[] POSITION_WEIGHT = new int[] {3, 1};
        int weight = POSITION_WEIGHT[rightPos % 2];
        return charValue * weight;
    }
}
