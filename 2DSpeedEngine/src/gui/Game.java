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
		//TODO use this functions to get the collision property of the place where the player is
		System.out.println(map.getTileId((int) (player.getxPos()/48), (int) (player.getyPos()/24), 0));
		System.out.println("157: "+map.getTileProperty(157, "collision", "notFound"));
		// TODO why is there 146??? --> change tiled map!
		System.out.println("146: "+map.getTileProperty(146, "collision", "notFound"));
		System.out.println("61: "+map.getTileProperty(61, "collision", "notFound"));
		// id 61 == false
		// id 157 == true
		System.out.println("player x: " + player.getxPos() + "\nPlayer y: " + player.getyPos());
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
		
		//debug:
		System.out.println(map.getTileId((int) (player.getxPos()/48), (int) (player.getyPos()/24), 0));
		
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
			player.accelerateToPosition(mPosX, Run.screenHeight-mPosY, delta);
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
