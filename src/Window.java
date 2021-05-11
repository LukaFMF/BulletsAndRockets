import java.awt.*;
import javax.swing.*;

public class Window extends JFrame
{
	private JPanel panel;
	Window(String name,int width,int height)
	{
		super();
		this.setTitle(name);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(width,height));
		this.setResizable(false);
		this.setLayout(new BorderLayout());

		this.panel = new JPanel();
		this.panel.setBackground(Color.GRAY);
	}
}

