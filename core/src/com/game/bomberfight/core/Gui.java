package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.GamePlay;
import com.game.bomberfight.screen.MainMenu;

public class Gui {
	
	private Stage uiStage;
	private Skin uiSkin;
	private GamePlay gamePlay;
	private Table stateTable;
	private Player playerA;
	private Player playerB;
	private Slider HPBarA;
	private Slider HPBarB;
	private Viewport gamePlayViewport;
	private Label buffLabel1;
	private Label buffLabel3;
	private HorizontalGroup buffTable;

	public Gui(GamePlay gamePlay) {
		// TODO Auto-generated constructor stub
		this.gamePlay = gamePlay;
		uiStage = new Stage(new ScreenViewport());
		InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(uiStage);
		uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		stateTable = new Table();
		stateTable.setFillParent(true);
		
		buffTable = new HorizontalGroup();
		buffTable.setFillParent(false);
		stateTable.add(buffTable).expand().top();
		
		
		
		Image buffImage1 = new Image(new Texture(Gdx.files.internal("img/texture/item1.png")));
		//Image buffImage2 = new Image(new Texture(Gdx.files.internal("img/texture/item2.png")));
		Image buffImage3 = new Image(new Texture(Gdx.files.internal("img/texture/item3.png")));
		
		LabelStyle buffLabelStyle = new LabelStyle();
		buffLabelStyle.font = new BitmapFont(Gdx.files.internal("data/GiddyupStd.fnt"));
		
		buffLabel1 = new Label("", buffLabelStyle);
		
		buffLabel3 = new Label("", buffLabelStyle);
		
		Table cellContainer1 = new Table();
		cellContainer1.add(buffImage1).maxSize(25, 25).padRight(10).padLeft(10);
		cellContainer1.row();
		cellContainer1.add(buffLabel1).maxSize(25, 25);
		buffTable.addActor(cellContainer1);
		
		Table cellContainer3 = new Table();
		cellContainer3.add(buffImage3).maxSize(25, 25).padRight(10).padLeft(10);
		cellContainer3.row();
		cellContainer3.add(buffLabel3).maxSize(25, 25);
		buffTable.addActor(cellContainer3);
		
		Label hpLabel = new Label("HP ", uiSkin);
		stateTable.add(hpLabel).expandY().top().right().padLeft(10);
		
		HPBarA = new Slider(0, 1, 0.1f, false, uiSkin);
		HPBarA.setAnimateDuration(0.5f);
		HPBarA.setValue(1);
		HPBarA.setStyle(uiSkin.get("hp-horizontal", SliderStyle.class));
		HPBarA.setDisabled(true);
		stateTable.add(HPBarA).top().right();
		
		HPBarB = new Slider(0, 1, 0.1f, false, uiSkin);
		HPBarB.setAnimateDuration(0.5f);
		HPBarB.setValue(1);
		HPBarB.setStyle(uiSkin.get("hp-horizontal", SliderStyle.class));
		HPBarB.setSize(50, 25);
		HPBarB.setDisabled(true);
		uiStage.addActor(HPBarB);
		
		//stateTable.debug();
		uiStage.addActor(stateTable);
		
		playerA = playerB = null;
		gamePlayViewport = null;
	}
	
	public void resize(Viewport viewport) {
		uiStage.getViewport().update(viewport.getViewportWidth(), viewport.getViewportHeight(), true);
		ExtendViewport extendViewport = (ExtendViewport) viewport;
		float desiredRatio = extendViewport.getMinWorldHeight() / extendViewport.getMinWorldWidth();
		float currentRatio = extendViewport.getWorldHeight() / extendViewport.getWorldWidth();
		if (currentRatio > desiredRatio) {
			float diff = uiStage.getViewport().getViewportHeight() - desiredRatio * uiStage.getViewport().getViewportWidth();
			stateTable.padTop(diff / 2);
			stateTable.padBottom(diff / 2);
			stateTable.padRight(0);
			stateTable.padLeft(0);
		}
		if (currentRatio < desiredRatio) {
			float diff = uiStage.getViewport().getViewportWidth() - uiStage.getViewport().getViewportHeight() / desiredRatio;
			stateTable.padRight(diff / 2);
			stateTable.padLeft(diff / 2);
			stateTable.padTop(0);
			stateTable.padBottom(0);
		}
		this.gamePlayViewport = viewport;
	}
	
	public void update() {
		if (playerA != null) {
			HPBarA.setValue(playerA.getAttr().getLife());
			updateBuffs(playerA);
		}
		if (playerB != null && gamePlayViewport != null) {
			float x = gamePlayViewport.project(playerB.getBox2dBody().getPosition()).x;
			float y = gamePlayViewport.project(playerB.getBox2dBody().getPosition()).y;
			x = x - HPBarB.getWidth() / 2;
			y = y + 15;
			HPBarB.setPosition(x, y);
			HPBarB.setValue(playerB.getAttr().getLife());
		} else {
			HPBarB.setVisible(false);
		}
		uiStage.act(Gdx.graphics.getDeltaTime());
	}
	
	public void draw() {
		uiStage.draw();
		Table.drawDebug(uiStage);
	}
	
	public void dispose() {
		uiStage.dispose();
		uiSkin.dispose();
	}

	/**
	 * @param playerA the playerA to set
	 */
	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
		float hp = playerA.getAttr().getLife();
		HPBarA.setRange(0, hp);
		HPBarA.setStepSize(hp / 100);
		HPBarA.setValue(hp);
	}

	/**
	 * @param playerB the playerB to set
	 */
	public void setPlayerB(Player playerB) {
		this.playerB = playerB;
		float hp = playerB.getAttr().getLife();
		HPBarB.setRange(0, hp);
		HPBarB.setStepSize(hp / 100);
		HPBarB.setValue(hp);
	}
	
	public void updateBuffs(Player player) {
		int power = (int) player.getAttr().getPowerX();
		buffLabel1.setText(""+power);
		buffLabel3.setText(""+player.getAttr().getNumBombPerRound());
		
		for (Actor actor : buffTable.getChildren()) {
			if (actor.getUserObject() != null) {
				Item item = (Item) actor.getUserObject();
				int time = (int) item.getAffectTime();
				if (time <= 0) {
					actor.remove();
				} else {
					Table cellContainer = (Table) actor;
					Label label = cellContainer.findActor("buffLabel2");
					label.setText(""+time);
				}
			}
		}
	}
	
	public void createBuff(Item item) {
		Image buffImage2 = new Image(new Texture(Gdx.files.internal("img/texture/item2.png")));
		
		LabelStyle buffLabelStyle = new LabelStyle();
		buffLabelStyle.font = new BitmapFont(Gdx.files.internal("data/GiddyupStd.fnt"));
		
		Label buffLabel2 = new Label(""+item.getAffectTime(), buffLabelStyle);
		buffLabel2.setName("buffLabel2");
		
		Table cellContainer2 = new Table();
		cellContainer2.add(buffImage2).maxSize(25, 25).padRight(10).padLeft(10);
		cellContainer2.row();
		cellContainer2.add(buffLabel2).maxSize(25, 25);
		cellContainer2.setUserObject(item);
		buffTable.addActor(cellContainer2);
	}
	
	public void showMenu() {
		Dialog dialog = new Dialog("Warning!", uiSkin) {
			protected void result (Object object) {
				boolean b = (Boolean) object;
				if (b) {
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				}
			}
			}.text("Do you really want to exit?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
		.key(Keys.ESCAPE, false).show(uiStage);
	}

}
