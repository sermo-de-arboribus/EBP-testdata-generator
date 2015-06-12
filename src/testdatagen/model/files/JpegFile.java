package testdatagen.model.files;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
import testdatagen.utilities.CoverUtils;
import testdatagen.utilities.ISBNUtils;
import testdatagen.utilities.Utilities;

public class JpegFile extends GraphicFile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ISBN;
	
	public JpegFile(long ISBN, GraphicFile.Type type)
	{
		super(Long.toString(ISBN) + "_" + type.toString().toLowerCase() + ".jpg", type);
		this.ISBN = ISBN;
	}
		
	public java.io.File generate(Title title, java.io.File destPath)
	{
		BufferedImage coverImage = paintCover(title);
		java.io.File storedFile = null;
		if(destPath.isDirectory())
		{
			storedFile = new java.io.File(FilenameUtils.concat(destPath.getPath(), title.getIsbn13() + ".jpg"));	
		}
		else
		{
			storedFile = new java.io.File(destPath.getPath());
		}
		storedFile.getParentFile().mkdirs();
		
		// save buffered image to file
		try
		{
			ImageIO.write(coverImage,  "jpg", storedFile);
		}
		catch (IOException e)
		{
			Utilities.showErrorPane("Error: could not write Jpeg file to disk", e);
			storedFile = null;
		}
		return storedFile;
	}
	
	public String toString()
	{
		return type.toString().toLowerCase() + "JPG["+ISBN+"]";
	}
	
	private BufferedImage paintCover(Title title)
	{
		Dimension coverDimension = CoverUtils.getRandomCoverDimension();
		BufferedImage bufImg = new JpegCover(title, (int) coverDimension.getWidth(), (int) coverDimension.getHeight());
		return bufImg;
	}
}

// non-public helper class for painting a cover
class JpegCover extends BufferedImage
{	
	JpegCover(Title title, int width, int height)
	{
		super(width, height, TYPE_INT_RGB);
		
		Graphics2D gra = this.createGraphics();
		
		// set random, but bright background colour
		Random random = new Random();
		int r1 = (int) (random.nextFloat() * 127.0 + 128.0);
		int g1 = (int) (random.nextFloat() * 127.0 + 128.0);
		int b1 = (int) (random.nextFloat() * 127.0 + 128.0);
		Color bgcolor = new Color(r1, g1, b1);
		gra.setBackground(bgcolor);
		gra.clearRect(0, 0, width, height);
		
		// set random, but dark font colour
		int r2 = (int) (random.nextFloat() * 127.0);
		int g2 = (int) (random.nextFloat() * 127.0);
		int b2 = (int) (random.nextFloat() * 127.0);
		Color fcolor = new Color(r2, g2, b2);
		gra.setColor(fcolor);
		
		// select a font randomly
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = env.getAllFonts();
		int fontNumber = random.nextInt(fonts.length - 1);
		Font baseFont = fonts[fontNumber];
		
		Font largeFont = new Font(baseFont.getName(), Font.BOLD, (int) (height * 0.75 * 0.08)); // 1 px is approximately 0.75 pt, and large font should be about 5% of the canvas height
		Font mediumFont = new Font(baseFont.getName(), Font.BOLD, (int) (height * 0.75 * 0.05));
		Font smallFont = new Font(baseFont.getName(), Font.PLAIN, (int) (height * 0.75 * 0.035));
		FontMetrics largeMetrics = gra.getFontMetrics(largeFont);
		FontMetrics mediumMetrics = gra.getFontMetrics(mediumFont);
		FontMetrics smallMetrics = gra.getFontMetrics(smallFont);
		
		// print title
		gra.setFont(largeFont);
		int titleWidth = largeMetrics.stringWidth(title.getName());
		if(titleWidth > getWidth())
		{
			// title too long, break it up into two lines
			int breakpoint = title.getName().length() / 2;
			String titleComponent1 = title.getName().substring(0, title.getName().indexOf(' ', breakpoint));
			String titleComponent2 = title.getName().substring(title.getName().indexOf(' ', breakpoint) + 1);
			
			gra.drawString(titleComponent1, 
					(getWidth() - largeMetrics.stringWidth(titleComponent1)) / 2,  // centered title in x dimension 
					getHeight() / 10); // y location of title
			gra.drawString(titleComponent2, 
					(getWidth() - largeMetrics.stringWidth(titleComponent2)) / 2,  // centered title in x dimension 
					(getHeight() / 10) + largeMetrics.getHeight()); // y location of title
		}
		else
		{
			// title fits into one line
			gra.drawString(title.getName(), 
					(getWidth() - titleWidth) / 2,  // centered title in x dimension 
					getHeight() / 10); // y location of title
		}
		
		// print author
		gra.setFont(mediumFont);
		gra.drawString("von " + title.getAuthor(),
				(getWidth() - mediumMetrics.stringWidth("von " + title.getAuthor())) / 2, // centered author in x dimension
				(int) ((getHeight() / 10.0) * 2.5)); // y location of title
		
		// print publisher string
		gra.setFont(smallFont);
		gra.drawString("erschienen im",
				(getWidth() - smallMetrics.stringWidth("erschienen im")) / 2,
				(int) ((getHeight() / 2.0)));
		gra.drawString("IT-E-Books-Verlag", 
				(getWidth() - smallMetrics.stringWidth("IT-E-Books-Verlag")) / 2,
				(int) ((getHeight() / 1.5)));
		gra.drawString("Stuttgart",
				(getWidth() - smallMetrics.stringWidth("Stuttgart")) / 2,
				(int) ((getHeight() / 1.5)) + smallMetrics.getHeight());
		int year = new GregorianCalendar().getWeekYear();
		String yearString = Integer.toString(year);
		gra.drawString(yearString,
				(getWidth() - smallMetrics.stringWidth(yearString)) / 2,
				(int) ((getHeight() / 1.5)) + 2 * smallMetrics.getHeight());
		String isbnString = ISBNUtils.hyphenateISBN(title.getIsbn13()); 
		gra.drawString(isbnString, 
				(getWidth() - smallMetrics.stringWidth(isbnString)) / 2, 
				(int) ((getHeight() / 1.5)) + 4 * smallMetrics.getHeight());
	}
}