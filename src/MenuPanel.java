import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class MenuPanel extends JPanel
{
	private int panelWidth;
	private int panelHeight;
	private double timer;
	private Background background;
	private Rect2D logoBoundingBox;
	private Image logoTexture;
	private Vec2D logoBobbing;
	private Rect2D sunBoundingBox;
	private Image sunTexture;
	private JButton playButton;
	private JButton helpButton;
	private JButton quitButton;
	private boolean gameStart;
	private boolean showHelp;
	private boolean closeWindow;
	
	MenuPanel(int width,int height)
	{
		super();
		this.panelWidth = width;
		this.panelHeight = height;
		this.setPreferredSize(new Dimension(this.panelWidth,this.panelHeight));
		this.setLayout(null);
		
		this.timer = 0.;
		
		this.background = new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight);

		this.gameStart = false;
		this.showHelp = false;
		this.closeWindow = false;
		
		this.sunBoundingBox = new Rect2D(850.f,50.f,200.f,200.f);
		this.logoBobbing = new Vec2D(0.f,0.f);
		this.logoBoundingBox = new Rect2D(this.panelWidth/2 - 250,50.f,500.f,300.f);
		try 
		{			
			this.sunTexture = Loader.loadImage(".\\assets\\images\\sun.png",(int)this.sunBoundingBox.getWidth(),(int)this.sunBoundingBox.getHeight());
			this.logoTexture = Loader.loadImage(".\\assets\\images\\logo.png",(int)this.logoBoundingBox.getWidth(),(int)this.logoBoundingBox.getHeight());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.playButton = new JButton("Play");
		this.playButton.setBounds(this.panelWidth/2 - 36,400,72,21);
		this.playButton.setFocusable(false);
		this.playButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				gameStart = true;
			}
		});
		this.add(this.playButton);
		
		
		this.helpButton = new JButton("Help");
		this.helpButton.setBounds(this.panelWidth/2 - 36,430,72,21);
		this.helpButton.setFocusable(false);
		this.helpButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				showHelp = true;
			}
		});
		this.add(this.helpButton);
		
		this.quitButton = new JButton("Quit");
		this.quitButton.setBounds(this.panelWidth/2 - 36,460,72,21);
		this.quitButton.setFocusable(false);
		this.quitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				closeWindow = true;
			}
		});
		this.add(this.quitButton);
	}
	
	public void updateState(float deltaTime)
	{
		this.timer += deltaTime;
		this.background.update(deltaTime);
		
		this.logoBobbing.setY((float)(Math.sin(this.timer/1e3) * 0.1f));
		this.logoBoundingBox.translate(this.logoBobbing);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D)g;
		
		final Vec2D logoOrigin = this.logoBoundingBox.getOrigin();
		
		this.background.draw(graphics);
		graphics.drawImage(this.sunTexture,850,50,null);
		graphics.drawImage(this.logoTexture,(int)logoOrigin.getX(),(int)logoOrigin.getY(),null);
	}
	
	public boolean gameShouldStart()
	{
		return this.gameStart;
	}
	
	public boolean helpShouldShow()
	{
		return this.showHelp;
	}
	
	public boolean shouldWindowClose()
	{
		return this.closeWindow;
	}
	
	public void resetVars()
	{
		this.gameStart = false;
		this.showHelp = false;
		this.closeWindow = false;
	}
}
