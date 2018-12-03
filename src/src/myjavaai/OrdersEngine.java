package myjavaai;

import com.springrts.ai.oo.*;
import com.springrts.ai.oo.clb.*;

public class OrdersEngine
{
    private OOAICallback _callback;

    private MyJavaAI _ai;

    private BaUnits _baUnits;

    private BaResources _baResources;

    private EconomyManager _economyManager;

    public OrdersEngine(OOAICallback callback, MyJavaAI ai, BaUnits baUnits, BaResources baResources, EconomyManager economyManager)
    {
        _callback = callback;
        _ai = ai;
        _baUnits = baUnits;
        _baResources = baResources;
        _economyManager = economyManager;
    }

    public void DecideWhatToDoWithUnit(Unit unit)
    {
        _ai.SendTextMessage("Deciding what to do with Unit " + unit.getDef().getHumanName());

        if(unit.getDef() == _baUnits.ArmCommander)
        {
            if(_economyManager.MetalPercent < .20)
            {
                _ai.SendTextMessage("Out of metal, building mex.");
                AIFloat3 closestSpot = _economyManager.ClosestAvailableMetalSpot(unit.getPos());
                _economyManager.MarkMetalPositionAsTaken(closestSpot);
                unit.build(_baUnits.ArmMetalExtractor, closestSpot, 0, (short) 0, Integer.MAX_VALUE);
            }
            else if(_economyManager.EnergyPercent < .20)
            {
                _ai.SendTextMessage("Out of energy, building solar.");
                unit.build(_baUnits.ArmSolarPlant, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
            }
            else
            {
                _ai.SendTextMessage("Economy is good, building Kbot lab.");
                unit.build(_baUnits.ArmKbotLab, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
            }
        }
        else if(unit.getDef() == _baUnits.ArmKbotLab)
        {
            unit.build(_baUnits.ArmPeeWee, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
            _ai.SendTextMessage("Don't know what to do with unit: " + unit.getDef().getHumanName());
        }
    }
}
