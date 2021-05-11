import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.io.*;
import java.util.Map;

public class Panel extends JPanel
{
	private Player player;
	private KeyboardControls keyboard;
	Panel()
	{
		super();
		//this.setBackground(Color.GRAY);
		
		try
		{
			this.player = new Player(new Rect2D(50.f,300.f,100.f,150.f),".\\assets\\images\\ship.png");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.keyboard = new KeyboardControls();
	}
	
	public void updateState()
	{
		player.update(this.keyboard);
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D graphics = (Graphics2D)g;
		this.player.draw(graphics);
//		graphics.setStroke(new BasicStroke(1.5f));
//		graphics.setColor(Color.BLUE);
//		
//		graphics.drawOval(50,50,100,100);
	}
	
	public KeyboardControls getKeyboard()
	{
		return this.keyboard;	
	}
}
