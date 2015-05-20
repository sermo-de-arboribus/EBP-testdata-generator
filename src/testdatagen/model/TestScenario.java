package testdatagen.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import testdatagen.model.*;

public class TestScenario implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private TitleTableModel tableOfTitles;
	private String name;
	
    public TestScenario(String name)
    {
    	this.name = name;
    	tableOfTitles = new TitleTableModel(new ArrayList<Title>());
    }
    
    public void addTitle(Title newTitle)
    {
    	tableOfTitles.addTitle(newTitle);
    }
    
    public boolean removeTitle(Title remTitle)
    {
    	return tableOfTitles.removeTitle(remTitle);
    }
    
    public String getName()
    {
    	return name;
    }
    
    public int getNumberOfTitles()
    {
    	return tableOfTitles.getRowCount();
    }
    
    public TitleTableModel getTitleTableModel()
    {
    	return tableOfTitles;
    }
    
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