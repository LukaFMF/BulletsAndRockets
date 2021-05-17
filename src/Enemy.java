import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Enemy
{
	protected float movespeed;
	protected Image texture;
	protected Vec2D endPoint;
	protected Rect2D enemy;
	//protected ... trijectori;
	
	Enemy(float movespeed,Image texture ,Vec2D endPoint,Rect2D enemy)
	{
		this.movespeed = movespeed;
		this.texture = texture;
		this.endPoint = endPoint;
		this.enemy = enemy;
	}
	public float getEndpointX()
	{
		return this.endPoint.getX();
	}
	public float getEndpointY()
	{
		return this.endPoint.getY();
	}

	public abstract void update(float deltaTime);

	public abstract void draw(Graphics2D g);
	
	
	
}
