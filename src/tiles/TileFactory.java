package tiles;

import main.BackgroundTile;
import main.DestructibleTile;
import main.IndestructibleTile;
import main.Level;

public class TileFactory 
{
	public static Tile createTileById(float x, float y, int row, int column, float size, int id, Level level)
	{	
		switch(id)
		{
		default:
			return new Tile(x, y, row, column, (int)size, id, level);
		case 1:
			return new BackgroundTile(x, y, row, column, (int)size, id, level);
		case 2:
			return new DestructibleTile(x, y, row, column, (int)size, id, level);
		case 3:
			return new IndestructibleTile(x, y, row, column, (int)size, id, level);
		}
	}
}
