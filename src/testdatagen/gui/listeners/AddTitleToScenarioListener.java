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
	
	public AddTitleToScenarioListener(JDialog parentDialog, TestScenario scenario)
	{
		this.scenario = scenario;
		this.parentDialog = parentDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt)
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
						String compName = tcomp.getName();
						if(compName == null)
						{
							continue;
						}
						switch(compName)
						{
							case("covertype"):
								@SuppressWarnings("unchecked")
								JComboBox<String> jcb1 = (JComboBox<String>) tcomp;
								String boxString = (String) jcb1.getSelectedItem();
								mfl = boxString.equals("Media file link") ? true : false;
								break;
							case("product"):
								@SuppressWarnings("unchecked")
								JComboBox<String> jcb2 = (JComboBox<String>) tcomp;
								format = (String) jcb2.getSelectedItem();
								break;
							case("screenshot"):
								JCheckBox jcb3 = (JCheckBox) tcomp;
								screenshot = jcb3.isSelected();
								break;
							case("packshot"):
								JCheckBox jcb4 = (JCheckBox) tcomp;
								packshot = jcb4.isSelected();
								break;
							case("extracts"):
								JCheckBox jcb5 = (JCheckBox) tcomp;
								extract = jcb5.isSelected();
								break;
							case("backcover"):
								JCheckBox jcb6 = (JCheckBox) tcomp;
								backcover = jcb6.isSelected();
								break;
							case("epubmobi"):
								JCheckBox jcb7 = (JCheckBox) tcomp;
								epubmobi = jcb7.isSelected();
								break;
							default: 
								break;
						}
					}
					
					// instantiate a new title object
					long nextIsbn = ISBNUtils.getNextISBN();
					Title newTitle = new Title(nextIsbn, "test-" + nextIsbn, TitleUtils.getNewTitle(), TitleUtils.getNewAuthor(), mfl);
					
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