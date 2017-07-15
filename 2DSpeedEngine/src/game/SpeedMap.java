package game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SpeedMap extends TiledMap
{
	private String mapName;
	private String resolution;
	
	public SpeedMap(String ref) throws SlickException 
	{
		super(ref);
		mapName = this.getMapProperty("Kartenname", "failed");
		resolution = this.width*this.tileWidth+"x"+this.height*this.tileHeight;
	}
	
	public int[] getStartPos()
	{
		for(int i = 0; i < this.getWidth(); i++)
		{
			for(int j = 0; j < this.getHeight(); j++)
			{
				if (this.getTileProperty(this.getTileId(i, j, 0), "startposition", "false").equals("start"))
				{
					return new int[] {i, j};
				}
			}
		}
		return new int[] {-1, -1};
	}
	
	public boolean checkForCollisionAt (int x, int y)
	{
		boolean ret = false;
		
		if (this.getTileProperty(this.getTileId(x, y, 0), "collision", "notfount").equals("true"))
		{
			ret = true;
		}
		
		return ret;
	}
	
	public String getMapName()
	{
		return this.mapName;
	}
	
	public String getResolution()
	{
		return this.resolution;
	}
}
