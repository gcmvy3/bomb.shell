package tiles;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TileMap
{
	File directory;
	
	public String name;
	
	public int numRows;
	public int numColumns;
	
	public int tileSize;
	
	public int[][] background;
	public int[][] foreground;
	
	public TileMap(File directory)
	{
		this.directory = directory;
		name = directory.getName();
	}
	
	public void init() throws Exception
	{
		loadFromFile();
	}
	
	/**
	 * Parse an XML map file created using Tiled
	 * @throws Exception If parsing fails
	 */
	private void loadFromFile() throws Exception
	{
		//Open up the XML file for parsing
		File tilemapFile = new File(directory.getPath() + "/tilemap.tmx");
		URL url = tilemapFile.toURI().toURL();
		
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		
		Element root = document.getRootElement();
		
		//Parse level metadata
		numRows = Integer.parseInt(root.attributeValue("height"));
		numColumns = Integer.parseInt(root.attributeValue("width"));
		tileSize = Integer.parseInt(root.attributeValue("tilewidth"));
		
		Element backgroundElement = null;
		Element foregroundElement = null;
		
		// Iterate through layers, looking for the background and foreground
	    for ( Iterator<Element> i = root.elementIterator("layer"); i.hasNext(); ) 
	    {
	        Element layer = (Element) i.next();
	        if(layer.attributeValue("name").equals("background"))
	        {
	        	backgroundElement = layer;
	        }
	        else if(layer.attributeValue("name").equals("foreground"))
	        {
	        	foregroundElement = layer;
	        }
	    }
		
		//If we didn't find the background or foreground, throw an exception
		if(backgroundElement == null || foregroundElement == null)
		{
			System.err.println("Could not find background and foreground layers in tilemap!");
			throw new Exception();
		}
		
		//Read in layer data
		background = new int[numColumns][numRows];
		foreground = new int[numColumns][numRows];
		
		Element backgroundData = backgroundElement.element("data");
		Element foregroundData = foregroundElement.element("data");
		
		//Make sure encoding is csv
		if(!(backgroundData.attributeValue("encoding").equals("csv") &&
				foregroundData.attributeValue("encoding").equals("csv")))
		{
			System.err.println("Only CSV encoding is supported!");
			throw new Exception();
		}
		
		String[] backgroundText = backgroundData.getText().split(",");
		String[] foregroundText = foregroundData.getText().split(",");
		
		int i = 0;
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				backgroundText[i] = backgroundText[i].replaceAll("\n", "");
				foregroundText[i] = foregroundText[i].replaceAll("\n", "");
				
				background[c][r] = Integer.parseInt(backgroundText[i]) - 1;
				foreground[c][r] = Integer.parseInt(foregroundText[i]) - 1;
				i++;
			}
		}
	}
	
	public void printForeground()
	{
		
		System.out.println("Foreground:");
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				System.out.print(foreground[c][r] + ",");
			}	
			System.out.print("\n");
		}
	}
	
	public void printBackground()
	{
		System.out.println("Background:");
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				System.out.print(background[c][r] + ",");
			}	
			System.out.print("\n");
		}
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}