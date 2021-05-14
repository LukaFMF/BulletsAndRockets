public class Vec2D 
{
	private float x;
	private float y;
	
	public Vec2D(float x, float y) 
	{
		this.x = x;
		this.y = y;
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
	
	public void zero() 
	{
		this.x = 0.f;
		this.y = 0.f;
	}

	public float distance(Vec2D v) 
	{
		return this.distance(v.getX(),v.getY());
	}
	
	public float distance(float x,float y) 
	{
		final float diffX = this.x - x;
		final float diffY = this.y - y;
		return (float)Math.sqrt(diffX*diffX + diffY*diffY);
	}
	
	public float norm()
	{
		return (float)Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public void normalize()
	{
		final float norm = this.norm();
		
		this.x /= norm;
		this.y /= norm; 
	}
	
	public boolean isZeroVec()
	{
		return Math.signum(this.x) == 0 && Math.signum(this.y) == 0; 
	}
	
	public float getX() 
	{
		return x;
	}

	public float getY() 
	{
		return y;
	}
	
	public void setX(float x) 
	{
		this.x = x;
	}

	public void setY(float y) 
	{
		this.y = y;
	}
}
