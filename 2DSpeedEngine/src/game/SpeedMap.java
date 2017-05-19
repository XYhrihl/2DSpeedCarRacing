package game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SpeedMap extends TiledMap
{
	public SpeedMap(String ref) throws SlickException 
	{
		super(ref);
	}
	
	public int[] getStartPos()
	{
		for(int i = 0; i < this.getWidth(); i++)
		{
			for(int j = 0; j < this.getHeight(); j++)
			{
				// id from the starting tile is 122
				// TODO read the propertys to be able to use other IDs
				if(this.getTileId(i, j, 0)==122)
				{
					return new int[] {i, j};
				}
			}
		}
		return new int[] {-1, -1};
	}
}
