package com.game.bomberfight.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.bomberfight.utility.Config;

public class LoadingScreen implements Screen {
	
	private Skin skin;
	private Stage stage;
	private ExtendViewport viewport;
	private AssetManager assetManager;
	private GamePlay gamePlay;
	private ProgressBar progressBar;

	public LoadingScreen() {
		// TODO Auto-generated constructor stub
		viewport = new ExtendViewport(Config.getInstance().get("viewportWidth", Float.class), Config.getInstance().get("viewportHeight", Float.class));
		skin = new Skin();
		stage = new Stage();
		//gamePlay = new MultiplayerGamePlay();
		gamePlay = new GamePlay();
		assetManager = gamePlay.getAssetManager();
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
		viewport.getCamera().update();
		load();
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
		TextureParameter textureParameter = new TextureParameter();
		textureParameter.minFilter = TextureFilter.Linear;
		textureParameter.magFilter = TextureFilter.Linear;
		assetManager.load("img/texture/crate4.jpg", Texture.class, textureParameter);
		assetManager.load("img/texture/brick3.jpg", Texture.class, textureParameter);
		assetManager.load("img/animation/soldier1.png", Texture.class, textureParameter);
		assetManager.load("particle/flame.p", ParticleEffect.class);
		assetManager.load("img/texture/bomb.png", Texture.class, textureParameter);
		assetManager.load("img/texture/item1.png", Texture.class, textureParameter);
		assetManager.load("img/texture/item2.png", Texture.class, textureParameter);
		assetManager.load("img/texture/item3.png", Texture.class, textureParameter);
		assetManager.load("img/texture/item4.png", Texture.class, textureParameter);
		assetManager.load("img/texture/item5.png", Texture.class, textureParameter);
		// load audio
		assetManager.load("audio/explosion/explosion1.mp3", Sound.class);
		assetManager.load("audio/timer/timer1.mp3", Sound.class);
		
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(GamePlay.gameInfo.mapInfo.tmx, TiledMap.class);
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		Table table = new Table();
		table.setFillParent(true);
		
		progressBar = new ProgressBar(0, 1, 0.1f, false, skin);
		progressBar.setStyle(skin.get("hp-horizontal", SliderStyle.class));
		table.add(progressBar).minSize(stage.getViewport().getViewportWidth() - 20, stage.getViewport().getViewportHeight() / 10);
		
		stage.addActor(table);
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
	
	public void load() {
		if (!assetManager.update()) {
			progressBar.setValue(assetManager.getProgress());
		} else {
			gamePlay.setAssetManager(assetManager);
			((Game) Gdx.app.getApplicationListener()).setScreen(gamePlay);
		}
	}

}
