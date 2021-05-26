import java.io.IOException;

public class Game 
{
	public static void main(String[] args) throws InterruptedException, IOException
	{
		Window win = new Window("Bullets & Rockets",1280,720);
		win.setVisible(true);
		
		final long fps = 60;
		final long frameDelay = (long)((1/(float)fps)*1e9); // koliko casa naj bo na ekranu vsaka slika v ns
		long lastFrameTime = System.nanoTime();
		while(true)
		{
			final long startOfFrame = System.nanoTime();
			
			final float deltaTime = (float)((startOfFrame - lastFrameTime)/1e6); // v ms
			
			// posodobimo stanje 
			win.updateState(deltaTime);
			// trenutno stanje narisemo
			win.draw();
			
			final long endOfFrame = System.nanoTime();
			final long delayThisFrame = Math.max(frameDelay - (endOfFrame - startOfFrame),0l);
			lastFrameTime = startOfFrame;
			
			// zakasnimo toliko, da je stevilo slik v sekundi priblizno enako
			Thread.sleep(delayThisFrame/(long)1e6,(int)(delayThisFrame % (long)(1e6)));
		}
	}
}