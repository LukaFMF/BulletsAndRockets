import java.awt.Image;
import java.io.IOException;

public class BossType
{
	private int id;
	private int hp;
	private float boundingBoxWidth;
	private float boundingBoxHeight;
	private float pixelsPerMilli;
	private Circle2D[] hitboxRelativeLocations;
	private Image[] textures;
	private float[] shootCooldowns;
	private BulletPattern[] patterns;
	private Image[] shieldTextures;
	private int stages;
	
	BossType(int id,int hp,float width,float height,float speed,Circle2D[] hitboxRelativeLocations,String[] pathsToTextures,float[] shootCooldowns,BulletPattern[] bulletPatterns,String pathToShieldTexture,int stages)
	{
		this.id = id;
		this.hp = hp;
		this.boundingBoxWidth = width;
		this.boundingBoxHeight = height;
		this.pixelsPerMilli = speed;
		this.hitboxRelativeLocations = hitboxRelativeLocations;
		this.shootCooldowns = shootCooldowns;
		this.patterns = bulletPatterns;
		this.stages = stages;
		
		this.textures = new Image[pathsToTextures.length];
		this.shieldTextures = new Image[this.hitboxRelativeLocations.length];
		try
		{
			for(int i = 0;i < this.textures.length;i++)
				this.textures[i] = Loader.loadImage(pathsToTextures[i],(int)this.boundingBoxWidth,(int)this.boundingBoxHeight);
			
			for(int i = 0;i < this.shieldTextures.length;i++)
			{
				final float radiusX2PlusOffest = this.hitboxRelativeLocations[i].getRadius()*2 + 10.f;
				this.shieldTextures[i] = Loader.loadImage(pathToShieldTexture,(int)radiusX2PlusOffest,(int)radiusX2PlusOffest);
			}

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getHp()
	{
		return this.hp;
	}
	
	public float getWidth()
	{
		return this.boundingBoxWidth;
	}
	
	public float getHeight()
	{
		return this.boundingBoxHeight;
	}
	
	public float getSpeed()
	{
		return this.pixelsPerMilli;
	}
	
	public Image[] getTextures()
	{
		return this.textures;
	}
	
	public float[] getShootCooldowns()
	{
		return this.shootCooldowns;
	}
	
	public BulletPattern[] getBulletPatterns()
	{
		return this.patterns;
	}
	
	public Circle2D[] getHitboxRelativeLocations()
	{
		return this.hitboxRelativeLocations;
	}
	
	public Image[] getShieldTextures()
	{
		return this.shieldTextures;
	}
	
	public int getStages()
	{
		return this.stages;
	}
}
