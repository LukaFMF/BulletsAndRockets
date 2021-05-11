public class Game {
	public static void main(String[] args) throws InterruptedException
	{
		Window win = new Window("Bullets & Rockets",1280,720);
		win.setVisible(true);
		
		final long fps = 60;
		final long frameDelay = (1/fps)*1_000_000_000;
		while(true)
		{
			final long startOfFrame = System.nanoTime();
			
			// draw here
			win.repaint();
			
			final long endOfFrame = System.nanoTime();
			
			final long delayThisFrame = Math.max(frameDelay - (endOfFrame - startOfFrame),0l); 
			Thread.sleep(0,(int)delayThisFrame);
		}
	}
}