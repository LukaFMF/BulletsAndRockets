import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
	private Panel panel;
	Window(String name,int width,int height)
	{
		super(name);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(width,height);
		this.validate();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		try
		{
			this.setIconImage(Loader.loadImage(".\\assets\\images\\winIcon.png",100,100));			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		this.panel = new Panel(width,height);
		this.add(this.panel);
		this.pack();
		
		this.addWindowFocusListener(new WindowFocusListener()
		{
		        @Override
		        public void windowLostFocus(WindowEvent e)
		        {
		        	// ta funkcija poskrbi, da ob kliku iz okna, ne obdrzimo zadnjega vnosa s tipkovnice
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