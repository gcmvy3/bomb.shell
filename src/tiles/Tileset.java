package tiles;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.newdawn.slick.Image;

public class Tileset 
{
	public String name;
	
	private File directory;

	private ArrayList<TileType> tileTypes;
	
	int numTiles;
	public int tileSize;
	
	boolean initialized = false;
	
	public Tileset(File directory)
	{
		this.directory = directory;
		name = directory.getName();
	}
	
	public void init() throws Exception
	{
		if(initialized)
		{
			return;
		}
		
		//Open up the XML file for parsing
		File tilesetFile = new File(directory.getPath() + "/tileset.tsx");
		URL url = tilesetFile.toURI().toURL();
		
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		
		Element root = document.getRootElement();
		
		numTiles = Integer.parseInt(root.attributeValue("tilecount"));
		tileSize = Integer.parseInt(root.attributeValue("tilewidth"));
		
		tileTypes = new ArrayList<TileType>(numTiles);
		
		//Add null tile type (id = -1)
		TileType nullTile = new TileType(-1);
		tileTypes.add(nullTile);
		
		// Iterate through tiles, creating a new TileType for each one
	    for ( Iterator<Element> i = root.elementIterator("tile"); i.hasNext(); ) 
	    {
	        Element tile = (Element) i.next();
	        
	        int id = Integer.parseInt(tile.attributeValue("id"));
	        
	        TileType newType = new TileType(id);
	        
	        Element properties = tile.element("properties");
	        
	        if(properties != null)
	        {
	        	//Read in custom tile properties
	        	for(Element property : properties.elements("property"))
	        	{
	        		try
	        		{
		        		switch(property.attributeValue("name"))
		        		{
		        		case "health":
		        			newType.health = Integer.parseInt(property.attributeValue("value"));
		        			break;
		        		case "solid":
		        			newType.solid = Boolean.valueOf(property.attributeValue("value"));
		        			break;
		        		case "destructible":
		        			newType.destructible = Boolean.valueOf(property.attributeValue("value"));
		        			break;
		        		case "visible":
		        			newType.visible = Boolean.valueOf(property.attributeValue("value"));
		        			break;
		        		case "spawn":
		        			newType.spawn = Boolean.valueOf(property.attributeValue("value"));
		        			break;
		        		case "randomRot":
		        			newType.randomRot = Boolean.valueOf(property.attributeValue("value"));
		        			break;
		        		default:
		        			throw new Exception();
		        		}
	        		}
	        		catch(Exception e)
	        		{
	        			System.err.println("Could not read in tile property! (tileID " + id + ")");
	        		}
	        	}
	        }
	        
	        Element imageElement = tile.element("image");
	        
		    String source = imageElement.attributeValue("source");
		        
		    Image image = new Image(directory.getPath() + File.separator + source);    
		    newType.sprite = image.getScaledCopy(tileSize, tileSize);
		    
		    tileTypes.add(newType);
	    }
	    
	    initialized = true;
	}
	
	public TileType getTileType(int id)
	{
		for(TileType t : tileTypes)
		{
			if(t.id == id)
			{
				return t;
			}
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
