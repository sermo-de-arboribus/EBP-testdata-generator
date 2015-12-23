package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.*;

import testdatagen.EBookFileFactory;
import testdatagen.GraphicFileFactory;
import testdatagen.gui.TitleForm;
import testdatagen.model.Price;
import testdatagen.model.Subject;
import testdatagen.model.TestScenario;
import testdatagen.model.Title;
import testdatagen.model.files.EBookFile;
import testdatagen.model.files.GraphicFile;
import testdatagen.onixbuilder.OnixNotificationTypeBuilder;
import testdatagen.onixbuilder.OnixProductAvailabilityBuilder;
import testdatagen.utilities.CoverUtils;
import testdatagen.utilities.ISBNUtils;
import testdatagen.utilities.TitleUtils;
import testdatagen.utilities.Utilities;

public class AddTitleToScenarioListener implements ActionListener
{
	private TestScenario scenario;
	private JDialog parentDialog;
	
	public AddTitleToScenarioListener(final JDialog parentDialog, final TestScenario scenario)
	{
		this.scenario = scenario;
		this.parentDialog = parentDialog;
	}
	
	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		// check where the event occurred
		String eventName = evt.getActionCommand();
		
		if(eventName.equals("Add test title") || eventName.equals("Add title to scenario"))
		{
			displayTitleForm();	
		}
		else if (eventName.equals("title submitted"))
		{
			// get reference to the form
			JButton button = (JButton) evt.getSource();
			TitleForm dialog = (TitleForm) button.getTopLevelAncestor();
			Map<String, String> formDataMap = dialog.getFormDataMap();
			Set<Subject> subjectSet = dialog.getConfiguredSubjects();
			Set<Price> pricesSet = dialog.getConfiguredPrices();
			
			// determine product format
			String format = formDataMap.get("product");
			String coverUploadType = formDataMap.get("covertype");
			
			// instantiate a new title object
			long nextIsbn = ISBNUtils.getNextISBN();
			Title newTitle = new Title(nextIsbn, "test-" + nextIsbn, TitleUtils.getNewTitle(), TitleUtils.getNewAuthor(), format);

			// do we need to upload the cover and include a media file link in the ONIX?
			if(coverUploadType.equals("Media file link"))
			{
				newTitle.setMediaFileUrl("");
			}
			
			// additional product identifiers required in ONIX file?
			if(Boolean.parseBoolean(formDataMap.get("gtin13")))
			{
				newTitle.getOnixPartsDirector().addProductIdentifier("03");
			}
			if(Boolean.parseBoolean(formDataMap.get("doi")))
			{
				newTitle.getOnixPartsDirector().addProductIdentifier("06");
			}
			if(Boolean.parseBoolean(formDataMap.get("urn")))
			{
				newTitle.getOnixPartsDirector().addProductIdentifier("22");
			}
			
			// additional product form details required in ONIX file?
			if(Boolean.parseBoolean(formDataMap.get("pfd-a103")))
			{
				newTitle.getOnixPartsDirector().addProductFormDetail("A103");
			}
			if(Boolean.parseBoolean(formDataMap.get("pfd-e200")))
			{
				newTitle.getOnixPartsDirector().addProductFormDetail("E200");
			}
			if(Boolean.parseBoolean(formDataMap.get("pfd-e201")))
			{
				newTitle.getOnixPartsDirector().addProductFormDetail("E201");
			}
			if(Boolean.parseBoolean(formDataMap.get("pfd-e202")))
			{
				newTitle.getOnixPartsDirector().addProductFormDetail("E202");
			}
			if(Boolean.parseBoolean(formDataMap.get("pfd-e203")))
			{
				newTitle.getOnixPartsDirector().addProductFormDetail("E203");
			}
			
			// additional titles required in ONIX file?
			if(Boolean.parseBoolean(formDataMap.get("tittyp03")))
			{
				newTitle.getOnixPartsDirector().addTitle("03");
			}
			if(Boolean.parseBoolean(formDataMap.get("tittyp06")))
			{
				newTitle.getOnixPartsDirector().addTitle("06");
			}
			if(Boolean.parseBoolean(formDataMap.get("tittyp08")))
			{
				newTitle.getOnixPartsDirector().addTitle("08");
			}
			if(Boolean.parseBoolean(formDataMap.get("tittyp11")))
			{
				newTitle.getOnixPartsDirector().addTitle("11");
			}
			if(Boolean.parseBoolean(formDataMap.get("tittyp13")))
			{
				newTitle.getOnixPartsDirector().addTitle("13");
			}
			
			// additional subjects required in ONIX file?
			if(!subjectSet.isEmpty())
			{
				Iterator<Subject> iterator = subjectSet.iterator();
				while(iterator.hasNext())
				{
					newTitle.addSubject(iterator.next());
				}
			}
			
			// additional prices required in ONIX file?
			if(!pricesSet.isEmpty())
			{
				Iterator<Price> iterator = pricesSet.iterator();
				while(iterator.hasNext())
				{
					newTitle.addPrice(iterator.next());
				}
			}
			
			// change of default notification or availability types needed?
			String notificationType = formDataMap.get("notificationtype");
			if(notificationType != null && !notificationType.isEmpty() && 
					!notificationType.equals(OnixNotificationTypeBuilder.DEFAULT_NOTIFICATION_TYPE))
			{
				newTitle.getOnixPartsDirector().replaceNotificationType(notificationType);
			}
			String availCode = formDataMap.get("availabilitycode");
			String prodAvail = formDataMap.get("productavailability");
			if(availabilityChanged(availCode, prodAvail))
			{
				newTitle.getOnixPartsDirector().replaceProductAvailability(availCode, prodAvail);
			}
			
			// instantiate selected ebook file types and add them to the title object
			EBookFileFactory eff = EBookFileFactory.getInstance();

			EBookFile ebookFile = eff.generateFile(Utilities.formatToFileType(format), newTitle);
			newTitle.addFile(ebookFile);

			// check for optional product content files and generate if needed
			if(Boolean.parseBoolean(formDataMap.get("epubmobi")))
			{
				EBookFile epubMobiFile = eff.generateFile("EpubMobi", newTitle);
				newTitle.addFile(epubMobiFile);
			}
			if(Boolean.parseBoolean(formDataMap.get("extracts")))
			{
				EBookFile extractFile = eff.generateDemoFile(Utilities.formatToFileType(format), newTitle);
				newTitle.addFile(extractFile);
			}

			// instantiate selected graphic file types and add them to the title object
			GraphicFileFactory gff = GraphicFileFactory.getInstance();
			// a cover file is needed in any case, choose randomly between jpg or pdf cover
			GraphicFile coverFile = gff.generateFile(CoverUtils.getRandomCoverFormat(), newTitle, GraphicFile.Type.COVER);
			newTitle.addFile(coverFile);
			// check for optional graphic files and generate if requested
			if(Boolean.parseBoolean(formDataMap.get("screenshot")))
			{
				Random random = new Random();
				int numberOfScreenshotFiles = random.nextInt(5) + 1;
				for(int i = 1; i <= numberOfScreenshotFiles; i++)
				{
					GraphicFile screenshotFile = gff.generateFile("JPEG", newTitle, GraphicFile.Type.SCREENSHOT, i);
					newTitle.addFile(screenshotFile);
				}
			}
			if(Boolean.parseBoolean(formDataMap.get("packshot")))
			{
				Random random = new Random();
				int numberOfScreenshotFiles = random.nextInt(5) + 1;
				for(int i = 1; i <= numberOfScreenshotFiles; i++)
				{
					GraphicFile packshotFile = gff.generateFile("JPEG", newTitle, GraphicFile.Type.PACKSHOT, i);
					newTitle.addFile(packshotFile);
				}
			}
			if(Boolean.parseBoolean(formDataMap.get("backcover")))
			{
				GraphicFile backcoverFile = gff.generateFile("JPEG", newTitle, GraphicFile.Type.BACKCOVER);
				newTitle.addFile(backcoverFile);
			}
					
			scenario.addTitle(newTitle);
					
			// refresh display
			parentDialog.repaint();
			dialog.dispose();
		}
	}
	
	private boolean availabilityChanged(String availCode, String prodAvail)
	{
		boolean availCodeChanged, prodAvailChanged;
		availCodeChanged = availCode != null && !availCode.isEmpty() && 
				!availCode.equals(OnixProductAvailabilityBuilder.DEFAULT_AVAILABILITY_CODE);
		prodAvailChanged = prodAvail != null && !prodAvail.isEmpty() && 
				!prodAvail.equals(OnixProductAvailabilityBuilder.DEFAULT_PRODUCT_AVAILABILITY);
		return availCodeChanged && prodAvailChanged;
	}

	private void displayTitleForm()
	{
		TitleForm titleForm = new TitleForm(scenario);
		titleForm.setVisible(true);
	}
}