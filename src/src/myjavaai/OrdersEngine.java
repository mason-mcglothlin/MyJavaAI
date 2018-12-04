package myjavaai;

import com.springrts.ai.oo.*;
import com.springrts.ai.oo.clb.*;

import java.util.ArrayList;
import java.util.List;

public class OrdersEngine
{
    private OOAICallback _callback;

    private MyJavaAI _ai;

    private BaUnits _baUnits;

    private BaResources _baResources;

    private EconomyManager _economyManager;

    private EnemyLocationManager _enemyLocationManager;

    public OrdersEngine(OOAICallback callback, MyJavaAI ai, BaUnits baUnits, BaResources baResources, EconomyManager economyManager, EnemyLocationManager enemyLocationManager)
    {
        _callback = callback;
        _ai = ai;
        _baUnits = baUnits;
        _baResources = baResources;
        _economyManager = economyManager;
        _enemyLocationManager = enemyLocationManager;
    }

    public void DecideWhatToDoWithUnit(Unit unit)
    {
        // Increasing pos.x moves you East
        // Decreasing pos.x moves you West
        // Increasing pos.y did nothing
        // Decreasing pos.y did nothing
        // Increasing pos.z moves you South
        // Decreasing pos.z moves you North

        _ai.SendTextMessage("Deciding what to do with Unit " + unit.getDef().getHumanName());

        if(unit.getDef() == _baUnits.ArmCommander)
        {
            /*AIFloat3 currentPosition = unit.getPos();
            AIFloat3 desiredPosition = new AIFloat3(currentPosition.x, currentPosition.y , currentPosition.z - 50);
            _ai.SendTextMessage("Moving from " + currentPosition + " to " + desiredPosition);
            unit.moveTo(desiredPosition, (short)0, 0);*/
            float desiredEconomyPercent = .60f;
            boolean needsMetal = _economyManager.MetalPercent < desiredEconomyPercent;
            boolean needsEnergy = _economyManager.EnergyPercent < desiredEconomyPercent;

            if(needsMetal && _economyManager.MetalPercent < _economyManager.EnergyPercent)
            {
                AIFloat3 closestSpot = _economyManager.ClosestAvailableMetalSpot(unit.getPos());
                _economyManager.MarkMetalPositionAsTaken(closestSpot);
                unit.build(_baUnits.ArmMetalExtractor, closestSpot, 0, (short) 0, Integer.MAX_VALUE);
            }
            else if(needsEnergy)
            {
                unit.build(_baUnits.ArmSolarPlant, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
            }
            else
            {
                unit.build(_baUnits.ArmKbotLab, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
            }
        }
        else if(unit.getDef() == _baUnits.ArmKbotLab)
        {
            ArrayList<UnitDef> options = new ArrayList<>();
            options.add(_baUnits.ArmWarrior);
            options.add(_baUnits.ArmPeeWee);
            int random = (int)(Math.random() * options.size());
            _ai.SendTextMessage("Generated random number: " + random);
            UnitDef selection = options.get(random);
            unit.build(selection, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
        }
        else if (unit.getDef() == _baUnits.ArmPeeWee || unit.getDef() == _baUnits.ArmWarrior)
        {
            if(_enemyLocationManager.AreEnemyLocationsKnown())
            {
                /*
                Need to implement this:
                what's my max range?
                what's the enemy's max range?
                If their max range is greater, then issue orders to get very close to them
                If my max range is greater, then issue orders to move to just outside their max range
                 */

                //For now, just move a little bit in their direction
                AIFloat3 myPosition = unit.getPos();
                AIFloat3 enemyPosition = _enemyLocationManager.GetClosestEnemyPositionToLocation(unit.getPos());

                float xDelta;
                float yDelta;
                float zDelta;

                if(enemyPosition.x > myPosition.x)
                {
                    xDelta = 50;
                }
                else
                {
                    xDelta = -50;
                }

                if(enemyPosition.y > myPosition.y)
                {
                    yDelta = 50;
                }
                else
                {
                    yDelta = -50;
                }

                if(enemyPosition.z > myPosition.z)
                {
                    zDelta = 50;
                }
                else
                {
                    zDelta = -50;
                }

                AIFloat3 desiredPosition = new AIFloat3(myPosition.x + xDelta, myPosition.y + yDelta, myPosition.z + zDelta);
                _ai.SendTextMessage("Moving from " + myPosition + " to " + desiredPosition + " which is toward enemy at " + enemyPosition);
                unit.moveTo(desiredPosition, (short) 0, 0);
            }
            else
            {
                //need to go find enemies. For now, just move in a set direction.

                AIFloat3 currentPosition = unit.getPos();
                AIFloat3 positionToMoveTo = new AIFloat3(currentPosition.x, currentPosition.y, currentPosition.z + 50);
                _ai.SendTextMessage("Moving unit from " + currentPosition + " to " + positionToMoveTo);
                unit.moveTo(positionToMoveTo, (short)0, 0);
            }
        }
    }
}
