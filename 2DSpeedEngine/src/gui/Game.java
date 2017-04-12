package gui;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import game.SpeedObj;

public class Game extends BasicGameState
{
	private int myIndex;
	private int mPosX = 0;
	private int mPosY = 0;
	private SpeedObj player;
	private Input input;
	private TiledMap map;
	
	public Game (int index)
	{
		myIndex = index;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		player = new SpeedObj();
		map = new TiledMap("res/maps/basic_speedmap.tmx");
		//TODO use this functions to get the collision property of the place where the palyer is
		System.out.println(map.getTileId(1, 2, 0));
		System.out.println(map.getTileProperty(61, "collision", "notFound"));
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		map.render(0, 0);
		
		player.renderObj(g);
		
		g.drawLine(player.getxPos(), player.getyPos(), mPosX, Run.screenHeight-mPosY);
		
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{
		mPosX = Mouse.getX();
		mPosY = Mouse.getY();
		
		// inputhandling:
		input = gc.getInput();
		
		// exit via escape key
		if (input.isKeyPressed(Input.KEY_ESCAPE))
		{
			// TODO add exit warning and question to continue or cancel
			System.exit(0);
		}
		
		if (input.isMouseButtonDown(0))
		{
			player.setMyMomentum(player.accelerateToPosition(mPosX, Run.screenHeight-mPosY, delta));
		}
		
		player.updatePosition(delta);
	}

	public int getID() 
	{
		return myIndex;
	}
	
	//overwrite mouseReleased method for button click handling
	public void mouseReleased(int button, int x, int y)
	{
		
	}
}
