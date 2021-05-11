import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
	private Panel panel;
	Window(String name,int width,int height)
	{
		super();
		this.setTitle(name);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(width,height));
		this.setResizable(false);
		this.setLayout(new BorderLayout());

		this.panel = new Panel();
		this.add(this.panel,BorderLayout.CENTER);
		
		this.addWindowFocusListener(new WindowFocusListener()
		{
		        @Override
		        public void windowLostFocus(WindowEvent e)
		        {
		        	// ta funkcija poskrbi, da ob kliku iz okna, ne obdrzimo zadnjega vnosa s tipkovnices
		        	Map<Integer,Boolean> keyMap = panel.getKeyboard().getKeyMap();
		        	Set<Integer> keys = keyMap.keySet();
		        	
		        	for(Integer key : keys)
		        		keyMap.put(key,false);
		        }
		        
		        @Override
		        public void windowGainedFocus(WindowEvent e){}

		});
		
		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				Map<Integer,Boolean> keyboard = panel.getKeyboard().getKeyMap();
				
				final int keyCode = e.getKeyCode();
				if(keyboard.containsKey(keyCode) && !keyboard.get(keyCode))
					keyboard.put(keyCode,true);
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
				Map<Integer,Boolean> keyboard = panel.getKeyboard().getKeyMap();
				
				final int keyCode = e.getKeyCode();
				if(keyboard.containsKey(keyCode))
					keyboard.put(keyCode,false);
			}
			
			@Override
			public void keyTyped(KeyEvent e){}
		});
	}
	
	public Panel getMainPanel()
	{
		return this.panel;
	}
}