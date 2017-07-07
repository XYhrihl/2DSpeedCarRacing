package game;

import java.util.Date;

public class HighScore 
{
	
	private long time;
	private String name;
	private Date datum;
	private int difficulty;
	
	public HighScore(long t, String n, int dif)
	{
		this.time = t;
		this.name = n;
		this.difficulty = dif;
		this.datum = new Date();
	}
	
	public HighScore(long t, String n, int dif, Date dat)
	{
		this.time = t;
		this.name = n;
		this.difficulty = dif;
		this.datum = dat;
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
	
	public int getDifficulty()
	{
		return this.difficulty;
	}
	
	public String toString()
	{
		return "HighScore Obj: time "+time+" | name "+name+" | difficulty "+difficulty+" | dateMillis "+datum.getTime()+" | dateString "+datum.toString();
	}
}
