import java.awt.*;
import java.util.LinkedList;

public class Boss
{
	private Rect2D boundingBox;
	private int hp;
	private int maxHp;
	private float pixelsPerMilli;
	private Vec2D movementDirection;
	private int currWaypointInx;
	private Vec2D[] waypoints;
	private Image[] textures;
	private EnemyWeapon[] enemyWeapons;
	private CircleHitbox[] hitboxes;
	private Shield[] shields;
	private boolean isIdle;
	private int currStage;
	private int stages;
	
	Boss(BossType bossType,Vec2D spawnPoint,Vec2D[] waypoints)
	{
		final float bossWidth = bossType.getWidth();
		final float bossHeight = bossType.getHeight(); 
		this.boundingBox = new Rect2D(spawnPoint.clone(),bossWidth,bossHeight);
		final Vec2D a = this.boundingBox.getOrigin();
				
		this.maxHp = bossType.getHp();
		this.hp = this.maxHp;
		this.pixelsPerMilli = bossType.getSpeed();
		this.movementDirection = new Vec2D(0.f,0.f);
		this.currWaypointInx = 0;
		this.waypoints = waypoints;
		this.textures = bossType.getTextures();
		
		final float[] shootCooldowns = bossType.getShootCooldowns();
		final BulletPattern[] patterns = bossType.getBulletPatterns();
		this.enemyWeapons = new EnemyWeapon[shootCooldowns.length];
		for(int i = 0;i < this.enemyWeapons.length;i++)
			this.enemyWeapons[i] = new EnemyWeapon(shootCooldowns[i],patterns[i]);
		
		final Circle2D[] bossHitboxLocations = bossType.getHitboxRelativeLocations();
 		this.hitboxes = new CircleHitbox[bossHitboxLocations.length];
		for(int i = 0;i < this.hitboxes.length;i++)
		{
			final Vec2D shipOrigin = this.boundingBox.getOrigin();
			Vec2D hitboxLocation = bossHitboxLocations[i].getOrigin().clone();
			final float hitboxRadius = bossHitboxLocations[i].getRadius();
			hitboxLocation.translate(shipOrigin);
			
			this.hitboxes[i] = new CircleHitbox(hitboxLocation,hitboxRadius);
		}
		
		final Image[] bossShieldTextures = bossType.getShieldTextures();
		this.shields = new Shield[bossShieldTextures.length];
		for(int i = 0;i < this.shields.length;i++)
		{
			final Circle2D currHitbox = this.hitboxes[i].getHitbox();
			this.shields[i] = new Shield(currHitbox.getOrigin().clone(),currHitbox.getRadius() + 5.f,bossShieldTextures[i]);
		}
		
		this.isIdle = false;
		this.currStage = 0;
		this.stages = bossType.getStages();
	}
	
	public void update(float deltaTime,double timer,LinkedList<EnemyBullet> enemyBullets,Vec2D target,LinkedList<PlayerBullet> playerBullets)
	{
		for(PlayerBullet currBullet : playerBullets)
		{
			boolean bulletHit = false;
			for(int i = this.currStage + 1;i < this.shields.length;i++)
				if(this.shields[i].getHitbox().collidesWith(currBullet.getHitbox()))
				{
					currBullet.destroy();
					bulletHit = true;
					break;
				}
					
			if(!bulletHit && this.hitboxes[this.currStage].collidesWith(currBullet.getHitbox()))
			{
				this.hp -= currBullet.getDamage();
				currBullet.destroy();
			}
		}
		
		
		final float hpRatio = this.hp/(float)this.maxHp; // koliksen delez zivljenje se ima nasprotnik
		for(int i = 0;i < this.stages;i++)
		{
			final float stageTreshold = 1.f - (i + 1)/(float)this.stages;
			if(hpRatio > stageTreshold)
			{
				this.currStage = i;
				break;
			}
		}
		
		this.enemyWeapons[this.currStage].update(timer,this.boundingBox,enemyBullets,target);
		
		if(this.isIdle)
		{
			this.movementDirection.zero();
			this.movementDirection.setY((float)(Math.sin(timer/1e3) * this.pixelsPerMilli));
			this.boundingBox.translate(this.movementDirection);
			
			for(int i = 0;i < this.hitboxes.length;i++)
				this.hitboxes[i].translate(this.movementDirection);
			
			for(int i = 0;i < this.shields.length;i++)
				this.shields[i].translate(this.movementDirection);
		}
		else
		{
			final Vec2D origin = this.boundingBox.getOrigin();
			
			final float currX = origin.getX();
			final float currY = origin.getY();
			
			if(this.waypoints[this.currWaypointInx].isWithinError(origin,5.f))
			{
				this.currWaypointInx++;
				if(this.currWaypointInx == this.waypoints.length)
				{
					this.isIdle = true;
					this.currWaypointInx--;
				}
			}
			
			final Vec2D currTarget = this.waypoints[this.currWaypointInx];
		
			this.movementDirection.zero();
			this.movementDirection.translate(currTarget.getX() - currX,currTarget.getY() - currY);
			
			if(!this.movementDirection.isZeroVec())
			{
				this.movementDirection.normalize();
				
				final float moveAmount = deltaTime * this.pixelsPerMilli;
				this.movementDirection.scalarMul(moveAmount);
				
				this.boundingBox.translate(this.movementDirection);
				
				for(int i = 0;i < this.hitboxes.length;i++)
					this.hitboxes[i].translate(this.movementDirection);
				
				for(int i = 0;i < this.shields.length;i++)
					this.shields[i].translate(this.movementDirection);
			}
		}
	}
	
	public void draw(Graphics2D g)
	{
		final Vec2D boxOrigin = this.boundingBox.getOrigin();
		g.drawImage(this.textures[this.currStage],(int)boxOrigin.getX(),(int)boxOrigin.getY(),null);
		
		for(int i = this.currStage + 1;i < this.shields.length;i++)
			this.shields[i].draw(g);
		
		for(int i = 0;i < this.hitboxes.length;i++)
		this.hitboxes[i].draw(g);
	}
	public int getCurrentStage()
	{
		return this.currStage;
	}
	
	public CircleHitbox[] getHitboxes()
	{
		return this.hitboxes;
	}
	
	public boolean isAlive()
	{
		return this.hp > 0;
	}
}
