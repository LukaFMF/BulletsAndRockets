import java.awt.Graphics2D;
import java.awt.Image;

public class EnemyBullet extends Projectile
{
	private CircleHitbox hitbox;
	
	EnemyBullet(Rect2D rect, Vec2D speed, Image texture)
	{
		super(rect,speed,texture);
		
		final Vec2D origin = this.rect.getOrigin();
		final float rectX = origin.getX();
		final float rectY = origin.getY();
		
		this.hitbox = new CircleHitbox(rectX + this.rect.getWidth()/2,rectY + this.rect.getHeight()/2,this.rect.getWidth()/2);  
	}

	
	@Override
	public void update(float deltaTime)
	{
		Vec2D movement = this.speed.clone();
		
		movement.scalarMul(deltaTime);
		
		this.rect.translate(movement);		// metki se premikajo le premocrtno
		this.hitbox.translate(movement);
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
		final Vec2D origin = this.rect.getOrigin();
		final float rectX = origin.getX();
		final float rectY = origin.getY();
		
		if(rectX < -this.rect.getWidth() || rectX > width || rectY < -this.rect.getHeight() || rectY > height)
			return true;
		return false;
	}
}
