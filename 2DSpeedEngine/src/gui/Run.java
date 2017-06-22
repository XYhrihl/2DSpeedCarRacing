package gui;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Run extends StateBasedGame
{
	public static int menuIndex = 0;
	public static int gameIndex = 1;
	public static String gamename = "2DSpeedEngine";
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	
	public static Color backgroundColor = new Color(26,120,50);
	
	public Run(String name) 
	{
		super(name);
		this.addState(new Menu(menuIndex));
		this.addState(new Game(gameIndex));
	}

	public void initStatesList(GameContainer gc) throws SlickException 
	{
		//init is allredy called in the constructor
		//this.getState(menuIndex).init(gc, this);
		//this.getState(gameIndex).init(gc, this);
		this.enterState(menuIndex);
	}

	public static void main(String[] args)
	{
		AppGameContainer appgc;
		try
		{
			appgc = new AppGameContainer(new Run(gamename));
			screenWidth = appgc.getScreenWidth();
			screenHeight = appgc.getScreenHeight();
			//appgc.setTargetFrameRate(200);
			appgc.setDisplayMode(screenWidth, screenHeight, false);
			appgc.setFullscreen(true); // need exit button in all states for true
			appgc.start();
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
	}
}
