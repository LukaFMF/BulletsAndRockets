import java.util.LinkedList;

public interface BulletPattern
{
	void formation(double timer,Rect2D location,LinkedList<EnemyBullet> enemyBullets,Vec2D target);
}
