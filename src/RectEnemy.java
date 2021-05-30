import java.awt.Graphics2D;
import java.awt.Image;
import java.util.LinkedList;

public class RectEnemy extends Enemy
{
	private RectHitbox hitbox;

	RectEnemy(EnemyType enemyType,Vec2D spawnPoint,Vec2D[] waypoints,boolean repeatWaypoints)
	{
		super(enemyType,spawnPoint,waypoints,repeatWaypoints);
		this.hitbox = new RectHitbox(this.boundingBox.getOrigin().clone(),this.boundingBox.getWidth(),this.boundingBox.getHeight());
	}
	
	public float getOriginX() 
	{
		return this.boundingBox.getOrigin().getX();
	}
	
	public float getOriginY() 
	{
		return this.boundingBox.getOrigin().getY();
	}
	
	@Override
	public void update(float deltaTime,double timer,LinkedList<EnemyBullet> enemyBullets,Vec2D target,LinkedList<PlayerBullet> playerBullets) 
	{
		for(PlayerBullet currBullet : playerBullets)
		{
			if(this.hitbox.collidesWith(currBullet.getHitbox()))
			{
				this.hp -= currBullet.getDamage();
				currBullet.destroy();
			}
		}
		
		this.enemyWeapon.update(timer,this.boundingBox,enemyBullets,target);
		
		if(this.isIdle)
		{
			final Vec2D currTarget = new Vec2D(0.f,(float)(Math.sin(timer/1e3) * this.pixelsPerMilli));
			this.boundingBox.translate(currTarget);
			this.hitbox.translate(currTarget);
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
					if(this.repeatWaypoints)
						this.currWaypointInx = 0;
					else
					{
						this.currWaypointInx--;
						this.isIdle = true;
					}
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
				this.hitbox.translate(this.movementDirection);
			}
		}
	}

	@Override
	public void draw(Graphics2D g)
	{
		final Vec2D imagePos = this.boundingBox.getOrigin();
		g.drawImage(texture,(int)imagePos.getX(),(int)imagePos.getY(),null);
		//this.hitbox.draw(g);
	}
	
	public RectHitbox getHitbox()
	{
		return this.hitbox;
	}
}
