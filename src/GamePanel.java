import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private CollisionGrid grid;
	private LinkedList<CircleEnemy> circleEnemies;
	private LinkedList<RectEnemy> rectEnemies;
	private LinkedList<EnemyBullet> enemyBullets;
	private EnemyType[] enemyTypes;
	private Level[] levels;
	
	private boolean buttonsEnabled;
	private Rect2D gameoverLocation;
	private Image gameoverTexture;
	private JButton retryButton;
	private JButton backToMenuButton;
	
	private boolean shouldRetry;
	private boolean shouldGoToMenu;
	
	
	GamePanel(int width,int height)
	{
		super(); 
		
		this.panelWidth = width;
		this.panelHeight = height;
		this.setPreferredSize(new Dimension(this.panelWidth,this.panelHeight));
		this.setLayout(null);
		this.timer = 0.;
		
		this.grid = new CollisionGrid(10,this.panelWidth,this.panelHeight);
		
		this.currLevel = 0;
		this.player = new Player(new Rect2D(150.f,this.panelHeight/2 - 75.f,100.f,150.f),this.panelWidth,this.panelHeight,this.grid);
		
		this.background = new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight);
		
		this.circleEnemies = new LinkedList<CircleEnemy>();
		this.rectEnemies = new LinkedList<RectEnemy>();
		this.enemyBullets = new LinkedList<EnemyBullet>();
		
		//textRend = new TextRenderer();
		
		this.gameoverLocation = new Rect2D(this.panelWidth/2 - 300,this.panelHeight/2 - 125,600,250);
		
		this.buttonsEnabled = false;
		this.retryButton = new JButton("Retry");
		this.retryButton.setBounds(500,400,72,21);
		this.retryButton.setFocusable(false);
		this.retryButton.setVisible(false);
		this.retryButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				shouldRetry = true;
			}
		});
		this.add(this.retryButton);
		
		this.backToMenuButton = new JButton("Menu");
		this.backToMenuButton.setBounds(708,400,72,21);
		this.backToMenuButton.setFocusable(false);
		this.backToMenuButton.setVisible(false);
		this.backToMenuButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				shouldGoToMenu = true;
			}
		});
		this.add(this.backToMenuButton);
		
		this.shouldRetry = false;
		this.shouldGoToMenu = false;
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
			
			this.gameoverTexture = Loader.loadImage(".\\assets\\images\\gameover.png",(int)this.gameoverLocation.getWidth(),(int)this.gameoverLocation.getHeight());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public void updateState(float deltaTime,KeyboardControl keyboard) // cas v ms
	{
		// IMPORTANT: global timer
		this.timer += deltaTime;
		
		this.player.update(this.timer,deltaTime,keyboard);
		this.background.update(deltaTime);
		
		if(this.player.hasLivesLeft())
			this.levels[this.currLevel].update(this.timer,this.circleEnemies,this.rectEnemies,this.enemyTypes);
		
		final Vec2D target = this.player.getAsTarget();
		for(int i = 0;i < this.circleEnemies.size();)
		{
			CircleEnemy currEnemy = this.circleEnemies.get(i);
			
			final CircleHitbox playerHitbox = this.player.getHitbox();
			final CircleHitbox enemyBulletHitbox =  currEnemy.getHitbox();
			
			if(!this.player.isDestroyed() && !this.player.isInvincible() && playerHitbox.collidesWith(enemyBulletHitbox))
				this.player.destroy(this.timer);
			
			currEnemy.update(deltaTime,this.timer,this.enemyBullets,target,this.player.getBullets());
			if(!currEnemy.isAlive())
				this.circleEnemies.remove(i);
			else
				i++;
		}
		
		for(int i = 0;i < this.rectEnemies.size();)
		{
			RectEnemy currEnemy = this.rectEnemies.get(i);
			
			final CircleHitbox playerHitbox = this.player.getHitbox();
			final RectHitbox enemyBulletHitbox =  currEnemy.getHitbox();
			
			if(!this.player.isDestroyed() && !this.player.isInvincible() && playerHitbox.collidesWith(enemyBulletHitbox))
				this.player.destroy(this.timer);
			
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
					this.player.destroy(this.timer);
					currBullet.destroy();
				}
			}
			
			currBullet.update(deltaTime,this.panelWidth,this.panelHeight);
			if(currBullet.hasBeenDestroyed())
				this.enemyBullets.remove(i);
			else
				i++;
		}			
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D)g;
		
		this.background.draw(graphics);
		//this.grid.draw(graphics,this.player.getNeighbouringCellInxs());
		if(!this.player.isDestroyed())
			this.player.draw(graphics);
		
		for(CircleEnemy circleEnemy : this.circleEnemies)
			circleEnemy.draw(graphics);
		
		for(RectEnemy rectEnemy : this.rectEnemies)
			rectEnemy.draw(graphics);
		
		for(EnemyBullet enemyBullet : this.enemyBullets)
			enemyBullet.draw(graphics);
		
		//this.textRend.draw(graphics);
		if(!this.player.hasLivesLeft()) // game over "okno"
		{
			if(!this.buttonsEnabled)
			{
				this.buttonsEnabled = true;
				this.enableGameOverButtons();
			}
			final Vec2D gameOverOrigin = this.gameoverLocation.getOrigin();
			graphics.drawImage(this.gameoverTexture,(int)gameOverOrigin.getX(),(int)gameOverOrigin.getY(),null);
		}
	}
	
	public void enableGameOverButtons()
	{
		this.retryButton.setVisible(true);
		this.backToMenuButton.setVisible(true);
	}
	
	public boolean shouldRetry()
	{
		return this.shouldRetry;
	}
	
	public boolean shouldGoToMenu()
	{
		return this.shouldGoToMenu;
	}
}
