package testdatagen.utilities;

import java.awt.Dimension;
import java.util.Random;

import testdatagen.config.ConfigurationRegistry;

public class CoverUtils
{
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