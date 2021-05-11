
public class Point {
	private int x;
	private int y;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public double distance (Object Point) {
		return Math.sqrt(getX() - ((Point) Point).getX())*(getX() - ((Point) Point).getX()) + (getY() - ((Point) Point).getY())*(getY() - ((Point) Point).getY());
	}
	public Point addy(int number) {
		Point point = new Point(this.x,this.y + number);
		return  point ;
	}
	public Point addx(int number) {
		Point point = new Point(this.x + number,this.y);
		return  point ;
	}
}
