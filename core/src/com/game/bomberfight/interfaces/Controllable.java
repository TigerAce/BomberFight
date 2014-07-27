package com.game.bomberfight.interfaces;

/**
 * This interface implement the methods that react to InputProcessor controls
 * Created by Tong on 2014/7/27.
 */
public interface Controllable {

    public boolean doKeyUp(int keycode);
    public boolean doKeyDown(int keycode);
    public boolean doKeyTyped(char character);
    public boolean doTouchDown(int screenX, int screenY, int pointer, int button);
    public boolean doTouchUp(int screenX, int screenY, int pointer, int button);
    public boolean doTouchDragged(int screenX, int screenY, int pointer);
    public boolean doMouseMoved(int screenX, int screenY);
    public boolean doScrolled(int amount);
}
