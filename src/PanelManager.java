import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Map;
import java.util.Set;

final class GameState // simulacija enum konstrukta
{
	public static final int MAINMENU = 0;
	public static final int HELP = 1;
	public static final int GAME = 2;
	public static final int GAMEPAUSED = 3;
}

public class PanelManager
{
	private Window parentWindow;
	private int windowWidth;
	private int windowHeight;
	private MenuPanel menu;
	private GamePanel panel;
	private int gameState;
	private boolean shouldWinClose;
	
	
	PanelManager(Window win,int width,int height)
	{
		this.parentWindow = win;
		this.windowWidth = width;
		this.windowHeight = height;
		
		this.menu = new MenuPanel(this.windowWidth,this.windowHeight);
		this.parentWindow.add(this.menu);
		this.parentWindow.pack();
		
		this.gameState = GameState.MAINMENU;
		this.shouldWinClose = false;
	}
	
	public void update(float deltaTime,KeyboardControl keyboard)
	{
		switch(this.gameState)
		{
			case GameState.MAINMENU:
			{
				if(this.menu.gameShouldStart())
				{
					this.gameState = GameState.GAME;
					this.restartGame();
					this.menu.setVisible(false);
					break;
				}
				this.shouldWinClose = this.menu.shouldWindowClose();
				this.menu.updateState(deltaTime);
				break;
			}
			case GameState.GAME:
			{
				this.panel.updateState(deltaTime,keyboard);
				
				if(this.panel.shouldRetry())
				{
					this.parentWindow.remove(this.panel);
					this.restartGame();
				}
				else if(this.panel.shouldGoToMenu())
				{
					this.menu.setVisible(true);
					this.menu.resetVars();
					this.parentWindow.remove(this.panel);
					this.gameState = GameState.MAINMENU;
				}
				break;
			}
		}
	}
	
	public void draw()
	{
		switch(this.gameState)
		{
			case GameState.MAINMENU:
			{
				this.menu.repaint();
				break;
			}
			case GameState.GAME:
			{
				this.panel.repaint();
				break;
			}
		}
	}
	
	public void restartGame()
	{
		this.panel = new GamePanel(this.windowWidth,this.windowHeight);
		this.parentWindow.add(this.panel);
		this.parentWindow.pack();
		this.hideAllPanels();
		this.panel.setVisible(true);
	}
	
	public boolean shouldWindowClose()
	{
		return this.shouldWinClose;
	}
	
	public void hideAllPanels()
	{
		this.panel.setVisible(false);
		this.menu.setVisible(false);
	}
}
