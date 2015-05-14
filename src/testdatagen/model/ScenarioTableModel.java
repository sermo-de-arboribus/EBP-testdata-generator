package testdatagen.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import testdatagen.TestScenario;

public class ScenarioTableModel extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// names of the columns
	private static final String[] COLUMN_NAMES = { "Scenario name", "Number of titles" };
	
	// indexes of the columns
	private static final int COLUMN_IDX_NAME = 0;
	private static final int COLUMN_IDX_NUMBER = 1;

	private final List<TestScenario> scenarios;
	
	public ScenarioTableModel(final List<TestScenario> scenarioList)
	{
		scenarios = new ArrayList<>(scenarioList);
	}
	
	@Override
	public int getColumnCount()
	{
		return COLUMN_NAMES.length;
	}

	public String getColumnName(final int colIndex)
	{
		return COLUMN_NAMES[colIndex];
	}
	
	@Override
	public int getRowCount()
	{
		return scenarios.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		TestScenario scenario = scenarios.get(rowIndex);
		
		if(colIndex == COLUMN_IDX_NAME)
			return scenario.getName();
		if(colIndex == COLUMN_IDX_NUMBER)
			return scenario.getNumberOfTitles();
		
		throw new IndexOutOfBoundsException("Invalid columnIndex: " + colIndex);
	}
	
	public TestScenario getScenarioFromRow(int row)
	{
		return scenarios.get(row);
	}
	
	public void addScenario(TestScenario scenario)
	{
		scenarios.add(scenario);
	}
	
	public ArrayList<TestScenario> getScenarioList()
	{
		return new ArrayList<TestScenario>(scenarios);
	}
}