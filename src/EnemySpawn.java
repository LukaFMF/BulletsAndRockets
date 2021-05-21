
public class EnemySpawn
{
	private int id;
	private boolean repeatWaypoints;
	private Vec2D spawnPoint;
	private Vec2D[] waypoints;
	
	EnemySpawn(int id,boolean repeatWaypoints,Vec2D spawnPoint,Vec2D[] waypoints)
	{
		this.id = id;
		this.repeatWaypoints = repeatWaypoints;
		this.spawnPoint = spawnPoint;
		this.waypoints = waypoints;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Vec2D getSpawnPoint()
	{
		return this.spawnPoint;
	}
	
	public void setSpawnPoint(Vec2D spawnPoint)
	{
		this.spawnPoint = spawnPoint;
	}
	
	public Vec2D[] getWaypoints()
	{
		return this.waypoints;
	}
	
	public void setWaypoints(Vec2D[] waypoints)
	{
		this.waypoints = waypoints;
	}
	
	public boolean hasRepeatingWaypoints()
	{
		return this.repeatWaypoints;
	}
	
	public void setRepeatWaypoints(boolean repeatWaypoints)
	{
		this.repeatWaypoints = repeatWaypoints;
	}
}
