import java.awt.*;

public abstract class Projectile 
{
	protected Rect2D rect;
	protected Vec2D speed;
	protected Image texture;
	
	Projectile(Rect2D rect,Vec2D speed,Image texture)
	{
		this.rect = rect;
		this.speed = speed; // koliko pikslov na ms
		this.texture = texture;
	}
	
	abstract public void update(float deltaTime);
	abstract public void draw(Graphics2D g);	
}
