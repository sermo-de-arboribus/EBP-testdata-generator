package testdatagen.model.files;

import java.awt.Dimension;
import java.io.IOException;
import java.util.GregorianCalendar;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import testdatagen.model.Title;
import testdatagen.utilities.CoverUtils;
import testdatagen.utilities.ISBNUtils;

/**
 * A class that represents PDF files as cover files (i.e. a graphic file which technically is a PDF)
 */
public class PDFCoverFile extends GraphicFile
{
	private static final long serialVersionUID = 2L;

	/**
	 * Constructor
	 * @param title The product Title object that this file belongs to
	 */
	public PDFCoverFile(final Title title)
	{
		super(title, GraphicFile.Type.COVER);
	}
	
	@Override
	public java.io.File generate(final java.io.File destFolder)
	{
		Dimension coverDimension = CoverUtils.getRandomCoverDimension();
		
		PDDocument coverDoc = new PDDocument();
		PDRectangle pageDimension = new PDRectangle((float) coverDimension.getWidth() / 7.0f, (float) coverDimension.getHeight() / 7.0f);
		PDPage coverPage = new PDPage(pageDimension);
		coverDoc.addPage(coverPage);

		writeCoverToPage(coverDoc, coverPage);
		
		java.io.File storedFile = new java.io.File(FilenameUtils.concat(destFolder.getPath(), buildFileName()));
		
		try
		{
			coverDoc.save(storedFile);
		}
		catch (COSVisitorException cve)
		{
			System.out.println("An exception that represents something gone wrong when visiting a PDF object, thrown by the Apache PDFBox library");
			cve.printStackTrace();
			storedFile = null;
		}
		catch (IOException ex)
		{
			System.out.println("I/O error occurred during saving of cover PDF");
			ex.printStackTrace();
			storedFile = null;
		}
		finally
		{
			try
			{
				coverDoc.close();	
			}
			catch(IOException ex)
			{
				// nop
			}
		}
		return storedFile;
	}
	
	@Override
	public String toString()
	{
		return "CoverPDF[" + title.getIsbn13() + "]";
	}
	
	// helper method which is also used by testdatagen.model.files.PDFFile, so it is "package-private"
	// Writes cover information (author, title, ...) into the PDPage object which is passed in as an argument
	void writeCoverToPage(final PDDocument coverDoc, final PDPage coverPage)
	{
		PDRectangle pageDimension = coverPage.getMediaBox();
		PDFont largeFont = PDType1Font.TIMES_BOLD;
		PDFont mediumFont = PDType1Font.TIMES_ITALIC;
		PDFont smallFont = PDType1Font.TIMES_ROMAN;
		
		float largeFontSize = pageDimension.getHeight() * 0.05f;
		float mediumFontSize = pageDimension.getHeight() * 0.035f;
		float smallFontSize = pageDimension.getHeight() * 0.028f;
		
		try
		{
			PDPageContentStream contentStream = new PDPageContentStream(coverDoc, coverPage);
			
			contentStream.beginText();
			
			// print title
			contentStream.setFont(largeFont, largeFontSize);
			contentStream.moveTextPositionByAmount(pageDimension.getWidth() / 10f, (pageDimension.getHeight() / 10f) * 8.5f);
			contentStream.drawString(title.getName());
			
			// print author
			contentStream.setFont(mediumFont, mediumFontSize);
			contentStream.moveTextPositionByAmount(0f, -2f * largeFontSize);
			contentStream.drawString("von " + title.getAuthor());
			
			// print publisher string
			contentStream.setFont(smallFont, smallFontSize);
			contentStream.moveTextPositionByAmount(0f, -5f * largeFontSize);
			contentStream.drawString("erschienen im");
			contentStream.moveTextPositionByAmount(0f, -2f * smallFontSize);
			contentStream.drawString("IT-E-Books-Verlag");
			contentStream.moveTextPositionByAmount(0f, -1.1f * smallFontSize);
			contentStream.drawString("Stuttgart");
			int year = new GregorianCalendar().getWeekYear();
			String yearString = Integer.toString(year);
			contentStream.moveTextPositionByAmount(0f, -1.1f * smallFontSize);
			contentStream.drawString(yearString);
			contentStream.moveTextPositionByAmount(0f, -2f * smallFontSize);
			String isbnString = ISBNUtils.hyphenateISBN(title.getIsbn13());
			contentStream.drawString(isbnString);
			
			contentStream.endText();
			contentStream.close();
		}
		catch (IOException ex)
		{
			System.out.println("I/O error occurred during PDF generation");
			ex.printStackTrace();
		}
	}
	
	@Override
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + "_cover.pdf";
	}
}