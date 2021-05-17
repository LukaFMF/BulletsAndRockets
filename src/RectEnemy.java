import java.awt.Graphics2D;
import java.awt.Image;

public class RectEnemy extends Enemy
{
	private int HP;
	private RectHitbox hitbox;
	
	private Vec2D startPoint;

	RectEnemy(float movespeed,Image texture,Vec2D endPoint,Rect2D enemy, int HP,RectHitbox hitbox)
	{
	super(movespeed,texture,endPoint,enemy);
	this.enemy = enemy;
	this.startPoint = this.enemy.getOrigin();  //levo zgoraj
	this.hitbox = hitbox;
	this.HP = 1;
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
	public void update(float deltaTime) 
	{
		
		float startx = this.startPoint.getX();
		float starty = this.startPoint.getY();
		
		float endx = this.endPoint.getX();
		float endy = this.endPoint.getY();
		
		
		Vec2D movementdirection = new Vec2D(endx - startx,endy - starty);
				
		movementdirection.normalize();

		
		float konst = this.movespeed * deltaTime;
		
		movementdirection.scalarMul(konst);

		
		this.enemy.translate(movementdirection);
		this.hitbox.translate(movementdirection);
		
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
		if (HP > 0)
			return true;
		return false;				
	}
}
