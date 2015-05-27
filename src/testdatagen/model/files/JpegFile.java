package testdatagen.model.files;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

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
		BufferedImage coverImage = paintCover(title);

		// save buffered image to file
		try
		{
			ImageIO.write(coverImage,  "jpg", new java.io.File(FilenameUtils.concat(destDir.getPath(), title.getIsbn13() + ".jpg")));
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
	
	private BufferedImage paintCover(Title title)
	{
		JComponent c = new JpegCover(title);
		BufferedImage img = paintComponent(c);
		
		// Debug data
		JFrame testframe = new JFrame();
		testframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		testframe.setLayout(new BorderLayout());
		testframe.setLocation(20,20);
		testframe.add(c, BorderLayout.CENTER);
		testframe.pack();
		testframe.setVisible(true);
		System.out.println("Generated new JpegCover: " + c.toString());
		
		return img;
	}
	
	public static BufferedImage paintComponent(Component c)
	{
		c.setSize(c.getPreferredSize());
		layoutComponent(c);
		
		BufferedImage img = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
		CellRendererPane crp = new CellRendererPane();
		crp.add(c);
		crp.paintComponent(img.createGraphics(), c, crp, c.getBounds());
		return img;
	}
	
	public static void layoutComponent(Component c)
	{
		synchronized(c.getTreeLock())
		{
			c.doLayout();
			if(c instanceof Container)
			{
				for(Component child : ((Container) c).getComponents())
				{
					layoutComponent(child);
				}
			}
		}
	}
}

// non-public helper class for painting a cover
class JpegCover extends JPanel
{
	private Title title;
	
	JpegCover(Title title)
	{
		this.title = title;
	}
	
	@Override
	public void paint(Graphics g)
	{
		paintComponent(g);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		System.out.println("Entering the JpegCover's paintComponent method");
		
		// random dimensions within certain bounds
		Random random = new Random();
		double width = random.nextDouble() * 2100.0 + 300.0;
		double height = width * 1.38;
		Dimension canvasSize = new Dimension((int) width, (int) height);
		
		setPreferredSize(canvasSize);
		setSize(canvasSize);
		Font largeFont = new Font(Font.SERIF, Font.BOLD, (int) (height * 0.75 * 0.05)); // 1 px is approximately 0.75 pt, and large font should be about 5% of the canvas height
		Font mediumFont = new Font(Font.SERIF, Font.BOLD, (int) (height * 0.75 * 0.035));
		Font smallFont = new Font(Font.SERIF, Font.PLAIN, (int) (height * 0.75 * 0.028));
		FontMetrics largeMetrics = g.getFontMetrics(largeFont);
		FontMetrics mediumMetrics = g.getFontMetrics(mediumFont);
		FontMetrics smallMetrics = g.getFontMetrics(smallFont);
		
		// print title
		g.setFont(largeFont);
		g.drawString(title.getName(), 
				(getWidth() - largeMetrics.stringWidth(title.getName())) / 2,  // centered title in x dimension 
				getHeight() / 10); // y location of title
		
		// print author
		g.setFont(mediumFont);
		g.drawString("von " + title.getAuthor(),
				(getWidth() - mediumMetrics.stringWidth("von " + title.getAuthor())) / 2, // centered author in x dimension
				(int) ((getHeight() / 10.0) * 2.5)); // y location of title
		
		// print publisher string
		g.setFont(smallFont);
		g.drawString("erschienen im",
				(getWidth() - smallMetrics.stringWidth("erschienen im")) / 2,
				(int) ((getHeight() / 5.0)));
		g.drawString("IT-E-Books-Verlag", 
				(getWidth() - smallMetrics.stringWidth("IT-E-Books-Verlag")) / 2,
				(int) ((getHeight() / 2.5)));
		g.drawString("Stuttgart",
				(getWidth() - smallMetrics.stringWidth("Stuttgart")) / 2,
				(int) ((getHeight() / 2.5)) + smallMetrics.getHeight());
		int year = new GregorianCalendar().getWeekYear();
		String yearString = Integer.toString(year);
		g.drawString(yearString,
				(getWidth() - smallMetrics.stringWidth(yearString)) / 2,
				(int) ((getHeight() / 2.5)) + 2 * smallMetrics.getHeight());
		String isbnString = ISBNUtils.hyphenateISBN(title.getIsbn13()); 
		g.drawString(isbnString, 
				(getWidth() - smallMetrics.stringWidth(isbnString)) / 2, 
				(int) ((getHeight() / 2.5)) + 4 * smallMetrics.getHeight());
		
		CellRendererPane crp = new CellRendererPane();
		crp.add(this);
		
		g.dispose();
		
		this.revalidate();
	} 
}