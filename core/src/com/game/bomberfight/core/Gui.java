package com.game.bomberfight.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.GamePlay;

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

	public Gui(GamePlay gamePlay) {
		// TODO Auto-generated constructor stub
		this.gamePlay = gamePlay;
		uiStage = new Stage(new ScreenViewport());
		InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(uiStage);
		uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		stateTable = new Table();
		stateTable.setFillParent(true);
		
		Label hpLabel = new Label("HP ", uiSkin);
		stateTable.add(hpLabel).expand().top().right();
		
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

}
