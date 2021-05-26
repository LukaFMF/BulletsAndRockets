import java.util.LinkedList;

public class EnemyWeapon
{
	private double lastShotAt;
	private float shootCooldown;
	private BulletPattern pattern;
	
	EnemyWeapon(float shootCooldown,BulletPattern pattern)
	{
		this.lastShotAt = 0.;
		this.shootCooldown = shootCooldown;
		this.pattern = pattern;
	}
	
	public void update(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target)
	{
		if(timer - lastShotAt > shootCooldown)
		{
			this.pattern.formation(timer,location,enemyBullets,target); // remove timer
			this.lastShotAt = timer;
		}
	}
}
