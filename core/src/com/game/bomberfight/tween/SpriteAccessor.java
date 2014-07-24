package com.game.bomberfight.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Tong on 2014/7/21.
 */
public class SpriteAccessor implements TweenAccessor<Sprite> {

    public static final int ALPHA = 0;


    @Override
    public int getValues(Sprite sprite, int i, float[] floats) {

        switch (i) {
            case ALPHA:
                floats[0] = sprite.getColor().a;
                return 1;   // returns the number of floats in floats[]
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Sprite sprite, int i, float[] floats) {

        switch (i) {
            case ALPHA:
                sprite.setColor(sprite.getColor().r, sprite.getColor().g, sprite.getColor().b, floats[0]);
                break;
            default:
                assert false;
        }
    }
}
