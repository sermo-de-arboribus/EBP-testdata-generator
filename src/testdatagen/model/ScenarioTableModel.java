package testdatagen.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * The ScenarioTableModel is used as a model behind a JTable. It manages the currently loaded and
 * configured test senarios.
 */
public class ScenarioTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 2L;

	// names of the columns
	private static final String[] COLUMN_NAMES = { "Scenario name", "Number of titles" };
	
	// indexes of the columns
	private static final int COLUMN_IDX_NAME = 0;
	private static final int COLUMN_IDX_NUMBER = 1;

	private List<TestScenario> scenarios;
	
	/**
	 * Constructor
	 * @param scenarioList A list of of TestScenarios
	 */
	public ScenarioTableModel(final List<TestScenario> scenarioList)
	{
		scenarios = new ArrayList<>(scenarioList);
	}
	
	@Override
	public int getColumnCount()
	{
		return COLUMN_NAMES.length;
	}

	@Override
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
	public Object getValueAt(final int rowIndex, final int colIndex)
	{
		TestScenario scenario = scenarios.get(rowIndex);
		
		if(colIndex == COLUMN_IDX_NAME)
			return scenario.getName();
		if(colIndex == COLUMN_IDX_NUMBER)
			return scenario.getNumberOfTitles();
		
		throw new IndexOutOfBoundsException("Invalid columnIndex: " + colIndex);
	}
	
	/**
	 * Get the TestScenario object represented by a certain table row
	 * @param row integer row number
	 * @return selected TestScenario object
	 */
	public TestScenario getScenarioFromRow(final int row)
	{
		return scenarios.get(row);
	}
	
	/**
	 * Add a scenario to the model and notify listeners
	 * @param scenario The TestScenario to be added
	 */
	public void addScenario(final TestScenario scenario)
	{
		scenarios.add(scenario);
		fireTableDataChanged();
	}
	
	/**
	 * Get a copy of the TestScenario list represented by this model
	 * @return ArrayList<TestScenario>
	 */
	public ArrayList<TestScenario> getScenarioList()
	{
		return new ArrayList<TestScenario>(scenarios);
	}
	
	/**
	 * Remove a selected Scenario from the model / table and notify listeners
	 * @param selectedScenario
	 */
	public void removeScenario(final TestScenario selectedScenario)
	{
		scenarios.remove(selectedScenario);
		fireTableDataChanged();
	}
	
	// control serialization
	private void writeObject(final ObjectOutputStream outStream) throws IOException
	{
		outStream.writeObject(scenarios);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(final ObjectInputStream inStream) throws IOException, ClassNotFoundException
	{
		scenarios = (List<TestScenario>) inStream.readObject();
	}
}