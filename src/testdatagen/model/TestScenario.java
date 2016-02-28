package testdatagen.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for a TestScenario. A test scenario holds a list of product Title objects that belong to the scenario.
 */
public class TestScenario implements Serializable
{
	private static final long serialVersionUID = 2L;
	private TitleTableModel tableOfTitles;
	private String name;
	
	/**
	 * Constructor. Initializes the title table.
	 * @param name String name of the test scenario
	 */
    public TestScenario(final String name)
    {
    	this.name = name;
    	tableOfTitles = new TitleTableModel(new ArrayList<Title>());
    }
    
    /**
     * Add a product Title object to the scenario.
     */
    public void addTitle(final Title newTitle)
    {
    	tableOfTitles.addTitle(newTitle);
    }
    
    /**
     * Remove a title from the scenario
     * @param remTitle The product Title object to be removed.
     * @return true, if title was in the scenario list.
     */
    public boolean removeTitle(Title remTitle)
    {
    	return tableOfTitles.removeTitle(remTitle);
    }

    /**
     * Returns the name of the test scenario
     * @return String title of scenario
     */
    public String getName()
    {
    	return name;
    }
    
    /**
     * Returns the number of titles in this scenario
     * @return int number of titles currently part of this scenario
     */
    public int getNumberOfTitles()
    {
    	return tableOfTitles.getRowCount();
    }
    
    /**
     * Returns the TitleTableModel for this scenario
     * @return The TitleTableModel
     */
    public TitleTableModel getTitleTableModel()
    {
    	return tableOfTitles;
    }
    
    /**
     * Returns the titles that are part of this scenario, as a list
     * @return List<Title> of this scenario
     */
    public List<Title> getTitleList()
    {
    	return tableOfTitles.getListOfTitles();
    }
    
    // control serialization, because when simply using the built-in
    // mechanism to serialize a TableModel in conjunction with our 
    // TableCellLineWrapRenderer an error will be thrown, saying that
    // the SizeSequence object is not serializable. 
	private void writeObject(final ObjectOutputStream outStream) throws IOException
	{
		outStream.writeUTF(name);
		outStream.writeObject(getTitleList());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(final ObjectInputStream inStream) throws IOException, ClassNotFoundException
	{
		name = inStream.readUTF();
		List<Title> titleList = (List<Title>) inStream.readObject();
		tableOfTitles = new TitleTableModel(titleList);
	}
}