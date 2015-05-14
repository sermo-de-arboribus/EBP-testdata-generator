package testdatagen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import testdatagen.model.*;

public class TestScenario implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
}