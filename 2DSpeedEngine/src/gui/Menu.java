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

public class Menu extends BasicGameState
{
	private int myIndex;
	private int mPosX = 0;
	private int mPosY = 0;
	private String buttonClicked = "none";
	private ArrayList<HighScore> highscore;
	private boolean hoverPlay = false;
	private boolean hoverExit = false;
	private Font buttonFont;
	private TrueTypeFont ttfButtonFont;
	private Font mediumFont;
	private TrueTypeFont ttfMediumFont;
	private Font textFont;
	private TrueTypeFont txtFieldFont;
	private TextField txtField;
	
	public Menu(int index)
	{
		myIndex = index;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		buttonFont = new Font(Font.MONOSPACED, Font.BOLD, 56);
		ttfButtonFont = new TrueTypeFont(buttonFont, true);
		
		mediumFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
		ttfMediumFont = new TrueTypeFont(mediumFont, true);
		
		textFont = new Font(Font.MONOSPACED, Font.PLAIN, 24);
		txtFieldFont = new TrueTypeFont(textFont, true);
		txtField = new TextField(gc, txtFieldFont, Run.screenWidth/4*3+15, Run.screenHeight/8, Run.screenWidth/4-50, 36);
		
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
		
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		if (buttonClicked == "play")
		{
			buttonClicked = "none";
			saveValuesToXML();
			sbg.enterState(Run.gameIndex);
		}
		
		// mousePosition for debugshowing in render method
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
			name.appendChild(doc.createTextNode(txtField.getText()));
			allValues.appendChild(name);
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
		}
	}
}
