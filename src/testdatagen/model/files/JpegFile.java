package testdatagen.model.files;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.io.FilenameUtils;

import testdatagen.model.Title;
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
		
	public void generate(Title title, java.io.File destDir)
	{
		JComponent coverImage = paintCover(title);
		// create buffered image out of cover component
		BufferedImage bi = new BufferedImage(coverImage.getWidth(), coverImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		coverImage.paint(g);
		g.dispose();
		// save buffered image to file
		try
		{
			ImageIO.write(bi,  "jpg", new java.io.File(FilenameUtils.concat(destDir.getPath(), title.getIsbn13() + ".jpg")));
			System.out.println("Saved file to path " + destDir.getPath());
		}
		catch (IOException e)
		{
			Utilities.showErrorPane("Error: could not create output directory", e);
		}
	}
	
	public String toString()
	{
		return type.toString().toLowerCase() + "JPG["+ISBN+"]";
	}
	
	private JComponent paintCover(Title title)
	{
		// random dimensions within certain bounds
		Random random = new Random();
		double width = random.nextDouble() * 2100.0 + 300.0;
		double height = width * 1.38;
		Dimension canvasSize = new Dimension((int) width, (int) height);
		
		JPanel canvas = new JPanel();
		canvas.setPreferredSize(canvasSize);
		Graphics g = canvas.getGraphics();
		if(g == null)
		{
			System.out.println("g is null!");
		}
		Font largeFont = new Font(Font.SERIF, Font.BOLD, (int) (height * 0.75 * 0.05)); // 1 px is approximately 0.75 pt, and large font should be about 5% of the canvas height
		Font mediumFont = new Font(Font.SERIF, Font.BOLD, (int) (height * 0.75 * 0.035));
		Font smallFont = new Font(Font.SERIF, Font.PLAIN, (int) (height * 0.75 * 0.028));
		FontMetrics largeMetrics = g.getFontMetrics(largeFont);
		FontMetrics mediumMetrics = g.getFontMetrics(mediumFont);
		FontMetrics smallMetrics = g.getFontMetrics(smallFont);
		
		// print title
		g.setFont(largeFont);
		g.drawString(title.getName(), 
				(canvas.getWidth() - largeMetrics.stringWidth(title.getName())) / 2,  // centered title in x dimension 
				canvas.getHeight() / 10); // y location of title
		
		// print author
		g.setFont(mediumFont);
		g.drawString("von " + title.getAuthor(),
				(canvas.getWidth() - mediumMetrics.stringWidth("von " + title.getAuthor())) / 2, // centered author in x dimension
				(int) ((canvas.getHeight() / 10.0) * 2.5)); // y location of title
		
		// print publisher string
		g.setFont(smallFont);
		g.drawString("erschienen im",
				(canvas.getWidth() - smallMetrics.stringWidth("erschienen im")) / 2,
				(int) ((canvas.getHeight() / 5.0)));
		g.drawString("IT-E-Books-Verlag", 
				(canvas.getWidth() - smallMetrics.stringWidth("IT-E-Books-Verlag")) / 2,
				(int) ((canvas.getHeight() / 2.5)));
		g.drawString("Stuttgart",
				(canvas.getWidth() - smallMetrics.stringWidth("Stuttgart")) / 2,
				(int) ((canvas.getHeight() / 2.5)) + smallMetrics.getHeight());
		int year = new GregorianCalendar().getWeekYear();
		String yearString = Integer.toString(year);
		g.drawString(yearString,
				(canvas.getWidth() - smallMetrics.stringWidth(yearString)) / 2,
				(int) ((canvas.getHeight() / 2.5)) + 2 * smallMetrics.getHeight());
		String isbnString = ISBNUtils.hyphenateISBN(title.getIsbn13()); 
		g.drawString(isbnString, 
				(canvas.getWidth() - smallMetrics.stringWidth(isbnString)) / 2, 
				(int) ((canvas.getHeight() / 2.5)) + 4 * smallMetrics.getHeight());
		g.dispose();
		
		return canvas;
	}
}