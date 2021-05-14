import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.Map;

public class Player
{
	private Rect2D rect;
	private Vec2D movementDirection;
	private Vec2D hitboxLocation; // relativna lokacija hitbox-a glede na izhodisce this.rect 
	private CircleHitbox hitbox;
	private Image texture;
	
	Player(Rect2D rect) 
	{
		this.rect = rect;
		
		this.movementDirection = new Vec2D(0.f,0.f);
		
		final Vec2D rOrigin = this.rect.getOrigin();
		
		final float rX = rOrigin.getX();
		final float rY = rOrigin.getY();
		
		final float rWidth = this.rect.getWidth();
		final float rHeight = this.rect.getHeight();
		
		this.hitboxLocation = new Vec2D(rWidth/2,rHeight/2);
		
		this.hitbox = new CircleHitbox(rX + this.hitboxLocation.getX(),rY + this.hitboxLocation.getY(),10.f);
		
		try
		{
			this.texture = Loader.loadImage(".\\assets\\images\\ship.png",(int)this.rect.getWidth(),(int)this.rect.getHeight());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void update(float deltaTime,KeyboardControls keyboard,int windowWidth,int windowHeight)
	{
		final float pixelsPerMilli = .5f;
		Map<Integer,Boolean> keyMap = keyboard.getKeyMap();
		
		this.movementDirection.zero();
		
		if(keyMap.get(KeyEvent.VK_UP))
			this.movementDirection.translate(.0f,-1.f);
		if(keyMap.get(KeyEvent.VK_DOWN))
			this.movementDirection.translate(.0f,1.f);
		if(keyMap.get(KeyEvent.VK_LEFT))
			this.movementDirection.translate(-1.f,.0f);
		if(keyMap.get(KeyEvent.VK_RIGHT))
			this.movementDirection.translate(1.f,.0f);
		
		if(!this.movementDirection.isZeroVec())
		{
			this.movementDirection.normalize();
			
			final float moveAmount = deltaTime * pixelsPerMilli;
			
			final float moveX = this.movementDirection.getX() * moveAmount;
			final float moveY = this.movementDirection.getY() * moveAmount;
			this.rect.translate(moveX,moveY);
			//this.hitbox.getHitbox().translate(moveX,moveY);
		}
		
		float rectX = this.rect.getOrigin().getX();
		float rectY = this.rect.getOrigin().getY();
		final float rectWidth = this.rect.getWidth();
		final float rectHeight = this.rect.getHeight();
		
		this.rect.getOrigin().setX(HelperFuncs.clamp(rectX,0,(float)windowWidth - rectWidth));
		this.rect.getOrigin().setY(HelperFuncs.clamp(rectY,0,(float)windowHeight - rectHeight));
		
		rectX = this.rect.getOrigin().getX();
		rectY = this.rect.getOrigin().getY();
		
		this.hitbox.getHitbox().getOrigin().setX(rectX);
		this.hitbox.getHitbox().getOrigin().setY(rectY);
		this.hitbox.getHitbox().translate(this.hitboxLocation);
	}
	
	public void draw(Graphics2D g)
	{
		Vec2D imagePos = rect.getOrigin();
		g.drawImage(texture,(int)imagePos.getX(),(int)imagePos.getY(),null);
		this.hitbox.draw(g);
	}
	
	public Rect2D getRect()
	{
		return rect;
	}
			
}
