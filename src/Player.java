import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

public class Player
{
	private int panelWidth; // le za informacijo o velikosti okna
	private int panelHeight;
	private Rect2D rect;
	private Vec2D movementDirection;
	private Vec2D hitboxLocation; // relativna lokacija hitbox-a glede na izhodisce this.rect 
	private CircleHitbox hitbox;
	private PlayerWeapon weapon;
	private Image texture;
	private LinkedList<PlayerBullet> bullets;
	
	Player(Rect2D rect,int windowWidth,int windowHeight) 
	{
		this.panelWidth = windowWidth;
		this.panelHeight = windowHeight;
		this.bullets = new LinkedList<PlayerBullet>();
		
		this.rect = rect;
		
		this.weapon = new PlayerWeapon();
		
		this.movementDirection = new Vec2D(0.f,0.f);
		
		final Vec2D rOrigin = this.rect.getOrigin();
		
		final float rX = rOrigin.getX();
		final float rY = rOrigin.getY();
		
		final float rWidth = this.rect.getWidth();
		final float rHeight = this.rect.getHeight();
		
		this.hitboxLocation = new Vec2D(rWidth/2 - 12,rHeight/2);
		
		this.hitbox = new CircleHitbox(rX + this.hitboxLocation.getX(),rY + this.hitboxLocation.getY(),18.f);
		
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
		int i = 0;
		while(i < this.bullets.size())
		{
			final PlayerBullet currBullet = this.bullets.get(i);
			currBullet.update(deltaTime);
			
			if(currBullet.isOffscreen(this.panelWidth,this.panelHeight))
				this.bullets.remove(i);
			else
				i++;
		}
		
		this.weapon.update(deltaTime);
		
		final float pixelsPerMilli = .5f;
		
		this.movementDirection.zero();
		
		Map<Integer,Boolean> keyMap = keyboard.getKeyMap();
		if(keyMap.get(KeyEvent.VK_UP))
			this.movementDirection.translate(.0f,-1.f);
		if(keyMap.get(KeyEvent.VK_DOWN))
			this.movementDirection.translate(.0f,1.f);
		if(keyMap.get(KeyEvent.VK_LEFT))
			this.movementDirection.translate(-1.f,.0f);
		if(keyMap.get(KeyEvent.VK_RIGHT))
			this.movementDirection.translate(1.f,.0f);
		if(keyMap.get(KeyEvent.VK_SPACE))
			this.weapon.tryToShoot(this.rect,this.bullets);
		
		if(!this.movementDirection.isZeroVec())
		{
			this.movementDirection.normalize();
			
			final float moveAmount = deltaTime * pixelsPerMilli;
			
			final float moveX = this.movementDirection.getX() * moveAmount;
			final float moveY = this.movementDirection.getY() * moveAmount;
			this.rect.translate(moveX,moveY);
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
		this.hitbox.translate(this.hitboxLocation);
	}
	
	public void draw(Graphics2D g)
	{
		for(PlayerBullet bullet : this.bullets)
			bullet.draw(g);
		
		final Vec2D imagePos = rect.getOrigin();
		g.drawImage(texture,(int)imagePos.getX(),(int)imagePos.getY(),null);
		this.hitbox.draw(g);
	}
	
	public Rect2D getRect()
	{
		return rect;
	}
			
}
