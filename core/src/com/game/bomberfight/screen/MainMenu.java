package com.game.bomberfight.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.bomberfight.model.GameInfo;
import com.game.bomberfight.utility.Config;

public class MainMenu implements Screen {
	
	private ExtendViewport viewport;

	private Object[] mapEntries = { 
			"Metal Classic", 
			"No Hide", 
			"Another map1", 
			"Another map2", 
			"Another map3", 
			"Another map4", 
			"Another map5", 
			"Another map6", 
			"Another map7", 
			"Another map8" 
			};
	
	private Object[] networkEntries = { 
			"Localhost", 
			"LAN", 
			"WAN"
			};
	
	private Object[] gameModeEntries = { 
			"Normal", 
			"RPG", 
			"MMORPG", 
			"Superman"
			};

	private Skin skin;
	private Stage stage;
	private GameInfo gameInfo;

	public MainMenu() {
		// TODO Auto-generated constructor stub
		viewport = new ExtendViewport(Config.getInstance().get("viewportWidth", Float.class), Config.getInstance().get("viewportHeight", Float.class));
		skin = new Skin();
		stage = new Stage();
		gameInfo = new GameInfo();
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
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);
		viewport.update(width, height);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void show() {
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		Table table = new Table();
		table.setFillParent(true);
		table.pad(5);
		table.top();
		table.left();
		
		Label mappreviewLabel = new Label("Map Preview:", skin);
		table.add(mappreviewLabel).expandX().left().top().padBottom(5);
		
		Label networkLabel = new Label("Network:", skin);
		table.add(networkLabel).left().top().padBottom(5).padRight(5);
		
		@SuppressWarnings("rawtypes")
		SelectBox networkDropdown = new SelectBox(skin);
		networkDropdown.setItems(networkEntries);
		networkDropdown.setSelected("Localhost");
		table.add(networkDropdown).expandX().fillX().left().top().padBottom(5);
		
		table.row();
		
		Image imageActor = new Image(new Texture(Gdx.files.internal("img/ui/metal_classic.png")));
		final Container<Image> imageContainer = new Container<Image>();
		imageContainer.setActor(imageActor);
		ScrollPane previewScrollPane = new ScrollPane(imageContainer, skin);
		table.add(previewScrollPane).minSize(400, 300).maxSize(400, 300).left().top().padBottom(5);
		
		Label gameModeLabel = new Label("Game Mode:", skin);
		table.add(gameModeLabel).left().top().padBottom(5).padRight(5);
		
		@SuppressWarnings("rawtypes")
		SelectBox gameModeDropdown = new SelectBox(skin);
		gameModeDropdown.setItems(gameModeEntries);
		gameModeDropdown.setSelected("Normal");
		table.add(gameModeDropdown).expandX().fillX().left().top().padBottom(5);
		
		table.row();
		
		Label mapLabel = new Label("Map:", skin);
		table.add(mapLabel).expandX().left().top().padBottom(5);
		
		table.row();
		
		@SuppressWarnings("rawtypes")
		final List list = new List(skin);
		list.setItems(mapEntries);
		list.getSelection().setMultiple(false);
		list.getSelection().setRequired(true);
		gameInfo.gameMap = "img/tmx/ground2.tmx";
		
		ScrollPane scrollPane = new ScrollPane(list, skin);
		scrollPane.setFlickScroll(true);
		table.add(scrollPane).minWidth(400).fillY().left().top();
		
		TextButton startButton = new TextButton("Start", skin);
		table.add(startButton).colspan(2).minSize(100, 50);
		
		table.pack();
		stage.addActor(table);
		//table.debug();
		
		scrollPane.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				String item = (String) list.getSelected();
				if (item.equalsIgnoreCase("Metal Classic")) {
					imageContainer.setActor(new Image(new Texture(Gdx.files
							.internal("img/ui/metal_classic.png"))));
					gameInfo.gameMap = "img/tmx/ground2.tmx";
				} else if (item.equalsIgnoreCase("No Hide")) {
					imageContainer.setActor(new Image(new Texture(Gdx.files
							.internal("img/ui/no_hide.png"))));
					gameInfo.gameMap = "img/tmx/ground1.tmx";
				} else {
					imageContainer.setActor(null);
					gameInfo.gameMap = null;
				}
			}
		});
		
		startButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				TextButton button = (TextButton) actor;
				if (button.isPressed()) {
					if (gameInfo.gameMap == null) {
						Dialog dialog = new Dialog("Warning!", skin);
						dialog.text("The map you selected is not available!").button("OK", true).key(Keys.ENTER, true)
						.key(Keys.ESCAPE, false).show(stage);
					} else {
						LoadingScreen loadingScreen = new LoadingScreen();
						loadingScreen.setGameInfo(gameInfo);
						((Game) Gdx.app.getApplicationListener()).setScreen(loadingScreen);
					}
				}
			}
		});
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
		stage.dispose();
		skin.dispose();
	}

}
