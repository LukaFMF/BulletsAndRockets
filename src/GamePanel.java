import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class GamePanel extends JPanel
{
	private int panelWidth; // le za informacijo o velikosti okna
	private int panelHeight;
	private int currLevel;
	private double timer;
	private Player player;
	private Background background;
	private KeyboardControls keyboard;
	private CollisionGrid grid;
	private LinkedList<CircleEnemy> circleEnemies;
	private LinkedList<RectEnemy> rectEnemies;
	private LinkedList<EnemyBullet> enemyBullets;
	private EnemyType[] enemyTypes;
	private Level[] levels;
	private TextRenderer textRend;
	
	
	GamePanel(int width,int height) throws IOException
	{
		super();
		
		this.panelWidth = width;
		this.panelHeight = height;
		this.setPreferredSize(new Dimension(this.panelWidth,this.panelHeight));
		this.timer = 0.;
		
		this.grid = new CollisionGrid(10,this.panelWidth,this.panelHeight);
		
		this.currLevel = 0;
		this.player = new Player(new Rect2D(50.f,300.f,100.f,150.f),this.panelWidth,this.panelHeight,this.grid);
		
		this.keyboard = new KeyboardControls();
		
		this.background = new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight);
		
		this.circleEnemies = new LinkedList<CircleEnemy>();
		this.rectEnemies = new LinkedList<RectEnemy>();
		this.enemyBullets = new LinkedList<EnemyBullet>();
		
		textRend = new TextRenderer();
		
		try
		{
			Image enemyBulletTexture = Loader.loadImage(".\\assets\\images\\enemyBullet.png",30,30);
			this.enemyTypes = new EnemyType[] {
				new EnemyType(0,100.f,100.f,true,.3f,7,".\\assets\\images\\enemy0.png",1500.f,
						(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
						{
							final Vec2D enemyOrigin = location.getOrigin();
							final float enemyWidth = location.getWidth();
							final float enemyHeight = location.getHeight();
							enemyBullets.add(new EnemyBullet(new Rect2D(enemyOrigin.getX() - 10.f,enemyOrigin.getY() + enemyHeight/2 - 15.f,30.f,30.f),new Vec2D(-.7f,.0f),enemyBulletTexture,this.grid));
						}),
			};
			
			this.levels = new Level[] {
				new Level(".\\assets\\levels\\lvl1.txt")
			};
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	public void updateState(float deltaTime) // cas v ms
	{
		// IMPORTANT: global timer
		this.timer += deltaTime;
		
		
		this.player.update(this.timer,deltaTime,this.keyboard);
		this.background.update(deltaTime);
		
		final Vec2D target = this.player.getAsTarget();
		
		this.levels[this.currLevel].update(this.timer,this.circleEnemies,this.rectEnemies,this.enemyTypes);
		
		for(int i = 0;i < this.circleEnemies.size();)
		{
			CircleEnemy currEnemy = this.circleEnemies.get(i);
			currEnemy.update(deltaTime,this.timer,this.enemyBullets,target,this.player.getBullets());
			if(!currEnemy.isAlive())
				this.circleEnemies.remove(i);
			else
				i++;
		}
		
		for(int i = 0;i < this.rectEnemies.size();)
		{
			RectEnemy currEnemy = this.rectEnemies.get(i);
			currEnemy.update(deltaTime,this.timer,this.enemyBullets,target,this.player.getBullets());
			
			if(!currEnemy.isAlive())
				this.rectEnemies.remove(i);
			else
				i++;
		}
		
		for(int i = 0;i < this.enemyBullets.size();)
		{
			EnemyBullet currBullet = enemyBullets.get(i);
			final Set<Integer> cellInxsNearPlayer = this.player.getNeighbouringCellInxs(); 
			
			if(cellInxsNearPlayer.contains(currBullet.getCollisionGridInx()))
			{
				final CircleHitbox playerHitbox = this.player.getHitbox();
				final CircleHitbox enemyBulletHitbox =  currBullet.getHitbox();
				
				if(!this.player.isDestroyed() && !this.player.isInvincible() && playerHitbox.collidesWith(enemyBulletHitbox))
				{
					this.player.destroy(this.timer,this.textRend);
					currBullet.destroy();
				}
			}
			
			currBullet.update(deltaTime,this.panelWidth,this.panelHeight);
			if(currBullet.hasBeenDestroyed())
				this.enemyBullets.remove(i);
			else
				i++;
		}
		
		this.textRend.update(timer);
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D graphics = (Graphics2D)g;
		
		this.background.draw(graphics);
		//this.grid.draw(graphics);
		if(!this.player.isDestroyed())
			this.player.draw(graphics);
		
		for(CircleEnemy circleEnemy : this.circleEnemies)
			circleEnemy.draw(graphics);
		
		for(RectEnemy rectEnemy : this.rectEnemies)
			rectEnemy.draw(graphics);
		
		for(EnemyBullet enemyBullet : this.enemyBullets)
			enemyBullet.draw(graphics);
		
		this.textRend.draw(graphics);
	}
	
	public KeyboardControls getKeyboard()
	{
		return this.keyboard;	
	}
}
