import java.awt.*;
import java.io.IOException;

public class WeaponType
{
	private float bulletWidth;	
	private float bulletHeight;	
	private Vec2D speed;
	private float shootCooldown;
	private int damage; // moc vsakega metka
	private Image texture;
	// velikost tabele pove koliko metkov nastane of enem strelu
	// vsake lokacija, pa pove kje se metek ob strelu pojavi glede na pozicijo ladje
	private Vec2D[] relativeSpawnLocations; 
	
	WeaponType(float width,float height,Vec2D speed,int damage,float shootCooldown,String pathToFile/* texture file*/,Vec2D[] relativeSpawnLocations)
	{
		this.bulletWidth = width;
		this.bulletHeight = height;
		this.speed = speed;
		this.damage = damage;
		this.shootCooldown = shootCooldown;
		
		try
		{			
			this.texture = Loader.loadImage(pathToFile,(int)this.bulletWidth,(int)this.bulletHeight);
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

	public float getShootCooldown()
	{
		return this.shootCooldown;
	}

	public int getDamage()
	{
		return this.damage;
	}

	public Image getTexture()
	{
		return this.texture;
	}

	public Vec2D[] getRelativeSpawnLocations()
	{
		return this.relativeSpawnLocations;
	}
	
}
