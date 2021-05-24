import java.awt.*;

public class Text
{
	private String text;
	private Font font;
	private float duration;
	private Vec2D origin;
	private Color color;
	private boolean expired;
	private double firstDraw;
	
	Text(String text,Vec2D origin,int fontSize,float duration,Color color) //origin je v tem primeru v levem *spodnjem* kotu!
	{
		this.text = text;
		//this.fontSize = fontSize;
		this.duration = duration;
		this.origin = origin; 
		this.color = color;
		this.font = new Font("Dialog",Font.PLAIN,fontSize);
		this.expired = false;
		this.firstDraw = 0.;
	}
	
	public void update(double timer)
	{
		if(this.firstDraw == 0.)
			this.firstDraw = timer;
		if(timer - this.firstDraw > this.duration)
			this.expired = true;
	}
	
	public void draw(Graphics2D g)
	{
		g.setColor(this.color);
		g.setFont(this.font);
		g.drawString(this.text,this.origin.getX(),this.origin.getY());
	}
	
	public boolean isExpired()
	{
		return this.expired;
	}
}
