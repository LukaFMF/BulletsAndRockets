
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public abstract class Enemy
{
	protected float movespeed;
	protected Image texture;
	protected Vec2D endPoint;
	protected Rect2D enemy;
	protected List<EnemyBullet> bullets;
	//protected ... trijectori;
	
	Enemy(float movespeed,Image texture ,Vec2D endPoint,Rect2D enemy)
	{
		this.movespeed = movespeed;
		this.texture = texture;
		this.endPoint = endPoint;
		this.enemy = enemy;
		this.bullets = new LinkedList<EnemyBullet>();
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
