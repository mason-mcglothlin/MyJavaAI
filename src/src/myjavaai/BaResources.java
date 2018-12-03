package myjavaai;

import com.springrts.ai.oo.clb.*;

public class BaResources
{
    Resource Metal;

    Resource Energy;

    public BaResources(OOAICallback callback, MyJavaAI ai)
    {
        Metal = callback.getResourceByName("Metal");
        Energy = callback.getResourceByName("Energy");
    }
}
