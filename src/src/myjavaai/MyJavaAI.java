package myjavaai;

import java.util.List;
import com.springrts.ai.oo.*;
import com.springrts.ai.oo.clb.*;
import serilogj.LoggerConfiguration;
import serilogj.events.LogEventLevel;

import static serilogj.sinks.seq.SeqSinkConfigurator.seq;

public class MyJavaAI extends AbstractOOAI
{
	private OOAICallback _callBack;

	private BaUnits _baUnits;

	private BaResources _baResources;

	private OrdersEngine _ordersEngine;

	private EconomyManager _economyManager;

	private EnemyLocationManager _enemyLocationManager;

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
	public int enemyDestroyed(Unit destroyedEnemy, Unit destroyer)
	{
		SendTextMessage("Event: enemyDestroyed UnitA: " + destroyedEnemy.getDef().getName() + " UnitB: " + destroyer.getDef().getName());
		_enemyLocationManager.RemoveDestroyedEnemy(destroyedEnemy);
		return super.enemyDestroyed(destroyedEnemy, destroyer);
	}

	@Override
	public int enemyEnterLOS(Unit unit)
	{
		SendTextMessage("Event: enemyEnterLOS Unit: " + unit.getDef().getName());
		_enemyLocationManager.AddEnemyPositionFromLOS(unit);
		return super.enemyEnterLOS(unit);
	}

	@Override
	public int enemyEnterRadar(Unit unit)
	{
		SendTextMessage("Event: enemyEnterRadar Unit: " + unit.getDef().getName());
		_enemyLocationManager.AddEnemyPositionFromRadar(unit);
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
		_enemyLocationManager.RemoveEnemyPositionFromLOS(unit);
		return super.enemyLeaveLOS(unit);
	}

	@Override
	public int enemyLeaveRadar(Unit unit)
	{
		SendTextMessage("Event: enemyLeaveRadar Unit: " + unit.getDef().getName());
		_enemyLocationManager.RemoveEnemyPositionFromRadar(unit);
		return super.enemyLeaveRadar(unit);
	}

	@Override
	public int init(int arg0, OOAICallback callback)
	{
		//arg0 may be the AI's ID?
		_callBack = callback;
		SendTextMessage("Init called.");
		_baUnits = new BaUnits(callback, this);
		_baResources = new BaResources(callback, this);
		_economyManager = new EconomyManager(callback, this, _baResources);
		_enemyLocationManager = new EnemyLocationManager(this);
		_ordersEngine = new OrdersEngine(callback, this, _baUnits, _baResources, _economyManager, _enemyLocationManager);

		try
		{
			serilogj.Log.setLogger(new LoggerConfiguration()
					.writeTo(seq("http://localhost:5341/"))
					.setMinimumLevel(LogEventLevel.Verbose)
					.createLogger());

			serilogj.Log.information("AI and Serilog initialized");
		}
		catch (Exception exception)
		{
			SendTextMessage("Error initializing Serilog: " + exception.toString());
		}

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

	@Override
	public int unitFinished(Unit unit)
	{
		SendTextMessage("Event: unitFinished Unit: " + unit.getDef().getName());
		_ordersEngine.DecideWhatToDoWithUnit(unit);
		return super.unitFinished(unit);
	}

	@Override
	public int unitGiven(Unit unit, int arg1, int arg2)
	{
		SendTextMessage("Event: unitGiven Unit: " + unit.getDef().getName() + " Arg1: " + arg1 + " Arg2: " + arg2);
		//do I need to verify if the unit was given to me? Probably.
		_ordersEngine.DecideWhatToDoWithUnit(unit);
		return super.unitGiven(unit, arg1, arg2);
	}

	@Override
	public int unitIdle(Unit unit)
	{
		SendTextMessage("Event: unitIdle Unit: " + unit.getDef().getName());
		_ordersEngine.DecideWhatToDoWithUnit(unit);
		return super.unitIdle(unit);
	}

	@Override
	public int unitMoveFailed(Unit unit)
	{
		SendTextMessage("Event: unitMoveFailed Unit: " + unit.getDef().getName());
		return super.unitMoveFailed(unit);
	}

	public void SendTextMessage(String message)
	{
		serilogj.Log.verbose("Received text message: " + message);
		_callBack.getGame().sendTextMessage(message, 0);
	}

	@Override
	public int update(int frame)
	{
		_economyManager.UpdateEconomy();
		_enemyLocationManager.Update();

		if(frame % 600 == 0)
		{
			SendTextMessage(_enemyLocationManager.GetStatus());
			SendTextMessage(_economyManager.GetStatus());
		}


		return super.update(frame);
	}

	@Override
	public int weaponFired(Unit unit, WeaponDef weapon)
	{
		SendTextMessage("Event: weaponFired Unit: " + unit.getDef().getName() + " Weapon: " + weapon);
		return super.weaponFired(unit, weapon);
	}
}
