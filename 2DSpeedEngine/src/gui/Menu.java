package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

public class Menu extends BasicGameState
{
	private int myIndex;
	private int mPosX = 0;
	private int mPosY = 0;
	private String buttonClicked = "none";
	private ArrayList<HighScore> highscore;
	
	public Menu(int index)
	{
		myIndex = index;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		highscore = new ArrayList<HighScore>();
		readXMLsaves("save/highscore.xml");
	}
	
	public void enter(GameContainer gc, StateBasedGame sbg)
	{
		readXMLsaves("save/highscore.xml");
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		g.setBackground(Run.backgroundColor);
		
		// exit button
		g.setColor(Color.red);
		g.fillRect(Run.screenWidth-120, Run.screenHeight-80, 100, 60);
		
		// play button
		g.setColor(Color.white);
		g.fillRect(Run.screenWidth/5*2, Run.screenHeight/5*2, Run.screenWidth/5*1, Run.screenHeight/5*1);
		
		//debug mouse pos:
		g.setColor(Color.white);
		g.drawString("Mouse X: " + mPosX, 100, 100);
		g.drawString("Mouse Y: " + mPosY, 100, 120);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		if (buttonClicked == "play")
		{
			buttonClicked = "none";
			sbg.enterState(Run.gameIndex);
		}
		
		// mousePosition for debugshowing in render method
		mPosX = Mouse.getX();
		mPosY = Mouse.getY();
		
	}

	public int getID() 
	{
		return myIndex;
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
	
	//overwrite mouseReleased method for button click handling
	public void mouseReleased(int button, int x, int y)
	{
		if (button == Input.MOUSE_LEFT_BUTTON)
		{
			// exit button
			if ((x<Run.screenWidth-20 && x>Run.screenWidth-120) && (y<Run.screenHeight-20 && y>Run.screenHeight-80))
			{
				System.exit(0);
			}
			
			// play button
			if ((x<Run.screenWidth/5*3 && x>Run.screenWidth/5*2) && (y<Run.screenHeight/5*3 && y>Run.screenHeight/5*2))
			{
				buttonClicked = "play";
			}
		}
	}
}
