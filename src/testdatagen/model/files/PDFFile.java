package testdatagen.model.files;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import testdatagen.model.Title;
import testdatagen.templates.EBookChapterTemplate;

public class PDFFile extends EBookFile
{
	private static final long serialVersionUID = 2L;
	private static final float POINTS_PER_INCH = 72;
	private static final float MM_PER_INCH = 1 / (10 * 2.54f) * POINTS_PER_INCH;
	
	public PDFFile(final Title title, final boolean demoFlag)
	{
		super(title, demoFlag);
	}
	
	public String toString()
	{
		String fileString = "PDF";
		if(this.isDemoFile())
		{
			fileString += "_extract";
		}
		return fileString + "[" + title.getIsbn13() +  "]";
	}

	@Override
	public File generate(File destDir)
	{
		// TODO: For convenience we just use a PDF cover file for generating the first page and then add additional pages later 
		PDFCoverFile pcf = new PDFCoverFile(title);
		
		PDDocument pdfDoc = new PDDocument();
		PDRectangle pageDimension = new PDRectangle(210 * MM_PER_INCH, 297 * MM_PER_INCH); // A4
		PDPage coverPage = new PDPage(pageDimension);
		PDPage chapterPage = new PDPage(pageDimension);
		pdfDoc.addPage(coverPage);
		pdfDoc.addPage(chapterPage);
		
		pcf.writeCoverToPage(pdfDoc, coverPage);
		writeChapterToPage(pdfDoc, chapterPage);
		
		java.io.File storedFile = new java.io.File(FilenameUtils.concat(destDir.getPath(), buildFileName()));
		
		try
		{
			pdfDoc.save(storedFile);
		}
		catch (COSVisitorException cve)
		{
			System.out.println("An exception that represents something gone wrong when visiting a PDF object, thrown by the Apache PDFBox library");
			cve.printStackTrace();
			storedFile = null;
		}
		catch (IOException ex)
		{
			System.out.println("I/O error occurred during saving of e-book PDF");
			ex.printStackTrace();
			storedFile = null;
		}
		finally
		{
			try
			{
				pdfDoc.close();	
			}
			catch(IOException ex)
			{
				// nop
			}
		}
		return storedFile;
	}
	
	protected String buildFileName()
	{
		return Long.toString(title.getIsbn13()) + (isDemoFile() ? "_Extract" : "" ) + ".pdf";
	}
	
	private void writeChapterToPage(PDDocument pdfDoc, PDPage chapterPage)
	{
		PDRectangle pageDimension = chapterPage.getMediaBox();
		PDFont smallFont = PDType1Font.TIMES_ROMAN;
		float fontSize = pageDimension.getHeight() * 0.02f;
		
		try
		{
			PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, chapterPage);
			
			contentStream.beginText();
			contentStream.setFont(smallFont, fontSize);
			contentStream.appendRawCommands(smallFont.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize + " TL\n");
			contentStream.moveTextPositionByAmount(100f, (pageDimension.getHeight() / 10f) * 8.5f);
			
			EBookChapterTemplate chapterTemplate = new EBookChapterTemplate(new Locale("de"), 1);
			String chapterText = chapterTemplate.fillWithPlainText();
			
			StringBuffer lineBuffer = new StringBuffer();
			String[] words = chapterText.split(" ");
			
			// PDF has no convenient way of line-wrapping, we have to take care of this by ourselves
			for(int i = 0; i < words.length; i++)
			{
				float bufferLength = smallFont.getStringWidth(lineBuffer.toString()) / 1000 * fontSize;
				float nextWordLength = smallFont.getStringWidth(" " + words[i]) / 1000 * fontSize;
				if(bufferLength + nextWordLength <= (pageDimension.getWidth() - 200))
				{
					// Word fits into current line, add it to the buffer
					lineBuffer.append(" " + words[i]);
				}
				else
				{
					// Line buffer is full. We need to write out the line and clear the buffer
					contentStream.drawString(lineBuffer.toString());
					// line feed + carriage return
					contentStream.appendRawCommands("T*\n");
					lineBuffer = new StringBuffer();
					lineBuffer.append(words[i]);
				}
			}
			contentStream.drawString(lineBuffer.toString());
			
			contentStream.endText();
			contentStream.close();
		}
		catch (IOException ex)
		{
			System.out.println("I/O error occurred during PDF generation");
			ex.printStackTrace();
		}
	}
}