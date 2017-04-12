package gui;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState
{
	int myIndex;
	int timeout = 5;
	int mPosX = 0;
	int mPosY = 0;
	String buttonClicked = "none";
	
	public Menu(int index)
	{
		myIndex = index;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		g.setBackground(Run.backgroundColor);
		
		// exit button
		g.setColor(Color.red);
		g.fillRect(Run.screenWidth-120, Run.screenHeight-80, 100, 60);
		
		// play button
		g.setColor(Color.blue);
		g.fillRect(Run.screenWidth/5*2, Run.screenHeight/5*2, Run.screenWidth/5*1, Run.screenHeight/5*1);
		
		//debug mouse pos:
		g.setColor(Color.white);
		g.drawString("Mouse X: " + mPosX, 100, 100);
		g.drawString("Mouse Y: " + mPosY, 100, 120);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		// check if the timeout is over.
		timeout = timeout - delta;
		if (timeout <= 0)
		{
			// stuff to do only and regular 200 times per second goes here
			timeout = 5;
			
			
		}
		
		// stuff to do all the time goes here
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
