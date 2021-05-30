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
	
	public Circle2D getHitbox()
	{
		return this.hitbox;
	}
	
	public CircleHitbox clone()
	{
		return new CircleHitbox(this.hitbox.getOrigin(),this.hitbox.getRadius());
	}
	
	public Vec2D getHitboxOrigin()
	{
		return this.hitbox.getOrigin();
	}
	
	public int getCollisionGridInx(CollisionGrid grid)
	{
		final Vec2D hitboxOrigin = this.hitbox.getOrigin();
		return (int)(hitboxOrigin.getY()/grid.getCellHeight())*grid.getGridSize() + (int)(hitboxOrigin.getX()/grid.getCellWidth());
	}
	
	public void draw(Graphics2D g)
	{	
		final Vec2D origin = this.hitbox.getOrigin();
		final float radius = this.hitbox.getRadius();
		final float radiusX2 = 2*radius;
		
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(1.f));
		g.drawOval((int)(origin.getX() - radius),(int)(origin.getY() - radius),(int)radiusX2,(int)radiusX2);
	}
}
