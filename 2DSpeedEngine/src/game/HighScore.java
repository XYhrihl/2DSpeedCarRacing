package game;

import java.util.Date;

import gui.Run;

public class HighScore 
{
	
	private long time;
	private String name;
	private Date datum;
	private int difficulty;
	private String mapName;
	
	public HighScore(long t, String n, int dif, String m)
	{
		this.time = t;
		this.name = n;
		this.difficulty = dif;
		this.mapName = m;
		this.datum = new Date();
	}
	
	public HighScore(long t, String n, int dif, String m, Date dat)
	{
		this.time = t;
		this.name = n;
		this.difficulty = dif;
		this.mapName = m;
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
	
	public String getDifShortcut()
	{
		if (difficulty==Run.DIF_EINFACH)
			return "E";
		else if (difficulty==Run.DIF_NORMAL)
			return "N";
		else if (difficulty==Run.DIF_SCHWER)
			return "S";
		else
			return "X";
	}
	
	public String toString()
	{
		return "HighScore Obj: time "+time+" | name "+name+" | difficulty "+difficulty+" | dateMillis "+datum.getTime()+" | dateString "+datum.toString();
	}
	
	public String getMapName()
	{
		return this.mapName;
	}
}
