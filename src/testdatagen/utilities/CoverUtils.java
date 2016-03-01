package testdatagen.utilities;

import java.awt.Dimension;
import java.util.Random;

import testdatagen.config.ConfigurationRegistry;

/**
 * This is a class with static helper methods concerning cover images 
 */
public class CoverUtils
{
	/**
	 * Returns random cover dimensions, which are within certain bounds, stored in the ConfigurationRegistry
	 * @return A Dimension object, giving pixel dimensions for a cover image
	 */
	public static Dimension getRandomCoverDimension()
	{
		// get reference to configuration registry
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		int maxWidth = registry.getIntValue("cover.maxWidth");
		int minWidth = registry.getIntValue("cover.minWidth");
		double maxXYratio = registry.getDoubleValue("cover.maxXYRatio");
		double minXYratio = registry.getDoubleValue("cover.minXYRatio");
		
		// generate random dimensions within certain bounds
		Random random = new Random();
		int width = random.nextInt(maxWidth - minWidth) + minWidth;
		// random doubles are generated as values between 0.0 and 1.0. To get a random ratio within the configured bounds,
		// divide the random number by the reciprocal value of the difference between the maximum and the minimum ratio to distribute 
		// the random number evenly, then add this result to the minXYratio.
		double ratio = minXYratio + (random.nextDouble() / ( 1 / (maxXYratio - minXYratio)));
		int height = (int) (width * ratio);
		return new Dimension(width, height);
	}

	/**
	 * This method lets a pseudo-random generator decide, if the cover for a title should be produced 
	 * in JPEG or PDF format. The likeliness for using PDF as a cover format is taken from the 
	 * ConfigurationRegistry
	 * @return A String representing the cover format to be used
	 */
	public static String getRandomCoverFormat()
	{
		Random random = new Random();
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		double pdfLikeliness = registry.getDoubleValue("cover.likelinessOfPDFCovers");
		
		float rnd = random.nextFloat();
		if(rnd < (1 - pdfLikeliness))
		{
			return "JPEG";
		}
		else
		{
			return "PDF";
		}
	}
}