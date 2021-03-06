import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class RectHitbox 
{
	private Rect2D hitbox;
	
	RectHitbox(Vec2D origin,float width,float height)
	{
		this(origin.getX(),origin.getY(),width,height);
	}
	
	RectHitbox(float x,float y,float width,float height)
	{
		this.hitbox = new Rect2D(x,y,width,height);
	}
	
	public boolean collidesWith(RectHitbox o)
	{
		return Collisions.RectRect(this.hitbox,o.getHitbox());
	}
	
	public boolean collidesWith(CircleHitbox o)
	{
		return Collisions.CircleRect(o.getHitbox(),this.hitbox);
	}
	
	public void translate(float x,float y)
	{
		this.hitbox.translate(x,y);
	}
	
	public void translate(Vec2D v)
	{
		this.translate(v.getX(),v.getY());
	}
	
	public void setHitboxOrigin(Vec2D v)
	{
		this.setHitboxOrigin(v.getX(),v.getY());
	}
	
	public void setHitboxOrigin(float x,float y)
	{
		this.hitbox.setOrigin(x,y);
	}
	
	public Rect2D getHitbox()
	{
		return this.hitbox;
	}
	
	public void draw(Graphics2D g)
	{	
		final Vec2D origin = this.hitbox.getOrigin();
		final float width = this.hitbox.getWidth();
		final float height = this.hitbox.getHeight();
		
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(1.f));
		g.drawRect((int)origin.getX(),(int)origin.getY(),(int)width,(int)height);
	}
}
