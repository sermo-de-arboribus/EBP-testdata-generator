package testdatagen.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import testdatagen.model.ScenarioTableModel;

public class ISBNUtils
{
	private static long lastISBN = 97810000019L;
	
	public static long getNextISBN()
	{
		long nextISBN = ++lastISBN;
		String checkDigit = calculateCheckDigit(String.valueOf(nextISBN));
		String nextISBNWithCheckDigit = String.valueOf(nextISBN) + checkDigit;
		saveLastISBN();
		return Long.parseLong(nextISBNWithCheckDigit);
	}
	
	public static String hyphenateISBN(long isbn)
	{
		String simpleString = String.valueOf(isbn);
		char[] isbnChars = simpleString.toCharArray();
		StringBuffer isbnBuffer = new StringBuffer();
		for(int i = 0; i < isbnChars.length; i++)
		{
			isbnBuffer.append(isbnChars[i]);
			if(i == 2 || i == 3 || i == 5 || i == 10)
			{
				isbnBuffer.append('-');
			}
		}
		return isbnBuffer.toString();
	}
	
	public static void loadLastISBN()
	{
		File configDir = Utilities.getConfigDir();
		File ISBNFile = new File(configDir.getPath() + "/isbn.ebp");
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
	    	Utilities.showErrorPane("Error: could not read from config from File", e);
	    }
	    catch (ClassNotFoundException e)
	    {
	    	Utilities.showErrorPane("Error: could not read from config from File", e);
	    }
	    finally
	    {
	    	if(loadISBNStream != null)
	    	{
	    		Utilities.safeClose(loadISBNStream);
	    	}
	    }
	}
	
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
			Utilities.showErrorPane("Error: could not save ISBN to config file " + e.toString(), e);
		}
		finally
		{
			if(saveISBNStream != null)
			{
				Utilities.safeClose(saveISBNStream);
			}
		}
	}
	
    private static String calculateCheckDigit(String code)
    {
        if (code == null || code.length() == 0)
        {
            throw new IllegalArgumentException("Code is missing");
        }
        int modulusResult = calculateModulus(code, false);
        int charValue = (10 - modulusResult) % 10;
        return toCheckDigit(charValue);
    }
    
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
    
    private static String toCheckDigit(int charValue)
    {
        if (charValue >= 0 && charValue <= 9)
        {
            return Integer.toString(charValue);
        }
        throw new IllegalArgumentException("Invalid Check Digit Value =" +
                + charValue);
    }
    
    private static int toInt(char character, int leftPos, int rightPos)
    {
        if (Character.isDigit(character))
        {
            return Character.getNumericValue(character);
        }
        throw new IllegalArgumentException("Invalid Character[" +
                leftPos + "] = '" + character + "'");
    }
    
    private static int weightedValue(int charValue, int leftPos, int rightPos)
    {
    	final int[] POSITION_WEIGHT = new int[] {3, 1};
        int weight = POSITION_WEIGHT[rightPos % 2];
        return charValue * weight;
    }
}