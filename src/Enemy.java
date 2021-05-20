
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public abstract class Enemy
{
	protected Rect2D enemy;
	protected float pixelsPerMilli;
	protected Vec2D movementDirection;
	protected int hp;
	protected Image texture;
	protected Vec2D endPoint;
	//protected List<EnemyBullet> bullets;
	//protected ... trijectori;
	
	Enemy(float pixelsPerMilli,Image texture ,Vec2D endPoint,Rect2D enemy)
	{
		this.pixelsPerMilli = pixelsPerMilli;
		this.movementDirection = new Vec2D(0.f,0.f);
		this.texture = texture;
		this.endPoint = endPoint;
		this.enemy = enemy;
		//this.bullets = new LinkedList<EnemyBullet>();
	}
	
	public float getEndpointX()
	{
		return this.endPoint.getX();
	}
	
	public float getEndpointY()
	{
		return this.endPoint.getY();
	}

	public abstract void update(double timer,float deltaTime);

	public abstract void draw(Graphics2D g);
}
