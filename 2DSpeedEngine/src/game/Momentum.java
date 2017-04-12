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
