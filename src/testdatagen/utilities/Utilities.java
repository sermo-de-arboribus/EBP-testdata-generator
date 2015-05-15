package testdatagen.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

public final class Utilities
{
	private static long lastISBN = 97810000019L;
	private static int lastTitleNumber =  19;
	private static final String COMMON_TITLE_COMPONENT = "Testtitel ";
	private static final String[] GENRES = {"Roman", "Gedichte", "Komödie", "Ratgeber"};
	private static final String[] FIRST_NAMES = {"Gesine", "Arnold", "Jascha", "Evelyne", "Horst", "Winfried", "Susanne", "Grazia"};
	private static final String[] LAST_NAMES = {"Testautor-Schmidt", "Testautor-Meier", "Testautor-Tiedenhub"};
	private static Random random = new Random();
	
	public static long getNextISBN()
	{
		long nextISBN = ++lastISBN;
		String checkDigit = calculateCheckDigit(String.valueOf(nextISBN));
		String nextISBNWithCheckDigit = String.valueOf(nextISBN) + checkDigit;
		return Long.parseLong(nextISBNWithCheckDigit);
	}
	
	public static Dimension getScreenDimensions(Component component)
	{
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(component.getGraphicsConfiguration());
    	int taskBarSize = scnMax.bottom;
    	
    	return new Dimension(screenSize.width, screenSize.height - taskBarSize);
	}
	
	public static long saveLastISBN()
	{
		return lastISBN;
	}
	
	public static long saveTitleNumber()
	{
		return lastTitleNumber;
	}
	
	public static void loadLastISBN(long lastIsbn)
	{
		lastISBN = lastIsbn;
	}

	public static void loadLastTitleNumber(int titleNo)
	{
		lastTitleNumber = titleNo;
	}
	
	public static String getNewTitle()
	{
		return COMMON_TITLE_COMPONENT + ++lastTitleNumber + ". " + GENRES[random.nextInt(GENRES.length)];
	}
	
	public static String getNewAuthor()
	{
		return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + " " + LAST_NAMES[random.nextInt(LAST_NAMES.length)];
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
    		JOptionPane.showMessageDialog(null, "Error: could not save file\n" + e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
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