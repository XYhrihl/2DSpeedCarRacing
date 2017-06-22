package game;

public class HighScore 
{
	// TODO add more attributes like difficulty.. usw.
	private long time;
	private long points;
	
	public HighScore(long t)
	{
		this.time = t;
		points = calculatePoints();
	}
	
	public long calculatePoints()
	{
		// TODO calculate points!!!
		return 1;
	}
	
	public long[] getScore()
	{
		return new long[] {time, points};
	}
	
	public String getTimeString()
	{
		return String.valueOf(time);
	}
	
	public String getPointString()
	{
		return String.valueOf(points);
	}
}
