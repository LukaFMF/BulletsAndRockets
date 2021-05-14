import java.awt.*;
import java.io.IOException;

public class Background 
{
	private Image backgroundTexture;
	private Image sunTexture;
	private float startPos;
	private float endPos;
	private float currentPos;
	private int backgroundWidth;
	
	Background(String pathToFile,int windowWidth,int windowHeight)
	{
		this.startPos = windowWidth;
		this.endPos = 0;
		this.currentPos = this.startPos;
		this.backgroundWidth = windowWidth;
		
		try
		{			
			this.backgroundTexture = Loader.loadImage(pathToFile, windowWidth, windowHeight);
			this.sunTexture = Loader.loadImage(".\\assets\\images\\sun.png",200,200);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public void update(float deltaTime)
	{
		final float pixelsPerMilli = .25f;
		this.currentPos -= pixelsPerMilli * deltaTime;
		if(this.currentPos <= this.endPos)
			this.currentPos = this.startPos;
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(this.backgroundTexture,(int)this.currentPos - this.backgroundWidth,0,null);
		g.drawImage(this.backgroundTexture,(int)this.currentPos,0,null);
		
		g.drawImage(this.sunTexture,850,50,null); // sonce
	}
	
}
