package gui;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

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
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		map.render(0, 0);
		
		g.setColor(Color.black);
		
		player.renderObj(g);
		g.drawLine(player.getxPos(), player.getyPos(), mPosX, Run.screenHeight-mPosY);
		
		g.setColor(Color.white);
		g.drawString(""+player.getShowtime(), Run.screenWidth/2-40, 20);
		
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
