package testdatagen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableModel;

import testdatagen.gui.listeners.*;
import testdatagen.model.*;
import testdatagen.model.files.EBookFile;
import testdatagen.model.files.GraphicFile;
import testdatagen.utilities.*;

public class TestDataGeneratorMain extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTable scenarioTable;
	private Dimension screenSize;
	private JProgressBar progressBar;
	private JLabel progressLabel;
    
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
    	
    	buildMenuBar();
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
    
    public JProgressBar getProgressBar()
    {
    	return progressBar;
    }
    
    public JLabel getProgressLabel()
    {
    	return progressLabel;
    }
    
	public JTable getScenarioTable()
	{
		return scenarioTable;
	}

	public ScenarioTableModel getScenarioTableModel()
	{
		return (ScenarioTableModel) scenarioTable.getModel();
	}
	
	public void setScenarios(ScenarioTableModel scenarios)
	{
		scenarioTable.setModel(scenarios);
	}
	
	private void buildBottomButtonPane()
	{
		JPanel bottomPanel = new JPanel(new GridLayout(0,1));

		JPanel progressPanel1 = new JPanel(new FlowLayout());
		JPanel progressPanel2 = new JPanel(new FlowLayout());
		
		progressLabel = new JLabel("");
		progressPanel1.add(progressLabel);
		
		progressBar = new JProgressBar();
		progressPanel2.add(progressBar);
		
		bottomPanel.add(progressPanel1);
		bottomPanel.add(progressPanel2);
		
		add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private void buildMenuBar()
	{
		JMenuBar menubar = new JMenuBar();
		
		// Info menu
		JMenu infoMenu = new JMenu("Info");
		JMenuItem generalInfoItem = new JMenuItem("How to use TDG");
		JMenuItem aboutInfoItem = new JMenuItem("About TDG");
		
		infoMenu.add(generalInfoItem);
		generalInfoItem.addActionListener(new MenuGeneralInfoListener());
		infoMenu.add(aboutInfoItem);
		aboutInfoItem.addActionListener(new MenuAboutInfoListener());
		
		// File menu
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveFileItem = new JMenuItem("Save scenarios");
		JMenuItem loadFileItem = new JMenuItem("Load scenarios");
		JMenuItem generateFileItem = new JMenuItem("Generate selected scenario");

		fileMenu.add(saveFileItem);
		saveFileItem.addActionListener(new SaveScenariosButtonListener(this));
		fileMenu.add(loadFileItem);
		loadFileItem.addActionListener(new LoadScenariosButtonListener(this));
		fileMenu.add(generateFileItem);
		generateFileItem.addActionListener(new GenerateDataButtonListener(this));
	    
		menubar.add(fileMenu);
		menubar.add(infoMenu);
		
		this.setJMenuBar(menubar);
	}
	
	private void buildTopButtonPane()
	{
		// create pane and buttons
	    JPanel topButtonPanel = new JPanel();
	    topButtonPanel.setPreferredSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight() / 10));
	    JButton addButton = new JButton("Add new scenario");
	    JButton editButton = new JButton("Edit selected scenario");
	    JButton delButton = new JButton("Delete scenario");
	    JButton saveButton = new JButton("Save scenarios");
	    JButton loadButton = new JButton("Load scenarios");
	    JButton generateDataButton = new JButton("Generate test data");
	    
	    // register action listeners
	    addButton.addActionListener(new AddScenarioButtonListener(this));
	    editButton.addActionListener(new EditScenarioButtonListener(this));
	    delButton.addActionListener(new DeleteScenarioButtonListener(this));
	    saveButton.addActionListener(new SaveScenariosButtonListener(this));
	    loadButton.addActionListener(new LoadScenariosButtonListener(this));
	    generateDataButton.addActionListener(new GenerateDataButtonListener(this));
	    
	    // add buttons to pane
	    topButtonPanel.add(addButton);
	    topButtonPanel.add(editButton);
	    topButtonPanel.add(delButton);
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
		
		Title testtitle001 = new Title(9783497600014L, "test-9783497600014", "Testtitel Nr. 1", "Fritz Riemann", "EPUB");
		Title testtitle002 = new Title(9787561333068L, "test-9787561333068", "Testtitel Nr. 2", "李叔同", "WMEPUB");
		Title testtitle003 = new Title(9787561331163L, "test-9787561331163", "Testtitel Nr. 3", "Shutong Li", "NDEPUB");
		Title testtitle004 = new Title(9783888146466L, "test-3-88814-646-1", "Changing New York", "Berenice Abbott", "PDF");
		Title testtitle005 = new Title(9783458058649L, "test-9783458058649", "Die vierte Zwischeneiszeit", "Kobo Abe", "WMPDF");
		Title testtitle006 = new Title(9780679733782L, "test-9780679733782", "The Woman in the Dunes", "Kōbō Abe", "NDPDF");
		Title testtitle007 = new Title(9783499504723L, "test-9783499504723", "Franz Werfel in Selbstzeugnissen", "Norbert Abels", "WMMOBI");
		Title testtitle008 = new Title(9783836925266L, "test-9783836925266", "Theater. Schauspiele von der Antike bis heute", "Norbert Abels", "NDMOBI");
		Title testtitle009 = new Title(9783923854561L, "test-9783923854561", "Xuma. Ein Roman aus Südafrika", "Peter Abrahams", "EPUB");
		Title testtitle010 = new Title(9780030549823L, "test-9780030549823", "A Glossary of Literary Terms", "Meyer Howard Abrams", "WMEPUB");
		Title testtitle011 = new Title(9787229004385L, "test-978-7-229-00438-5", "Anthills of the Savannah", "Chinua Achebe", "NDEPUB");
		Title testtitle012 = new Title(9780435905255L, "test-9780435905255", "Things Fall Apart", "Chinua Achebe", "EPUB");
		Title testtitle013 = new Title(9789402008005L, "test-9789402008005", "Das Laubenspiel", "Adam de la Halle", "WMEPUB");
		
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
				EBookFile ebookFile = eff.generateFile(tit.getEpubType(), tit.getIsbn13());
				tit.addFile(ebookFile);
				
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
