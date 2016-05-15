package myjavaai;

import com.springrts.ai.oo.*;
import com.springrts.ai.oo.clb.*;

public class MyJavaAI extends AbstractOOAI
{		
	@Override
	public int init(int teamId, OOAICallback callback)
	{
	    return 0;
	}
	
	@Override
	public int unitFinished(Unit unit)
	{
	    return 0;
	}
	
	@Override
	public int commandFinished (Unit arg0, int arg1, int arg2)
	{
		
		return 0;	
	}
	
	@Override
	public int enemyCreated(Unit arg0)
	{
		return 0;
	}
	
	@Override
	public int enemyDamaged(Unit arg0, Unit arg1, float arg2, AIFloat3 arg3, WeaponDef arg4, boolean arg5)
	{
		return 0;
	}
}