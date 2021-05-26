import java.awt.*;
import java.io.IOException;

public class WeaponType
{
	private float bulletWidth;	
	private float bulletHeight;	
	private Vec2D speed;
	private Vec2D acceleration;
	private float shootCooldown;
	private int damage; // moc vsakega metka
	private Image weaponTexture;
	private Image bulletTexture;
	// velikost tabele pove koliko metkov nastane ob enem strelu
	// vsaka lokacija, pa pove kje se metek ob strelu pojavi glede na pozicijo ladje
	private Vec2D[] relativeSpawnLocations; 
	
	WeaponType(float width,float height,Vec2D speed,Vec2D acceleration,int damage,float shootCooldown,Rect2D shipBoundingBox,String pathToWeaponTexture,String pathToBulletTexture,Vec2D[] relativeSpawnLocations)
	{
		this.bulletWidth = width;
		this.bulletHeight = height;
		this.speed = speed;
		this.acceleration = acceleration;
		this.damage = damage;
		this.shootCooldown = shootCooldown;
		
		try
		{			
			this.weaponTexture = Loader.loadImage(pathToWeaponTexture,(int)shipBoundingBox.getWidth(),(int)shipBoundingBox.getHeight());
			this.bulletTexture = Loader.loadImage(pathToBulletTexture,(int)this.bulletWidth,(int)this.bulletHeight);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.relativeSpawnLocations = relativeSpawnLocations;
	}

	public float getBulletWidth()
	{
		return this.bulletWidth;
	}

	public float getBulletHeight()
	{
		return this.bulletHeight;
	}

	public Vec2D getSpeed()
	{
		return this.speed;
	}
	
	public Vec2D getAcceleration()
	{
		return this.acceleration;
	}

	public float getShootCooldown()
	{
		return this.shootCooldown;
	}

	public int getDamage()
	{
		return this.damage;
	}

	public Image getWeaponTexture()
	{
		return this.weaponTexture;
	}
	
	public Image getBulletTexture()
	{
		return this.bulletTexture;
	}

	public Vec2D[] getRelativeSpawnLocations()
	{
		return this.relativeSpawnLocations;
	}
	
}
