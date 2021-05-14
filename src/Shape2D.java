
public abstract class Shape2D 
{
	protected Vec2D origin;
	
	Shape2D(Vec2D origin)
	{
		this.origin = origin;
	}
	
	Shape2D(float x,float y)
	{
		this(new Vec2D(x,y));
	}
	
	public void translate(Vec2D v)
	{
		this.origin.translate(v);
	}
	
	public void translate(float x,float y)
	{
		this.origin.translate(x,y);
	}
	
	public Vec2D getOrigin()
	{
		return this.origin;
	}
}
