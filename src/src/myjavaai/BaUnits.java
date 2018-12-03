package myjavaai;

import java.util.List;
import com.springrts.ai.oo.clb.*;

public class BaUnits
{
    public UnitDef ArmCommander;

    public UnitDef ArmSolarPlant;

    public UnitDef ArmMetalExtractor;

    public UnitDef ArmKbotLab;

    public UnitDef ArmPeeWee;

    public BaUnits(OOAICallback callback, MyJavaAI ai)
    {
        List<UnitDef> unitDefs = callback.getUnitDefs();

        for(UnitDef unitDef : unitDefs)
        {
            String name = unitDef.getName();

            switch (name)
            {
                case "armcom": ArmCommander = unitDef; break;
                case "armsolar": ArmSolarPlant = unitDef; break;
                case "armmex": ArmMetalExtractor = unitDef; break;
                case "armlab": ArmKbotLab = unitDef; break;
                case "armpw": ArmPeeWee = unitDef; break;
                default: ai.SendTextMessage("Unknown unit. Name: " + unitDef.getName()+ " Friendly Name: " + unitDef.getHumanName());
            }
        }
    }
}
