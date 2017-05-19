package game;

public class PauseState 
{
	private String maparea;
	private float xMomentum, yMomentum;
	
	public PauseState ()
	{
		
	}
	
	public PauseState (String maparea, float xM, float yM)
	{
		this.maparea = maparea;
		this.xMomentum = xM;
		this.yMomentum = yM;
	}
	
	// Getter und Setter:
	
	public String getMaparea()
	{
		return this.maparea;
	}
	
	public float getxMomentum()
	{
		return this.xMomentum;
	}
	
	public float getyMomentum()
	{
		return this.yMomentum;
	}
}
