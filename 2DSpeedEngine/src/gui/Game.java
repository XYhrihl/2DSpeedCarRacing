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
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import game.HighScore;
import game.SpeedMap;
import game.SpeedObj;

public class Game extends BasicGameState
{
	private int myIndex;
	private int mPosX = 0;
	private int mPosY = 0;
	private int difficulty;
	private SpeedObj player;
	private Input input;
	private ArrayList<SpeedMap> allMaps;
	private SpeedMap map;
	private boolean pause, finished, collided, spacedown;
	private ArrayList<HighScore> highscore;
	private String name;
	private boolean tutorialhint = false;
	private int tutorialState = 0;
	
	private Font mediumFont;
	private TrueTypeFont ttfMediumFont;
	private Font textFont;
	private TrueTypeFont ttfTextFont;
	
	public Game (int index)
	{
		myIndex = index;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		pause = false;
		finished = false;
		collided = false;
		spacedown = false;
		difficulty = Run.DIF_NORMAL;
		
		mediumFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
		ttfMediumFont = new TrueTypeFont(mediumFont, true);
		
		textFont = new Font(Font.MONOSPACED, Font.PLAIN, 24);
		ttfTextFont = new TrueTypeFont(textFont, true);
		
		map = new SpeedMap("res/maps/basic_speedmap.tmx");
		player = new SpeedObj(map);
		highscore = new ArrayList<HighScore>();
		readXMLsaves("save/highscore.xml");
		allMaps = new ArrayList<SpeedMap>();
		loadMaps();
	}

	public void enter(GameContainer gc, StateBasedGame sbg)
	{
		readXMLValues("save/values.xml");
		
		if (map.getMapName().equals("Tutorial Map"))
			tutorialhint = true;
		else
			tutorialhint = false;
		
		player = new SpeedObj(map);
		
		if (tutorialhint)
			player.setAccelFactor(SpeedObj.DIF_EINFACH_FACTOR);
		else if (difficulty == Run.DIF_EINFACH)
			player.setAccelFactor(SpeedObj.DIF_EINFACH_FACTOR);
		else if (difficulty == Run.DIF_NORMAL)
			player.setAccelFactor(SpeedObj.DIF_NORMAL_FACTOR);
		else if (difficulty == Run.DIF_SCHWER)
			player.setAccelFactor(SpeedObj.DIF_SCHWER_FACTOR);
		else
			System.out.println("[ERROR]: Failed to set difficulty! defaulting to Normal.");
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		map.render(0, 0);
		
		if (tutorialhint)
		{
			g.setColor(Color.blue);
			g.setFont(ttfTextFont);
			g.drawString("Lehrtaste --> Restart", 450, 520);
			g.setFont(ttfMediumFont);
			if (tutorialState == 0)
				g.drawString("Klicke um zu Beschleunigen!", 80, 480);
			if (tutorialState == 1)
				g.drawString("Klicke hinter dich um zu Bremsen!", 80, 200);
			if (tutorialState == 2)
				g.drawString("Klicke seitlich um zu lenken!", 200, 450);
			if (tutorialState == 3)
			{	
				g.drawString("Folge dem Kurs und", 300, 200);
				g.drawString("erriech die Grüne", 300, 250);
				g.drawString("fläche schenllstmöglich", 300, 300);
			}
		}
		
		g.setColor(Color.black);
		g.setFont(ttfTextFont);
		
		player.renderObj(g);
		g.drawLine(player.getxPos(), player.getyPos(), mPosX, Run.screenHeight-mPosY);
		
		g.setColor(Color.white);
		g.drawString(""+player.getRunTimeMillis(), Run.screenWidth/2-40, 20);
		
		if (pause || finished || collided)
		{
			g.setColor(Run.backgroundColor);
			g.fillRect(Run.screenWidth/4, Run.screenHeight/4, Run.screenWidth/2, Run.screenHeight/2);
			g.setColor(Color.white);
			g.fillRect(Run.screenWidth/4+Run.screenWidth/32, Run.screenHeight/4+Run.screenHeight/32, Run.screenWidth/2-Run.screenWidth/16, Run.screenHeight/4-Run.screenHeight/16);
			g.setColor(Color.red);
			g.fillRect(Run.screenWidth/4+Run.screenWidth/32, Run.screenHeight/2+Run.screenHeight/32, Run.screenWidth/2-Run.screenWidth/16, Run.screenHeight/4-Run.screenHeight/16);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{
		mPosX = Mouse.getX();
		mPosY = Mouse.getY();
		
		// for inputhandling in checkForFinish():
		input = gc.getInput();
		
		if (checkForFinish())
		{
			if(!finished)
			{
				highscore.add(new HighScore(player.getRunTimeMillis(), name, difficulty, map.getMapName()));
				saveToXML();
			}
			finished = true;
		}
		
		if (input.isMouseButtonDown(0))
		{
			player.accelerateToPosition(mPosX, Run.screenHeight-mPosY, delta);
		}
		
		player.updatePosition(delta);
		
		if(player.checkCollisionstate())
		{
			player.collided();
			if (player.getxMomentum()<0.05F && player.getyMomentum()<0.05F)
			{
				collided = true;
			}
		}
		
		if (input.isKeyDown(Input.KEY_SPACE) && !spacedown)
		{
			spacedown = true;
		}
		if ((!input.isKeyDown(Input.KEY_SPACE)) && spacedown)
		{
			spacedown = false;
			player.restartGame(map);
			finished = false;
			collided = false;
			tutorialState = 0;
		}
		
		// tutorial stuff
		if (tutorialhint)
		{
			if (player.getMaparea()!="start" && tutorialState == 0)
				tutorialState = 1;
			if (player.getyPos()>350 && tutorialState == 1)
				tutorialState = 2;
			if (player.getxPos()>300 && tutorialState == 2)
				tutorialState = 3;
		}
	}

	public int getID() 
	{
		return myIndex;
	}
	
	public boolean checkForFinish()
	{
		// Trigger pause and finish from this method
		
		// pause menu via escape key
		if (input.isKeyPressed(Input.KEY_ESCAPE))
		{
			player.pauseGame();
			pause = true;
		}
		
		if (player.getAndUpdateMaparea(map) == "finish")
		{
			player.setMyMomentum(player.getxMomentum()*0.9F, player.getyMomentum()*0.9F);
			if (player.getxMomentum()<0.05F && player.getxMomentum()>-0.05F)
			{
				return true;
			}
		}
		
		return false;
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
				int thisDifficulty = Integer.parseInt(thisscore.item(2).getTextContent());
				long thisTimeMillis = Long.parseLong(thisscore.item(3).getTextContent());
				String thisMapName = thisscore.item(5).getTextContent();
				highscore.add(new HighScore(thisTime, thisName, thisDifficulty, thisMapName, new Date(thisTimeMillis)));
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
	
	public void readXMLValues(String filename)
	{
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(filename);
			
			Element doc = dom.getDocumentElement();
			NodeList values = doc.getChildNodes();
			name = values.item(0).getTextContent();
			difficulty = Integer.parseInt(values.item(1).getTextContent());
			for (SpeedMap m: allMaps)
			{
				if (m.getMapName().equals(values.item(2).getTextContent()))
					map = m;
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
		
		if (name == null)
		{
			name = "";
		}
	}
	
	public void saveToXML()
	{
		Document doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			
			Element allScores = doc.createElement("allScores");
			
			for (HighScore h: highscore)
			{
				Element score = doc.createElement("score");
				Element time = doc.createElement("Zeit");
				Element name = doc.createElement("Name");
				Element diff = doc.createElement("Schwierigkeit");
				Element datumMillis = doc.createElement("Datum_Millis");
				Element datumString = doc.createElement("Datum");
				Element mapsv = doc.createElement("Map");
				time.appendChild(doc.createTextNode(h.getTimeString()));
				name.appendChild(doc.createTextNode(h.getName()));
				diff.appendChild(doc.createTextNode(Integer.toString(h.getDifficulty())));
				datumMillis.appendChild(doc.createTextNode(Long.toString(h.getDateInMillis())));
				datumString.appendChild(doc.createTextNode(h.getDateString()));
				mapsv.appendChild(doc.createTextNode(h.getMapName()));
				score.appendChild(time);
				score.appendChild(name);
				score.appendChild(diff);
				score.appendChild(datumMillis);
				score.appendChild(datumString);
				score.appendChild(mapsv);
				allScores.appendChild(score);
			}
			
			doc.appendChild(allScores);
			
			try
			{
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("save/highscore.xml"));
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
		if (pause || finished || collided)
		{
			if ((mPosX > Run.screenWidth/4+Run.screenWidth/32) && (mPosY > Run.screenHeight/4+Run.screenHeight/32) && (mPosX < Run.screenWidth/4*3-Run.screenWidth/32) && (mPosY < Run.screenHeight/2-Run.screenHeight/32))
			{
				System.exit(0);
			}
		}
		if (pause)
		{
			if ((mPosX > Run.screenWidth/4+Run.screenWidth/32) && (mPosY > Run.screenHeight/2+Run.screenHeight/32) && (mPosX < Run.screenWidth/4*3-Run.screenWidth/32) && (mPosY < Run.screenHeight/4*3-Run.screenHeight/32))
			{
				player.continueGame();
				pause = false;
			}
		}
		if (finished || collided)
		{
			if ((mPosX > Run.screenWidth/4+Run.screenWidth/32) && (mPosY > Run.screenHeight/2+Run.screenHeight/32) && (mPosX < Run.screenWidth/4*3-Run.screenWidth/32) && (mPosY < Run.screenHeight/4*3-Run.screenHeight/32))
			{
				player.restartGame(map);
				finished = false;
				collided = false;
				tutorialState = 0;
			}
		}
	}
}
