import java.awt.Graphics2D;
import java.awt.Image;
import java.util.LinkedList;

public class RectEnemy extends Enemy
{
	private RectHitbox hitbox;

	RectEnemy(float movespeed, int hp,Image texture,Rect2D enemy,Vec2D[] waypoints,boolean isCyclying,EnemyWeapon enemyWeapon)
	{
		super(movespeed,hp,texture,enemy,waypoints,isCyclying,enemyWeapon);
		this.hitbox = new RectHitbox(enemy.getOrigin(),enemy.getWidth(),enemy.getHeight());
	}
	
	public float getOriginX() 
	{
		return this.enemy.getOrigin().getX();
	}
	
	public float getOriginY() 
	{
		return this.enemy.getOrigin().getY();
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
		
		this.enemyWeapon.update(timer,this.enemy,enemyBullets,target);
		
		if(this.isIdle)
		{
			final Vec2D currTarget = new Vec2D(0.f,(float)(Math.sin(timer/1e3) * this.pixelsPerMilli));
			this.enemy.translate(currTarget);
			this.hitbox.translate(currTarget);
		}
		else
		{
			final Vec2D origin = this.enemy.getOrigin();
			
			final float currX = origin.getX();
			final float currY = origin.getY();
			
			if(this.waypoints[this.currWaypointInx].isWithinError(origin,3.f))
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
				
				this.enemy.translate(this.movementDirection);
				this.hitbox.translate(this.movementDirection);
			}
		}
	}

	@Override
	public void draw(Graphics2D g)
	{
		final Vec2D imagePos = this.enemy.getOrigin();
		g.drawImage(texture,(int)imagePos.getX(),(int)imagePos.getY(),null);
		this.hitbox.draw(g);
	}
	
	public boolean IsHit(PlayerBullet bullet) 
	{
		//TODO BULLET DMG
		return this.hitbox.collidesWith(bullet.getHitbox());
	}
	public boolean HasHP() 
	{
		if (this.hp > 0)
			return true;
		return false;				
	}
}
