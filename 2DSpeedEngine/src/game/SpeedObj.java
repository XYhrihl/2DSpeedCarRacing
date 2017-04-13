package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import gui.Run;

public class SpeedObj 
{
	private Momentum myMomentum;
	private float xPos, yPos;
	private final float sizeX = 20, sizeY = 20;
	
	public SpeedObj()
	{
		setMyMomentum(new Momentum(0,0));
		xPos = Run.screenWidth/2;
		yPos = Run.screenHeight/2;
	}

	public boolean renderObj (Graphics g)
	{
		if (xPos-sizeX < 0 || xPos+sizeX > Run.screenWidth || yPos-sizeY < 0 || yPos+sizeY > Run.screenHeight)
		{
			return false;
		}
		g.setColor(Color.black);
		g.fillRect(xPos-sizeX, yPos-sizeY, 2*sizeX, 2*sizeY);
		return true;
	}
	
	public void updatePosition(int delta)
	{
		xPos = xPos + (myMomentum.getxDir()*delta/5);
		yPos = yPos + (myMomentum.getyDir()*delta/5);
		String collisionstate = checkForCollision();
		if (collisionstate!="none")
		{
			calcNewMomentum(collisionstate);
		}
	}
	
	public void accelerateToPosition (int x, int y, int delta)
	{
		// TODO balance acceleration-rate in this method
		// TODO maybe physics bug? --> resolved?
		// TODO add difficulty which influences the speed factor --> add factor
		int factor = 100000;
		myMomentum.addToMomentum(new Momentum((x-this.getxPos())*delta/factor, (y-this.getyPos())*delta/factor));
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
	
	public String checkForCollision()
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
