import java.awt.*;

public class CircleHitbox 
{
	private Circle2D hitbox;
	
	CircleHitbox(Vec2D origin,float radius)
	{
		this(origin.getX(),origin.getY(),radius);
	}
	
	CircleHitbox(float x,float y,float radius)
	{
		this.hitbox = new Circle2D(x,y,radius);
	}
	
	public boolean collidesWith(CircleHitbox o)
	{
		return Collisions.CircleCircle(this.hitbox,o.getHitbox());
	}
	
	public boolean collidesWith(RectHitbox o)
	{
		return Collisions.CircleRect(this.hitbox,o.getHitbox());
	}
	
	public Circle2D getHitbox()
	{
		return this.hitbox;
	}
	
	public void draw(Graphics2D g)
	{	
		final Vec2D origin = this.hitbox.getOrigin();
		final float radius = this.hitbox.getRadius();
		final float radiusX2 = 2*radius;
		
		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(1.f));
		g.drawOval((int)(origin.getX() - radius),(int)(origin.getY() - radius),(int)radiusX2,(int)radiusX2);
	}
}
