import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyboardControl
{
	private Map<Integer,Boolean> keyMap;
	
	KeyboardControl()
	{
		keyMap = new HashMap<Integer,Boolean>();
		
		keyMap.put(KeyEvent.VK_UP,false);
		keyMap.put(KeyEvent.VK_DOWN,false);
		keyMap.put(KeyEvent.VK_LEFT,false);
		keyMap.put(KeyEvent.VK_RIGHT,false);
		keyMap.put(KeyEvent.VK_SPACE,false);
		keyMap.put(KeyEvent.VK_TAB,false);
		keyMap.put(KeyEvent.VK_SHIFT,false);
	}
	
	public Map<Integer,Boolean> getKeyMap()
	{
		return this.keyMap;
	}
}
