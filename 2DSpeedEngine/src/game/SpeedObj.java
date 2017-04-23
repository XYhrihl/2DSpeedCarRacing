package game;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.tiled.TiledMap;

import gui.Run;

public class SpeedObj 
{
	private Momentum myMomentum;
	private Shape hitbox;
	
	private float xPos, yPos;
	private int xTile, yTile;
	private final float sizeX = 20, sizeY = 10;
	private float lastTransformRad = 0;
	
	public SpeedObj()
	{
		// TODO init TilePos, get the pos from the maps starting position
		xPos = Run.screenWidth/2;
		yPos = Run.screenHeight/2;
		xTile = 10;
		yTile = 10;
		
		hitbox = new Rectangle(xPos-sizeX, yPos-sizeY, 2*sizeX, 2*sizeY);
		setMyMomentum(new Momentum(0,0));
	}

	public void renderObj (Graphics g)
	{
		g.fill(hitbox);
	}
	
	public void updatePosition(int delta)
	{
		xPos = xPos + (myMomentum.getxDir()*delta/5);
		yPos = yPos + (myMomentum.getyDir()*delta/5);
		hitbox.setCenterX(xPos);
		hitbox.setCenterY(yPos);
		hitbox = hitbox.transform(Transform.createRotateTransform(-lastTransformRad, hitbox.getCenterX(), hitbox.getCenterY()));
		hitbox = hitbox.transform(Transform.createRotateTransform(getAngleRAD(), hitbox.getCenterX(), hitbox.getCenterY()));
		lastTransformRad = getAngleRAD();
		// TODO angle only works to the rigth side. angle to the left side is 90° off.
		
		String windowExit = checkForWindowExit();
		if (windowExit!="none")
		{
			calcNewMomentum(windowExit);
		}
	}
	
	public int[] getTilePos()
	{
		xTile = (int)this.getxPos()/48;
		yTile = (int)this.getyPos()/24;
		return new int[]{xTile, yTile};
	}
	
	public void accelerateToPosition (int x, int y, int delta)
	{
		// TODO balance acceleration-rate in this method
		// TODO add difficulty which influences the speed factor
		int factor = 100000;
		myMomentum.addToMomentum(new Momentum((x-this.getxPos())*delta/factor, (y-this.getyPos())*delta/factor));
	}
	
	public float getAngleRAD()
	{
		// alpha = arcsin(y/sqrt(x²+y²))
		float xDir = xPos - Mouse.getX();
		float yDir = yPos - Mouse.getY();
		float angle = (float)Math.asin(yDir/Math.sqrt(xDir*xDir+yDir*yDir));
		if (Float.isNaN(angle))
		{
			angle = 0;
		}
		return angle; 
	}
	
	public void calcNewMomentum(String side)
	{
		if (side=="right" || side=="left")
		{
			myMomentum.setxDir(-myMomentum.getxDir());
		}
		if (side=="top" || side=="bot")
		{
			myMomentum.setyDir(-myMomentum.getyDir());
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
	
	public boolean checkCollisionstate(TiledMap map)
	{
		// id 61 == false
		// id 157 == true
		int[] tilePos = this.getTilePos();
		if (map.getTileProperty(map.getTileId(tilePos[0], tilePos[1], 0), "collision", "notFound") == map.getTileProperty(157, "collision", "xxx"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Getter und Setter:
	
	public Momentum getMyMomentum() 
	{
		return myMomentum;
	}

	public void setMyMomentum(Momentum myMomentum) 
	{
		this.myMomentum = myMomentum;
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
