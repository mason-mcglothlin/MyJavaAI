package myjavaai;

import com.springrts.ai.oo.AIFloat3;
import com.springrts.ai.oo.clb.*;

import java.util.ArrayList;
import java.util.List;

public class EnemyLocationManager
{
    private List<Unit> _knownEnemiesByRadar;

    private List<Unit> _knownEnemiesByLOS;

    private MyJavaAI _ai;

    public EnemyLocationManager(MyJavaAI ai)
    {
        _knownEnemiesByRadar = new ArrayList<>();
        _knownEnemiesByLOS = new ArrayList<>();
        _ai = ai;
    }

    public void AddEnemyPositionFromRadar(Unit unit)
    {
        _ai.SendTextMessage("Adding enemy position from Radar. Unit: " + unit.getDef().getName() + " Health: " + unit.getHealth());
        _knownEnemiesByRadar.add(unit);
    }

    public void RemoveEnemyPositionFromRadar(Unit unit)
    {
        _knownEnemiesByRadar.remove(unit);
    }

    public void AddEnemyPositionFromLOS(Unit unit)
    {
        _ai.SendTextMessage("Adding enemy position from LOS. Unit: " + unit.getDef().getName() + " Health: " + unit.getHealth());
        _knownEnemiesByLOS.add(unit);
    }

    public void RemoveEnemyPositionFromLOS(Unit unit)
    {
        _knownEnemiesByLOS.remove(unit);
    }

    public void RemoveDestroyedEnemy(Unit unit)
    {
        if(_knownEnemiesByLOS.contains(unit))
        {
            _knownEnemiesByLOS.remove(unit);
        }

        if(_knownEnemiesByRadar.contains(unit))
        {
            _knownEnemiesByRadar.remove(unit);
        }
    }

    public boolean AreEnemyLocationsKnown()
    {
        if(!_knownEnemiesByLOS.isEmpty() || !_knownEnemiesByRadar.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public AIFloat3 GetClosestEnemyPositionToLocation(AIFloat3 location)
    {
        List<Unit> allUnits = new ArrayList<>();
        allUnits.addAll(_knownEnemiesByLOS);
        allUnits.addAll(_knownEnemiesByRadar);

        AIFloat3 closestPosition = null;
        double closestDistance = 0;

        for (Unit unit : allUnits)
        {
            if(closestPosition == null)
            {
                 closestPosition = unit.getPos();
                 closestDistance = UtilityFunctions.CalculateDistance(location, closestPosition);
            }
            else
            {
                AIFloat3 position = unit.getPos();
                double distance = UtilityFunctions.CalculateDistance(location, position);

                if(distance < closestDistance)
                {
                    closestPosition = position;
                    closestDistance = distance;
                }
            }

            _ai.SendTextMessage("Returning enemy " + unit.getDef().getHumanName() + " with health " + unit.getHealth() + " at " + unit.getPos() + " to attack from radar.");
        }

        return closestPosition;
    }

    public String GetStatus()
    {
        return "Known Enemies by Radar: " + _knownEnemiesByRadar.size() + " Known Enemies by Line of Sight: " + _knownEnemiesByLOS.size();
    }

    public void Update()
    {
        /*
        If an enemy is destroyed and it wasn't by me (for example, friendly fire or some other team killed them),
        then they will still be in my cache even though they're dead. Their positions become 0, 0, 0. That's not useful.
        So remove them if they're dead.
        */
        try
        {
            List<Unit> unitsToRemoveFromRadar = new ArrayList<>();

            for (Unit unit : _knownEnemiesByRadar)
            {
                if(unit.getHealth() == 0)
                {
                    _ai.SendTextMessage("Removing enemy from radar because it's presumed dead.");
                    unitsToRemoveFromRadar.add(unit);
                }
            }

            for(Unit unit : unitsToRemoveFromRadar)
            {
                _knownEnemiesByRadar.remove(unit);
            }

            List<Unit> unitsToRemoveFromLOS = new ArrayList<>();

            for (Unit unit : _knownEnemiesByLOS)
            {
                if(unit.getHealth() == 0)
                {
                    _ai.SendTextMessage("Removing enemy from LOS because it's presumed dead.");
                    unitsToRemoveFromLOS.add(unit);
                }
            }

            for(Unit unit : unitsToRemoveFromLOS)
            {
                _knownEnemiesByLOS.remove(unit);
            }
        }
        catch (Exception exception)
        {
            _ai.SendTextMessage("Failure to remove presumed dead unit: " + exception.toString());
        }
    }
}
