package game;

public class HighScore 
{
	private int time;
	private int points;
	
	public HighScore(int t)
	{
		this.time = t;
		points = calculatePoints();
	}
	
	public int calculatePoints()
	{
		// TODO calculate points!!!
		return 1;
	}
	
	public int[] getScore()
	{
		return new int[] {time, points};
	}
}
