
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
		this.translate(v.getX(),v.getY());
	}
	
	public void translate(float x,float y)
	{
		this.origin.translate(x,y);
	}
	
	public void setOrigin(Vec2D v)
	{
		this.setOrigin(v.getX(),v.getY());
	}
	
	public void setOrigin(float x,float y)
	{
		this.origin.setX(x);
		this.origin.setY(y);
	}
	
	public Vec2D getOrigin()
	{
		return this.origin;
	}
	
}
