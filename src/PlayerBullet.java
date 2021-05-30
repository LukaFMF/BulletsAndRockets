import java.awt.*;

public class PlayerBullet extends Projectile 
{
	private RectHitbox hitbox;
	private int damage;
	
	PlayerBullet(Rect2D rect,Vec2D speed,Vec2D acceleration,int damage,Image texture)
	{
		super(rect,speed,acceleration,texture);
		this.damage = damage;
		this.hitbox = new RectHitbox(this.rect.getOrigin(),this.rect.getWidth(),this.rect.getHeight());
		this.isDestroyed = false;
	}
	
	@Override
	public void update(float deltaTime,int windowWidth,int windowHeight)
	{
		if(this.isOffscreen(windowWidth,windowHeight))
			this.destroy();
		
		Vec2D accelerationAmount = this.acceleration.clone();
		accelerationAmount.scalarMul(deltaTime);
		this.speed.add(accelerationAmount);
		
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
		//this.hitbox.draw(g);
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
