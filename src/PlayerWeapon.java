import java.awt.Image;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

final class WepType // simulacija enum konstrukta
{
	public static final int MACHINEGUN = 0;
	public static final int LASER = 1;
	public static final int ROCKETS = 2;
}

public class PlayerWeapon
{
//	static final int bullX = 35; // TODO zbrisi pol 
//	static final int bullY = 15; // zbrisi pol
//	static final Vec2D bullSpd = new Vec2D(1.5f,0.f); // zbrisi pol
//	static final float shootCooldown = 250.f; // zbrisi pol
	
	private double lastShotAt;
	private int currWeaponType;
	private WeaponType[] wepaonTypes;
	
	PlayerWeapon()
	{
		this.lastShotAt = 0.;
		this.currWeaponType = WepType.MACHINEGUN;


		final Vec2D[] machinegunRelativeLocations = new Vec2D[] {
			new Vec2D(85.f,25.f),
			new Vec2D(85.f,150.f - 25.f - 8.f)
		};
		
//		final Vec2D[] laserRelativeLocations = new Vec2D[] {
//				
//		};
//		
//		final Vec2D[] rocketsRelativeLocations = new Vec2D[] {
//				
//		};
		
		this.wepaonTypes = new WeaponType[] {
			new WeaponType(20.f,8.f,new Vec2D(1.2f,.0f),1,250.f,".\\assets\\images\\weaponDefault.png",machinegunRelativeLocations)	
		};
	}
	
	public void update(float deltaTime)
	{

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
				final Vec2D speed = currWeapon.getSpeed();
				final int damage = currWeapon.getDamage();
				final Image texture = currWeapon.getTexture();
				
				bullets.add(new PlayerBullet(new Rect2D(bulletOrigin,bulletWidth,bulletHeight),speed,damage,texture));
			}
			this.lastShotAt = timer;
		}
	}
}
