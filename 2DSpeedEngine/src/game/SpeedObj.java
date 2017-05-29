package game;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import gui.Run;

public class SpeedObj 
{
	private Shape hitbox;
	
	private float xMomentum, yMomentum;
	private float xPos, yPos;
	private int xTile, yTile;
	private final float sizeX = 20, sizeY = 10;
	private long starttime, finishtime, runtime, showtime;
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
	
	private float lastTransformRad = 0;
	//private PauseState pauseState;
	
	public SpeedObj(SpeedMap map)
	{
		int[] startTile = map.getStartPos();
		xTile = startTile[0];
		yTile = startTile[1];
		xPos = xTile*map.getTileWidth()+sizeX;
		yPos = yTile*map.getTileHeight()+sizeY;
		// TODO add getter and setter for accelFactor, make a difficulty option wich changes this factor
		accelFactor = 100000;
		maparea = "start";
		starttime = 0;
		
		hitbox = new Rectangle(xPos-sizeX, yPos-sizeY, 2*sizeX, 2*sizeY);
		calculateHitboxCorner(getAngleRAD());
		
		setMyMomentum(0,0);
	}

	public void renderObj (Graphics g)
	{
		g.fill(hitbox);
	}
	
	public void updatePosition(int delta)
	{
		float angle = getAngleRAD();
		xPos = xPos + (xMomentum*delta/5);
		yPos = yPos + (yMomentum*delta/5);
		hitbox.setCenterX(xPos);
		hitbox.setCenterY(yPos);
		hitbox = hitbox.transform(Transform.createRotateTransform(-lastTransformRad, hitbox.getCenterX(), hitbox.getCenterY()));
		hitbox = hitbox.transform(Transform.createRotateTransform(angle, hitbox.getCenterX(), hitbox.getCenterY()));
		calculateHitboxCorner(angle);
		lastTransformRad = angle;
		
		// TODO do collisiondetection here and trigger consequences
		String windowExit = checkForWindowExit();
		if (windowExit!="none")
		{
			calcNewMomentum(windowExit);
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
		xTile = (int)x/48;
		yTile = (int)y/24;
		return new int[]{xTile, yTile};
	}
	
	public void accelerateToPosition (int x, int y, int delta)
	{
		// TODO balance acceleration-rate in this method
		// TODO add difficulty which influences the speed factor
		if (maparea != "pause" && maparea != "collided")
		{
			xMomentum = xMomentum + (x-this.getxPos())*delta/accelFactor;
			yMomentum = yMomentum + (y-this.getyPos())*delta/accelFactor;
		}
	}
	
	public float getAngleRAD()
	{
		// alpha = arcsin(y/sqrt(x�+y�))
		float xDir = xPos - Mouse.getX();
		float yDir = Run.screenHeight-yPos - Mouse.getY();
		float angle = (float)Math.asin(yDir/Math.sqrt(xDir*xDir+yDir*yDir));
		if (Float.isNaN(angle))
		{
			angle = 0;
		}
		if (xDir > 0)
		{
			angle = -angle;
		}
		return angle; 
	}
	
	public void calcNewMomentum(String side)
	{
		if (side=="right" || side=="left")
		{
			xMomentum = -xMomentum;
		}
		if (side=="top" || side=="bot")
		{
			yMomentum = -yMomentum;
		}
	}
	
	public String checkForWindowExit()
	{
		if (xPos-sizeX < 0)
		{
			return "left";
		}
		else if (xPos+sizeX > Run.screenWidth)
		{
			return "right";
		}	
		else if (yPos-sizeY < 0)
		{
			return "bot";
		}
		else if (yPos+sizeY > Run.screenHeight)
		{
			return "top";
		}
		else
		{
			return "none";
		}
	}
	
	public boolean checkCollisionstate(SpeedMap map)
	{
		// id 61 == false
		// id 157 == true
		boolean retvalue = false;
		for (int i = 0; i < 4; i++)
		{
			int[] tilePos = this.getTilePos(cornerX[i+2], cornerY[i+2]);
			if (map.getTileProperty(map.getTileId(tilePos[0], tilePos[1], 0), "collision", "notFound") == map.getTileProperty(157, "collision", "xxx"))
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
					starttime = System.currentTimeMillis();
				}
			}
			else if (maparea=="run")
			{
				if (map.getTileProperty(map.getTileId(tilePos[0], tilePos[1], 0), "zielarea", "false") == map.getTileProperty(26, "zielarea", "xxx"))
				{
					maparea="finish";
					gameFinished();
				}
			}
		}
		return maparea;
	}
	
	public long getRunTimeMillis()
	{
		if (maparea == "finish")
		{
			return finishtime - starttime;
		}
		else
		{
			return -1;
		}
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
	
	public void gameFinished()
	{
		finishtime = System.currentTimeMillis();
		runtime = (finishtime-starttime);
		for (PauseState p : pauses)
		{
			runtime = runtime - (p.getFinishtime()-p.getStarttime());
		}
		System.out.println("Zeit in millis: " + runtime);
	}
	
	public void restartGame(SpeedMap map)
	{
		// TODO Save Score
		
		// Reset Logic:
		finishtime = 0;
		starttime = 0;
		runtime = 0;
		pauses = new ArrayList<PauseState>();
		
		// Reset Position and Momentum:
		int[] startTile = map.getStartPos();
		xTile = startTile[0];
		yTile = startTile[1];
		xPos = xTile*map.getTileWidth()+sizeX;
		yPos = yTile*map.getTileHeight()+sizeY;
		maparea = "start";
		calculateHitboxCorner(getAngleRAD());
		setMyMomentum(0,0);
	}
	
	public void collided()
	{
		maparea = "collided";
		xMomentum = xMomentum * 0.8F;
		yMomentum = yMomentum * 0.8F;
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

	public long getShowtime()
	{
		if (starttime == 0)
		{
			showtime = 0;
		}
		else
		{
			if (maparea!="pause" && maparea!="finish")
			{
				showtime = (System.currentTimeMillis()-starttime);
				for (PauseState p : pauses)
				{
					showtime = showtime - (p.getFinishtime()-p.getStarttime());
				}
			}
		}
		return this.showtime;
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
