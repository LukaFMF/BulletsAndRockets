import java.awt.*;

public class CircleEnemy extends Enemy
{
	private CircleHitbox hitbox;
	
	CircleEnemy(float movespeed,Image texture,Vec2D endPoint,Rect2D enemy, int hp,CircleHitbox hitbox)
	{
		super(movespeed,texture,endPoint,enemy);
		this.enemy = enemy;
		this.hitbox = hitbox;
		this.hp = 1;
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
		
		float endX = this.endPoint.getX();
		float endY = this.endPoint.getY();
		
		this.movementDirection.zero();
		this.movementDirection.translate(endX - currX,endY - currY);
		
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
