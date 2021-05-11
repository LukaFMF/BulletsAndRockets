public class Vec2D 
{
	private float x;
	private float y;
	public Vec2D(float x, float y) 
	{
		this.x = x;
		this.y = y;
		
	}
	
	public float getX() 
	{
		return x;
	}

	public float getY() 
	{
		return y;
	}

	public float distance(Vec2D v) 
	{
		final float diffX = this.x - v.getX();
		final float diffY = this.y - v.getY();
		return (float)Math.sqrt(diffX*diffX + diffY*diffY);
	}
	
	public void translate(Vec2D v)
	{
		this.translate(v.getX(),v.getY());
	}
	
	public void translate(float x,float y)
	{
		this.x += x;
		this.y += y;
	}
}
