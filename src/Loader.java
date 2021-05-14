import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Loader 
{
	public static Image loadImage(String pathToFile,int desiredWidth,int desiredHeight) throws IOException
	{
		File imageFile = new File(pathToFile);
		
		if(!imageFile.canRead())
			throw new IOException("Image at " + pathToFile + " cannot be accessed!");
		
		BufferedImage readImage = ImageIO.read(imageFile);
		
		return readImage.getScaledInstance(desiredWidth,desiredHeight,Image.SCALE_SMOOTH);
	}
}
