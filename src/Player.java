import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.Map;

public class Player
{
	private Rect2D rect;
//	Cricle2D
	private Image texture;
	
	Player(Rect2D rect,String pathToTexture) throws IOException
	{
		this.rect = rect;
		
		File imageFile = new File(pathToTexture);
		
		if(!imageFile.canRead())
			throw new IOException("Image at " + pathToTexture + " cannot be accessed!");
		
		BufferedImage readImage = ImageIO.read(imageFile);
		this.texture = readImage.getScaledInstance((int)this.rect.getWidth(),(int)this.rect.getHeight(),Image.SCALE_SMOOTH);
	}
	
	public void update(KeyboardControls keyboard)
	{
		Map<Integer,Boolean> keyMap = keyboard.getKeyMap();
		if(keyMap.get(KeyEvent.VK_UP))
			this.rect.translate(.0f,-.75f);
		if(keyMap.get(KeyEvent.VK_DOWN))
			this.rect.translate(.0f,.75f);
		if(keyMap.get(KeyEvent.VK_LEFT))
			this.rect.translate(-0.75f,.0f);
		if(keyMap.get(KeyEvent.VK_RIGHT))
			this.rect.translate(0.75f,.0f);
			
	}
	
	public void draw(Graphics2D g)
	{
		Vec2D imagePos = rect.getPos();
		g.drawImage(texture,(int)imagePos.getX(),(int)imagePos.getY(),null);
	}
	
	public Rect2D getRect()
	{
		return rect;
	}
			
}
