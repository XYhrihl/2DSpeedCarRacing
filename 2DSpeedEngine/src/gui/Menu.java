package gui;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import game.HighScore;
import game.SpeedMap;

// TODO BETA avoid writing long mapnames onto the resolution

public class Menu extends BasicGameState
{
	private int myIndex;
	private int mPosX = 0;
	private int mPosY = 0;
	private int difficulty;
	private String buttonClicked = "none";
	private ArrayList<HighScore> highscore;
	private ArrayList<SpeedMap> allMaps;
	private SpeedMap activeMap;
	private boolean includeAllMaps = false;
	private boolean includeAllDiffs = false;
	private boolean hoverPlay = false;
	private boolean hoverExit = false;
	private int diffhover = -1;
	private int maphover = -1;
	
	private Font buttonFont;
	private TrueTypeFont ttfButtonFont;
	private Font mediumFont;
	private TrueTypeFont ttfMediumFont;
	private Font textFont;
	private TrueTypeFont ttfTextFont;
	private TextField txtField;
	
	public Menu(int index)
	{
		myIndex = index;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		difficulty = Run.DIF_NORMAL;
		
		buttonFont = new Font(Font.MONOSPACED, Font.BOLD, 56);
		ttfButtonFont = new TrueTypeFont(buttonFont, true);
		
		mediumFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
		ttfMediumFont = new TrueTypeFont(mediumFont, true);
		
		textFont = new Font(Font.MONOSPACED, Font.PLAIN, 24);
		ttfTextFont = new TrueTypeFont(textFont, true);
		txtField = new TextField(gc, ttfTextFont, Run.screenWidth/4*3+15, Run.screenHeight/8, Run.screenWidth/4-50, 36);
		
		allMaps = new ArrayList<SpeedMap>();
		loadMaps();
		if (allMaps.size()>0)
		{
			activeMap = allMaps.get(0);
		}
		else
		{
			System.out.println("[ERROR]: No Maps found!");
		}
		
		highscore = new ArrayList<HighScore>();
		readXMLsaves("save/highscore.xml");
	}
	
	public void enter(GameContainer gc, StateBasedGame sbg)
	{
		highscore.clear();
		readXMLsaves("save/highscore.xml");
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		g.setBackground(Run.backgroundColor);
		
		g.setFont(ttfButtonFont);
		
		// play button
		g.setColor(Color.white);
		g.fillRoundRect(Run.screenWidth/4*3, Run.screenHeight/8*2, Run.screenWidth/4-20, Run.screenHeight/4, 40);
		if (hoverPlay)
			g.setColor(Run.hoverColor);
		else
			g.setColor(Run.backgroundColor);
		
		g.fillRoundRect(Run.screenWidth/4*3+15, Run.screenHeight/8*2+15, Run.screenWidth/4-50, Run.screenHeight/4-30, 30);
		g.setColor(Color.white);
		g.drawString("SPIELEN", Run.screenWidth/4*3+110, Run.screenHeight/8*3-42);
		
		// exit button
		g.setColor(Color.red);
		g.fillRoundRect(Run.screenWidth/4*3, Run.screenHeight/8*5, Run.screenWidth/4-20, Run.screenHeight/4, 40);
		if (hoverExit)
			g.setColor(Run.hoverColor);
		else
			g.setColor(Run.backgroundColor);
		
		g.fillRoundRect(Run.screenWidth/4*3+15, Run.screenHeight/8*5+15, Run.screenWidth/4-50, Run.screenHeight/4-30, 30);
		g.setColor(Color.red);
		g.drawString("BEENDEN", Run.screenWidth/4*3+110, Run.screenHeight/8*6-42);
		
		// name input TextField
		g.setColor(Color.white);
		g.setFont(ttfMediumFont);
		g.drawString("Spielername:", Run.screenWidth/4*3+15, Run.screenHeight/16);
		txtField.setBackgroundColor(Run.hoverColor);
		txtField.setBorderColor(Run.hoverColor);
		txtField.render(gc, g);
		
		// difficulty selection
		g.setFont(ttfTextFont);
		g.setColor(Color.white);
		g.drawRect(Run.screenWidth/3+20, Run.screenHeight/16, Run.screenWidth/3-40, Run.screenHeight/16-20);
		g.drawLine(Run.screenWidth/9*4+10, Run.screenHeight/16, Run.screenWidth/9*4+10, Run.screenHeight/8-22);
		g.drawLine(Run.screenWidth/9*5-10, Run.screenHeight/16, Run.screenWidth/9*5-10, Run.screenHeight/8-22);
		if (diffhover == Run.DIF_EINFACH)
		{
			g.setColor(Run.hoverColor);
			g.fillRect(Run.screenWidth/3+21, Run.screenHeight/16+1, Run.screenWidth/9-12, Run.screenHeight/16-21);
			g.setColor(Color.white);
		}
		if (difficulty == Run.DIF_EINFACH)
		{
			g.fillRect(Run.screenWidth/3+20, Run.screenHeight/16, Run.screenWidth/9-10, Run.screenHeight/16-20);
			g.setColor(Run.backgroundColor);
		}
		g.drawString("Einfach", Run.screenWidth/3+70, Run.screenHeight/15);
		
		g.setColor(Color.white);
		if (diffhover == Run.DIF_NORMAL)
		{
			g.setColor(Run.hoverColor);
			g.fillRect(Run.screenWidth/9*4+11, Run.screenHeight/16+1, Run.screenWidth/9-21, Run.screenHeight/16-21);
			g.setColor(Color.white);
		}
		if (difficulty == Run.DIF_NORMAL)
		{
			g.fillRect(Run.screenWidth/9*4+11, Run.screenHeight/16+1, Run.screenWidth/9-20, Run.screenHeight/16-20);
			g.setColor(Run.backgroundColor);
		}
		g.drawString("Normal", Run.screenWidth/9*4+65, Run.screenHeight/15);
		
		g.setColor(Color.white);
		if (diffhover == Run.DIF_SCHWER)
		{
			g.setColor(Run.hoverColor);
			g.fillRect(Run.screenWidth/9*5-9, Run.screenHeight/16+1, Run.screenWidth/9-9, Run.screenHeight/16-21);
			g.setColor(Color.white);
		}
		if (difficulty == Run.DIF_SCHWER)
		{
			g.fillRect(Run.screenWidth/9*5-10, Run.screenHeight/16, Run.screenWidth/9-8, Run.screenHeight/16-20);
			g.setColor(Run.backgroundColor);
		}
		g.drawString("Schwer", Run.screenWidth/9*5+55, Run.screenHeight/15);
		
		// map selection
		int y_mapNullpoint = Run.screenHeight/16*3;
		g.setColor(Color.white);
		g.fillRect(Run.screenWidth/3+20, y_mapNullpoint, Run.screenWidth/3-40, Run.screenHeight/4*3);
		g.setColor(Run.backgroundColor);
		g.fillRect(Run.screenWidth/3+24, y_mapNullpoint+4, Run.screenWidth/3-48, Run.screenHeight/4*3-8);
		g.setColor(Color.white);
		g.setFont(ttfMediumFont);
		g.drawString("Mapauswahl:", Run.screenWidth/3+24, y_mapNullpoint-42);
		g.setFont(ttfTextFont);
		g.drawString("Aufl�sung", Run.screenWidth/9*5+60, y_mapNullpoint-34);
				
		// the actual maps
		for (SpeedMap m: allMaps)
		{
			// TODO limit the listlenght and create more sites
			
			g.setColor(Color.white);
			if (maphover == allMaps.indexOf(m))
			{
				g.setColor(Run.hoverColor);
				g.fillRect(Run.screenWidth/3+24, y_mapNullpoint+maphover*28+4, Run.screenWidth/3-48, 28);
				g.setColor(Color.white);
			}
			if (activeMap == m)
			{
				g.fillRect(Run.screenWidth/3+24, y_mapNullpoint+allMaps.indexOf(m)*28+4, Run.screenWidth/3-48, 28);
				g.setColor(Run.backgroundColor);
			}
			g.drawString(m.getMapName(), Run.screenWidth/3+28, y_mapNullpoint+allMaps.indexOf(m)*28);
			g.drawString(m.getResolution(), Run.screenWidth/9*5+60, y_mapNullpoint+allMaps.indexOf(m)*28);
		}
		
		// highscores
		g.setColor(Color.white);
		g.fillRect(60, Run.screenHeight/32+4, 30, 30);
		if (includeAllMaps)
			g.setColor(Color.lightGray);
		else
			g.setColor(Run.backgroundColor);
		g.fillRect(64, Run.screenHeight/32+8, 22, 22);
		g.setColor(Color.white);
		g.setFont(ttfTextFont);
		g.drawString("Alle Maps anzeigen", 110, Run.screenHeight/32);
		
		g.fillRect(60, Run.screenHeight/32+64, 30, 30);
		if (includeAllDiffs)
			g.setColor(Color.lightGray);
		else
			g.setColor(Run.backgroundColor);
		g.fillRect(64, Run.screenHeight/32+68, 22, 22);
		g.setColor(Color.white);
		g.setFont(ttfTextFont);
		g.drawString("Alle Schwierigkeiten anzeigen", 110, Run.screenHeight/32+60);
		
		g.fillRect(20, y_mapNullpoint, Run.screenWidth/3-40, Run.screenHeight/4*3);
		g.setColor(Run.backgroundColor);
		g.fillRect(24, y_mapNullpoint+4, Run.screenWidth/3-48, Run.screenHeight/4*3-8);
		g.setColor(Color.white);
		g.setFont(ttfMediumFont);
		g.drawString("HighScores:", 24, y_mapNullpoint-42);
		
		g.drawLine(20, y_mapNullpoint+32, Run.screenWidth/3-24, y_mapNullpoint+32);
		g.drawLine(20, y_mapNullpoint+31, Run.screenWidth/3-24, y_mapNullpoint+31);
		g.drawLine(20, y_mapNullpoint+33, Run.screenWidth/3-24, y_mapNullpoint+33);
		g.drawLine(Run.screenWidth/3-128, y_mapNullpoint, Run.screenWidth/3-128, Run.screenHeight/16*15);
		g.setFont(ttfTextFont);
		g.drawString("Spieler:", 28, y_mapNullpoint);
		g.drawString("Zeit[s]", Run.screenWidth/3-124, y_mapNullpoint);
		
		ArrayList<HighScore> showHighScores = calcShowHighScores();
		
		for (HighScore h: showHighScores)
		{
			g.drawString(h.getName(), 28, y_mapNullpoint+(showHighScores.indexOf(h)+1)*28);
			g.drawString(h.getTimeString(), Run.screenWidth/3-124, y_mapNullpoint+(showHighScores.indexOf(h)+1)*28);
			g.drawLine(24, y_mapNullpoint+(showHighScores.indexOf(h)+2)*28+4, Run.screenWidth/3-24, y_mapNullpoint+(showHighScores.indexOf(h)+2)*28+4);
		}

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		if (buttonClicked == "play")
		{
			buttonClicked = "none";
			saveValuesToXML();
			sbg.enterState(Run.gameIndex);
		}
		
		mPosX = Mouse.getX();
		mPosY = Mouse.getY();
		
		// for hover effect on buttons
		// play button
		if ((mPosX<Run.screenWidth-35 && mPosX>Run.screenWidth/4*3+15) && (mPosY<Run.screenHeight/4*3-15 && mPosY>Run.screenHeight/2+15))
			hoverPlay = true;
		else
			hoverPlay = false;
		
		// exit button
		if ((mPosX<Run.screenWidth-35 && mPosX>Run.screenWidth/4*3+15) && (mPosY<Run.screenHeight/8*3-15 && mPosY>Run.screenHeight/8+15))
			hoverExit = true;
		else
			hoverExit = false;
		
		// dif hover
		if ((mPosX>Run.screenWidth/3+20 && mPosX<Run.screenWidth/9*4+10) && (mPosY<Run.screenHeight/16*15+8 && mPosY>Run.screenHeight/8*7+20))
			diffhover = Run.DIF_EINFACH;
		else if ((mPosX>Run.screenWidth/9*4+10 && mPosX<Run.screenWidth/9*5-10) && (mPosY<Run.screenHeight/16*15+8 && mPosY>Run.screenHeight/8*7+20))
			diffhover = Run.DIF_NORMAL;
		else if ((mPosX>Run.screenWidth/9*5-10 && mPosX<Run.screenWidth/3*2-20) && (mPosY<Run.screenHeight/16*15+8 && mPosY>Run.screenHeight/8*7+20))
			diffhover = Run.DIF_SCHWER;
		else
			diffhover = -1;
		
		// map hover
		int y_mapNullPoint = Run.screenHeight/16*13+4;
		if ((mPosX>Run.screenWidth/3+24 && mPosX<Run.screenWidth/3*2-24) && (mPosY>Run.screenHeight/16+4 && mPosY<y_mapNullPoint))
		{
			if (mPosY<y_mapNullPoint-allMaps.size()*28)
				maphover = -1;
			else
				maphover = (y_mapNullPoint-mPosY)/28;
		}
		else
		{
			maphover = -1;
		}
	}

	public int getID() 
	{
		return myIndex;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<HighScore> calcShowHighScores()
	{
		ArrayList<HighScore> retList = new ArrayList<HighScore>();
	
		// TODO check the other cases
		// TODO add numbers
		if (!includeAllDiffs && !includeAllMaps)
		{
			retList = sortScores((ArrayList<HighScore>) highscore.clone());
		}
		
		return retList;
	}
	
	public ArrayList<HighScore> sortScores(ArrayList<HighScore> scores)
	{
		ArrayList<HighScore> retList = new ArrayList<HighScore>();
		
		while (scores.size()>0)
		{
			long min = Long.MAX_VALUE;
			int index = -1;
			
			for (HighScore h: scores)
			{
				if (h.getTime()<min)
				{
					min = h.getTime();
					index = scores.indexOf(h);
				}
			}
			retList.add(scores.get(index));
			scores.remove(index);
		}
		
		return retList;
	}
	
	public void readXMLsaves(String filename)
	{
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(filename);
			
			Element doc = dom.getDocumentElement();
			NodeList scoresFromXML = doc.getChildNodes();
			
			// save every xml score tag in highscore ArrayList
			for (int i = 0; i<scoresFromXML.getLength(); i++)
			{
				NodeList thisscore = scoresFromXML.item(i).getChildNodes();
				long thisTime = Long.parseLong(thisscore.item(0).getTextContent());
				String thisName = thisscore.item(1).getTextContent();
				long thisTimeMillis = Long.parseLong(thisscore.item(3).getTextContent());
				highscore.add(new HighScore(thisTime, thisName, difficulty, new Date(thisTimeMillis)));
			}
		}
		catch (ParserConfigurationException pce) 
		{
            System.out.println(pce.getMessage());
        } 
		catch (SAXException se) 
		{
            System.out.println(se.getMessage());
        } 
		catch (IOException ioe) 
		{
            System.err.println(ioe.getMessage());
        }
	}
	
	public void saveValuesToXML()
	{
		Document doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			
			Element allValues = doc.createElement("allValues");
			Element name = doc.createElement("Name");
			Element diff = doc.createElement("Schwierigkeit");
			name.appendChild(doc.createTextNode(txtField.getText()));
			diff.appendChild(doc.createTextNode(difficulty+""));
			allValues.appendChild(name);
			allValues.appendChild(diff);
			doc.appendChild(allValues);
			
			try
			{
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("save/values.xml"));
				tr.transform(source, result);
			}
			catch (TransformerException te) 
			{
	            System.out.println(te.getMessage());
	        }
		}
		catch(ParserConfigurationException pce)
		{
			System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		}
	}
	
	public void loadMaps()
	{
		// TODO add a name to the map to show it in the list
		File mapDir = new File("res/maps");
		for (File f: mapDir.listFiles())
		{
			try 
			{
				allMaps.add(new SpeedMap(f.toString()));
			} 
			catch (SlickException e) 
			{
				System.out.println("Failed to load Map at "+f.toString());
				e.printStackTrace();
			}
		}
	}
	
	//overwrite mouseReleased method for button click handling
	public void mouseReleased(int button, int x, int y)
	{
		if (button == Input.MOUSE_LEFT_BUTTON)
		{
			// play button
			if ((x<Run.screenWidth-35 && x>Run.screenWidth/4*3+15) && (y<Run.screenHeight/2-15 && y>Run.screenHeight/4+15))
			{
				buttonClicked = "play";
			}
			
			// exit button
			if ((x<Run.screenWidth-35 && x>Run.screenWidth/4*3+15) && (y<Run.screenHeight/8*7-15 && y>Run.screenHeight/8*5+15))
			{
				System.exit(0);
			}
			
			// difficulty buttons
			if ((mPosX>Run.screenWidth/3+20 && mPosX<Run.screenWidth/9*4+10) && (mPosY<Run.screenHeight/16*15+8 && mPosY>Run.screenHeight/8*7+20))
			{
				difficulty = Run.DIF_EINFACH;
			}
			if ((mPosX>Run.screenWidth/9*4+10 && mPosX<Run.screenWidth/9*5-10) && (mPosY<Run.screenHeight/16*15+8 && mPosY>Run.screenHeight/8*7+20))
			{
				difficulty = Run.DIF_NORMAL;
			}
			if ((mPosX>Run.screenWidth/9*5-10 && mPosX<Run.screenWidth/3*2-20) && (mPosY<Run.screenHeight/16*15+8 && mPosY>Run.screenHeight/8*7+20))
			{
				difficulty = Run.DIF_SCHWER;
			}
			
			// map selection
			int y_mapNullpoint = Run.screenHeight/16*3;
			if ((x>Run.screenWidth/3+24 && x<Run.screenWidth/3*2-24) && (y<Run.screenWidth/3-48 && y>y_mapNullpoint-4))
			{
				if (y<y_mapNullpoint+allMaps.size()*28+4)
				{
					activeMap = allMaps.get((y-y_mapNullpoint-4)/28);
				}
			}
			
			// toggle includeAllMaps
			if ((x>60 && x<90) && (y>Run.screenHeight/32+4 && y<Run.screenHeight/32+34))
				includeAllMaps = !includeAllMaps;
			
			// toggle includeAllDiffs
			if ((x>60 && x<90) && (y>Run.screenHeight/32+64 && y<Run.screenHeight/32+94))
				includeAllDiffs = !includeAllDiffs;
		}
	}
}
