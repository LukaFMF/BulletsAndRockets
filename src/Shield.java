import java.awt.*;

public class Shield
{
	private Rect2D boundingBox;
	private CircleHitbox hitbox;
	private Image texture;
	private boolean enabled;
	
	Shield(Vec2D origin,float radius,Image texture)
	{
		this.hitbox = new CircleHitbox(origin.clone(),radius);

		final float radiusX2 = radius*2;
		final Vec2D boundingBoxOrigin = new Vec2D(origin.getX() - radius,origin.getY() - radius);
		this.boundingBox = new Rect2D(boundingBoxOrigin,radiusX2,radiusX2); // predpostavimo, da je vedno kvadrat
		
		this.texture = texture;
		this.enabled = true;
	}
	
	Shield(Rect2D boundingBox,CircleHitbox hitbox,Image texture) // konstruktor za kloniranje
	{
		this.boundingBox = boundingBox; 
		this.texture = texture;
		this.hitbox = hitbox;
		this.enabled = false;
	}
	
	public void update()
	{
		
	}
	
	public void draw(Graphics2D g)
	{
		final Vec2D boxOrigin = this.boundingBox.getOrigin();  
		g.drawImage(texture,(int)boxOrigin.getX(),(int)boxOrigin.getY(),null);
	}
	
	public void translate(Vec2D v)
	{
		this.boundingBox.translate(v);
		this.hitbox.translate(v);
	}
	
	public CircleHitbox getHitbox()
	{
		return this.hitbox;
	}
	
	public boolean isEnabled()
	{
		return this.enabled;
	}
	
	public void disable()
	{
		this.enabled = false;
	}
	
	public Shield clone()
	{
		return new Shield(this.boundingBox.clone(),this.hitbox.clone(),this.texture);
	}
}
