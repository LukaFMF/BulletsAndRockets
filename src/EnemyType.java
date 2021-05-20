import java.awt.*;
import java.io.IOException;

public class EnemyType
{
	private int id;
	private float boundingBoxWidth;
	private float boundingBoxHeight;	
	private boolean circleHitbox;
	private float pixelsPerMilli;
	private int hp;
	private Image texture;
	//private Image bulletTexture;
	
	EnemyType(int id,float width,float height,boolean circleHitbox,float speed,int hp,String pathToFile) throws Exception
	{
		this.id = id;
		this.boundingBoxWidth = width; 
		this.boundingBoxHeight = height; 
		this.circleHitbox = circleHitbox;
		this.pixelsPerMilli = speed;
		this.hp = hp;
		
		if(this.circleHitbox && this.boundingBoxWidth != this.boundingBoxHeight)
			throw new Exception("If circular hitbox is selected, width and height of the bounding box must be equal!");
		
		try
		{
			this.texture = Loader.loadImage(pathToFile,(int)this.boundingBoxWidth,(int)this.boundingBoxHeight);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public int getId()
	{
		return this.id;
	}

	public float getBoundingBoxWidth()
	{
		return this.boundingBoxWidth;
	}
	
	public float getBoundingBoxHeight()
	{
		return this.boundingBoxHeight;
	}

	public boolean hasCircleHitbox()
	{
		return this.circleHitbox;
	}

	public float getSpeed()
	{
		return this.pixelsPerMilli;
	}
	
	public int getHp()
	{
		return this.hp;
	}

	public Image getTexture()
	{
		return this.texture;
	}

//	public Image getBulletTexture()
//	{
//		return this.bulletTexture;
//	}
	
}
