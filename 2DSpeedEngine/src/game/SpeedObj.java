package game;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SpeedObj 
{
	// balance acceleration-rate here
	public static final int DIF_EINFACH_FACTOR = 400000;
	public static final int DIF_NORMAL_FACTOR =  150000;
	public static final int DIF_SCHWER_FACTOR =  100000;
	
	private SpeedMap map;
	private Image car;
	
	private float xMomentum, yMomentum;
	private float xPos, yPos;
	private int xTile, yTile;
	private final float sizeX = 20, sizeY = 10;
	private long runtime;
	private ArrayList<PauseState> pauses = new ArrayList<PauseState>();
	private int accelFactor;
	
	// mapare remembers where the object is. start / run / finish / pause / collided
	private String maparea;
	
	/*corner Points
	 index | location
	-------|-----------------
	 0     | botcenter
	 1     | topcenter
	 2-5   | cornerPoint 1-4
	*/
	private float[] cornerX = new float[6];
	private float[] cornerY = new float[6];
	
	public SpeedObj(SpeedMap map)
	{
		this.map = map;
		int[] startTile = map.getStartPos();
		xTile = startTile[0];
		yTile = startTile[1];
		xPos = xTile*map.getTileWidth()+sizeX;
		yPos = yTile*map.getTileHeight()+sizeY;
		accelFactor = DIF_NORMAL_FACTOR;
		maparea = "start";
		
		try 
		{
			car = new Image("res/pic/speedcar.png");
		} 
		catch (SlickException e) 
		{
			System.out.println("[ERROR]: Failed to load car image!");
			e.printStackTrace();
		}
		calculateHitboxCorner(getAngleDEG());
		
		setMyMomentum(0,0);
	}

	public void renderObj (Graphics g)
	{
		g.drawImage(car, xPos-sizeX, yPos-sizeY);
	}
	
	public void updatePosition(int delta)
	{
		float angle = getAngleDEG();
		xPos = xPos + (xMomentum*delta/5);
		yPos = yPos + (yMomentum*delta/5);
		car.setRotation(angle);
		calculateHitboxCorner((float)Math.toRadians(angle));
		
		if (maparea == "run")
		{
			runtime = runtime + delta;
		}
	}
	
	public void calculateHitboxCorner(float angle)
	{
		cornerX[0] = xPos - (float) (Math.sin(angle)*sizeY);
		cornerY[0] = yPos + (float) (Math.cos(angle)*sizeY);
		cornerX[1] = xPos + (float) (Math.sin(angle)*sizeY);
		cornerY[1] = yPos - (float) (Math.cos(angle)*sizeY);
		
		cornerX[2] = cornerX[0] - (float) (Math.cos(angle)*sizeX);
		cornerY[2] = cornerY[0] - (float) (Math.sin(angle)*sizeX);
		cornerX[3] = cornerX[0] + (float) (Math.cos(angle)*sizeX);
		cornerY[3] = cornerY[0] + (float) (Math.sin(angle)*sizeX);
		cornerX[4] = cornerX[1] - (float) (Math.cos(angle)*sizeX);
		cornerY[4] = cornerY[1] - (float) (Math.sin(angle)*sizeX);
		cornerX[5] = cornerX[1] + (float) (Math.cos(angle)*sizeX);
		cornerY[5] = cornerY[1] + (float) (Math.sin(angle)*sizeX);
	}
	
	public int[] getTilePos(float x, float y)
	{
		xTile = (int)x/map.getTileWidth();
		yTile = (int)y/map.getTileHeight();
		return new int[]{xTile, yTile};
	}
	
	public void accelerateToPosition (float x, float y, int delta)
	{
		if (maparea != "pause" && maparea != "collided")
		{
			xMomentum = xMomentum + (x-this.getxPos()) * delta/accelFactor;
			yMomentum = yMomentum + (y-this.getyPos()) * delta/accelFactor;
		}
	}
	
	public float getAngleDEG()
	{
		// alpha = arcsin(y/sqrt(x²+y²))
		float xDir = xMomentum;
		float yDir = yMomentum;
		float angle = (float)Math.asin(yDir/Math.sqrt(xDir*xDir+yDir*yDir));
		angle = (float) Math.toDegrees(angle);
		if (Float.isNaN(angle))
		{
			angle = 0;
		}
		if (xDir < 0)
		{
			angle = 180-angle;
		}
		return angle; 
	}
	
	public boolean checkCollisionstate()
	{
		// id 61 == false
		// id 157 == true
		boolean retvalue = false;
		for (int i = 0; i < 4; i++)
		{
			int[] tilePos = this.getTilePos(cornerX[i+2], cornerY[i+2]);
			if (map.checkForCollisionAt(tilePos[0], tilePos[1]))
			{
				retvalue = true;
			}
		}
		return retvalue;
	}
	
	public String getAndUpdateMaparea(SpeedMap map)
	{
		for (int i = 0; i < 4; i++)
		{
			int[] tilePos = this.getTilePos(cornerX[i+2], cornerY[i+2]);
			if (maparea=="start")
			{
				if (map.getTileProperty(map.getTileId(tilePos[0], tilePos[1], 0), "startarea", "false")=="false")
				{
					maparea="run";
				}
			}
			else if (maparea=="run")
			{
				if (map.getTileProperty(map.getTileId(tilePos[0], tilePos[1], 0), "zielarea", "false")!="false")
				{
					maparea="finish";
				}
			}
		}
		return maparea;
	}
	
	public String getMaparea()
	{
		return maparea;
	}
	
	public long getRunTimeMillis()
	{
		return this.runtime;
	}
	
	public void pauseGame()
	{
		pauses.add(new PauseState(System.currentTimeMillis(), maparea, xMomentum, yMomentum));
		maparea="pause";
		xMomentum = 0;
		yMomentum = 0;
	}
	
	public void continueGame()
	{
		maparea = pauses.get(pauses.size()-1).getMaparea();
		xMomentum = pauses.get(pauses.size()-1).getxMomentum();
		yMomentum = pauses.get(pauses.size()-1).getyMomentum();
		pauses.get(pauses.size()-1).setFinishtime(System.currentTimeMillis());
	}
	
	public void restartGame(SpeedMap map)
	{
		// Reset Logic:
		runtime = 0;
		pauses = new ArrayList<PauseState>();
		
		// Reset Position and Momentum:
		int[] startTile = map.getStartPos();
		xTile = startTile[0];
		yTile = startTile[1];
		xPos = xTile*map.getTileWidth()+sizeX;
		yPos = yTile*map.getTileHeight()+sizeY;
		maparea = "start";
		calculateHitboxCorner(getAngleDEG());
		setMyMomentum(0,0);
	}
	
	public void collided()
	{
		maparea = "collided";
		xMomentum = xMomentum * 0.8F;
		yMomentum = yMomentum * 0.8F;
	}
	
	public void setAccelFactor(int factor)
	{
		this.accelFactor = factor;
	}
	
	//Getter und Setter:
	
	public float getxMomentum() 
	{
		return xMomentum;
	}
	
	public float getyMomentum()
	{
		return yMomentum;
	}

	public void setMyMomentum(float x, float y) 
	{
		this.xMomentum = x;
		this.yMomentum = y;
	}

	public float getxPos() 
	{
		return xPos;
	}

	public void setxPos(float xPos) 
	{
		this.xPos = xPos;
	}

	public float getyPos() 
	{
		return yPos;
	}

	public void setyPos(float yPos) 
	{
		this.yPos = yPos;
	}
}
