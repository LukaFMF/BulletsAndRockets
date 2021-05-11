public class Rect2D
{
	private Vec2D position;
	private float width;
	private float height;
	
	Rect2D(Vec2D v,float w,float h)
	{
		this.position = v;
		this.width = w;
		this.height = h;
	}
	
	Rect2D(float x,float y,float w,float h)
	{
		this(new Vec2D(x,y),w,h);
	}

	public void translate(Vec2D v)
	{
		this.position.translate(v);
	}
	
	public void translate(float x,float y)
	{
		this.position.translate(x,y);
	}
	
	public Vec2D getPos()
	{
		return this.position;
	}
	
	public float getWidth()
	{
		return this.width;
	}
	
	public float getHeight()
	{
		return this.height;
	}
}

