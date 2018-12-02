package myjavaai;

import java.util.List;
import com.springrts.ai.oo.clb.*;

public class BaUnits
{
    public UnitDef ArmSolarPlant;

    public UnitDef ArmMetalExtractor;

    public BaUnits(List<UnitDef> unitDefs)
    {
        for(UnitDef unitDef : unitDefs)
        {
            String name = unitDef.getName();
            
            switch (name)
            {
                case "armsolar": ArmSolarPlant = unitDef; break;
                case "armmex": ArmMetalExtractor = unitDef; break;
            }
        }
    }
}
