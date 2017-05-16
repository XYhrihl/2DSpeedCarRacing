package game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SpeedMap extends TiledMap
{
	private int[][] ids;
	
	public SpeedMap(String ref) throws SlickException 
	{
		super(ref);
		ids = new int [this.getWidth()] [this.getHeight()];
		for(int i = 0; i <  this.getWidth(); i++)
		{
			for(int j = 0; j < this.getHeight(); j++)
			{
				ids[i][j] = this.getTileId(i, j, 0);
			}
		}
	}
	
	public int getIdAt(int x, int y)
	{
		return ids[x][y];
	}
	
	public int[] getStartPos()
	{
		for(int i = 0; i < this.getWidth(); i++)
		{
			for(int j = 0; j < this.getHeight(); j++)
			{
				// id from the starting tile is 122
				// TODO read the propertys to be able to use other IDs
				if(getIdAt(i,j)==122)
				{
					return new int[] {i, j};
				}
			}
		}
		return new int[] {-1, -1};
	}
}
