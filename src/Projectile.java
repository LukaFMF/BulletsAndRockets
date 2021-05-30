import java.awt.*;

public abstract class Projectile 
{
	protected Rect2D rect;
	protected Vec2D speed;
	protected Vec2D acceleration;
	protected Image texture;
	protected boolean isDestroyed;
	
	Projectile(Rect2D rect,Vec2D speed,Vec2D acceleration,Image texture)
	{
		this.rect = rect;
		this.speed = speed; // koliko pikslov na ms
		this.acceleration = acceleration; // koliko pikslov na ms
		this.texture = texture;
		this.isDestroyed = false;
	}
	
	abstract public void update(float deltaTime,int windowWidth,int windowHeight);
	abstract public void draw(Graphics2D g);	
}
