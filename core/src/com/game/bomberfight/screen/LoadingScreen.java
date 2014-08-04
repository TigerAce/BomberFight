package com.game.bomberfight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.bomberfight.utility.Config;

public class LoadingScreen implements Screen {
	
	private Skin skin;
	private Stage stage;
	private ExtendViewport viewport;

	public LoadingScreen() {
		// TODO Auto-generated constructor stub
		viewport = new ExtendViewport(Config.getInstance().get("viewportWidth", Float.class), Config.getInstance().get("viewportHeight", Float.class));
		skin = new Skin();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
		viewport.getCamera().update();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		ProgressBar progressBar = new ProgressBar(0, 100, 1, false, skin);
		
		stage.addActor(progressBar);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		skin.dispose();
		stage.dispose();
	}

}
