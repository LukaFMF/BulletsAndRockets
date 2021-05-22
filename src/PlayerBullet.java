import java.awt.*;

public class PlayerBullet extends Projectile 
{
	private RectHitbox hitbox;
	private int damage;
	private boolean isDestroyed;
	
	PlayerBullet(Rect2D rect,Vec2D speed,int damage,Image texture)
	{
		super(rect,speed,texture);
		this.hitbox = new RectHitbox(this.rect.getOrigin(),this.rect.getWidth(),this.rect.getHeight());
		this.damage = damage;
		this.isDestroyed = false;
	}
	
	@Override
	public void update(float deltaTime)
	{
		//TODO add list of enemies
		Vec2D movementAmount = this.speed.clone();
		movementAmount.scalarMul(deltaTime);
		
		this.rect.translate(movementAmount);
		this.hitbox.translate(movementAmount);
	}

	@Override
	public void draw(Graphics2D g)
	{
		final Vec2D imagePos = this.rect.getOrigin();
		g.drawImage(texture,(int)imagePos.getX(),(int)imagePos.getY(),null);
		this.hitbox.draw(g);
	}
	
	public boolean isOffscreen(int width,int height)
	{
		// vsi metki grejo le naravnost 
		return this.rect.getOrigin().getX() > width;
	}
	
	public RectHitbox getHitbox()
	{
		return this.hitbox;
	}
	
	public int getDamage()
	{
		return this.damage;
	}
	
	
	public void destroy()
	{
		this.isDestroyed = true;
	}
	
	public boolean hasBeenDestroyed()
	{
		return this.isDestroyed;
	}
}
