package myjavaai;

import com.springrts.ai.oo.*;
import com.springrts.ai.oo.clb.*;

import java.util.ArrayList;

public class OrdersEngine
{
    private OOAICallback _callback;

    private MyJavaAI _ai;

    private BaUnits _baUnits;

    private BaResources _baResources;

    private EconomyManager _economyManager;

    private EnemyLocationManager _enemyLocationManager;

    private float _desiredEconomyPercent = .50f;

    private AIFloat3 _startingLocation;

    private float maxDistanceToTravelForMetalFromStartPosition = 2500f;


    public OrdersEngine(OOAICallback callback, MyJavaAI ai, BaUnits baUnits, BaResources baResources, EconomyManager economyManager, EnemyLocationManager enemyLocationManager)
    {
        _callback = callback;
        _ai = ai;
        _baUnits = baUnits;
        _baResources = baResources;
        _economyManager = economyManager;
        _enemyLocationManager = enemyLocationManager;
        _startingLocation = callback.getMap().getStartPos();
    }

    public void DecideWhatToDoWithUnit(Unit unit)
    {
        try
        {

            // Increasing pos.x moves you East
            // Decreasing pos.x moves you West
            // Increasing pos.y did nothing
            // Decreasing pos.y did nothing
            // Increasing pos.z moves you South
            // Decreasing pos.z moves you North

            _ai.SendTextMessage("Deciding what to do with Unit " + unit.getDef().getHumanName());
            UnitDef unitDef = unit.getDef();

            if (unitDef == _baUnits.ArmCommander) {
                boolean needsMetal = _economyManager.MetalPercent < _desiredEconomyPercent;
                boolean needsEnergy = _economyManager.EnergyPercent < _desiredEconomyPercent;

                AIFloat3 closestMetalSpot = _economyManager.ClosestAvailableMetalSpot(unit.getPos());
                double distanceToClosestMetalSpot = UtilityFunctions.CalculateDistance(_startingLocation, closestMetalSpot);
                _ai.SendTextMessage("Distance to closest metal spot from starting spot: " + distanceToClosestMetalSpot);

                if (needsMetal && _economyManager.MetalPercent < _economyManager.EnergyPercent && distanceToClosestMetalSpot < maxDistanceToTravelForMetalFromStartPosition) {
                    _economyManager.MarkMetalPositionAsTaken(closestMetalSpot);
                    unit.build(_baUnits.ArmMetalExtractor, closestMetalSpot, 0, (short) 0, Integer.MAX_VALUE);
                    serilogj.Log.information("Unit {Unit} decided to build {BuildUnit}", unit.getDef().getHumanName(), _baUnits.ArmMetalExtractor.getHumanName());
                }
                else if(needsMetal && _economyManager.MetalPercent < _economyManager.EnergyPercent)
                {
                    unit.build(_baUnits.ArmEnergyConverter, unit.getPos(), 0, (short)0, Integer.MAX_VALUE);
                    serilogj.Log.information("Unit {Unit} decided to build {BuildUnit}", unit.getDef().getHumanName(), _baUnits.ArmEnergyConverter.getHumanName());
                }
                else if (needsEnergy)
                {
                    unit.build(_baUnits.ArmSolarPlant, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
                    serilogj.Log.information("Unit {Unit} decided to build {BuildUnit}", unit.getDef().getHumanName(), _baUnits.ArmSolarPlant.getHumanName());
                }
                else
                {
                    unit.build(_baUnits.ArmKbotLab, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
                    serilogj.Log.information("Unit {Unit} decided to build {BuildUnit}", unit.getDef().getHumanName(), _baUnits.ArmKbotLab.getHumanName());
                }
            }
            else if (unit.getDef() == _baUnits.ArmConstructionKbot)
            {
                boolean needsMetal = _economyManager.MetalPercent < _desiredEconomyPercent;
                boolean needsEnergy = _economyManager.EnergyPercent < _desiredEconomyPercent;

                if (needsMetal && _economyManager.MetalPercent < _economyManager.EnergyPercent)
                {
                    AIFloat3 closestSpot = _economyManager.ClosestAvailableMetalSpot(unit.getPos());
                    _economyManager.MarkMetalPositionAsTaken(closestSpot);
                    unit.build(_baUnits.ArmMetalExtractor, closestSpot, 0, (short) 0, Integer.MAX_VALUE);
                    serilogj.Log.information("Unit {Unit} decided to build {BuildUnit}", unit.getDef().getHumanName(), _baUnits.ArmMetalExtractor.getHumanName());

                }
                else if (needsMetal && _economyManager.MetalPercent < _economyManager.EnergyPercent)
                {
                    unit.build(_baUnits.ArmEnergyConverter, unit.getPos(), 0, (short)0, Integer.MAX_VALUE);
                    serilogj.Log.information("Unit {Unit} decided to build {BuildUnit}", unit.getDef().getHumanName(), _baUnits.ArmEnergyConverter.getHumanName());
                }
                else if (needsEnergy)
                {
                    unit.build(_baUnits.ArmSolarPlant, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
                    serilogj.Log.information("Unit {Unit} decided to build {BuildUnit}", unit.getDef().getHumanName(), _baUnits.ArmSolarPlant.getHumanName());
                }
                else
                {
                    ArrayList<UnitDef> options = new ArrayList<>();
                    options.add(_baUnits.ArmKbotLab);
                    options.add(_baUnits.ArmAdvancedKbotLab);
                    int random = (int) (Math.random() * options.size());
                    UnitDef selection = options.get(random);
                    unit.build(selection, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
                    serilogj.Log.information("Unit {Unit} decided to build {BuildUnit}", unit.getDef().getHumanName(), selection.getHumanName());
                }
            }
            else if (unitDef == _baUnits.ArmKbotLab)
            {
                ArrayList<UnitDef> options = new ArrayList<>();
                options.add(_baUnits.ArmWarrior);
                options.add(_baUnits.ArmPeeWee);
                options.add(_baUnits.ArmConstructionKbot);
                int random = (int) (Math.random() * options.size());
                UnitDef selection = options.get(random);
                unit.build(selection, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
            } else if (unitDef == _baUnits.ArmAdvancedKbotLab) {
                ArrayList<UnitDef> options = new ArrayList<>();
                options.add(_baUnits.ArmMaverick);
                options.add(_baUnits.ArmFatBoy);
                int random = (int) (Math.random() * options.size());
                UnitDef selection = options.get(random);
                unit.build(selection, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
            } else if (unitDef == _baUnits.ArmPeeWee || unitDef == _baUnits.ArmWarrior || unitDef == _baUnits.ArmMaverick || unitDef == _baUnits.ArmFatBoy) {
                if (_enemyLocationManager.AreEnemyLocationsKnown()) {
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

                    int translationAmount = 4000;

                    int xDelta;
                    int yDelta;
                    int zDelta;

                    if (enemyPosition.x > myPosition.x) {
                        xDelta = translationAmount;
                    } else {
                        xDelta = translationAmount * -1;
                    }

                    if (enemyPosition.y > myPosition.y) {
                        yDelta = translationAmount;
                    } else {
                        yDelta = translationAmount * -1;
                    }

                    if (enemyPosition.z > myPosition.z) {
                        zDelta = translationAmount;
                    } else {
                        zDelta = -translationAmount * -1;
                    }

                    AIFloat3 desiredPosition = new AIFloat3(myPosition.x + xDelta, myPosition.y + yDelta, myPosition.z + zDelta);
                    _ai.SendTextMessage("Moving from " + myPosition + " to " + desiredPosition + " which is toward enemy at " + enemyPosition);
                    unit.moveTo(desiredPosition, (short) 0, 0);
                } else {
                    //need to go find enemies. For now, just move in a set direction.

                    AIFloat3 currentPosition = unit.getPos();
                    AIFloat3 positionToMoveTo = new AIFloat3(currentPosition.x, currentPosition.y, currentPosition.z + 4000);
                    _ai.SendTextMessage("Moving unit from " + currentPosition + " to " + positionToMoveTo);
                    unit.moveTo(positionToMoveTo, (short) 0, 0);
                }
            }
        }
        catch (Exception exception)
        {
            serilogj.Log.error(exception, "Error giving orders to unit " + unit.getDef().getHumanName());
        }
    }
}
