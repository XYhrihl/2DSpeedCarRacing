package game;

import java.util.Date;

public class HighScore 
{
	// TODO add more attributes like difficulty.. usw.
	private long time;
	private String name;
	private Date datum;
	
	public HighScore(long t, String n)
	{
		this.time = t;
		this.name = n;
		this.datum = new Date();
	}
	
	public HighScore(long t, String n, Date d)
	{
		this.time = t;
		this.name = n;
		this.datum = d;
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
	
	public long getDateInMillis()
	{
		return this.datum.getTime();
	}
	
	public String getDateString()
	{
		return this.datum.toString();
	}
}
