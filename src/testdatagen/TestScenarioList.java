package testdatagen;

import java.util.ArrayList;
import java.util.List;

public class TestScenarioList
{
    private List<TestScenario> listOfConfigurations;
    
    public TestScenarioList()
    {
    	listOfConfigurations = new ArrayList<TestScenario>();
    }
    
    public void addConfig(TestScenario newConfig)
    {
    	listOfConfigurations.add(newConfig);
    }
    
    public boolean removeConfig(TestScenario remConfig)
    {
    	return listOfConfigurations.remove(remConfig);
    }
}