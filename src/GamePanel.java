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
	private double timer;
	private Player player;
	private Background[] background;
	private CollisionGrid grid;
	private LinkedList<CircleEnemy> circleEnemies;
	private LinkedList<RectEnemy> rectEnemies;
	private LinkedList<EnemyBullet> enemyBullets;
	private EnemyType[] enemyTypes;
	private int currLevel;
	private Level[] levels;
	private Image[] levelTexts;
	private Vec2D[] levelTextsRelativeLocations;
	private boolean levelBannerShown;
	private double levelBannerShownSince;
	
	private Rect2D messagePanelBoundingBox;
	private Image messagePanelTexture;
	
	private boolean gameOverButtonsEnabled;
	private Vec2D gameOverRelativeLocation;
	private Image gameOverTexture;
	private JButton retryButton;
	private JButton backToMenuButton;
	
	private boolean shouldRetry;
	private boolean shouldGoToMenu;
	
	private boolean nextLevelButtonEnabled;
	private boolean levelCompleted;
	private Image levelCompletedTexture;
	private Vec2D levelCompletedRelativeLocation;
	private JButton continueButton;
	
	private boolean shouldContinue;
	
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
		
		this.background = new Background[] {
				new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight),
				new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight), //TODO change background textures
				new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight)  //TODO change background textures
		};
		
		this.circleEnemies = new LinkedList<CircleEnemy>();
		this.rectEnemies = new LinkedList<RectEnemy>();
		this.enemyBullets = new LinkedList<EnemyBullet>();
	
		this.gameOverButtonsEnabled = false;
		this.retryButton = new JButton("Retry");
		this.retryButton.setBounds(530,380,72,21);
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
		this.backToMenuButton.setBounds(678,380,72,21);
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
		
		this.continueButton = new JButton("Continue");
		this.continueButton.setBounds(this.panelWidth/2 - 45,405,90,21);
		this.continueButton.setFocusable(false);
		this.continueButton.setVisible(false);
		this.continueButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				shouldContinue = true;
			}
		});
		
		this.add(this.continueButton);
		
		this.messagePanelBoundingBox = new Rect2D(this.panelWidth/2 - 175,this.panelHeight/2 - 100,350,200);
		this.gameOverRelativeLocation = new Vec2D(this.messagePanelBoundingBox.getWidth()/2 - 85f,50f);
		this.levelCompletedRelativeLocation = new Vec2D(this.messagePanelBoundingBox.getWidth()/2 - 85.f,20f);
		
		this.levelBannerShown = false;
		this.levelTextsRelativeLocations = new Vec2D[]{
				new Vec2D(this.messagePanelBoundingBox.getWidth()/2 - 68f,55.f),
				new Vec2D(this.messagePanelBoundingBox.getWidth()/2 - 72f,55.f),
				new Vec2D(this.messagePanelBoundingBox.getWidth()/2 - 71f,55.f)
		};
		
		
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
					new Level(".\\assets\\levels\\lvl1.txt"),
					new Level(".\\assets\\levels\\lvl1.txt"),
					new Level(".\\assets\\levels\\lvl1.txt")
			};
			
			this.messagePanelTexture = Loader.loadImage(".\\assets\\images\\messagePanel.png",(int)this.messagePanelBoundingBox.getWidth(),(int)this.messagePanelBoundingBox.getHeight());
			this.gameOverTexture = Loader.loadImage(".\\assets\\images\\gameover.png",170,50);
			this.levelCompletedTexture = Loader.loadImage(".\\assets\\images\\levelComplete.png",170,125);
			this.levelTexts = new Image[]{
					Loader.loadImage(".\\assets\\images\\level1.png",136,75),
					Loader.loadImage(".\\assets\\images\\level2.png",145,75),
					Loader.loadImage(".\\assets\\images\\level3.png",143,75)
			};
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
		
		final Level currentLevel = this.levels[this.currLevel];
		
		this.player.update(this.timer,deltaTime,keyboard);
		this.background[this.currLevel].update(deltaTime);
		
		
		if(this.player.hasLivesLeft())
			currentLevel.update(this.timer,this.circleEnemies,this.rectEnemies,this.enemyTypes);
		
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
		
		if(currentLevel.haveAllEnemiesBeenSpawned() && this.circleEnemies.size() == 0 && this.rectEnemies.size() == 0)
		{
			this.levelCompleted = true;
		}
		
		if(!this.levelBannerShown)
		{
			this.levelBannerShown = true;
			this.levelBannerShownSince = this.timer;
		}
		
		if(this.shouldContinue)
		{
			this.currLevel++;
			if(this.currLevel < this.levels.length) // naslednji nivo
			{
				this.levelBannerShown = false;
				this.gameOverButtonsEnabled = false;
				this.nextLevelButtonEnabled = false;
				this.shouldContinue = false;
				this.levelCompleted = false;
				this.continueButton.setVisible(false);
				this.timer = 0.;
				this.player.resetAfterLevel();
			}
			else // igralec je zmagal
				this.shouldGoToMenu = true;
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D)g;
		
		this.background[this.currLevel].draw(graphics);
		//this.grid.draw(graphics,this.player.getNeighbouringCellInxs());
		if(!this.player.isDestroyed())
			this.player.draw(graphics);
		
		for(CircleEnemy circleEnemy : this.circleEnemies)
			circleEnemy.draw(graphics);
		
		for(RectEnemy rectEnemy : this.rectEnemies)
			rectEnemy.draw(graphics);
		
		for(EnemyBullet enemyBullet : this.enemyBullets)
			enemyBullet.draw(graphics);
		
		if(this.timer - this.levelBannerShownSince < 3000.f)
		{
			final Image currBanner = this.levelTexts[this.currLevel];
			final Vec2D currRelativeLocation = this.levelTextsRelativeLocations[this.currLevel];
			final Vec2D massagePanelOrigin = this.messagePanelBoundingBox.getOrigin();
			graphics.drawImage(this.messagePanelTexture,(int)massagePanelOrigin.getX(),(int)massagePanelOrigin.getY(),null);
			graphics.drawImage(currBanner,(int)(massagePanelOrigin.getX() + currRelativeLocation.getX()),(int)(massagePanelOrigin.getY() + currRelativeLocation.getY()),null);
		}
		
		if(this.levelCompleted)
		{
			//this.enemyBullets.clear(); // izbrisemo vse metke nasprotnikov 
			if(!this.nextLevelButtonEnabled)
			{
				this.nextLevelButtonEnabled = false;
				this.continueButton.setVisible(true);
			}
			final Vec2D massagePanelOrigin = this.messagePanelBoundingBox.getOrigin();
			graphics.drawImage(this.messagePanelTexture,(int)massagePanelOrigin.getX(),(int)massagePanelOrigin.getY(),null);
			graphics.drawImage(this.levelCompletedTexture,(int)(massagePanelOrigin.getX() + this.levelCompletedRelativeLocation.getX()),(int)(massagePanelOrigin.getY() + this.levelCompletedRelativeLocation.getY()),null);
		}
		
		if(!this.player.hasLivesLeft()) // game over "okno"
		{
			if(!this.gameOverButtonsEnabled)
			{
				this.gameOverButtonsEnabled = true;
				this.enableGameOverButtons();
			}
			final Vec2D massagePanelOrigin = this.messagePanelBoundingBox.getOrigin();
			graphics.drawImage(this.messagePanelTexture,(int)massagePanelOrigin.getX(),(int)massagePanelOrigin.getY(),null);
			graphics.drawImage(this.gameOverTexture,(int)(massagePanelOrigin.getX() + this.gameOverRelativeLocation.getX()),(int)(massagePanelOrigin.getY() + this.gameOverRelativeLocation.getY()),null);
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
