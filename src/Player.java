import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

final class ShipThrusterState // simulacija enum konstrukta
{
	public static final int DECELERATING  = 0;
	public static final int STATIONARY = 1;
	public static final int ACCELERATING = 2;
}

public class Player
{
	private int panelWidth; // le za informacijo o velikosti okna
	private int panelHeight;
	private Rect2D rect;
	private float pixelsPerMilli; // speed
	private Vec2D movementDirection;
	private float focusSpeed;
	private int thrusterState;
	private Image thrusterStationary;
	private Image thrusterAccelerating;
	private int collisionGridInx;
	private Set<Integer> neighbouringCellInxs;
	private CollisionGrid grid;
	private Vec2D hitboxLocation; // relativna lokacija hitbox-a glede na izhodisce this.rect
	private CircleHitbox hitbox;
	private PlayerWeapon weapon;
	private Image texture;
	private LinkedList<PlayerBullet> bullets;
	private int lives;
	private Image lifeTexture;
	private boolean isInvincible;
	private double invincibleAt;
	private boolean isDestroyed;
	private double destroyedAt;
	
	Player(Rect2D rect,int windowWidth,int windowHeight,CollisionGrid grid)
	{
		this.panelWidth = windowWidth;
		this.panelHeight = windowHeight;
		this.bullets = new LinkedList<PlayerBullet>();
		
		this.rect = rect;
		this.pixelsPerMilli = .5f;
		this.focusSpeed = .4f;
		
		this.weapon = new PlayerWeapon(this.rect);
		
		this.thrusterState = ShipThrusterState.STATIONARY;
		this.movementDirection = new Vec2D(0.f,0.f);
		
		final Vec2D rOrigin = this.rect.getOrigin();
		
		final float rX = rOrigin.getX();
		final float rY = rOrigin.getY();
		
		final float rWidth = this.rect.getWidth();
		final float rHeight = this.rect.getHeight();
		
		this.hitboxLocation = new Vec2D(rWidth/2 - 12,rHeight/2);
		
		this.hitbox = new CircleHitbox(rX + this.hitboxLocation.getX(),rY + this.hitboxLocation.getY(),18.f);
		
		this.grid = grid;
		this.neighbouringCellInxs = new HashSet<Integer>();
		this.collisionGridInx = this.hitbox.getCollisionGridInx(this.grid);
		this.updateNeighbouringCells();
		
		this.lives = 3;
		this.isInvincible = false;
		this.isDestroyed = false;
		this.destroyedAt = 0.;
		this.invincibleAt = 0.;
		
		try
		{
			this.thrusterStationary = Loader.loadImage(".\\assets\\images\\thrusterSta.png",21,30);
			this.thrusterAccelerating = Loader.loadImage(".\\assets\\images\\thrusterAcc.png",32,30);
			this.texture = Loader.loadImage(".\\assets\\images\\ship.png",(int)this.rect.getWidth(),(int)this.rect.getHeight());
			this.lifeTexture = Loader.loadImage(".\\assets\\images\\playerLife.png",50,50);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void update(double timer,float deltaTime,KeyboardControl keyboard)
	{
		for(int i = 0;i < this.bullets.size();)
		{
			final PlayerBullet currBullet = this.bullets.get(i);
			currBullet.update(deltaTime,this.panelWidth,this.panelHeight);
			
			if(currBullet.hasBeenDestroyed())
				this.bullets.remove(i);
			else
				i++;
		}
		
		this.weapon.update(deltaTime);
		
		if(this.isInvincible)
		{
			if(timer - this.invincibleAt > 2000.f) // ladja je neunicljiva za 2 sec
			{
				this.isInvincible = false;
			}
				
		}
		
		if(this.isDestroyed)
		{
			if(this.hasLivesLeft() && timer - this.destroyedAt > 2000.f) // ladje ni za 2 sec in prikazala se bo spet le, ce ima igralec se kaj zivljenj
			{
				this.isDestroyed = false;
				this.isInvincible = true;
				this.invincibleAt = timer;
			}
		}
		else
		{
			this.movementDirection.zero();
			
			boolean focusMode = false;
			Map<Integer,Boolean> keyMap = keyboard.getKeyMap();
			if(keyMap.get(KeyEvent.VK_UP))
				this.movementDirection.translate(.0f,-1.f);
			if(keyMap.get(KeyEvent.VK_DOWN))
				this.movementDirection.translate(.0f,1.f);
			if(keyMap.get(KeyEvent.VK_LEFT))
				this.movementDirection.translate(-1.f,.0f);
			if(keyMap.get(KeyEvent.VK_RIGHT))
				this.movementDirection.translate(1.f,.0f);
			if(keyMap.get(KeyEvent.VK_TAB))
				this.weapon.switchWeapon(timer);
			if(keyMap.get(KeyEvent.VK_SPACE))
				this.weapon.tryToShoot(this.rect,timer,this.bullets);
			if(keyMap.get(KeyEvent.VK_SHIFT))
				focusMode = true;
			
			if(!this.movementDirection.isZeroVec())
			{
				final float xDirection = this.movementDirection.getX();
				if(xDirection == 0.f)
					this.thrusterState = ShipThrusterState.STATIONARY;  
				else 
					this.thrusterState = xDirection > 0.f ? ShipThrusterState.ACCELERATING : ShipThrusterState.DECELERATING;
				
				this.movementDirection.normalize();
				
				final float moveAmount = deltaTime * this.pixelsPerMilli * (focusMode ? this.focusSpeed : 1.f);
				this.movementDirection.scalarMul(moveAmount);
				
				this.rect.translate(this.movementDirection);
			
				final Vec2D origin = this.rect.getOrigin();
				final float rectX = origin.getX();
				final float rectY = origin.getY();
				final float rectWidth = this.rect.getWidth();
				final float rectHeight = this.rect.getHeight();
				
				origin.setX(HelperFuncs.clamp(rectX,0,(float)this.panelWidth - rectWidth));
				origin.setY(HelperFuncs.clamp(rectY,0,(float)this.panelHeight - rectHeight));
				
				final Vec2D before = this.hitbox.getHitboxOrigin().clone();
				
				this.hitbox.setHitboxOrigin(origin);
				this.hitbox.translate(this.hitboxLocation);
				
				final Vec2D after = this.hitbox.getHitboxOrigin();
				final float xInxApprox = after.getX()/grid.getCellWidth();
				final float yInxApprox = after.getY()/grid.getCellHeight();
				
				final float cellWidth = this.grid.getCellWidth();
				final float cellHeight = this.grid.getCellHeight();
				
				float xBound1 = (int)(xInxApprox)*cellWidth,xBound2 = xBound1 + cellWidth;
				float yBound1 = (int)(yInxApprox)*cellHeight,yBound2 = yBound1 + cellHeight;
				
				final float lowerX = Math.min(before.getX(),after.getX()); 
				final float upperX = Math.max(before.getX(),after.getX());
				final float lowerY = Math.min(before.getY(),after.getY());
				final float upperY = Math.max(before.getY(),after.getY());
				
				if((lowerX <= xBound1 && xBound1 <= upperX) || 
				   (lowerX <= xBound2 && xBound2 <= upperX) || 
				   (lowerY <= yBound1 && yBound1 <= upperY) || 
				   (lowerY <= yBound2 && yBound2 <= upperY))
				{
					this.collisionGridInx = this.hitbox.getCollisionGridInx(this.grid);
					this.updateNeighbouringCells();
				}
					
			}
			else
				this.thrusterState = ShipThrusterState.STATIONARY;
		}
	}
	
	public void draw(Graphics2D g)
	{
		if(this.isInvincible)
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.4f));
		
		final Vec2D imagePos = rect.getOrigin();
		switch(this.thrusterState)
		{
			case ShipThrusterState.STATIONARY:
			{
				g.drawImage(this.thrusterStationary,(int)imagePos.getX() - 21,(int)imagePos.getY() + 75 - 15,null);
				break;
			}
			case ShipThrusterState.ACCELERATING:
			{
				g.drawImage(this.thrusterAccelerating,(int)imagePos.getX() - 32,(int)imagePos.getY() + 75 - 15,null);
				break;
			}
		}
		this.weapon.draw(g,this.rect.getOrigin());
		g.drawImage(texture,(int)imagePos.getX(),(int)imagePos.getY(),null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.f));
		
		//this.hitbox.draw(g);
	}
	
	public void drawPlayerLives(Graphics2D g)
	{
		for(int i = 0;i < this.lives;i++)
			g.drawImage(this.lifeTexture,i*50,0, null);
	}
	
	public void drawPlayerBullets(Graphics2D g)
	{
		for(PlayerBullet bullet : this.bullets)
			bullet.draw(g);
	}
	
	public void updateNeighbouringCells()
	{
		this.neighbouringCellInxs.clear();
		this.neighbouringCellInxs.add(this.collisionGridInx);
		
		final int gridSize = this.grid.getGridSize();
		
		final boolean inLeftmostColumn = this.collisionGridInx % gridSize == 0;
		final boolean inRightmostColumn = this.collisionGridInx % gridSize == gridSize - 1;
		final boolean inUpmostRow = this.collisionGridInx / gridSize == 0;
		final boolean inLowestRow = this.collisionGridInx / gridSize == gridSize - 1;
		
		if(!inLeftmostColumn) // je v prvem stolpcu
			this.neighbouringCellInxs.add(this.collisionGridInx - 1);
		if(!inRightmostColumn) // je v zadnjem stolpcu
			this.neighbouringCellInxs.add(this.collisionGridInx + 1);
		
		if(!inUpmostRow) // je v prvi vrstici
		{
			this.neighbouringCellInxs.add(this.collisionGridInx - gridSize);
			
			if(!inLeftmostColumn)
				this.neighbouringCellInxs.add(this.collisionGridInx - gridSize - 1);
			if(!inRightmostColumn)
				this.neighbouringCellInxs.add(this.collisionGridInx - gridSize + 1);
		}
		if(!inLowestRow) // je v zadnji vrstici
		{
			this.neighbouringCellInxs.add(this.collisionGridInx + gridSize);
			
			if(!inLeftmostColumn)
				this.neighbouringCellInxs.add(this.collisionGridInx + gridSize - 1);
			if(!inRightmostColumn)
				this.neighbouringCellInxs.add(this.collisionGridInx + gridSize + 1);
		}
	}
	
	public void resetAfterLevel()
	{
		this.destroyedAt = 0.;
		this.weapon.reset();
	}
	
	public void destroy(double timer)
	{
		this.isDestroyed = true;
		this.destroyedAt = timer;
		this.lives--;
	}
	
	public boolean isDestroyed()
	{
		return this.isDestroyed;
	}
	
	public Rect2D getRect()
	{
		return rect;
	}
	
	public Vec2D getAsTarget()
	{
		return this.hitbox.getHitbox().getOrigin();
	}
	
	public LinkedList<PlayerBullet> getBullets()
	{
		return this.bullets;
	}
	
	public int getCollisionGridInx()
	{
		return this.collisionGridInx;
	}
	public Set<Integer> getNeighbouringCellInxs()
	{
		return this.neighbouringCellInxs;
	}
	
	public CircleHitbox getHitbox()
	{
		return this.hitbox;
	}
	
	public boolean isInvincible()
	{
		return this.isInvincible;
	}
	
	public boolean hasLivesLeft()
	{
		return this.lives > 0;
	}

}
