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

    public UnitDef ArmWarrior;

    public UnitDef ArmConstructionKbot;

    public UnitDef ArmAdvancedKbotLab;

    public UnitDef ArmMaverick;

    public UnitDef ArmFatBoy;

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
                case "armwar": ArmWarrior = unitDef; break;
                case "armck": ArmConstructionKbot = unitDef; break;
                case "armalab": ArmAdvancedKbotLab = unitDef; break;
                case "armmav": ArmMaverick = unitDef; break;
                case "armfboy": ArmFatBoy = unitDef; break;
                default: ai.SendTextMessage("Unknown unit. Name: " + unitDef.getName()+ " Friendly Name: " + unitDef.getHumanName());
            }
        }
    }
}
