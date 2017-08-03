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
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
	private boolean exitHover, menuHover, restartHover;
	private ArrayList<HighScore> highscore;
	private String name;
	private String buttonClicked = "none";
	private boolean tutorialhint = false;
	private boolean exclamationFlag = true;
	private int tutorialState = 0;
	private int animationState = 0;
	private long animationStamp;
	private int exclamationCounter = 0;
	private float scaleX;
	private float scaleY;
	
	private Font mediumFont;
	private TrueTypeFont ttfMediumFont;
	private Font textFont;
	private TrueTypeFont ttfTextFont;
	private Font bigFont;
	private TrueTypeFont ttfBigFont;
	
	private Image bluerr;
	
	private Image crashed40;
	private Image crashed44;
	private Image crashed40big;
	private Image exclamationRed;
	private Image[] crashedimgs;
	private Animation crashedAni;
	private Image[] crashedimgsExclamation;
	private Animation crashedAniExclamation;
	
	private Image finished40;
	private Image finished44;
	private Image finished40big;
	private Image exclamationGreen;
	private Image[] finishedimgs;
	private Animation finishedAni;
	private Image[] finishedimgsExclamation;
	private Animation finishedAniExclamation;
	
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
		exitHover = false;
		menuHover = false;
		restartHover = false;
		difficulty = Run.DIF_NORMAL;
		
		mediumFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
		ttfMediumFont = new TrueTypeFont(mediumFont, true);
		
		textFont = new Font(Font.MONOSPACED, Font.PLAIN, 24);
		ttfTextFont = new TrueTypeFont(textFont, true);
		
		bigFont = new Font(Font.MONOSPACED, Font.BOLD, 60);
		ttfBigFont = new TrueTypeFont(bigFont, true);
		
		bluerr = new Image("res/pic/roundrectblue_t140.png");
		
		crashed40 = new Image("res/animation/crashed_40.png");
		crashed44 = new Image("res/animation/crashed_44.png");
		crashed40big = new Image("res/animation/crashed_40_big.png");
		exclamationRed = new Image("res/animation/exclamation_red.png");
		
		crashedimgs = new Image[12];
		crashedimgs[0] = new Image("res/animation/crashed_40_t30.png");
		crashedimgs[1] = new Image("res/animation/crashed_40_t50.png");
		crashedimgs[2] = new Image("res/animation/crashed_40_t70.png");
		crashedimgs[3] = new Image("res/animation/crashed_40_t90.png");
		crashedimgs[4] = new Image("res/animation/crashed_40_t110.png");
		crashedimgs[5] = new Image("res/animation/crashed_40_t130.png");
		crashedimgs[6] = new Image("res/animation/crashed_40_t150.png");
		crashedimgs[7] = new Image("res/animation/crashed_40_t170.png");
		crashedimgs[8] = new Image("res/animation/crashed_40_t190.png");
		crashedimgs[9] = new Image("res/animation/crashed_40_t210.png");
		crashedimgs[10] = new Image("res/animation/crashed_40_t230.png");
		crashedimgs[11] = crashed40;
		
		crashedimgsExclamation = new Image[2];
		crashedimgsExclamation[0] = crashed44;
		crashedimgsExclamation[1] = crashed40big;
		
		crashedAni = new Animation(crashedimgs, 50);
		crashedAniExclamation = new Animation(crashedimgsExclamation, 20);
		
		finished40 = new Image("res/animation/finished_40.png");
		finished44 = new Image("res/animation/finished_44.png");
		finished40big = new Image("res/animation/finished_40_big.png");
		exclamationGreen = new Image("res/animation/exclamation_green.png");
		
		finishedimgs = new Image[12];
		finishedimgs[0] = new Image("res/animation/finished_40_t30.png");
		finishedimgs[1] = new Image("res/animation/finished_40_t50.png");
		finishedimgs[2] = new Image("res/animation/finished_40_t70.png");
		finishedimgs[3] = new Image("res/animation/finished_40_t90.png");
		finishedimgs[4] = new Image("res/animation/finished_40_t110.png");
		finishedimgs[5] = new Image("res/animation/finished_40_t130.png");
		finishedimgs[6] = new Image("res/animation/finished_40_t150.png");
		finishedimgs[7] = new Image("res/animation/finished_40_t170.png");
		finishedimgs[8] = new Image("res/animation/finished_40_t190.png");
		finishedimgs[9] = new Image("res/animation/finished_40_t210.png");
		finishedimgs[10] = new Image("res/animation/finished_40_t230.png");
		finishedimgs[11] = finished40;
		
		finishedimgsExclamation = new Image[2];
		finishedimgsExclamation[0] = finished44;
		finishedimgsExclamation[1] = finished40big;
		
		finishedAni = new Animation(finishedimgs, 50);
		finishedAniExclamation = new Animation(finishedimgsExclamation, 20);
		
		map = new SpeedMap("res/maps/basic_speedmap.tmx");
		player = new SpeedObj(map);
		player.setDifficulty(difficulty);
		highscore = new ArrayList<HighScore>();
		readXMLsaves("save/highscore.xml");
		allMaps = new ArrayList<SpeedMap>();
		loadMaps();
		
		scaleX = (float) Run.screenWidth / (float) (map.getWidth()*map.getTileWidth());
		scaleY = (float) Run.screenHeight / (float) (map.getHeight()*map.getTileHeight());
	}

	public void enter(GameContainer gc, StateBasedGame sbg)
	{
		readXMLValues("save/values.xml");
		
		scaleX = (float) Run.screenWidth / (float) (map.getWidth()*map.getTileWidth());
		scaleY = (float) Run.screenHeight / (float) (map.getHeight()*map.getTileHeight());
		
		pause = false;
		finished = false;
		collided = false;
		spacedown = false;
		exitHover = false;
		menuHover = false;
		restartHover = false;
		tutorialState = 0;
		animationState = 0;
		exclamationCounter = 0;
		
		if (map.getMapName().equals("Tutorial Map"))
			tutorialhint = true;
		else
			tutorialhint = false;
		
		player = new SpeedObj(map);
		player.setDifficulty(difficulty);
		
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
		g.scale(scaleX, scaleY);
		g.setBackground(Color.lightGray);
		map.render(0, 0);
		
		if (tutorialhint)
		{
			g.setColor(Color.blue);
			g.setFont(ttfTextFont);
			g.drawString("Leertaste --> Neustart", 430, 520);
			g.setFont(ttfMediumFont);
			if (tutorialState == 0)
				g.drawString("Klicke um zu beschleunigen!", 80, 480);
			if (tutorialState == 1)
				g.drawString("Klicke hinter dich um zu bremsen!", 80, 200);
			if (tutorialState == 2)
				g.drawString("Klicke seitlich um zu lenken!", 200, 450);
			if (tutorialState == 3)
			{	
				g.drawString("Folge dem Kurs und", 300, 200);
				g.drawString("erreich die grüne", 300, 250);
				g.drawString("Fläche schnellstmöglich", 300, 300);
			}
		}
		
		g.setColor(Color.black);
		g.setFont(ttfTextFont);
		
		player.renderObj(g);
		g.scale(1/scaleX, 1/scaleY);
		
		g.setColor(Color.white);
		g.drawString(""+player.getRunTimeMillis(), Run.screenWidth/2-40, 20);
		
		if (((finished || collided) && (exclamationCounter > 1)) || pause)
		{
			if (restartHover)
				g.setColor(Color.lightGray);
			else
				g.setColor(Color.white);
			g.fillRoundRect(Run.screenWidth/10, Run.screenHeight/5*3, Run.screenWidth/5, Run.screenHeight/8, 25);
			
			if (menuHover)
				g.setColor(Color.lightGray);
			else
				g.setColor(Color.white);
			g.fillRoundRect(Run.screenWidth/10*4, Run.screenHeight/5*3, Run.screenWidth/5, Run.screenHeight/8, 25);
			
			if (exitHover)
				g.setColor(Color.lightGray);
			else
				g.setColor(Color.white);
			g.fillRoundRect(Run.screenWidth/10*7, Run.screenHeight/5*3, Run.screenWidth/5, Run.screenHeight/8, 25);
			
			g.setColor(Run.hoverColor);
			g.fillRoundRect(Run.screenWidth/10+12, Run.screenHeight/5*3+12, Run.screenWidth/5-24, Run.screenHeight/8-24, 22);
			g.setColor(Run.backgroundColor);
			g.fillRoundRect(Run.screenWidth/10*4+12, Run.screenHeight/5*3+12, Run.screenWidth/5-24, Run.screenHeight/8-24, 22);
			g.setColor(Run.darkred);
			g.fillRoundRect(Run.screenWidth/10*7+12, Run.screenHeight/5*3+12, Run.screenWidth/5-24, Run.screenHeight/8-24, 22);
			
			g.setFont(ttfMediumFont);
			g.setColor(Color.white);
			
			if (pause)
				g.drawString("Weiter", Run.screenWidth/10+130, Run.screenHeight/5*3+40);
			else
				g.drawString("Neustart", Run.screenWidth/10+110, Run.screenHeight/5*3+40);
			
			g.drawString("Hauptmenü", Run.screenWidth/10*4+105, Run.screenHeight/5*3+40);
			g.drawString("Beenden", Run.screenWidth/10*7+130, Run.screenHeight/5*3+40);
		}
		
		if (animationState != 0 || pause)
		{
			g.drawImage(bluerr, Run.screenWidth/2-227, Run.screenHeight/3-28);
			if (pause)
			{
				g.setColor(Color.yellow);
				g.setFont(ttfBigFont);
				g.drawString("PAUSE", Run.screenWidth/2-90, Run.screenHeight/3);
			}
		}
		
		if (animationState == -1)
		{
			g.drawAnimation(crashedAni, Run.screenWidth/2-178, Run.screenHeight/3);
		}
		else if (animationState == -2)
		{
			g.drawImage(crashed40, Run.screenWidth/2-178, Run.screenHeight/3);
			exclamationFlag = true;
		}
		else if (animationState == -3)
		{
			g.drawAnimation(crashedAniExclamation, Run.screenWidth/2-190, Run.screenHeight/3);
			if (exclamationFlag)
			{
				exclamationCounter ++;
				exclamationFlag = false;
			}
		}
		else if (animationState == -4)
		{
			g.drawImage(crashed44, Run.screenWidth/2-190, Run.screenHeight/3);
		}
		
		if (animationState == 1)
		{
			g.drawAnimation(finishedAni, Run.screenWidth/2-178, Run.screenHeight/3);
		}
		else if (animationState == 2)
		{
			g.drawImage(finished40, Run.screenWidth/2-178, Run.screenHeight/3);
			exclamationFlag = true;
		}
		else if (animationState == 3)
		{
			g.drawAnimation(finishedAniExclamation, Run.screenWidth/2-190, Run.screenHeight/3);
			if (exclamationFlag)
			{
				exclamationCounter ++;
				exclamationFlag = false;
			}
		}
		else if (animationState == 4)
		{
			g.drawImage(finished44, Run.screenWidth/2-190, Run.screenHeight/3);
			g.setColor(Run.finishColor);
			g.setFont(ttfMediumFont);
			g.drawString("Zeit: " + player.getRunTimeMillis() + " ms", Run.screenWidth/2-120, Run.screenHeight/3+60);
		}
		
		for(int i = 0; i<exclamationCounter; i++)
		{
			if (animationState<0)
				g.drawImage(exclamationRed, Run.screenWidth/2+142+i*20, Run.screenHeight/3);
			else
				g.drawImage(exclamationGreen, Run.screenWidth/2+142+i*20, Run.screenHeight/3);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{
		mPosX = Mouse.getX();
		mPosY = Mouse.getY();
		
		if (buttonClicked.equals("mainMenu"))
		{
			buttonClicked = "none";
			sbg.enterState(Run.menuIndex);
		}
		
		// hovereffects
		if ((mPosX > Run.screenWidth/10) && (mPosX < Run.screenWidth/10*3) && (mPosY < Run.screenHeight-Run.screenHeight/5*3) && (mPosY > Run.screenHeight-Run.screenHeight/5*3-Run.screenHeight/8))
			restartHover = true;
		else
			restartHover = false;
		
		if ((mPosX > Run.screenWidth/10*4) && (mPosX < Run.screenWidth/10*6) && (mPosY < Run.screenHeight-Run.screenHeight/5*3) && (mPosY > Run.screenHeight-Run.screenHeight/5*3-Run.screenHeight/8))
			menuHover = true;
		else
			menuHover = false;
		
		if ((mPosX > Run.screenWidth/10*7) && (mPosX < Run.screenWidth/10*9) && (mPosY < Run.screenHeight-Run.screenHeight/5*3) && (mPosY > Run.screenHeight-Run.screenHeight/5*3-Run.screenHeight/8))
			exitHover = true;
		else
			exitHover = false;
		
		// for inputhandling in checkForFinish():
		input = gc.getInput();
		
		if (checkForFinish())
		{
			if (animationState == 0)
				animationState = 1;
			
			if(!finished)
			{
				highscore.add(new HighScore(player.getRunTimeMillis(), name, difficulty, map.getMapName()));
				saveToXML();
			}
			finished = true;
		}
		
		if (input.isMouseButtonDown(0))
		{
			player.accelerateToPosition(mPosX/scaleX, (Run.screenHeight-mPosY)/scaleY, delta);
		}
		
		player.updatePosition(delta);
		
		if(player.checkCollisionstate())
		{
			if (animationState == 0)
				animationState = -1;
			
			player.collided();
			if (player.getxMomentum()<0.05F && player.getyMomentum()<0.05F)
			{
				collided = true;
			}
		}
		
		if (animationState == 1 && finishedAni.getCurrentFrame() == finished40)
		{
			finishedAni.restart();
			animationState = 2;
			animationStamp = System.currentTimeMillis();
		}
		
		if (animationState == -1 && crashedAni.getCurrentFrame() == crashed40)
		{
			crashedAni.restart();
			animationState = -2;
			animationStamp = System.currentTimeMillis();
		}
		
		if (System.currentTimeMillis()-animationStamp > 400 && (animationState == 2 || animationState == 3))
		{
			if (animationState == 2)
				animationState = 3;
			else if (animationState == 3)
				animationState = 2;
			animationStamp = System.currentTimeMillis();
			if (exclamationCounter >= 3)
			{
				animationState = 4;
			}
		}
		
		if (System.currentTimeMillis()-animationStamp > 400 && (animationState == -2 || animationState == -3))
		{
			if (animationState == -2)
				animationState = -3;
			else if (animationState == -3)
				animationState = -2;
			animationStamp = System.currentTimeMillis();
			if (exclamationCounter >= 3)
			{
				animationState = -4;
			}
		}
		
		if (input.isKeyDown(Input.KEY_SPACE) && !spacedown && player.getMaparea()!="pause")
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
			animationState = 0;
			exclamationCounter = 0;
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
		if (input.isKeyPressed(Input.KEY_ESCAPE) && !finished && !collided)
		{
			if (!pause)
			{
				player.pauseGame();
				pause = true;
			}
			else
			{
				player.continueGame();
				pause = false;
			}
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
		if ((finished || collided) && (exclamationCounter > 1))
		{
			if ((x > Run.screenWidth/10*7) && (x < Run.screenWidth/10*9) && (y > Run.screenHeight/5*3) && (y < Run.screenHeight/5*3+Run.screenHeight/8))
			{
				System.exit(0);
			}
			
			if ((x > Run.screenWidth/10*4) && (x < Run.screenWidth/10*6) && (y > Run.screenHeight/5*3) && (y < Run.screenHeight/5*3+Run.screenHeight/8))
			{
				buttonClicked = "mainMenu";
			}
			
			if ((x > Run.screenWidth/10) && (x < Run.screenWidth/10*3) && (y > Run.screenHeight/5*3) && (y < Run.screenHeight/5*3+Run.screenHeight/8))
			{
				player.restartGame(map);
				finished = false;
				collided = false;
				tutorialState = 0;
				animationState = 0;
				exclamationCounter = 0;
			}
		}
		if (pause)
		{
			if ((x > Run.screenWidth/10*7) && (x < Run.screenWidth/10*9) && (y > Run.screenHeight/5*3) && (y < Run.screenHeight/5*3+Run.screenHeight/8))
			{
				System.exit(0);
			}
			
			if ((x > Run.screenWidth/10*4) && (x < Run.screenWidth/10*6) && (y > Run.screenHeight/5*3) && (y < Run.screenHeight/5*3+Run.screenHeight/8))
			{
				buttonClicked = "mainMenu";
			}
			
			if ((x > Run.screenWidth/10) && (x < Run.screenWidth/10*3) && (y > Run.screenHeight/5*3) && (y < Run.screenHeight/5*3+Run.screenHeight/8))
			{
				player.continueGame();
				pause = false;
			}
		}
	}
}
