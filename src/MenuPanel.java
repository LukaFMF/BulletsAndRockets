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
	private Rect2D logoBox;
	private Vec2D logoBobbing;
	private Image logoTexture;
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
//		try
//		{
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		this.gameStart = false;
		this.showHelp = false;
		this.closeWindow = false;
		
		this.logoBobbing = new Vec2D(0.f,0.f);
		this.logoBox = new Rect2D(this.panelWidth/2 - 250,50,500,300);
		try 
		{			
			this.logoTexture = Loader.loadImage(".\\assets\\images\\logo.png",(int)this.logoBox.getWidth(),(int)this.logoBox.getHeight());
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
		this.logoBox.translate(this.logoBobbing);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D)g;
		
		final Vec2D logoOrigin = this.logoBox.getOrigin();
		
		this.background.draw(graphics);
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
