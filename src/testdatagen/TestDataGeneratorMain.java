package testdatagen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableModel;

import testdatagen.gui.listeners.AddScenarioButtonListener;
import testdatagen.gui.listeners.EditScenarioButtonListener;
import testdatagen.gui.listeners.GenerateDataButtonListener;
import testdatagen.gui.listeners.LoadScenariosButtonListener;
import testdatagen.gui.listeners.SaveScenariosButtonListener;
import testdatagen.model.ScenarioTableModel;
import testdatagen.model.TestScenario;
import testdatagen.model.Title;
import testdatagen.model.files.EBookFile;
import testdatagen.model.files.GraphicFile;
import testdatagen.utilities.CoverUtils;
import testdatagen.utilities.ISBNUtils;
import testdatagen.utilities.TitleUtils;
import testdatagen.utilities.Utilities;

public class TestDataGeneratorMain extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTable scenarioTable;
	private Dimension screenSize;
    
	public TestDataGeneratorMain(String name)
    {
    	super(name);
    	
    	init();
    	
    	// determine screen dimensions and height of task bar
    	screenSize = Utilities.getScreenDimensions(this);
    	
    	// define window dimensions
    	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    	setLocation(0, 0);
    	setSize(screenSize);
    	
    	buildScenarioTable();
    	buildTopButtonPane();
    	buildBottomButtonPane();
    	
    	// display window
    	setVisible(true);
    }

    public static void main(String[] args)
    {
    	new TestDataGeneratorMain("KNV E-Book-Plant test data generator");
    }
    
	public JTable getScenarioTable()
	{
		return scenarioTable;
	}
	
	public void setScenarios(ScenarioTableModel scenarios)
	{
		scenarioTable.setModel(scenarios);
	}
	
	private void addRefreshButtonListener(JButton refreshButton)
	{
		refreshButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				Component source = (Component) evt.getSource();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(source);
				frame.repaint();
			}
		});
	}
	private void buildBottomButtonPane()
	{
		JButton refreshButton = new JButton("Refresh");
		addRefreshButtonListener(refreshButton);
		add(refreshButton, BorderLayout.SOUTH);
	}
	
	private void buildTopButtonPane()
	{
		// create pane and buttons
	    JPanel topButtonPanel = new JPanel();
	    topButtonPanel.setPreferredSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight() / 10));
	    JButton addButton = new JButton("Add new scenario");
	    JButton editButton = new JButton("Edit selected scenario");
	    JButton saveButton = new JButton("Save scenarios");
	    JButton loadButton = new JButton("Load scenarios");
	    JButton generateDataButton = new JButton("Generate test data");
	    
	    // register action listeners
	    addButton.addActionListener(new AddScenarioButtonListener());
	    editButton.addActionListener(new EditScenarioButtonListener(scenarioTable));
	    saveButton.addActionListener(new SaveScenariosButtonListener((ScenarioTableModel) scenarioTable.getModel()));
	    loadButton.addActionListener(new LoadScenariosButtonListener(this));
	    generateDataButton.addActionListener(new GenerateDataButtonListener(scenarioTable));
	    
	    // add buttons to pane
	    topButtonPanel.add(addButton);
	    topButtonPanel.add(editButton);
	    topButtonPanel.add(saveButton);
	    topButtonPanel.add(loadButton);
	    topButtonPanel.add(generateDataButton);
	    
	    // add pane to frame
	    add(topButtonPanel, BorderLayout.NORTH);
	}

	private void buildScenarioTable()
	{
		// TODO: scenario list currently filled with sample data at program start; remove this later?
		List<TestScenario> scenarios = new ArrayList<>();
		TestScenario scenario01 = new TestScenario("Testszenario Nr. 1");
		TestScenario scenario02 = new TestScenario("Testszenario Nr. 2");
		TestScenario scenario03 = new TestScenario("Testszenario Nr. 3");
		scenarios.add(scenario01);
		scenarios.add(scenario02);
		scenarios.add(scenario03);
		
		Title testtitle001 = new Title(9783497600014L, "test-9783497600014", "Testtitel Nr. 1", "Fritz Riemann", true);
		Title testtitle002 = new Title(9787561333068L, "test-9787561333068", "Testtitel Nr. 2", "李叔同", false);
		Title testtitle003 = new Title(9787561331163L, "test-9787561331163", "Testtitel Nr. 3", "Shutong Li", false);
		Title testtitle004 = new Title(9783888146466L, "test-3-88814-646-1", "Changing New York", "Berenice Abbott", true);
		Title testtitle005 = new Title(9783458058649L, "test-9783458058649", "Die vierte Zwischeneiszeit", "Kobo Abe", true);
		Title testtitle006 = new Title(9780679733782L, "test-9780679733782", "The Woman in the Dunes", "Kōbō Abe", false);
		Title testtitle007 = new Title(9783499504723L, "test-9783499504723", "Franz Werfel in Selbstzeugnissen", "Norbert Abels", true);
		Title testtitle008 = new Title(9783836925266L, "test-9783836925266", "Theater. Schauspiele von der Antike bis heute", "Norbert Abels", false);
		Title testtitle009 = new Title(9783923854561L, "test-9783923854561", "Xuma. Ein Roman aus Südafrika", "Peter Abrahams", false);
		Title testtitle010 = new Title(9780030549823L, "test-9780030549823", "A Glossary of Literary Terms", "Meyer Howard Abrams", true);
		Title testtitle011 = new Title(9787229004385L, "test-978-7-229-00438-5", "Anthills of the Savannah", "Chinua Achebe", false);
		Title testtitle012 = new Title(9780435905255L, "test-9780435905255", "Things Fall Apart", "Chinua Achebe", false);
		Title testtitle013 = new Title(9402008, "test-9402008", "Das Laubenspiel", "Adam de la Halle", true);
		
		scenario01.addTitle(testtitle001);
		scenario01.addTitle(testtitle002);
		scenario01.addTitle(testtitle003);
		scenario01.addTitle(testtitle004);
		scenario01.addTitle(testtitle005);
		scenario01.addTitle(testtitle006);
		scenario02.addTitle(testtitle007);
		scenario02.addTitle(testtitle008);
		scenario02.addTitle(testtitle009);
		scenario02.addTitle(testtitle010);
		scenario02.addTitle(testtitle011);
		scenario03.addTitle(testtitle012);
		scenario03.addTitle(testtitle013);
		scenario03.addTitle(testtitle004);
		scenario03.addTitle(testtitle008);

		final TableModel scenarioTableModel = new ScenarioTableModel(scenarios);
		scenarioTable = new JTable(scenarioTableModel);
		scenarioTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		EBookFileFactory eff = EBookFileFactory.getInstance();
		GraphicFileFactory gff = GraphicFileFactory.getInstance();
		Iterator<TestScenario> itr = scenarios.iterator();
		while(itr.hasNext())
		{
			TestScenario sc = itr.next();
			List<Title> titl = sc.getTitleList();
			Iterator<Title> titr = titl.iterator();
			while(titr.hasNext())
			{
				Title tit = titr.next();
				EBookFile ebookFile = eff.generateFile(Utilities.formatToFileType("EPUB"), tit.getIsbn13());
				tit.addMainProductFile(ebookFile, "EPUB");
				
				GraphicFile coverFile = gff.generateFile(CoverUtils.getRandomCoverFormat(), tit.getIsbn13(), GraphicFile.Type.COVER);
				tit.addFile(coverFile);
			}
		}
		
		add(new JScrollPane(scenarioTable), BorderLayout.CENTER);
	}
	
	private void init()
	{
		// this will set the ISBN and title number counter to the value it reached in the previous session
		ISBNUtils.loadLastISBN();
		TitleUtils.loadLastTitleNumber();
	}
}