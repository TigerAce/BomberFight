package com.game.bomberfight.InputSource;

import java.util.HashSet;
import java.util.Iterator;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.game.bomberfight.interfaces.Controllable;
import com.game.bomberfight.screen.GamePlay;

/**
 * Created by Tong on 2014/7/27.
 */
public class GamePlayScreenKeyboard implements InputProcessor {
	
	private GamePlay gamePlay;
	private HashSet<Controllable> controllableObjects;
	
	public GamePlayScreenKeyboard(GamePlay gamePlay) {
		this.gamePlay = gamePlay;
	}

    @Override
    public boolean keyDown(int keycode) {
        //Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        //HashSet<Controllable> controllableObjects = ((GamePlay) currentScreen).getControllableObjects();
    	controllableObjects = gamePlay.getControllableObjects();

        Iterator<Controllable> iterator = controllableObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().doKeyDown(keycode);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
    	
		if(keycode == Input.Keys.F){
			System.out.println("f");
		//switch debug view
		if(Gdx.app.getLogLevel() == Application.LOG_DEBUG)
		Gdx.app.setLogLevel(Application.LOG_NONE);
		else Gdx.app.setLogLevel(Application.LOG_DEBUG);
		}
		
        //Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        //HashSet<Controllable> controllableObjects = ((GamePlay) currentScreen).getControllableObjects();
		controllableObjects = gamePlay.getControllableObjects();
        
        if(keycode == Input.Keys.ESCAPE) {
        	gamePlay.getGui().showMenu();
		}

        Iterator<Controllable> iterator = controllableObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().doKeyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        //Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        //HashSet<Controllable> controllableObjects = ((GamePlay) currentScreen).getControllableObjects();
    	controllableObjects = gamePlay.getControllableObjects();

        Iterator<Controllable> iterator = controllableObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().doKeyTyped(character);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        //HashSet<Controllable> controllableObjects = ((GamePlay) currentScreen).getControllableObjects();
    	controllableObjects = gamePlay.getControllableObjects();

        Iterator<Controllable> iterator = controllableObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().doTouchDown(screenX, screenY, pointer, button);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        //HashSet<Controllable> controllableObjects = ((GamePlay) currentScreen).getControllableObjects();
    	controllableObjects = gamePlay.getControllableObjects();

        Iterator<Controllable> iterator = controllableObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().doTouchUp(screenX, screenY, pointer, button);
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        //HashSet<Controllable> controllableObjects = ((GamePlay) currentScreen).getControllableObjects();
    	controllableObjects = gamePlay.getControllableObjects();

        Iterator<Controllable> iterator = controllableObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().doTouchDragged(screenX, screenY, pointer);
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        //HashSet<Controllable> controllableObjects = ((GamePlay) currentScreen).getControllableObjects();
    	controllableObjects = gamePlay.getControllableObjects();

        Iterator<Controllable> iterator = controllableObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().doMouseMoved(screenX, screenY);
        }

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        //Screen currentScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
        //HashSet<Controllable> controllableObjects = ((GamePlay) currentScreen).getControllableObjects();
    	controllableObjects = gamePlay.getControllableObjects();

        Iterator<Controllable> iterator = controllableObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().doScrolled(amount);
        }

        return false;
    }
}