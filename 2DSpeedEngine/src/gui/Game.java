package gui;

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
	private SpeedObj player;
	private Input input;
	private SpeedMap map;
	private boolean pause, finished, collided;
	private ArrayList<HighScore> highscore;
	private String name;
	
	public Game (int index)
	{
		myIndex = index;
		pause = false;
		finished = false;
		collided = false;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		map = new SpeedMap("res/maps/basic_speedmap.tmx");
		player = new SpeedObj(map);
		highscore = new ArrayList<HighScore>();
		readXMLsaves("save/highscore.xml");
	}

	public void enter(GameContainer gc, StateBasedGame sbg)
	{
		readXMLValues("save/values.xml");
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		map.render(0, 0);
		
		g.setColor(Color.black);
		
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
				// TODO let the player enter his/her name
				highscore.add(new HighScore(player.getRunTimeMillis(), "placeholder"));
				saveToXML();
			}
			finished = true;
		}
		
		if (input.isMouseButtonDown(0))
		{
			player.accelerateToPosition(mPosX, Run.screenHeight-mPosY, delta);
		}
		
		player.updatePosition(delta);
		
		if(player.checkCollisionstate(map))
		{
			player.collided();
			if (player.getxMomentum()<0.05F && player.getyMomentum()<0.05F)
			{
				collided = true;
			}
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
				long thisTimeMillis = Long.parseLong(thisscore.item(2).getTextContent());
				highscore.add(new HighScore(thisTime, thisName, new Date(thisTimeMillis)));
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
				Element datumMillis = doc.createElement("Datum_Millis");
				Element datumString = doc.createElement("Datum");
				time.appendChild(doc.createTextNode(h.getTimeString()));
				name.appendChild(doc.createTextNode(h.getName()));
				datumMillis.appendChild(doc.createTextNode(Long.toString(h.getDateInMillis())));
				datumString.appendChild(doc.createTextNode(h.getDateString()));
				score.appendChild(time);
				score.appendChild(name);
				score.appendChild(datumMillis);
				score.appendChild(datumString);
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
			}
		}
	}
}
