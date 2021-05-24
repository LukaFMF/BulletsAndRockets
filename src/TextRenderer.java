import java.awt.Graphics2D;
import java.util.LinkedList;

public class TextRenderer
{
	LinkedList<Text> texts;

	TextRenderer()
	{
		this.texts = new LinkedList<Text>();
	}
	
	public void update(double timer)
	{
		for(int i = 0;i < this.texts.size();)
		{
			final Text currText = this.texts.get(i);
			
			currText.update(timer);
			if(currText.isExpired())
				this.texts.remove(i);
			else
				i++;
		}
	}
	
	public void draw(Graphics2D g)
	{
		for(Text text : this.texts)
			text.draw(g);
	}
	
	public void add(Text text)
	{
		this.texts.add(text);
	}
}
