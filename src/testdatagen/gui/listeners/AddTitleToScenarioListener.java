package testdatagen.gui.listeners;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
			// some local variables for title form data
			String format = "";
			boolean mfl = false, screenshot = false, packshot = false, extract = false, backcover = false, epubmobi = false;
			boolean gtin13 = false, doi = false, urn = false;
			// get reference to the form
			JButton button = (JButton) evt.getSource();
			JDialog dialog = (JDialog) button.getTopLevelAncestor();
			Container cp = dialog.getContentPane();
			Component[] components = cp.getComponents();
			for(Component component : components)
			{
				if(component.getName() != null && component.getName().equals("titleOptionsPanel"))
				{
					JPanel titleOptionsPanel = (JPanel) component;
					Component[] titleComponents = titleOptionsPanel.getComponents();
					
					//process the form data
					for(Component tcomp : titleComponents)
					{
						
						JCheckBox checkBox; 
						String compName = tcomp.getName();
						if(compName == null)
						{
							continue;
						}
						switch(compName)
						{
							case("covertype"):
								JComboBox<String> comboBox1 = (JComboBox<String>) tcomp;
								String boxString = (String) comboBox1.getSelectedItem();
								mfl = boxString.equals("Media file link") ? true : false;
								break;
							case("product"):
								JComboBox<String> comboBox2 = (JComboBox<String>) tcomp;
								format = (String) comboBox2.getSelectedItem();
								break;
							case("screenshot"):
								checkBox = (JCheckBox) tcomp;
								screenshot = checkBox.isSelected();
								break;
							case("packshot"):
								checkBox = (JCheckBox) tcomp;
								packshot = checkBox.isSelected();
								break;
							case("extracts"):
								checkBox = (JCheckBox) tcomp;
								extract = checkBox.isSelected();
								break;
							case("backcover"):
								checkBox = (JCheckBox) tcomp;
								backcover = checkBox.isSelected();
								break;
							case("epubmobi"):
								checkBox = (JCheckBox) tcomp;
								epubmobi = checkBox.isSelected();
								break;
							case ("GTIN-13"):
								checkBox = (JCheckBox) tcomp;
								gtin13 = checkBox.isSelected();
							case ("DOI"):
								checkBox = (JCheckBox) tcomp;
								doi = checkBox.isSelected();
							case ("URN"):
								checkBox = (JCheckBox) tcomp;
								urn = checkBox.isSelected();
							default: 
								break;
						}
					}
					
					// instantiate a new title object
					long nextIsbn = ISBNUtils.getNextISBN();
					Title newTitle = new Title(nextIsbn, "test-" + nextIsbn, TitleUtils.getNewTitle(), TitleUtils.getNewAuthor(), mfl);
					
					// additional product identifiers required in ONIX file?
					if(gtin13)
					{
						newTitle.getOnixPartsDirector().addProductIdentifier("03");
					}
					if(doi)
					{
						newTitle.getOnixPartsDirector().addProductIdentifier("06");
					}
					if(urn)
					{
						newTitle.getOnixPartsDirector().addProductIdentifier("22");
					}
					
					// instantiate selected ebook file types and add them to the title object
					EBookFileFactory eff = EBookFileFactory.getInstance();
					
					EBookFile ebookFile = eff.generateFile(Utilities.formatToFileType(format), nextIsbn);
					newTitle.addMainProductFile(ebookFile, format);
					
					// check for optional product content files and generate if needed
					if(epubmobi)
					{
						EBookFile epubMobiFile = eff.generateFile("EpubMobi", nextIsbn);
						newTitle.addFile(epubMobiFile);
					}
					if(extract)
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
					if(screenshot)
					{
						GraphicFile screenshotFile = gff.generateFile("JPEG", nextIsbn, GraphicFile.Type.SCREENSHOT);
						newTitle.addFile(screenshotFile);
					}
					if(packshot)
					{
						GraphicFile packshotFile = gff.generateFile("JPEG", nextIsbn, GraphicFile.Type.PACKSHOT);
						newTitle.addFile(packshotFile);
					}
					if(backcover)
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
		}
	}
	
	private void displayTitleForm()
	{
		TitleForm titleForm = new TitleForm(scenario);
		titleForm.setVisible(true);
	}
}
