import java.awt.Graphics2D;
import java.awt.Image;

public class RectEnemy extends Enemy
{
	private RectHitbox hitbox;

	RectEnemy(float movespeed, int hp,Image texture,Rect2D enemy,Vec2D[] waypoints,boolean isCyclying)
	{
		super(movespeed,hp,texture,enemy,waypoints,isCyclying);
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
	public void update(double timer,float deltaTime) 
	{
		final Vec2D origin = this.enemy.getOrigin();
		
		final float currX = origin.getX();
		final float currY = origin.getY();
		
		boolean moving = true;
		if(this.waypoints[this.currWaypointInx].isWithinError(origin,3.f))
		{
			moving = false;
			this.currWaypointInx++;
			if(this.currWaypointInx == this.waypoints.length)
			{
				if(this.repeatWaypoints)
					this.currWaypointInx = 0;
				else
					this.currWaypointInx--;
			}
		}
		
		final Vec2D currTarget = this.waypoints[this.currWaypointInx];
		
		this.movementDirection.zero();
		if(moving)
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
