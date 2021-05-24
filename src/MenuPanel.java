import java.awt.*;
import javax.swing.*;

public class MenuPanel extends JPanel
{
	private int panelWidth;
	private int panelHeight;
	private Background background;
	private Rect2D logoBox;
	private Image logoTexture;
	private JButton playButton;
	private JButton helpButton;
	private JButton quitButton;
	
	MenuPanel(int width,int height)
	{
		super();
		this.panelWidth = width;
		this.panelHeight = height;
		this.setPreferredSize(new Dimension(this.panelWidth,this.panelHeight));
		
		this.background = new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight);
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.playButton = new JButton("Play");
		this.playButton.setBounds(this.panelWidth/2 - 36,300,72,21);
		this.add(this.playButton);
	}
	
	public void updateState(float deltaTime)
	{
		this.background.update(deltaTime);
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D graphics = (Graphics2D)g;
		
		this.background.draw(graphics);
	}
	
	public JButton getPlayButton()
	{
		return this.playButton;
	}
}
