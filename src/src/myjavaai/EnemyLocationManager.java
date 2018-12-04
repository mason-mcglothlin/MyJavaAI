package myjavaai;

import com.springrts.ai.oo.AIFloat3;
import com.springrts.ai.oo.clb.*;

import java.util.ArrayList;
import java.util.List;

public class EnemyLocationManager
{
    private List<Unit> _knownEnemiesByRadar;

    private List<Unit> _knownEnemiesByLOS;

    public EnemyLocationManager()
    {
        _knownEnemiesByRadar = new ArrayList<>();
        _knownEnemiesByLOS = new ArrayList<>();
    }

    public void AddEnemyPositionFromRadar(Unit unit)
    {
        _knownEnemiesByRadar.add(unit);
    }

    public void RemoveEnemyPositionFromRadar(Unit unit)
    {
        _knownEnemiesByRadar.remove(unit);
    }

    public void AddEnemyPositionFromLOS(Unit unit)
    {
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
        //for know we'll just return the first units location

        for (Unit unit : _knownEnemiesByRadar)
        {
            return unit.getPos();
        }

        for (Unit unit : _knownEnemiesByLOS)
        {
            return unit.getPos();
        }

        return location;//this shouldn't be reached
    }

    public String GetStatus()
    {
        return "Known Enemies by Radar: " + _knownEnemiesByRadar.size() + " Known Enemies by Line of Sight: " + _knownEnemiesByLOS.size();
    }
}
