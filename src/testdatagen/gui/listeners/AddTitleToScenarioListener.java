package testdatagen.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.*;

import testdatagen.EBookFileFactory;
import testdatagen.GraphicFileFactory;
import testdatagen.gui.TitleForm;
import testdatagen.model.TestScenario;
import testdatagen.model.Title;
import testdatagen.model.files.EBookFile;
import testdatagen.model.files.GraphicFile;
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
			
			// determine product format
			String format = formDataMap.get("product");
					
			// instantiate a new title object
			long nextIsbn = ISBNUtils.getNextISBN();
			Title newTitle = new Title(nextIsbn, "test-" + nextIsbn, TitleUtils.getNewTitle(), TitleUtils.getNewAuthor(), formDataMap.get("covertype").equals("Media file link") ? true : false);

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
			if(Boolean.parseBoolean(formDataMap.get("pfd-E203")))
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
					
			// instantiate selected ebook file types and add them to the title object
			EBookFileFactory eff = EBookFileFactory.getInstance();
					
			EBookFile ebookFile = eff.generateFile(Utilities.formatToFileType(format), nextIsbn);
			newTitle.addMainProductFile(ebookFile, format);
					
			// check for optional product content files and generate if needed
			if(Boolean.parseBoolean(formDataMap.get("epubmobi")))
			{
				EBookFile epubMobiFile = eff.generateFile("EpubMobi", nextIsbn);
				newTitle.addFile(epubMobiFile);
			}
			if(Boolean.parseBoolean(formDataMap.get("extracts")))
			{
				EBookFile extractFile = eff.generateDemoFile(Utilities.formatToFileType(format), nextIsbn);
				newTitle.addFile(extractFile);
			}
					
			// instantiate selected graphic file types and add them to the title object
			GraphicFileFactory gff = GraphicFileFactory.getInstance();
			// a cover file is needed in any case, choose randomly between jpg or pdf cover
			GraphicFile coverFile = gff.generateFile(CoverUtils.getRandomCoverFormat(), nextIsbn, GraphicFile.Type.COVER);
			newTitle.addFile(coverFile);
			// check for optional graphic files and generate if requested
			if(Boolean.parseBoolean(formDataMap.get("screenshot")))
			{
				GraphicFile screenshotFile = gff.generateFile("JPEG", nextIsbn, GraphicFile.Type.SCREENSHOT);
				newTitle.addFile(screenshotFile);
			}
			if(Boolean.parseBoolean(formDataMap.get("packshot")))
			{
				GraphicFile packshotFile = gff.generateFile("JPEG", nextIsbn, GraphicFile.Type.PACKSHOT);
				newTitle.addFile(packshotFile);
			}
			if(Boolean.parseBoolean(formDataMap.get("backcover")))
			{
				GraphicFile backcoverFile = gff.generateFile("JPEG", nextIsbn, GraphicFile.Type.BACKCOVER);
				newTitle.addFile(backcoverFile);
			}
					
			scenario.addTitle(newTitle);
					
			// refresh display
			parentDialog.repaint();
			dialog.dispose();
		}
	}
	
	private void displayTitleForm()
	{
		TitleForm titleForm = new TitleForm(scenario);
		titleForm.setVisible(true);
	}
}
