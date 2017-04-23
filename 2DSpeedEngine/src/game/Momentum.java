package game;

public class Momentum 
{
	private float xDir, yDir;
	
	public Momentum(){}
	
	public Momentum(float x, float y)
	{
		setxDir(x);
		setyDir(y);
	}

	public Momentum addToMomentum(Momentum m)
	{
		xDir = xDir + m.getxDir();
		yDir = yDir + m.getyDir();
		return this;
	}
	
	public float getAngleRAD()
	{
		// TODO replace movedirection with direction to the mouse (maybe move to speedobj class)
		// alpha = arcsin(y/sqrt(x²+y²))
		float angle = (float)Math.asin(yDir/Math.sqrt(xDir*xDir+yDir*yDir));
		if (Float.isNaN(angle))
		{
			angle = 0;
		}
		return angle; 
	}
	
	// Getter und Setter:
	
	public float getxDir() 
	{
		return xDir;
	}

	public void setxDir(float xDir) 
	{
		this.xDir = xDir;
	}

	public float getyDir() 
	{
		return yDir;
	}

	public void setyDir(float yDir) 
	{
		this.yDir = yDir;
	}
}
