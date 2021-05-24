import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class CollisionGrid
{
	private int gridSize;
	private float cellWidth;
	private float cellHeight;
	CollisionGrid(int gridSize,int windowWidth,int windowHeight)
	{
		this.gridSize = gridSize;
		this.cellWidth = (float)windowWidth/gridSize;
		this.cellHeight = (float)windowHeight/gridSize;
	}
	
	public int getGridSize()
	{
		return this.gridSize;
	}
	
	public float getCellWidth()
	{
		return this.cellWidth;
	}
	
	public float getCellHeight()
	{
		return this.cellHeight;
	}
	
	public void draw(Graphics2D g)
	{
		g.setStroke(new BasicStroke(1.f));
		g.setColor(Color.RED);
		
		for(int i = 0;i < this.gridSize;i++)
			for(int j = 0;j < this.gridSize;j++)
				g.drawRect(j*(int)this.cellWidth,i*(int)this.cellHeight,(int)this.cellWidth,(int)this.cellHeight);
	}
}
