import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.io.*;
import java.util.Map;

public class Panel extends JPanel
{
	private int panelWidth; // le za informacijo o velikosti okna
	private int panelHeight;
	private Player player;
	private Background background;
	private KeyboardControls keyboard;
	
	
	Panel(int width,int height)
	{
		super();
		
		this.panelWidth = width;
		this.panelHeight = height;
		this.setPreferredSize(new Dimension(this.panelWidth,this.panelHeight));
		
		this.player = new Player(new Rect2D(50.f,300.f,100.f,150.f),this.panelWidth,this.panelHeight);
		
		this.keyboard = new KeyboardControls();
		
		this.background = new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight);
		
		
		
	}
	
	public void updateState(float deltaTime) // cas v ms
	{
		this.player.update(deltaTime,this.keyboard,this.panelWidth,this.panelHeight);
		this.background.update(deltaTime);
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D graphics = (Graphics2D)g;
		
		this.background.draw(graphics);
		this.player.draw(graphics);

	}
	
	public KeyboardControls getKeyboard()
	{
		return this.keyboard;	
	}
}
