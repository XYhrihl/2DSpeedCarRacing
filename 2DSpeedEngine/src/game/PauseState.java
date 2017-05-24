package game;

public class PauseState 
{
	private String maparea;
	private float xMomentum, yMomentum;
	private long starttime, finishtime;
	
	public PauseState ()
	{
		
	}
	
	public PauseState (long starttime, String maparea, float xM, float yM)
	{
		this.starttime = starttime;
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
	
	public long getStarttime()
	{
		return this.starttime;
	}
	
	public long getFinishtime()
	{
		return this.finishtime;
	}
	
	public void setFinishtime(long t)
	{
		this.finishtime = t;
	}
}
