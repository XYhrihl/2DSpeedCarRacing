package game;

public class HighScore 
{
	// TODO add more attributes like difficulty.. usw.
	private long time;
	
	public HighScore(long t)
	{
		this.time = t;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	public String getTimeString()
	{
		return String.valueOf(time);
	}
}
