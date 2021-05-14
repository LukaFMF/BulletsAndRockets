
public class Collisions 
{
	public static boolean RectRect(Rect2D r1,Rect2D r2)
	{		
		final Vec2D r1Origin = r1.getOrigin();  
		final Vec2D r2Origin = r2.getOrigin();
		
		final float r1X = r1Origin.getX();
		final float r1Y = r1Origin.getY();
		final float r2X = r2Origin.getX();
		final float r2Y = r2Origin.getY();
		
		final float r1Width = r1.getWidth();
		final float r1Height = r1.getHeight();
		final float r2Width = r2.getWidth();
		final float r2Height = r2.getHeight();
		
		if(r1X <= r2X + r2Width && r1X + r1Width >= r2X && 
		   r1Y <= r2Y + r2Height && r1Y + r1Height >= r2Y)
			return true;
		return false;
	}
	
	public static boolean CircleCircle(Circle2D c1,Circle2D c2)
	{
		final float sumRadii = c1.getRadius() + c2.getRadius();
		final Vec2D origin1 = c1.getOrigin();
		final Vec2D origin2 = c2.getOrigin();
		
		return sumRadii >= origin1.distance(origin2);
	}
	
	public static boolean CircleRect(Circle2D c,Rect2D r)
	{
		final Vec2D cOrigin = c.getOrigin();  
		final Vec2D rOrigin = r.getOrigin();
		
		final float cX = cOrigin.getX();
		final float cY = cOrigin.getY();
		final float rX = rOrigin.getX();
		final float rY = rOrigin.getY();
		
		final float rWidth = r.getWidth();
		final float rHeight = r.getHeight();
		
		// ali je sredisce kroga v pravokotniku
		if(rX <= cX && cX <= rX + rWidth &&
		   rY <= cY && cY <= rY + rHeight)	
			return true;
			
		final float cRadius = c.getRadius();
		
		// najdemo tocko na pravokotniku, ki je najblizje srediscu kroga
		final float closestX = HelperFuncs.clamp(cX,rX,rX + rWidth);
		final float closestY = HelperFuncs.clamp(cY,rY,rY + rHeight);
		
		return cOrigin.distance(closestX,closestY) <= cRadius;
	}
}
