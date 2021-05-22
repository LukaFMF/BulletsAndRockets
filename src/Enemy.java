
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
	protected EnemyWeapon enemyWeapon;
	protected boolean isIdle;
	
	Enemy(float pixelsPerMilli,int hp,Image texture,Rect2D enemy,Vec2D[] waypoints,boolean isCyclying,EnemyWeapon enemyWeapon)
	{
		this.pixelsPerMilli = pixelsPerMilli;
		this.hp = hp;
		this.movementDirection = new Vec2D(0.f,0.f);
		this.texture = texture;
		this.enemy = enemy;
		this.currWaypointInx = 0;
		this.waypoints = waypoints;
		this.repeatWaypoints = isCyclying;
		this.enemyWeapon = enemyWeapon;
		this.isIdle = false;
	}
	
	public abstract void update(float deltaTime,double timer,LinkedList<EnemyBullet> enemyBullets,Vec2D target,LinkedList<PlayerBullet> playerBullets);

	public abstract void draw(Graphics2D g);
	
	public boolean isAlive() 
	{
		return this.hp > 0;		
	}
}
