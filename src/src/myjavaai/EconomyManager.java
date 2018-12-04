package myjavaai;

import com.springrts.ai.oo.*;
import com.springrts.ai.oo.clb.*;

import java.util.ArrayList;
import java.util.List;

public class EconomyManager
{
    private OOAICallback _callback;

    private MyJavaAI _ai;

    private BaResources _baResources;

    private List<AIFloat3> _metalPositions;

    private List<AIFloat3> _takenMetalPositions;

    public float MetalPercent;

    public float EnergyPercent;

    public EconomyManager(OOAICallback callback, MyJavaAI ai, BaResources baResources)
    {
        _callback = callback;
        _ai = ai;
        _baResources = baResources;
        _metalPositions = callback.getMap().getResourceMapSpotsPositions(baResources.Metal);
        _takenMetalPositions = new ArrayList<>();
    }

    public void UpdateEconomy()
    {
        Economy economy = _callback.getEconomy();
        float currentMetal = economy.getCurrent(_baResources.Metal);
        float currentEnergy = economy.getCurrent(_baResources.Energy);

        float metalStorage = economy.getStorage(_baResources.Metal);
        float energyStorage = economy.getStorage(_baResources.Energy);

        MetalPercent = currentMetal / metalStorage;
        EnergyPercent = currentEnergy / energyStorage;
    }

    public AIFloat3 ClosestAvailableMetalSpot(AIFloat3 unitPosition)
    {
        try
        {
            AIFloat3 closestSpot = null;

            for (AIFloat3 metalSpot : _metalPositions)
            {
                if (closestSpot == null)
                {
                    closestSpot = metalSpot;
                }
                else if(UtilityFunctions.CalculateDistance(metalSpot, unitPosition) < UtilityFunctions.CalculateDistance(closestSpot, unitPosition) && !_takenMetalPositions.contains(metalSpot))
                {
                    closestSpot = metalSpot;
                }
            }

            return closestSpot;
        }
        catch(Exception exception)
        {
            _ai.SendTextMessage("Exception calculating Closest available Metal Spot: " + exception.toString());
            return unitPosition;
        }
    }

    public void MarkMetalPositionAsTaken(AIFloat3 position)
    {
        _takenMetalPositions.add(position);
    }

    public String GetStatus()
    {
        return "Total Metal Positions: " + _metalPositions.size() + " Taken Metal Positions: " + _takenMetalPositions.size() + " Metal Percent: " + MetalPercent + " Energy Percent: " + EnergyPercent;
    }
}
