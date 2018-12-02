package myjavaai;

import java.util.List;

import com.springrts.ai.oo.*;
import com.springrts.ai.oo.clb.*;

public class MyJavaAI extends AbstractOOAI
{
	private OOAICallback _callBack;

	private BaUnits _baUnits;

	List<AIFloat3> _metalPositions;

	@Override
	public int commandFinished(Unit arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return super.commandFinished(arg0, arg1, arg2);
	}

	@Override
	public int enemyCreated(Unit arg0) {
		// TODO Auto-generated method stub
		return super.enemyCreated(arg0);
	}

	@Override
	public int enemyDamaged(Unit arg0, Unit arg1, float arg2, AIFloat3 arg3, WeaponDef arg4, boolean arg5) {
		// TODO Auto-generated method stub
		return super.enemyDamaged(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public int enemyDestroyed(Unit arg0, Unit arg1) {
		// TODO Auto-generated method stub
		return super.enemyDestroyed(arg0, arg1);
	}

	@Override
	public int enemyEnterLOS(Unit arg0) {
		// TODO Auto-generated method stub
		return super.enemyEnterLOS(arg0);
	}

	@Override
	public int enemyEnterRadar(Unit arg0) {
		// TODO Auto-generated method stub
		return super.enemyEnterRadar(arg0);
	}

	@Override
	public int enemyFinished(Unit arg0) {
		// TODO Auto-generated method stub
		return super.enemyFinished(arg0);
	}

	@Override
	public int enemyLeaveLOS(Unit arg0) {
		// TODO Auto-generated method stub
		return super.enemyLeaveLOS(arg0);
	}

	@Override
	public int enemyLeaveRadar(Unit arg0) {
		// TODO Auto-generated method stub
		return super.enemyLeaveRadar(arg0);
	}

	@Override
	public int init(int arg0, OOAICallback callback) {
		_baUnits = new BaUnits(callback.getUnitDefs());
		_callBack = callback;
		return 0;
	}

	@Override
	public int load(String arg0) {
		// TODO Auto-generated method stub
		return super.load(arg0);
	}

	@Override
	public int luaMessage(String arg0) {
		// TODO Auto-generated method stub
		return super.luaMessage(arg0);
	}

	@Override
	public int message(int arg0, String arg1) {
		// TODO Auto-generated method stub
		return super.message(arg0, arg1);
	}

	@Override
	public int playerCommand(List<Unit> arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return super.playerCommand(arg0, arg1, arg2);
	}

	@Override
	public int release(int arg0) {
		// TODO Auto-generated method stub
		return super.release(arg0);
	}

	@Override
	public int save(String arg0) {
		// TODO Auto-generated method stub
		return super.save(arg0);
	}

	@Override
	public int seismicPing(AIFloat3 arg0, float arg1) {
		// TODO Auto-generated method stub
		return super.seismicPing(arg0, arg1);
	}

	@Override
	public int unitCaptured(Unit arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return super.unitCaptured(arg0, arg1, arg2);
	}

	@Override
	public int unitCreated(Unit arg0, Unit arg1) {
		// TODO Auto-generated method stub
		return super.unitCreated(arg0, arg1);
	}

	@Override
	public int unitDamaged(Unit arg0, Unit arg1, float arg2, AIFloat3 arg3, WeaponDef arg4, boolean arg5) {
		// TODO Auto-generated method stub
		return super.unitDamaged(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public int unitDestroyed(Unit arg0, Unit arg1) {
		// TODO Auto-generated method stub
		return super.unitDestroyed(arg0, arg1);
	}

	Unit _commander;

	@Override
	public int unitFinished(Unit unit) {
		if(unit.getDef().getName().equals("armcom")) {
			_callBack.getGame().sendTextMessage("Got the _commander", 0);
			_commander = unit;
		}
		// TODO Auto-generated method stub
		return super.unitFinished(unit);
	}

	@Override
	public int unitGiven(Unit arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return super.unitGiven(arg0, arg1, arg2);
	}

	@Override
	public int unitIdle(Unit arg0) {
		// TODO Auto-generated method stub
		return super.unitIdle(arg0);
	}

	@Override
	public int unitMoveFailed(Unit arg0) {
		// TODO Auto-generated method stub
		return super.unitMoveFailed(arg0);
	}

	public void checkForMetal()
	{
		SendTextMessage("Checking for resources...");
		Resource metal = _callBack.getResourceByName("Metal");
		_metalPositions = _callBack.getMap().getResourceMapSpotsPositions(metal);

		for (AIFloat3 metalSpot : _metalPositions) {
			SendTextMessage("Metal Spot at X: "+metalSpot.x + ", Y: "+metalSpot.y +", Z: "+metalSpot.z);
		}
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

	public void SendTextMessage(String message){
		_callBack.getGame().sendTextMessage(message, 0);
	}

	@Override
	public int update(int frame) {
		if(frame == 0)
		{
			checkForMetal();
			_commander.build(_baUnits.ArmSolarPlant, _commander.getPos(), 0, (short) 0, Integer.MAX_VALUE);
		}
		else if (frame == 600)
		{
			AIFloat3 closestspot=closestMetalSpot(_commander.getPos());
			_commander.build(_baUnits.ArmMetalExtractor, closestspot, 0, (short) 0, Integer.MAX_VALUE);
		}

		return super.update(frame);
	}

	@Override
	public int weaponFired(Unit arg0, WeaponDef arg1) {
		// TODO Auto-generated method stub
		return super.weaponFired(arg0, arg1);
	}
}