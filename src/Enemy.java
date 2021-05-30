
import java.awt.*;
import java.util.LinkedList;

public abstract class Enemy
{
	protected int hp;
	protected Rect2D boundingBox;
	protected float pixelsPerMilli;
	protected Vec2D movementDirection;
	protected int currWaypointInx;
	protected Vec2D[] waypoints;
	protected Image texture;
	protected EnemyWeapon enemyWeapon;
	protected boolean repeatWaypoints;
	protected boolean isIdle;
	
	Enemy(EnemyType enemyType,Vec2D spawnPoint,Vec2D[] waypoints,boolean repeatWaypoints)
	{
		this.hp = enemyType.getHp();
		this.boundingBox = new Rect2D(spawnPoint.getX(),spawnPoint.getY(),enemyType.getBoundingBoxWidth(),enemyType.getBoundingBoxHeight());
		this.pixelsPerMilli = enemyType.getSpeed();
		this.movementDirection = new Vec2D(0.f,0.f);
		this.currWaypointInx = 0;
		this.waypoints = waypoints;
		this.texture = enemyType.getTexture();
		this.enemyWeapon = new EnemyWeapon(enemyType.getShootCooldown(),enemyType.getPattern());
		this.repeatWaypoints = repeatWaypoints;
		this.isIdle = false;
	}
	
	public abstract void update(float deltaTime,double timer,LinkedList<EnemyBullet> enemyBullets,Vec2D target,LinkedList<PlayerBullet> playerBullets);

	public abstract void draw(Graphics2D g);
	
	public boolean isAlive() 
	{
		return this.hp > 0;		
	}
}
