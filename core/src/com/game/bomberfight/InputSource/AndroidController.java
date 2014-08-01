package com.game.bomberfight.InputSource;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.game.bomberfight.core.Bomber;
import com.game.bomberfight.model.Controller;

public class AndroidController extends Controller {
	
	private boolean isAccelerometerAvailable = false;
	private float sensibilityX = 1.2f;
	private float sensibilityY = 1.2f;
	
	public AndroidController() {
		// TODO Auto-generated constructor stub
		this.isAccelerometerAvailable = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (isAccelerometerAvailable) {
			float accelX = Gdx.input.getAccelerometerX();
		    float accelY = Gdx.input.getAccelerometerY();
		    Bomber bomber = (Bomber) owner;
		    
		    if (-accelX > sensibilityX) {
		    	bomber.moveUp();
			}
		    if (-accelX < -sensibilityX) {
		    	bomber.moveDown();
			}
		    if (-accelX <= sensibilityX && -accelX >= -sensibilityX) {
		    	bomber.stopVerticalMove();
			}
		    
		    if (accelY > sensibilityY) {
		    	bomber.moveRight();
			}
		    if (accelY < -sensibilityY) {
		    	bomber.moveLeft();
			}
		    if (accelY <= sensibilityY && accelY >= -sensibilityY) {
		    	bomber.stopHorizontalMove();
			}
		    
		    if (Gdx.input.justTouched()) {
		    	bomber.placeBomb();
			}
		    
		    if (Gdx.input.getDeltaX() > 30) {
				if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
					Gdx.app.setLogLevel(Application.LOG_NONE);
				} else {
					Gdx.app.setLogLevel(Application.LOG_DEBUG);
				}
			}
		}
	}

}
