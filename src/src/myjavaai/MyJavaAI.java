package myjavaai;

import java.util.List;
import com.springrts.ai.oo.*;
import com.springrts.ai.oo.clb.*;

public class MyJavaAI extends AbstractOOAI
{
	private OOAICallback _callBack;

	private BaUnits _baUnits;

	private Unit _commander;

	List<AIFloat3> _metalPositions;

	private String FormatPosition(AIFloat3 position)
	{
		return "X: " + position.x + " Y: " + position.y + " Z: " + position.z;
	}

	@Override
	public int commandFinished(Unit unit, int arg1, int arg2)
	{
		SendTextMessage("Event: commandFinished Unit: " + unit.getDef().getName() + " Arg1: " + arg1 + " Arg2: " + arg2);
		return super.commandFinished(unit, arg1, arg2);
	}

	@Override
	public int enemyCreated(Unit unit)
	{
		SendTextMessage("Event: enemyCreated Unit: " + unit.getDef().getName());
		return super.enemyCreated(unit);
	}

	@Override
	public int enemyDamaged(Unit unitA, Unit unitB, float arg2, AIFloat3 position, WeaponDef weapon, boolean arg5)
	{
		SendTextMessage("Event: enemyDamaged UnitA: " + unitA.getDef().getName() + " UnitB: " + unitB + " Arg2: " + arg2 + " Position: " + FormatPosition(position) + " Weapon: " + weapon + " Arg5: " + arg5);
		return super.enemyDamaged(unitA, unitB, arg2, position, weapon, arg5);
	}

	@Override
	public int enemyDestroyed(Unit unitA, Unit unitB)
	{
		SendTextMessage("Event: enemyDestroyed UnitA: " + unitA.getDef().getName() + " UnitB: " + unitB);
		return super.enemyDestroyed(unitA, unitB);
	}

	@Override
	public int enemyEnterLOS(Unit unit)
	{
		SendTextMessage("Event: enemyEnterLOS Unit: " + unit.getDef().getName());
		return super.enemyEnterLOS(unit);
	}

	@Override
	public int enemyEnterRadar(Unit unit)
	{
		SendTextMessage("Event: enemyEnterRadar Unit: " + unit.getDef().getName());
		return super.enemyEnterRadar(unit);
	}

	@Override
	public int enemyFinished(Unit unit)
	{
		SendTextMessage("Event: enemyFinished Unit: " + unit.getDef().getName());
		return super.enemyFinished(unit);
	}

	@Override
	public int enemyLeaveLOS(Unit unit)
	{
		SendTextMessage("Event: enemyLeaveLOS Unit: " + unit.getDef().getName());
		return super.enemyLeaveLOS(unit);
	}

	@Override
	public int enemyLeaveRadar(Unit unit)
	{
		SendTextMessage("Event: enemyLeaveRadar Unit: " + unit.getDef().getName());
		return super.enemyLeaveRadar(unit);
	}

	@Override
	public int init(int arg0, OOAICallback callback)
	{
		//arg0 may be the AI's ID?
		_callBack = callback;
		_baUnits = new BaUnits(callback.getUnitDefs(), this);
		SendTextMessage("Init called.");
		return 0;
	}

	@Override
	public int load(String arg0)
	{
		SendTextMessage("Event: load Arg0: " + arg0);
		return super.load(arg0);
	}

	@Override
	public int luaMessage(String arg0)
	{
		SendTextMessage("Event: luaMessage Arg0: " + arg0);
		return super.luaMessage(arg0);
	}

	@Override
	public int message(int arg0, String arg1)
	{
		SendTextMessage("Event: message Arg0: " + arg0 + " Arg1: " + arg1);
		return super.message(arg0, arg1);
	}

	@Override
	public int playerCommand(List<Unit> unit, int arg1, int arg2)
	{
		SendTextMessage("Event: playerCommand Unit: " + unit.size() + " Arg1: " + arg1 + " Arg2: " + arg2);
		return super.playerCommand(unit, arg1, arg2);
	}

	@Override
	public int release(int arg0)
	{
		SendTextMessage("Event: release Arg0: " + arg0);
		return super.release(arg0);
	}

	@Override
	public int save(String arg0)
	{
		SendTextMessage("Event: save Arg0: " + arg0);
		return super.save(arg0);
	}

	@Override
	public int seismicPing(AIFloat3 position, float arg1)
	{
		//arg1 may be size?
		SendTextMessage("Event: seismicPing Position: " + FormatPosition(position) + " Arg1: " + arg1);
		return super.seismicPing(position, arg1);
	}

	@Override
	public int unitCaptured(Unit unit, int arg1, int arg2)
	{
		SendTextMessage("Event: unitCaptured Unit: " + unit.getDef().getName() + " Arg1: " + arg1 + " Arg2" + arg2);
		return super.unitCaptured(unit, arg1, arg2);
	}

	@Override
	public int unitCreated(Unit createdUnit, Unit creator)
	{
		SendTextMessage("Event: unitCreated CreatedUnit: " + createdUnit.getDef().getName() + " Creator: " + creator.getDef().getName());
		return super.unitCreated(createdUnit, creator);
	}

	@Override
	public int unitDamaged(Unit unitA, Unit unitB, float arg2, AIFloat3 position, WeaponDef weapon, boolean arg5)
	{
		SendTextMessage("Event: enemyDamaged UnitA: " + unitA.getDef().getName() + " UnitB: " + unitB + " Arg2: " + arg2 + " Position: " + FormatPosition(position) + " Weapon: " + weapon + " Arg5: " + arg5);
		return super.unitDamaged(unitA, unitB, arg2, position, weapon, arg5);
	}

	@Override
	public int unitDestroyed(Unit unitA, Unit unitB)
	{
		SendTextMessage("Event: unitDestroyed UnitA: " + unitA.getDef().getName() + " UnitB: " + unitB);
		return super.unitDestroyed(unitA, unitB);
	}

	private void DecideWhatToDoWithUnit(Unit unit)
	{
		SendTextMessage("Deciding what to do with Unit " + unit.getDef().getHumanName());

		if(unit.getDef() == _baUnits.ArmCommander)
		{
			Resource Metal = _callBack.getResourceByName("Metal");
			Resource Energy = _callBack.getResourceByName("Energy");

			Economy economy = _callBack.getEconomy();
			float currentMetal = economy.getCurrent(Metal);
			float currentEnergy = economy.getCurrent(Energy);

			float metalStorage = economy.getStorage(Metal);
			float energyStorage = economy.getStorage(Energy);

			float metalPercent = currentMetal / metalStorage;
			float energyPercent = currentEnergy / energyStorage;

			if(metalPercent < .20)
			{
				SendTextMessage("Out of metal, building mex.");
				AIFloat3 closestSpot = closestMetalSpot(unit.getPos());
				unit.build(_baUnits.ArmMetalExtractor, closestSpot, 0, (short) 0, Integer.MAX_VALUE);
			}
			else if(energyPercent < .20)
			{
				SendTextMessage("Out of energy, building solar.");
				unit.build(_baUnits.ArmSolarPlant, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
			}
			else
			{
				SendTextMessage("Economy is good, building Kbot lab.");
				unit.build(_baUnits.ArmKbotLab, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
			}
		}
		else if(unit.getDef() == _baUnits.ArmKbotLab)
		{
			unit.build(_baUnits.ArmPeeWee, unit.getPos(), 0, (short) 0, Integer.MAX_VALUE);
			SendTextMessage("Don't know what to do with unit: " + unit.getDef().getHumanName());
		}
	}

	@Override
	public int unitFinished(Unit unit)
	{
		SendTextMessage("Event: unitFinished Unit: " + unit.getDef().getName());

		if(unit.getDef().getName().equals("armcom"))
		{
			SendTextMessage("Got the commander");
			_commander = unit;
		}

		DecideWhatToDoWithUnit(unit);

		return super.unitFinished(unit);
	}

	@Override
	public int unitGiven(Unit unit, int arg1, int arg2)
	{
		SendTextMessage("Event: unitGiven Unit: " + unit.getDef().getName() + " Arg1: " + arg1 + " Arg2: " + arg2);
		return super.unitGiven(unit, arg1, arg2);
	}

	@Override
	public int unitIdle(Unit unit)
	{
		SendTextMessage("Event: unitIdle Unit: " + unit.getDef().getName());
		DecideWhatToDoWithUnit(unit);
		return super.unitIdle(unit);
	}

	@Override
	public int unitMoveFailed(Unit unit)
	{
		SendTextMessage("Event: unitMoveFailed Unit: " + unit.getDef().getName());
		return super.unitMoveFailed(unit);
	}

	public void checkForMetal()
	{
		SendTextMessage("Checking for resources...");
		Resource metal = _callBack.getResourceByName("Metal");
		_metalPositions = _callBack.getMap().getResourceMapSpotsPositions(metal);
	}

	public float CalculateDistance(AIFloat3 a, AIFloat3 b)
	{
		float xDistance = a.x - b.x;
		float yDistance = a.y - b.y;
		float zDistance = a.z - b.z;
		float totalDistanceSquared = xDistance*xDistance + yDistance*yDistance + zDistance*zDistance;
		return totalDistanceSquared;
	}

	public AIFloat3 closestMetalSpot(AIFloat3 unitposition)
	{
		AIFloat3 closestspot=null;
		for (AIFloat3 metalspot : _metalPositions) {
			if (closestspot==null)
			{
				closestspot=metalspot;
			}
			else if(CalculateDistance(metalspot, unitposition) < CalculateDistance(closestspot, unitposition) && metalspot.hashCode()!=unitposition.hashCode())
			{
				closestspot=metalspot;
			}
		}

		return closestspot;

	}

	public void SendTextMessage(String message)
	{
		_callBack.getGame().sendTextMessage(message, 0);
	}

	@Override
	public int update(int frame)
	{
		/*
		if(frame == 0)
		{
			checkForMetal();
			DecideWhatToDoWithUnit(_commander);
		}*/

		return super.update(frame);
	}

	@Override
	public int weaponFired(Unit unit, WeaponDef weapon)
	{
		SendTextMessage("Event: weaponFired Unit: " + unit.getDef().getName() + " Weapon: " + weapon);
		return super.weaponFired(unit, weapon);
	}
}
