package com.game.bomberfight.screen;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.bomberfight.model.MapInfo;
import com.game.bomberfight.model.PlayerInfo;
import com.game.bomberfight.utility.Config;

public class MainMenu implements Screen {
	
	private ExtendViewport viewport;
	private Array<MapInfo> mapInfoList;
	
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
	private Table table;
	
	public String networkMode = "Localhost";
	public String gameMode = "Normal";
	public String nickname = "Kill La Kill";

	public MainMenu() {
		// TODO Auto-generated constructor stub
		viewport = new ExtendViewport(Config.getInstance().get("viewportWidth", Float.class), Config.getInstance().get("viewportHeight", Float.class));
		skin = new Skin();
		stage = new Stage();
		mapInfoList = new Array<MapInfo>();
		loadMapInfo("data/map.xml");
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
		//Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);
		viewport.update(width, height);
	}

	@Override
	public void show() {
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		skin.add("container_background", new NinePatch(new Texture(Gdx.files.internal("img/texture/ContainerBackground.9.png")), 11, 11, 11, 11));
		
		table = new Table();
		table.setFillParent(true);
		table.left().top().pad(5);
		
		SplitPane splitPane = new SplitPane(createLeftSplit(), createRightSplit(), false, skin);
		splitPane.setName("splitPane");
		splitPane.setMinSplitAmount(0.5f);
		splitPane.setMaxSplitAmount(0.500001f);
		table.add(splitPane).fill().expand();
		
		table.pack();
		stage.addActor(table);
		table.debug();
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
	
	public Table createMapPreview() {
		Table table = new Table();
		table.setName("map preview");
		table.debug();
		
		Label label = new Label("Map Preview:", skin);
		label.getStyle().background = skin.getDrawable("container_background");
		label.setName("label");
		table.add(label).left().top().expand();
		
		table.row();
		
		Image image = new Image(new Texture(Gdx.files.internal("img/ui/metal_classic.png")));
		image.setName("image");
		
		Container<Image> container = new Container<Image>();
		container.setName("container");
		container.setActor(image);
		
		ScrollPane scrollPane = new ScrollPane(container, skin);
		scrollPane.setName("scrollpane");
		table.add(scrollPane).expand();
		
		return table;
	}
	
	public Table createMapList() {
		Table table = new Table();
		table.setName("map list");
		table.debug();
		
		Label label = new Label("Map:", skin);
		label.getStyle().background = skin.getDrawable("container_background");
		label.setName("label");
		table.add(label).left().top();
		
		table.row();
		
		List<Object> list = new List<Object>(skin);
		list.setName("list");
		list.setItems(mapInfoList);
		list.getSelection().setMultiple(false);
		list.getSelection().setRequired(true);
		GamePlay.gameInfo.mapInfo = mapInfoList.get(0);
		
		ScrollPane scrollPane = new ScrollPane(list, skin);
		scrollPane.getStyle().background = skin.getDrawable("container_background");
		scrollPane.setName("scrollpane");
		scrollPane.setFlickScroll(true);
		table.add(scrollPane).left().expand().fill();
		
		scrollPane.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if (actor instanceof List) {
					@SuppressWarnings("unchecked")
					List<MapInfo> list = (List<MapInfo>) actor;
					MapInfo item = (MapInfo) list.getSelected();
					Group parent = list.getParent();
					while (!parent.getName().equalsIgnoreCase("splitPane")) {
						parent = parent.getParent();
					}
					if (parent.getName().equalsIgnoreCase("splitPane")) {
						Table table = parent.findActor("map preview");
						ScrollPane scrollPane = table.findActor("scrollpane");
						Container<Image> container = scrollPane.findActor("container");
						container.setActor(new Image(new Texture(item.preview)));
						GamePlay.gameInfo.mapInfo = item;
					}
				}
			}
		});
		
		return table;
	}
	
	public Table createLeftSplit() {
		Table table = new Table();
		table.setName("left split");
		table.debug();
		
		SplitPane splitPane = new SplitPane(createMapPreview(), createMapList(), true, skin);
		splitPane.setName("splitPane");
		splitPane.setMinSplitAmount(0.5f);
		splitPane.setMaxSplitAmount(0.500001f);
		table.add(splitPane).fill().expand();
		
		return table;
	}
	
	public Table createNetworkMode() {
		Table table = new Table();
		table.setName("network mode");
		table.defaults().maxHeight(30);
		table.debug();
		
		Label label = new Label("Network:", skin);
		label.getStyle().background = skin.getDrawable("container_background");
		label.setName("label");
		table.add(label).left().bottom().padRight(5).width(100);
		
		SelectBox<Object> selectBox = new SelectBox<Object>(skin);
		selectBox.getStyle().background = skin.getDrawable("container_background");
		selectBox.setItems(networkEntries);
		selectBox.setSelected("Localhost");
		table.add(selectBox).expandX().fillX().bottom();
		
		selectBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				SelectBox<Object> selectBox = (SelectBox<Object>) actor;
				networkMode = (String) selectBox.getSelected();
			}
		});
		
		return table;
	}
	
	public Table createGameMode() {
		Table table = new Table();
		table.setName("game mode");
		table.defaults().maxHeight(30);
		table.debug();
		
		Label label = new Label("Mode:", skin);
		label.getStyle().background = skin.getDrawable("container_background");
		label.setName("label");
		table.add(label).left().bottom().padRight(5).width(100);
		
		SelectBox<Object> selectBox = new SelectBox<Object>(skin);
		selectBox.getStyle().background = skin.getDrawable("container_background");
		selectBox.setItems(gameModeEntries);
		selectBox.setSelected("Normal");
		table.add(selectBox).expandX().fillX().bottom();
		
		selectBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				SelectBox<Object> selectBox = (SelectBox<Object>) actor;
				gameMode = (String) selectBox.getSelected();
			}
		});
		
		return table;
	}
	
	public Table createStartButton() {
		Table table = new Table();
		table.setName("start button");
		table.debug();
		
		TextButton textButton = new TextButton("Start", skin);
		textButton.getStyle().up = skin.getDrawable("container_background");
		table.add(textButton);
		
		textButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				TextButton button = (TextButton) actor;
				if (button.isPressed()) {
					if (!networkMode.equalsIgnoreCase("Localhost") && nickname.isEmpty()) {
						showDialog("Warning!", "Please input your nickname so that your friends can talk to you!");
					} else {
						GamePlay.gameInfo.gameMode = gameMode;
						GamePlay.gameInfo.networkMode = networkMode;
						PlayerInfo playerInfo = new PlayerInfo();
						playerInfo.name = nickname;
						GamePlay.gameInfo.playerInfo = playerInfo;
						LoadingScreen loadingScreen = new LoadingScreen();
						((Game) Gdx.app.getApplicationListener()).setScreen(loadingScreen);
					}
				}
			}
		});
		
		return table;
	}
	
	public Table createNickName() {
		Table table = new Table();
		table.setName("nickname");
		table.defaults().maxHeight(30);
		table.debug();
		
		Label label = new Label("Nickname:", skin);
		label.getStyle().background = skin.getDrawable("container_background");
		label.setName("label");
		table.add(label).left().bottom().padRight(5).width(100);
		
		TextField textField = new TextField("Kill La Kill", skin);
		textField.getStyle().background = skin.getDrawable("container_background");
		table.add(textField).expandX().fillX().bottom();
		
		textField.addListener(new InputListener(){
			public boolean keyTyped (InputEvent event, char character) {
				if (event.getListenerActor() == event.getTarget()) {
					TextField textField = (TextField) event.getListenerActor();
					nickname = textField.getText();
					return true;
				}
				return false;
			}
		});
		
		return table;
	}
	
	public Table createRightSplit() {
		Table table = new Table();
		table.setName("right split");
		table.debug();
		
		table.defaults().padBottom(5);
		table.add(createNetworkMode()).expandX().fillX();
		table.row();
		table.add(createGameMode()).expandX().fillX();
		table.row();
		table.add(createNickName()).expandX().fillX();
		table.row();
		table.add(createStartButton()).expandY().bottom().padBottom(50);
		
		return table;
	}
	
	public void showDialog(String caption, String text) {
		Dialog dialog = new Dialog(caption, skin);
		dialog.text(text).button("OK", true).key(Keys.ENTER, true)
		.key(Keys.ESCAPE, false).show(stage);
	}
	
	public void loadMapInfo(String filename) {
		XmlReader reader = new XmlReader();
		Element root = null;
		try {
			root = reader.parse(Gdx.files.internal(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			MapInfo mapInfo = new MapInfo();
			mapInfo.name = root.getChild(i).getChildByName("name").getText();
			mapInfo.preview = root.getChild(i).getChildByName("preview").getText();
			mapInfo.tmx = root.getChild(i).getChildByName("tmx").getText();
			mapInfo.maxNumPlayer = Integer.parseInt(root.getChild(i).getChildByName("maxNumPlayer").getText());
			mapInfoList.add(mapInfo);
		}
	}

}
