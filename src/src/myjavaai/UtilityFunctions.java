package myjavaai;

import com.springrts.ai.oo.*;

public class UtilityFunctions
{
    public static double CalculateDistance(AIFloat3 a, AIFloat3 b)
    {
        float xDistance = a.x - b.x;
        float yDistance = a.y - b.y;
        float zDistance = a.z - b.z;
        float totalDistanceSquared = xDistance*xDistance + yDistance*yDistance + zDistance*zDistance;
        return Math.sqrt(totalDistanceSquared);
    }
}
