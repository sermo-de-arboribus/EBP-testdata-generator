package testdatagen.utilities;

import java.awt.Dimension;
import java.util.Random;

public class CoverUtils
{
	public static Dimension getRandomCoverDimension()
	{
		// generate random dimensions within certain bounds
		Random random = new Random();
		int width = random.nextInt(2100) + 300;
		double ratio = random.nextDouble() / 5.0;
		int height = (int) (width * (1.28 + ratio));
		return new Dimension(width, height);
	}
}