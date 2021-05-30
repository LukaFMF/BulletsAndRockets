import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
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
	private LinkedList<Boss> bosses;
	private LinkedList<EnemyBullet> enemyBullets;
	private EnemyType[] enemyTypes;
	private BossType[] bossTypes;
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
			new Background(".\\assets\\images\\backgroundLvl1.png",this.panelWidth,this.panelHeight),
			new Background(".\\assets\\images\\backgroundLvl2.png",this.panelWidth,this.panelHeight),
			new Background(".\\assets\\images\\backgroundLvl3.png",this.panelWidth,this.panelHeight) 
		};
		
		this.circleEnemies = new LinkedList<CircleEnemy>();
		this.rectEnemies = new LinkedList<RectEnemy>();
		this.bosses = new LinkedList<Boss>();
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
				new EnemyType(0,7,100.f,100.f,.3f,true,".\\assets\\images\\enemy0.png",1500.f,
					(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
					{
						final Vec2D enemyOrigin = location.getOrigin();
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOrigin.getX() + 10.f,enemyOrigin.getY() + 50.f - 15.f,30.f,30.f),new Vec2D(-.7f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
					}),
				new EnemyType(1,25,150.f,150.f,.15f,true,".\\assets\\images\\enemy1.png",3000.f,
					(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
					{
						final Vec2D enemyOrigin = location.getOrigin();
						final float enemyWidth = location.getWidth();
						final float enemyHeight = location.getHeight();
						
						final Rect2D bulletBoundingBox = new Rect2D(enemyOrigin.getX() + enemyWidth/2 - 15.f,enemyOrigin.getY() + enemyHeight/2 - 15.f,30.f,30.f);
						
						enemyBullets.add(new EnemyBullet(bulletBoundingBox.clone(),new Vec2D(.0f,1.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(bulletBoundingBox.clone(),new Vec2D(.7071f,.7071f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(bulletBoundingBox.clone(),new Vec2D(1.f,.0f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(bulletBoundingBox.clone(),new Vec2D(.7071f,-.7071f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(bulletBoundingBox.clone(),new Vec2D(.0f,-1.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(bulletBoundingBox.clone(),new Vec2D(-.7071f,-.7071f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(bulletBoundingBox.clone(),new Vec2D(-1.f,.0f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(bulletBoundingBox.clone(),new Vec2D(-.7071f,.7071f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));					
					}),
				new EnemyType(2,20,250.f,250.f,.25f,true,".\\assets\\images\\enemy2.png",1500.f,
					(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
					{
						final Vec2D enemyOrigin = location.getOrigin();
						final float enemyOriginX = enemyOrigin.getX();
						final float enemyOriginY = enemyOrigin.getY();
					
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 90.f - 15.f,enemyOriginY + 30.f - 15.f,30.f,30.f),new Vec2D(-.4f,-.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 80.f - 15.f,enemyOriginY + 70.f - 15.f,30.f,30.f),new Vec2D(-.5f,-.01f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 80.f - 15.f,enemyOriginY + 180.f - 15.f,30.f,30.f),new Vec2D(-.5f,.01f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 90.f - 15.f,enemyOriginY + 220.f - 15.f,30.f,30.f),new Vec2D(-.4f,.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
					}),
				new EnemyType(3,15,150.f,150.f,.4f,true,".\\assets\\images\\enemy3.png",3500.f,
					(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
					{
						final Vec2D enemyOrigin = location.getOrigin();
						final float enemyWidth = location.getWidth();
						final float enemyHeight = location.getHeight();
						final float enemyOriginX = enemyOrigin.getX();
						final float enemyOriginY = enemyOrigin.getY();
						final Vec2D enemyCenter = new Vec2D(enemyOriginX + enemyWidth/2,enemyOriginY + enemyHeight/2);
						
						Vec2D targetDirection = target.clone();
						targetDirection.sub(enemyCenter);
						targetDirection.normalize();
						targetDirection.scalarMul(.5f);
						
						// metek na sredini
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 75.f - 15.f,enemyOriginY + 75.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						
						// pravokotni metki
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 75.f - 15.f,enemyOriginY + 12.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 75.f - 15.f,enemyOriginY + 138.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 12.f - 15.f,enemyOriginY + 75.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 138.f - 15.f,enemyOriginY + 75.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						
						// diagonalni metki
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 30.f - 15.f,enemyOriginY + 30.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 30.f - 15.f,enemyOriginY + 120.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 120.f - 15.f,enemyOriginY + 30.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 120.f - 15.f,enemyOriginY + 120.f - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
					}),
			};
			
			final Vec2D boss1HitboxOrigin1 = new Vec2D(160.f,33f);
			final float boss1HitboxRadius1 = 32.f;
			final Vec2D boss1HitboxOrigin2 = new Vec2D(160.f,267.f);
			final float boss1HitboxRadius2 = 32.f;
			final Vec2D boss1HitboxOrigin3 = new Vec2D(45.f,150.f);
			final float boss1HitboxRadius3 = 40.f;
			
			final Vec2D boss2HitboxOrigin1 = new Vec2D(183.f,52f);
			final float boss2HitboxRadius1 = 60.f;
			final Vec2D boss2HitboxOrigin2 = new Vec2D(183.f,498.f);
			final float boss2HitboxRadius2 = 60.f;
			final Vec2D boss2HitboxOrigin3 = new Vec2D(95.f,275.f);
			final float boss2HitboxRadius3 = 95.f;
			
			final Vec2D boss3HitboxOrigin1 = new Vec2D(180.f,55f);
			final float boss3HitboxRadius1 = 32.f;
			final Vec2D boss3HitboxOrigin2 = new Vec2D(180.f,545.f);
			final float boss3HitboxRadius2 = 32.f; 
			final Vec2D boss3HitboxOrigin3 = new Vec2D(150.f,180.f);
			final float boss3HitboxRadius3 = 40.f;
			final Vec2D boss3HitboxOrigin4 = new Vec2D(150.f,420.f);
			final float boss3HitboxRadius4 = 40.f;
			final Vec2D boss3HitboxOrigin5 = new Vec2D(50.f,300.f);
			final float boss3HitboxRadius5 = 60.f;
			
			this.bossTypes = new BossType[] {
				new BossType(4,50,250.f,300.f,.5f,
						new Circle2D[] {
							new Circle2D(boss1HitboxOrigin1,boss1HitboxRadius1),
							new Circle2D(boss1HitboxOrigin2,boss1HitboxRadius2),
							new Circle2D(boss1HitboxOrigin3,boss1HitboxRadius3)
						},
						new String[] {
							".\\assets\\images\\boss1_1.png",
							".\\assets\\images\\boss1_2.png",
							".\\assets\\images\\boss1_3.png"
						},
						new float[] {
							1000.f,
							1000.f,
							500.f,
						},
						new BulletPattern[] {
							(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
							{
								final Vec2D enemyOrigin = location.getOrigin();
								final float enemyWidth = location.getWidth();
								final float enemyHeight = location.getHeight();
								final float enemyOriginX = enemyOrigin.getX();
								final float enemyOriginY = enemyOrigin.getY();
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 100.f,enemyOriginY + 25.f,30.f,30.f),new Vec2D(-.5f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 10.f,enemyOriginY + enemyHeight/2 - 15.f,30.f,30.f),new Vec2D(-.5f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 100.f,enemyOriginY + 245.f,30.f,30.f),new Vec2D(-.5f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
							},
							(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
							{
								final Vec2D enemyOrigin = location.getOrigin();
								final float enemyWidth = location.getWidth();
								final float enemyHeight = location.getHeight();
								final float enemyOriginX = enemyOrigin.getX();
								final float enemyOriginY = enemyOrigin.getY();
							
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 15.f,enemyOriginY + enemyHeight/2 - 60.f,30.f,30.f),new Vec2D(-.4f,-.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 10.f - 15.f,enemyOriginY + enemyHeight/2 - 40.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 10.f - 15.f,enemyOriginY + enemyHeight/2 + 10.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 15.f,enemyOriginY + enemyHeight/2 + 30.f,30.f,30.f),new Vec2D(-.4f,.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							},
							(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
							{
								final Vec2D enemyOrigin = location.getOrigin();
								final float enemyWidth = location.getWidth();
								final float enemyHeight = location.getHeight();
								final float enemyOriginX = enemyOrigin.getX();
								final float enemyOriginY = enemyOrigin.getY();
								final Vec2D shotOrigin = new Vec2D(enemyOrigin.getX() + 5.f,enemyOrigin.getY() + 150.f);
								
								Vec2D targetDirection = target.clone();
								targetDirection.sub(shotOrigin);
								targetDirection.normalize();
								targetDirection.scalarMul(.75f);
								
								enemyBullets.add(new EnemyBullet(new Rect2D(shotOrigin.getX() - 15.f,shotOrigin.getY() - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							}
						},
						".\\assets\\images\\shield.png",3),
				
				new BossType(5,100,300.f,550.f,.2f,
					new Circle2D[] {
						new Circle2D(boss2HitboxOrigin1,boss2HitboxRadius1),
						new Circle2D(boss2HitboxOrigin2,boss2HitboxRadius2),
						new Circle2D(boss2HitboxOrigin3,boss2HitboxRadius3)
					},
					new String[] {
						".\\assets\\images\\boss2_1.png",
						".\\assets\\images\\boss2_2.png",
						".\\assets\\images\\boss2_3.png"
					},
					new float[] {
						1000.f,
						750.f,
						100.f,
					},
					new BulletPattern[] {
						(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
						{
							final Vec2D enemyOrigin = location.getOrigin();
							final float enemyWidth = location.getWidth();
							final float enemyHeight = location.getHeight();
							final float enemyOriginX = enemyOrigin.getX();
							final float enemyOriginY = enemyOrigin.getY();
							final Vec2D shotOrigin1 = new Vec2D(enemyOriginX + 165.f,enemyOriginY + 50.f);
							final Vec2D shotOrigin2 = new Vec2D(enemyOriginX + 165.f,enemyOriginY + 500.f);
							
							Vec2D targetDirection1 = target.clone();
							targetDirection1.sub(shotOrigin1);
							targetDirection1.normalize();
							targetDirection1.scalarMul(.5f);
							
							Vec2D targetDirection2 = target.clone();
							targetDirection2.sub(shotOrigin2);
							targetDirection2.normalize();
							targetDirection2.scalarMul(.5f);
							
							enemyBullets.add(new EnemyBullet(new Rect2D(shotOrigin1.getX() - 15.f,shotOrigin1.getY() - 15.f,30.f,30.f),targetDirection1.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							enemyBullets.add(new EnemyBullet(new Rect2D(shotOrigin2.getX() - 15.f,shotOrigin2.getY() - 15.f,30.f,30.f),targetDirection2.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						},
						(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
						{
							final Vec2D enemyOrigin = location.getOrigin();
							final float enemyWidth = location.getWidth();
							final float enemyHeight = location.getHeight();
							final float enemyOriginX = enemyOrigin.getX();
							final float enemyOriginY = enemyOrigin.getY();
						
							enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 15.f,enemyOriginY + enemyHeight/2 - 60.f,30.f,30.f),new Vec2D(-.4f,-.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 10.f - 15.f,enemyOriginY + enemyHeight/2 - 40.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 10.f - 15.f,enemyOriginY + enemyHeight/2 + 10.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 15.f,enemyOriginY + enemyHeight/2 + 30.f,30.f,30.f),new Vec2D(-.4f,.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
						},
						(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
						{
							final Vec2D enemyOrigin = location.getOrigin();
							final float enemyWidth = location.getWidth();
							final float enemyHeight = location.getHeight();
							
							Random rand = new Random();
							double yDirection = rand.nextDouble() * .3f ;
							
							enemyBullets.add(new EnemyBullet(new Rect2D(enemyOrigin.getX() - 10.f,enemyOrigin.getY() + enemyHeight/2 - 15.f,30.f,30.f),new Vec2D(-.2f,-.15f + (float)yDirection),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
						}
					},
					".\\assets\\images\\shield.png",3),
				
				new BossType(6,200,350.f,600.f,.3f,
						new Circle2D[] {
							new Circle2D(boss3HitboxOrigin1,boss3HitboxRadius1),
							new Circle2D(boss3HitboxOrigin2,boss3HitboxRadius2),
							new Circle2D(boss3HitboxOrigin3,boss3HitboxRadius3),
							new Circle2D(boss3HitboxOrigin4,boss3HitboxRadius4),
							new Circle2D(boss3HitboxOrigin5,boss3HitboxRadius5)
						},
						new String[] {
							".\\assets\\images\\boss3_1.png",
							".\\assets\\images\\boss3_2.png",
							".\\assets\\images\\boss3_3.png",
							".\\assets\\images\\boss3_4.png",
							".\\assets\\images\\boss3_5.png"
						},
						new float[] {
							600.f,
							1000.f,
							2000.f,
							150.f,
							600.f
						},
						new BulletPattern[] {
							(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
							{
								final Vec2D enemyOrigin = location.getOrigin();
								final float enemyWidth = location.getWidth();
								final float enemyHeight = location.getHeight();
								final float enemyOriginX = enemyOrigin.getX();
								final float enemyOriginY = enemyOrigin.getY();
								
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 165.f - 15.f,enemyOriginY + 55.f - 15.f,30.f,30.f),new Vec2D(-.5f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 135.f - 15.f,enemyOriginY + 185.f - 15.f,30.f,30.f),new Vec2D(-.5f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 165.f - 15.f,enemyOriginY + 545.f - 15.f,30.f,30.f),new Vec2D(-.5f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 135.f - 15.f,enemyOriginY + 415.f - 15.f,30.f,30.f),new Vec2D(-.5f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
							},
							(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
							{
								final Vec2D enemyOrigin = location.getOrigin();
								final float enemyWidth = location.getWidth();
								final float enemyHeight = location.getHeight();
								final float enemyOriginX = enemyOrigin.getX();
								final float enemyOriginY = enemyOrigin.getY();
								final Vec2D shotOrigin1 = new Vec2D(enemyOriginX + 135.f,enemyOriginY + 180.f);
								final Vec2D shotOrigin2 = new Vec2D(enemyOriginX + 135.f,enemyOriginY + 420.f);
								final Vec2D shotOrigin3 = new Vec2D(enemyOriginX + 165.f,enemyOriginY + 545.f);
								
								Vec2D targetDirection1 = target.clone();
								targetDirection1.sub(shotOrigin1);
								targetDirection1.normalize();
								targetDirection1.scalarMul(.75f);
								
								Vec2D targetDirection2 = target.clone();
								targetDirection2.sub(shotOrigin2);
								targetDirection2.normalize();
								targetDirection2.scalarMul(.75f);
								
								Vec2D targetDirection3 = target.clone();
								targetDirection3.sub(shotOrigin3);
								targetDirection3.normalize();
								targetDirection3.scalarMul(.75f);
								
								enemyBullets.add(new EnemyBullet(new Rect2D(shotOrigin1.getX() - 15.f,shotOrigin1.getY() - 15.f,30.f,30.f),targetDirection1.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(shotOrigin2.getX() - 15.f,shotOrigin2.getY() - 15.f,30.f,30.f),targetDirection2.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(shotOrigin3.getX() - 15.f,shotOrigin3.getY() - 15.f,30.f,30.f),targetDirection3.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							},
							(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
							{
								final Vec2D enemyOrigin = location.getOrigin();
								final float enemyWidth = location.getWidth();
								final float enemyHeight = location.getHeight();
								final float enemyOriginX = enemyOrigin.getX();
								final float enemyOriginY = enemyOrigin.getY();
								
								
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 15.f,enemyOriginY + enemyHeight/2 - 60.f,30.f,30.f),new Vec2D(-.4f,-.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 10.f - 15.f,enemyOriginY + enemyHeight/2 - 40.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 10.f - 15.f,enemyOriginY + enemyHeight/2 + 10.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX - 15.f,enemyOriginY + enemyHeight/2 + 30.f,30.f,30.f),new Vec2D(-.4f,.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 120.f - 15.f,enemyOriginY + 185.f - 45.f - 15.f,30.f,30.f),new Vec2D(-.4f,-.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 125.f - 15.f,enemyOriginY + 185.f - 25.f - 15.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 125.f - 15.f,enemyOriginY + 185.f + 25.f - 15.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 120.f - 15.f,enemyOriginY + 185.f + 45.f - 15.f,30.f,30.f),new Vec2D(-.4f,.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 120.f - 15.f,enemyOriginY + 415.f - 45.f - 15.f,30.f,30.f),new Vec2D(-.4f,-.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 125.f - 15.f,enemyOriginY + 415.f - 25.f - 15.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 125.f - 15.f,enemyOriginY + 415.f + 25.f - 15.f,30.f,30.f),new Vec2D(-.5f,0.f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 120.f - 15.f,enemyOriginY + 415.f + 45.f - 15.f,30.f,30.f),new Vec2D(-.4f,.1f),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							},
							(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
							{
								final Vec2D enemyOrigin = location.getOrigin();
								final float enemyWidth = location.getWidth();
								final float enemyHeight = location.getHeight();
								final float enemyOriginX = enemyOrigin.getX();
								final float enemyOriginY = enemyOrigin.getY();
								
								Random rand = new Random();
								double yDirection1 = rand.nextDouble() * .3f;
								double yDirection2 = rand.nextDouble() * .2f;
								
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 5.f - 15.f,enemyOriginY + enemyHeight/2 - 15.f,30.f,30.f),new Vec2D(-.2f,-.15f + (float)yDirection1),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOriginX + 135.f - 15.f,enemyOriginY + 420.f - 15.f,30.f,30.f),new Vec2D(-.2f,-.1f + (float)yDirection2),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
							},
							(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target) -> 
							{
								final Vec2D enemyOrigin = location.getOrigin();
								final float enemyWidth = location.getWidth();
								final float enemyHeight = location.getHeight();
								final float enemyOriginX = enemyOrigin.getX();
								final float enemyOriginY = enemyOrigin.getY();
								final Vec2D shotOrigin = new Vec2D(enemyOriginX + 55.f,enemyOriginY + 300.f);
		
								Vec2D targetDirection = target.clone();
								targetDirection.sub(shotOrigin);
								targetDirection.normalize();
								targetDirection.scalarMul(.3f);
								
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOrigin.getX() + 20.f - 15.f,enemyOrigin.getY() + enemyHeight/2 - 20.f - 15.f,30.f,30.f),new Vec2D(-.7f,-.12f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOrigin.getX() + 5.f - 15.f,enemyOrigin.getY() + enemyHeight/2 - 15.f,30.f,30.f),new Vec2D(-.7f,.0f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(enemyOrigin.getX() + 20.f - 15.f,enemyOrigin.getY() + enemyHeight/2 + 20.f - 15.f,30.f,30.f),new Vec2D(-.7f,.12f),new Vec2D(0.f,0.f),enemyBulletTexture,this.grid));
								enemyBullets.add(new EnemyBullet(new Rect2D(shotOrigin.getX() - 15.f,shotOrigin.getY() - 15.f,30.f,30.f),targetDirection.clone(),new Vec2D(.0f,.0f),enemyBulletTexture,this.grid));
							}
						},
						".\\assets\\images\\shield.png",5),
			};
			
			this.levels = new Level[] {
				new Level(".\\assets\\levels\\lvl1.txt"),
				new Level(".\\assets\\levels\\lvl2.txt"),
				new Level(".\\assets\\levels\\lvl3.txt")
			};
			
			this.messagePanelTexture = Loader.loadImage(".\\assets\\images\\messagePanel.png",(int)this.messagePanelBoundingBox.getWidth(),(int)this.messagePanelBoundingBox.getHeight());
			this.gameOverTexture = Loader.loadImage(".\\assets\\images\\gameover.png",170,50);
			this.levelCompletedTexture = Loader.loadImage(".\\assets\\images\\levelComplete.png",170,125);
			this.levelTexts = new Image[] {
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
			currentLevel.update(this.timer,this.circleEnemies,this.rectEnemies,this.bosses,this.enemyTypes,this.bossTypes);
		
		final Vec2D target = this.player.getAsTarget();
		for(int i = 0;i < this.circleEnemies.size();)
		{
			CircleEnemy currEnemy = this.circleEnemies.get(i);
			
			final CircleHitbox playerHitbox = this.player.getHitbox();
			final CircleHitbox enemyHitbox = currEnemy.getHitbox();
			
			if(!this.player.isDestroyed() && !this.player.isInvincible() && playerHitbox.collidesWith(enemyHitbox))
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
			final RectHitbox enemyHitbox = currEnemy.getHitbox();
			
			if(!this.player.isDestroyed() && !this.player.isInvincible() && playerHitbox.collidesWith(enemyHitbox))
				this.player.destroy(this.timer);
			
			currEnemy.update(deltaTime,this.timer,this.enemyBullets,target,this.player.getBullets());
			if(!currEnemy.isAlive())
				this.rectEnemies.remove(i);
			else
				i++;
		}
		
		for(int i = 0;i < this.bosses.size();)
		{
			Boss currEnemy = this.bosses.get(i);
			
			final CircleHitbox playerHitbox = this.player.getHitbox();
			final CircleHitbox[] enemyHitboxes =  currEnemy.getHitboxes();
			
			
			for(int j = currEnemy.getCurrentStage();j < enemyHitboxes.length;j++)
				if(!this.player.isDestroyed() && !this.player.isInvincible() && playerHitbox.collidesWith(enemyHitboxes[j]))
				{
					this.player.destroy(this.timer);
					break;
				}
			
			currEnemy.update(deltaTime,this.timer,this.enemyBullets,target,this.player.getBullets());
			if(!currEnemy.isAlive())
				this.bosses.remove(i);
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
		
		if(currentLevel.haveAllEnemiesBeenSpawned() && this.circleEnemies.size() == 0 && this.rectEnemies.size() == 0 && this.bosses.size() == 0)
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
		this.player.drawPlayerBullets(graphics);
		if(!this.player.isDestroyed())
			this.player.draw(graphics);
		
		for(CircleEnemy circleEnemy : this.circleEnemies)
			circleEnemy.draw(graphics);
		
		for(RectEnemy rectEnemy : this.rectEnemies)
			rectEnemy.draw(graphics);
		
		for(Boss boss : this.bosses)
			boss.draw(graphics);
		
		for(EnemyBullet enemyBullet : this.enemyBullets)
			enemyBullet.draw(graphics);
		
		this.player.drawPlayerLives(graphics);
		
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
