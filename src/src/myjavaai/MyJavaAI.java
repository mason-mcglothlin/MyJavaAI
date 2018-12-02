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
	public int init(int arg0, OOAICallback callback)
	{
		_callBack = callback;
		_baUnits = new BaUnits(callback.getUnitDefs(), this);
		SendTextMessage("Init called.");
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

	private void DecideWhatToDoWithUnit(Unit unit)
	{
		SendTextMessage("Deciding what to do with Unit " + unit.getDef().getHumanName());

		if(unit.getDef().getName().equals("armcom"))
		{
			Resource Metal = _callBack.getResourceByName("Metal");
			Resource Energy = _callBack.getResourceByName("Energy");
			float currentMetal = _callBack.getEconomy().getCurrent(Metal);
			float currentEnergy = _callBack.getEconomy().getCurrent(Energy);

			if(currentMetal == 0)
			{
				SendTextMessage("Out of metal, building mex.");
				AIFloat3 closestspot = closestMetalSpot(_commander.getPos());
				_commander.build(_baUnits.ArmMetalExtractor, closestspot, 0, (short) 0, Integer.MAX_VALUE);
			}
			else if(currentEnergy == 0)
			{
				SendTextMessage("Out of energy, building solar.");
				_commander.build(_baUnits.ArmSolarPlant, _commander.getPos(), 0, (short) 0, Integer.MAX_VALUE);
			}
			else
			{
				SendTextMessage("Economy is good, I dont know what to do.");
				_commander.build(_baUnits.ArmKbotLab, _commander.getPos(), 0, (short) 0, Integer.MAX_VALUE);
			}
		}
	}

	@Override
	public int unitFinished(Unit unit) {
		SendTextMessage("Unit finished");

		if(unit.getDef().getName().equals("armcom")) {
			_callBack.getGame().sendTextMessage("Got the _commander", 0);
			_commander = unit;
		}

		DecideWhatToDoWithUnit(unit);

		return super.unitFinished(unit);
	}

	@Override
	public int unitGiven(Unit arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return super.unitGiven(arg0, arg1, arg2);
	}

	@Override
	public int unitIdle(Unit unit)
	{
		AIFloat3 position = unit.getPos();
		SendTextMessage("Unit " + unit.getDef().getHumanName() + " is idle at X: " + position.x + " Y: " + position.y + " Z: " + position.z);
		DecideWhatToDoWithUnit(unit);
		return super.unitIdle(unit);
	}

	@Override
	public int unitMoveFailed(Unit unit) {
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

	public void SendTextMessage(String message){
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
	public int weaponFired(Unit arg0, WeaponDef arg1) {
		// TODO Auto-generated method stub
		return super.weaponFired(arg0, arg1);
	}
}