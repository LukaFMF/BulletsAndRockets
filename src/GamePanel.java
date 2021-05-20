import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Map;

public class GamePanel extends JPanel
{
	private int panelWidth; // le za informacijo o velikosti okna
	private int panelHeight;
	private double timer;
	private Player player;
	private Background background;
	private KeyboardControls keyboard;
	private LinkedList<CircleEnemy> circleEnemies;
	private LinkedList<RectEnemy> rectEnemies;
	private LinkedList<EnemyBullet> enemyBullets;
	private EnemyType[] enemyTypes;
	private Level[] levels;
	
	
	GamePanel(int width,int height)
	{
		super();
		
		this.panelWidth = width;
		this.panelHeight = height;
		this.timer = 0.;
		this.setPreferredSize(new Dimension(this.panelWidth,this.panelHeight));
		
		this.player = new Player(new Rect2D(50.f,300.f,100.f,150.f),this.panelWidth,this.panelHeight);
		
		this.keyboard = new KeyboardControls();
		
		this.background = new Background(".\\assets\\images\\background.png",this.panelWidth,this.panelHeight);
		
		this.circleEnemies = new LinkedList<CircleEnemy>();
		this.rectEnemies = new LinkedList<RectEnemy>();
		this.enemyBullets = new LinkedList<EnemyBullet>();
		
		try
		{
			this.enemyTypes = new EnemyType[] {
				new EnemyType(0,100.f,100.f,true,.3f,5,".\\assets\\images\\enemy0.png")
			};
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.levels = new Level[] {
				new Level(".\\assets\\levels\\lvl1.txt")
		};
			
	}
	
	public void updateState(float deltaTime) // cas v ms
	{
		// IMPORTANT: global timer
		this.timer += deltaTime;
		
		
		this.player.update(this.timer,deltaTime,this.keyboard);
		this.background.update(deltaTime);
		
		for(CircleEnemy circleEnemy : this.circleEnemies)
			circleEnemy.update(this.timer,deltaTime);
		
		for(RectEnemy rectEnemy : this.rectEnemies)
			rectEnemy.update(this.timer,deltaTime);
		
		for(EnemyBullet enemyBullet : this.enemyBullets)
			enemyBullet.update(deltaTime);
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D graphics = (Graphics2D)g;
		
		this.background.draw(graphics);
		this.player.draw(graphics);
		
		for(CircleEnemy circleEnemy : this.circleEnemies)
			circleEnemy.draw(graphics);
		
		for(RectEnemy rectEnemy : this.rectEnemies)
			rectEnemy.draw(graphics);
		
		for(EnemyBullet enemyBullet : this.enemyBullets)
			enemyBullet.draw(graphics);

	}
	
	public KeyboardControls getKeyboard()
	{
		return this.keyboard;	
	}
}
