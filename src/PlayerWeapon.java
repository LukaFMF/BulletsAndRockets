import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

final class WepType // simulacija enum konstrukta
{
	public static final int BULLETS = 0;
	public static final int LASER = 1;
	public static final int ROCKETS = 2;
}

public class PlayerWeapon
{

	private double lastShotAt;
	private double lastSwitchedAt;
	private int currWeaponType;
	private WeaponType[] wepaonTypes;
	
	PlayerWeapon(Rect2D shipBoundingBox)
	{
		this.lastShotAt = 0.;
		this.lastSwitchedAt= 0.;
		this.currWeaponType = WepType.BULLETS;
		
		final float shipWidth = shipBoundingBox.getWidth();
		final float shipHeight = shipBoundingBox.getHeight();

		final float bulletWidth = 15.f;
		final float bulletHeight = 6.f;
		
		final float laserWidth = 50.f;
		final float laserHeight = 5.f;
		
		final float rocketWidth = 60.f;
		final float rocketHeight = 20.f;

		final Vec2D[] machinegunRelativeLocations = new Vec2D[] {
			new Vec2D(shipWidth - 30.f,27.f),
			new Vec2D(shipWidth - 30.f,shipHeight - bulletHeight - 28.f)
		};
		
		final Vec2D[] laserRelativeLocations = new Vec2D[] {
				new Vec2D(shipWidth - 28.f,26.f),
				new Vec2D(shipWidth - 28.f,shipHeight - laserHeight - 26.f)
		};
		
		final Vec2D[] rocketsRelativeLocations = new Vec2D[] {
				new Vec2D(shipWidth - 87.f,14.f),
				new Vec2D(shipWidth - 87.f,shipHeight - rocketHeight - 14.f)
		};
		
		this.wepaonTypes = new WeaponType[] {
			new WeaponType(bulletWidth,bulletHeight,new Vec2D(1.2f,.0f),new Vec2D(0.f,0.f),1,150.f,shipBoundingBox,".\\assets\\images\\machinegunWeapon.png",".\\assets\\images\\machinegunBullet.png",machinegunRelativeLocations),
			new WeaponType(laserWidth,laserHeight,new Vec2D(3.f,.0f),new Vec2D(0.f,0.f),2,250.f,shipBoundingBox,".\\assets\\images\\laserWeapon.png",".\\assets\\images\\laserShot.png",laserRelativeLocations),
			new WeaponType(rocketWidth,rocketHeight,new Vec2D(0.3f,.0f),new Vec2D(.2f,0.f),3,350.f,shipBoundingBox,".\\assets\\images\\rocketWeapon.png",".\\assets\\images\\rocket.png",rocketsRelativeLocations)
		};
	}
	
	public void update(float deltaTime)
	{
		
	}
	
	public void draw(Graphics2D g,Vec2D shipOrigin)
	{
		g.drawImage(this.wepaonTypes[this.currWeaponType].getWeaponTexture(),(int)shipOrigin.getX(),(int)shipOrigin.getY(),null);
	}
	
	public void tryToShoot(Rect2D shipPosition,double timer,LinkedList<PlayerBullet> bullets) // funkcija modificira spremenljivko bullets
	{
		final WeaponType currWeapon = this.wepaonTypes[this.currWeaponType];
		if(timer - this.lastShotAt > currWeapon.getShootCooldown())
		{
			for(Vec2D relativeLocation : currWeapon.getRelativeSpawnLocations())
			{
				final Vec2D shipOrigin = shipPosition.getOrigin().clone();
				shipOrigin.translate(relativeLocation);
				
				final Vec2D bulletOrigin = shipOrigin;
				final float bulletWidth = currWeapon.getBulletWidth();
				final float bulletHeight = currWeapon.getBulletHeight();
				final Vec2D speed = currWeapon.getSpeed().clone(); 
				final Vec2D acceleration = currWeapon.getAcceleration();
				final int damage = currWeapon.getDamage();
				final Image bulletTexture = currWeapon.getBulletTexture();
				
				bullets.add(new PlayerBullet(new Rect2D(bulletOrigin,bulletWidth,bulletHeight),speed,acceleration,damage,bulletTexture));
			}
			this.lastShotAt = timer;
		}
	}
	
	public void switchWeapon(double timer)
	{
		if(timer - this.lastSwitchedAt > 1500.f) // igralec lahko spremeni orozje le vsake 1.5s
		{
			this.currWeaponType++;
			if(this.currWeaponType == this.wepaonTypes.length)
				this.currWeaponType = 0;
			this.lastSwitchedAt = timer;
		}
	}
	
	public void reset()
	{
		this.lastShotAt = 0.;
	}
}
