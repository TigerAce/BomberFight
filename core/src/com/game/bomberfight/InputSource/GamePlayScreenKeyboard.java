package com.game.bomberfight.InputSource;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.game.bomberfight.screen.GamePlay;

/**
 * Created by Tong on 2014/7/27.
 */
public class GamePlayScreenKeyboard implements InputProcessor {
	
	private GamePlay gamePlay;
	
	public GamePlayScreenKeyboard(GamePlay gamePlay) {
		this.gamePlay = gamePlay;
	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.F) {
			System.out.println("f");
			// switch debug view
			if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
				Gdx.app.setLogLevel(Application.LOG_NONE);
			else
				Gdx.app.setLogLevel(Application.LOG_DEBUG);
		}
        
        if(keycode == Input.Keys.ESCAPE) {
        	gamePlay.getGui().showMenu();
		}
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}