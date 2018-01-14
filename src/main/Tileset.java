package main;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.newdawn.slick.Image;

public class Tileset 
{
	public String name;
	
	private File directory;

	private Image[] tiles;
	
	public int tileSize;
	int numTiles;
	
	public Tileset(File directory)
	{
		this.directory = directory;
		name = directory.getName();
	}
	
	public void init() throws Exception
	{
		//Open up the XML file for parsing
		File tilesetFile = new File(directory.getPath() + "/tileset.tsx");
		URL url = tilesetFile.toURI().toURL();
		
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		
		Element root = document.getRootElement();
		
		tileSize = Integer.parseInt(root.attributeValue("tilewidth"));
		numTiles = Integer.parseInt(root.attributeValue("tilecount"));
		
		tiles = new Image[numTiles];
		
		// Iterate through tiles, loading each one into memory
	    for ( Iterator<Element> i = root.elementIterator("tile"); i.hasNext(); ) 
	    {
	        Element tile = (Element) i.next();
	        
	        int id = Integer.parseInt(tile.attributeValue("id"));
	        
	        Element imageElement = (Element) tile.elements().get(0);
	        
	        String source = imageElement.attributeValue("source");
	        
	        Image image = new Image(directory.getPath() + File.separator + source);
	        
	        tiles[id] = image;
	    }
	}
	
	public Image getTile(int id)
	{
		return tiles[id];
	}
}
