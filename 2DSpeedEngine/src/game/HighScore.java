package game;

public class HighScore 
{
	// TODO add more attributes like difficulty.. usw.
	private long time;
	private String name;
	
	public HighScore(long t, String n)
	{
		this.time = t;
		this.name = n;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	public String getTimeString()
	{
		return String.valueOf(time);
	}
	
	public String getName()
	{
		return this.name;
	}
}
