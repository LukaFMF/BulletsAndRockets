public class Game 
{
	public static void main(String[] args) throws InterruptedException
	{
		Window win = new Window("Bullets & Rockets",1280,720);
		win.setVisible(true);
		
		final long fps = 60;
		final long frameDelay = (long)((1/(float)fps)*1e9);
		long lastFrameTime = System.nanoTime();
		Panel mainPanel = win.getMainPanel();
		while(true)
		{
			final long startOfFrame = System.nanoTime();
			// draw here
			mainPanel.updateState((float)((startOfFrame - lastFrameTime)/1e6));
						
			mainPanel.repaint();
			
			final long endOfFrame = System.nanoTime();
			final long delayThisFrame = Math.max(frameDelay - (endOfFrame - startOfFrame),0l);
			lastFrameTime = startOfFrame;
			
			Thread.sleep(delayThisFrame/(long)1e6,(int)(delayThisFrame % (long)(1e6)));
		}
	}
}