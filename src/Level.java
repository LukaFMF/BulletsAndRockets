import java.awt.Image;
import java.io.*;
import java.util.LinkedList;
import java.util.Vector;


public class Level 
{
	private int currentInx;
	private float[] spawnTimes;
	private Vector<EnemySpawn>[] spawns;
	private boolean allEnemiesSpawned;
	
	Level(String pathToLvlFile) throws IOException
	{
		this.allEnemiesSpawned = false;
		this.currentInx = 0;
		
		File file = new File(pathToLvlFile);
		
		if(!file.exists() || !file.canRead())
			throw new IOException("File \"" + pathToLvlFile + "\" cannot be read or does not exist!");
	
		RandomAccessFile reader = new RandomAccessFile(file,"r");
		
		String currLine = reader.readLine();
		int numSpawnTimes = Integer.parseInt(currLine);
		
		this.spawnTimes = new float[numSpawnTimes];
		this.spawns = (Vector<EnemySpawn>[]) new Vector[numSpawnTimes];
			
		for(int i = 0;i < numSpawnTimes;i++)
		{	
			do
			{
				currLine = reader.readLine();
			}while(currLine.strip().length() == 0);

			this.spawnTimes[i] = Float.parseFloat(currLine);
				
			int numEnemiesAtTime = Integer.parseInt(reader.readLine());
			this.spawns[i] = new Vector<EnemySpawn>();
			this.spawns[i].setSize(numEnemiesAtTime);
				
			for(int j = 0;j < numEnemiesAtTime;j++)
			{
				do
				{
					currLine = reader.readLine();
				}while(currLine.strip().length() == 0);
					
				String[] spawnData = currLine.split(" ");
				int id = Integer.parseInt(spawnData[0]);
				boolean repeatWaypoints = Boolean.parseBoolean(spawnData[1]);
				Vec2D spawnPoint = new Vec2D(Float.parseFloat(spawnData[2]),Float.parseFloat(spawnData[3]));
					
				int numWaypoints = Integer.parseInt(spawnData[4]);
					
				Vec2D[] waypoints = new Vec2D[numWaypoints];
				int waypointInx = 0;
				for(int k = 5;k < numWaypoints*2 + 5;k += 2,waypointInx++)
					waypoints[waypointInx] =  new Vec2D(Float.parseFloat(spawnData[k]),Float.parseFloat(spawnData[k+1]));
						
				this.spawns[i].setElementAt(new EnemySpawn(id,repeatWaypoints,spawnPoint,waypoints),j);
			}
		}
		reader.close();
	}
	
	public float[] getSpawnTimes() 
	{
		return this.spawnTimes;
	}
	
	public Vector<EnemySpawn>[] getSpawns() 
	{
		return this.spawns;
	}
	
	void update(double timer,LinkedList<CircleEnemy> circleEnemies,LinkedList<RectEnemy> rectEnemies,EnemyType[] enemyTypes) // modifies enemy lists
	{
		if(this.currentInx < this.spawnTimes.length && timer > this.spawnTimes[this.currentInx])
		{
			Vector<EnemySpawn> enemiesToSpawn = spawns[this.currentInx];
			for(EnemySpawn enemyToSpawn : enemiesToSpawn)
			{
				EnemyType currEnemyType = enemyTypes[enemyToSpawn.getId()];
				
				final int hp = currEnemyType.getHp();
				final float pixelsPerMilli = currEnemyType.getSpeed();
				final Image texture = currEnemyType.getTexture();
				final Rect2D boundingBox = new Rect2D(enemyToSpawn.getSpawnPoint(),currEnemyType.getBoundingBoxWidth(),currEnemyType.getBoundingBoxHeight());
				final boolean repeatedWaypoints = enemyToSpawn.hasRepeatingWaypoints();
				final Vec2D[] waypoints = enemyToSpawn.getWaypoints();
				final float shootCooldown = currEnemyType.getShootCooldown();
				final BulletPattern pattern = currEnemyType.getPattern();
				
				if(currEnemyType.hasCircleHitbox())
					circleEnemies.add(new CircleEnemy(pixelsPerMilli,hp,texture,boundingBox,waypoints,repeatedWaypoints,new EnemyWeapon(shootCooldown,pattern)));
				else
					rectEnemies.add(new RectEnemy(pixelsPerMilli,hp,texture,boundingBox,waypoints,repeatedWaypoints,new EnemyWeapon(shootCooldown,pattern)));
			}
			this.currentInx++;
		}
		
		if(this.currentInx == this.spawnTimes.length)
			this.allEnemiesSpawned = true;
	}
	
	void reset()
	{
		this.currentInx = 0;
	}
	
	public boolean haveAllEnemiesBeenSpawned()
	{
		return this.allEnemiesSpawned;
	}
}