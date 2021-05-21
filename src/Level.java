import java.io.*;
import java.io.FileReader;
import java.io.IOException;


public class Level 
{
	private float[] spawnTimes;
	private int[][] ids;
	private Vec2D[][] startPoint;
	private Vec2D[][] endPoint;
	
	Level(String pathToLvlFile) throws IOException
	{
	try
		{		
		BufferedReader reader = new BufferedReader(new FileReader(pathToLvlFile));
		
		int n = 20; // ZA DOPOLNIT hard settano na max 2 enemija na doloèen èas ZA DOPOLNIT

		String strCurrentLine = reader.readLine(); // da preberemo 1. vrstico in setamo dimenzijo èasovnega Array-a
		int numOfRows = Integer.parseInt(strCurrentLine);
		this.ids = new int[numOfRows][n];
		this.startPoint = new Vec2D[numOfRows][n];
		this.endPoint = new Vec2D[numOfRows][n];
		this.spawnTimes = new float[numOfRows];
		int timeDimensionIndex = -1;  // da vemo v kateri vrsti se nahajamo, -1 da lažje uvrstimo podatke
		int dataDimensionIndex = 0; // da vemo katri elemnt vrste obravnavamo
		
		while ((strCurrentLine = reader.readLine()) != null) 
		{
			if (strCurrentLine.length() == 0)
				continue;
			String[] dataOfRow = strCurrentLine.split(" ");
			if (dataOfRow.length == 1) //imamo podatek o èasu
			{	
				timeDimensionIndex ++;
				dataDimensionIndex = 0;
				float time = Float.parseFloat(strCurrentLine.substring(0, strCurrentLine.length() - 1)); 
				// znebimo se ":" na koncu in niz spremenimo v float
				this.spawnTimes[timeDimensionIndex] = time; 
			}
			
			else if (dataOfRow.length > 1) // imamo podatke id, startxy, endxy
			{
				//prebrani podatki
				final int id = Integer.parseInt(dataOfRow[0]);
				final float startX = Float.parseFloat(dataOfRow[1]);
				final float startY = Float.parseFloat(dataOfRow[2]);
				final float endX = Float.parseFloat(dataOfRow[3]);
				final float endY = Float.parseFloat(dataOfRow[4]);
				
				//za pregled
				System.out.println(id);
				System.out.println(startX);
				System.out.println(startY);
				System.out.println(endX);
				System.out.println(endY);
				
				this.ids[timeDimensionIndex][dataDimensionIndex] = id;
				this.startPoint[timeDimensionIndex][dataDimensionIndex] = new Vec2D(startX,startY);
				this.endPoint[timeDimensionIndex][dataDimensionIndex] = new Vec2D(endX,endY);
				
				dataDimensionIndex++;
			}
		}
	    reader.close();
		}
	catch (IOException e) 
		{
	    e.printStackTrace();
		}
	
}
	public float[] getSpawnTimes() 
	{
		return this.spawnTimes;
	}
	public int[][] getIds() 
	{
		return this.ids;
	}
	public Vec2D[][] getStartPoint() 
	{
		return this.startPoint;
	}
	public Vec2D[][] getEndPoint() 
	{
		return this.endPoint;
	}
}