
public class HelperFuncs 
{
	public static float clamp(float value,float min,float max)
	{
		return Math.max(Math.min(max,value),min);
	}
}
