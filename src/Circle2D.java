
public class Circle2D extends Shape2D
{
	private float radius;
	
	Circle2D(Vec2D origin,float radius)
	{
		super(origin);
		this.radius = radius;
	}
	
	Circle2D(float x,float y,float radius)
	{
		this(new Vec2D(x,y),radius);
	}
	
	public float getRadius()
	{
		return this.radius;
	}
}
