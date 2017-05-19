package gui;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
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
	private boolean pause;
	
	public Game (int index)
	{
		myIndex = index;
		pause = false;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		map = new SpeedMap("res/maps/basic_speedmap.tmx");
		player = new SpeedObj(map);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		map.render(0, 0);
		
		if (player.checkCollisionstate(map) == false)
		{
			g.setColor(Color.black);
		}
		else
		{
			g.setColor(Color.red);
		}
		player.renderObj(g);
		
		g.drawLine(player.getxPos(), player.getyPos(), mPosX, Run.screenHeight-mPosY);
		
		if (pause)
		{
			g.setColor(Run.backgroundColor);
			g.fillRect(Run.screenWidth/4, Run.screenHeight/4, Run.screenWidth/2, Run.screenHeight/2);
			g.setColor(Color.blue);
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
		
		checkForFinish();
		
		if (input.isMouseButtonDown(0))
		{
			player.accelerateToPosition(mPosX, Run.screenHeight-mPosY, delta);
		}
		
		player.updatePosition(delta);
	}

	public int getID() 
	{
		return myIndex;
	}
	
	public void checkForFinish()
	{
		// Trigger pause and finish from this method
		
		// exit via escape key and mouseclick
		if (input.isKeyPressed(Input.KEY_ESCAPE))
		{
			player.pauseGame();
			pause = true;
		}
		
		if (player.getAndUpdateMaparea(map) == "finish")
		{
			player.setMyMomentum(player.getxMomentum()*0.9F, player.getyMomentum()*0.9F);
		}
	}
	
	//overwrite mouseReleased method for button click handling
	public void mouseReleased(int button, int x, int y)
	{
		// TODO add exit warning and question to continue or cancel
		// TODO add buttons and mousePosition
		if (pause)
		{
			if ((mPosX > Run.screenWidth/4+Run.screenWidth/32) && (mPosY > Run.screenHeight/4+Run.screenHeight/32) && (mPosX < Run.screenWidth/4*3-Run.screenWidth/32) && (mPosY < Run.screenHeight/2-Run.screenHeight/32))
			{
				System.exit(0);
			}
			if ((mPosX > Run.screenWidth/4+Run.screenWidth/32) && (mPosY > Run.screenHeight/2+Run.screenHeight/32) && (mPosX < Run.screenWidth/4*3-Run.screenWidth/32) && (mPosY < Run.screenHeight/4*3-Run.screenHeight/32))
			{
				player.continueGame();
				pause = false;
			}
		}
	}
}
