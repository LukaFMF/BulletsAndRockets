public class Rect2D extends Shape2D
{
	private float width;
	private float height;
	
	Rect2D(Vec2D origin,float width,float height)
	{
		super(origin);
		this.width = width;
		this.height = height;
	}
	
	Rect2D(float x,float y,float width,float height)
	{
		this(new Vec2D(x,y),width,height);
	}
	
	public float getWidth()
	{
		return this.width;
	}
	
	public float getHeight()
	{
		return this.height;
	}
	
	public void setWidth(float width)
	{
		this.width = width;
	}
	
	public void setHeight(float height)
	{
		this.height = height;
	}
	
	public Rect2D clone()
	{
		return new Rect2D(this.origin.clone(),this.width,this.height);
	}
}

