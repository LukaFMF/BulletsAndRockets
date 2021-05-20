import java.awt.Image;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

public class PlayerWeapon
{
	static final int bullX = 35; // TODO zbrisi pol 
	static final int bullY = 15; // zbrisi pol
	static final Vec2D bullSpd = new Vec2D(1.5f,0.f); // zbrisi pol
	static final float shootCooldown = 250.f; // zbrisi pol
	
	private double lastShotAt;
	private Image[] bulletTextures;
	
	PlayerWeapon()
	{
		this.lastShotAt = 0.;

		try
		{
			bulletTextures = new Image[] {
				Loader.loadImage(".\\assets\\images\\weaponDefault.png",bullX,bullY)
			};
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void update(float deltaTime)
	{

	}
	
	public void tryToShoot(Rect2D shipPosition,double timer,LinkedList<PlayerBullet> bullets) // funkcija modificira spremenljivko bullets
	{
		if(timer - this.lastShotAt > shootCooldown)
		{
			this.lastShotAt = timer;
			
			final float bulletX = shipPosition.getOrigin().getX() + shipPosition.getWidth(); 
			final float bulletY = shipPosition.getOrigin().getY() + shipPosition.getHeight()/2 - bullY/2;
			
			final Rect2D bulletPos = new Rect2D(bulletX,bulletY,bullX,bullY);
			
			bullets.add(new PlayerBullet(bulletPos,bullSpd,bulletTextures[0]));
		}
	}
}
