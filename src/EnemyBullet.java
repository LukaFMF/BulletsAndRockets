import java.awt.Graphics2D;
import java.awt.Image;

public class EnemyBullet extends Projectile
{
	private CircleHitbox hitbox;
	private int collisionGridInx;
	private CollisionGrid grid;
	
	EnemyBullet(Rect2D rect,Vec2D speed,Vec2D acceleration,Image texture,CollisionGrid grid)
	{
		super(rect,speed,acceleration,texture);
		
		final Vec2D origin = this.rect.getOrigin();
		final float rectX = origin.getX();
		final float rectY = origin.getY();
		
		this.hitbox = new CircleHitbox(rectX + this.rect.getWidth()/2,rectY + this.rect.getHeight()/2,this.rect.getWidth()/2);
		
		this.grid = grid;
		this.collisionGridInx = this.hitbox.getCollisionGridInx(this.grid);
	}

	public int getCollisionGridInx()
	{
		return this.collisionGridInx;
	}
	
	public CircleHitbox getHitbox()
	{
		return this.hitbox;
	}
	
	public void destroy()
	{
		this.isDestroyed = true;
	}

	public boolean hasBeenDestroyed()
	{
		return this.isDestroyed;
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
		
		this.rect.translate(movementAmount);	// metki se premikajo le premocrtno
		this.hitbox.translate(movementAmount);
		
		this.collisionGridInx = this.hitbox.getCollisionGridInx(this.grid);
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
