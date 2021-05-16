import java.awt.*;

public class PlayerBullet extends Projectile 
{
	private RectHitbox hitbox;
	
	PlayerBullet(Rect2D rect,Vec2D speed,Image texture)
	{
		super(rect,speed,texture);
		this.hitbox = new RectHitbox(this.rect.getOrigin(),this.rect.getWidth(),this.rect.getHeight());
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
}
