
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
	protected int currWaypointInx;
	protected Vec2D[] waypoints;
	protected boolean repeatWaypoints;
	
	Enemy(float pixelsPerMilli,int hp,Image texture,Rect2D enemy,Vec2D[] waypoints,boolean isCyclying)
	{
		this.pixelsPerMilli = pixelsPerMilli;
		this.hp = hp;
		this.movementDirection = new Vec2D(0.f,0.f);
		this.texture = texture;
		this.enemy = enemy;
		this.currWaypointInx = 0;
		this.waypoints = waypoints;
		this.repeatWaypoints = isCyclying;
	}
	
	public abstract void update(double timer,float deltaTime);

	public abstract void draw(Graphics2D g);
}
