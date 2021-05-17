import java.awt.*;
import java.io.IOException;

public class WeaponType
{
	private Rect2D bullet;
	private Vec2D speed;
	private float shootCooldown;
	private int damage; // moc vsakega metka
	private Image texture;
	// velikost tabele pove koliko metkov nastane of enem strelu
	// vsake lokacija, pa pove kje se metek ob strelu pojavi glede na pozicijo ladje
	private Vec2D[] relativeSpawnLocations; 
	
	WeaponType(Rect2D bullet,Vec2D speed,int damage,float shootCooldown,String pathToFile/* texture file*/,Vec2D[] relativeSpawnLocations)
	{
		this.bullet = bullet;
		this.speed = speed;
		this.damage = damage;
		this.shootCooldown = shootCooldown;
		
		try
		{			
			this.texture = Loader.loadImage(pathToFile,(int)this.bullet.getWidth(),(int)this.bullet.getHeight());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.relativeSpawnLocations = relativeSpawnLocations;
	}
	
	
	
}
