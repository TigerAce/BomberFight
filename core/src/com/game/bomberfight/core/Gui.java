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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.bomberfight.model.GameObject;
import com.game.bomberfight.model.Player;
import com.game.bomberfight.screen.MainMenu;

public class Gui {
	
	private Stage uiStage;
	private Skin uiSkin;
	private Table stateTable;
	private Viewport gamePlayViewport;

	public Gui() {
		// TODO Auto-generated constructor stub
		uiStage = new Stage(new ScreenViewport());
		InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(uiStage);
		uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		stateTable = new Table();
		stateTable.setFillParent(true);
		
		//stateTable.debug();
		uiStage.addActor(stateTable);
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
		for (Actor actor : uiStage.getActors()) {
			if (actor instanceof Table) {
				HorizontalGroup horizontalGroup = ((Table) actor).findActor("fixedstatusbar");
				if (horizontalGroup != null) {
					updateBuffs(horizontalGroup);
				}
			} else if (actor instanceof HorizontalGroup) {
				updateBuffs((HorizontalGroup) actor);
				Player player = (Player) actor.getUserObject();
				if (gamePlayViewport != null) {
					if (player.getState() == GameObject.RECYCLE) {
						actor.remove();
						actor.setUserObject(null);
					} else {
						float x = gamePlayViewport.project(player.getBox2dBody().getPosition()).x;
						float y = gamePlayViewport.project(player.getBox2dBody().getPosition()).y;
						actor.setPosition(x, y);
					}
				}
			}
		}
		uiStage.act(Gdx.graphics.getDeltaTime());
	}
	
	public void draw() {
		uiStage.draw();
		//Table.drawDebug(uiStage);
	}
	
	public void dispose() {
		uiStage.dispose();
		uiSkin.dispose();
	}

	/**
	 * @param playerA the playerA to set
	 * Then create corresponding ui for playerA
	 */
	public void setFixedStatusBar(Player player) {
		HorizontalGroup horizontalGroup = new HorizontalGroup();
		horizontalGroup.setUserObject(player);
		horizontalGroup.setName("fixedstatusbar");
		horizontalGroup.top();
		stateTable.add(horizontalGroup).expand().top().right();
		
		float hp = player.getAttr().getLife();
		
		horizontalGroup.addActor(createBuff("bombnumber", player));
		horizontalGroup.addActor(createBuff("bombpower", player));
		
		Table table = createHPBar("hpbar", player);
		Slider slider = table.findActor("slider");
		slider.setRange(0, hp);
		slider.setStepSize(hp / 100f);
		slider.setValue(hp);
		horizontalGroup.addActor(table);
	}

	/**
	 * @param playerB the playerB to set
	 * Then create corresponding ui for playerB
	 */
	public void setHUD(Player player) {
		HorizontalGroup horizontalGroup = new HorizontalGroup();
		horizontalGroup.setUserObject(player);
		horizontalGroup.top();
		uiStage.addActor(horizontalGroup);
		
		float hp = player.getAttr().getLife();
		
		horizontalGroup.addActor(createBuff("bombnumber", player));
		horizontalGroup.addActor(createBuff("bombpower", player));
		
		Table table = createHPBar("hpbar", player);
		Slider slider = table.findActor("slider");
		slider.setRange(0, hp);
		slider.setStepSize(hp / 100f);
		slider.setValue(hp);
		horizontalGroup.addActor(table);
	}
	
	public void updateBuffs(HorizontalGroup buffGroup) {
		for (Actor actor : buffGroup.getChildren()) {
			Table table = (Table) actor;
			if (table.getName().equalsIgnoreCase("bombnumber")) {
				Label label = table.findActor("label");
				Player player = (Player) table.getUserObject();
				label.setText(""+player.getAttr().getNumBombPerRound());
			} else if (table.getName().equalsIgnoreCase("bombpower")) {
				Label label = table.findActor("label");
				Player player = (Player) table.getUserObject();
				label.setText(""+(int)player.getAttr().getPowerX());
			} else if (table.getName().equalsIgnoreCase("hpbar")) {
				Slider slider = table.findActor("slider");
				Player player = (Player) table.getUserObject();
				slider.setValue(player.getAttr().getLife());
			} else {
				if (table.getUserObject() instanceof Item) {
					Item item = (Item) table.getUserObject();
					if (item.getAffectTime() <= 0) {
						actor.remove();
					} else {
						Label label = table.findActor("label");
						label.setText(""+(int)item.getAffectTime());
					}
				}
			}
		}
	}
	
	public void pickUpBuff(Player player, Item item) {
		for (Actor actor : uiStage.getActors()) {
			HorizontalGroup horizontalGroup = null;
			if (actor instanceof Table) {
				horizontalGroup = ((Table) actor).findActor("fixedstatusbar");
			} else if (actor instanceof HorizontalGroup) {
				horizontalGroup = (HorizontalGroup) actor;
			}
			if (horizontalGroup != null) {
				Player tempPlayer = (Player) horizontalGroup.getUserObject();
				if (player == tempPlayer) {
					horizontalGroup.addActorAt(0, createBuff(item.getName(), item));
				}
			}
		}
	}
	
	public void showMenu() {
		@SuppressWarnings("unused")
		Dialog dialog = new Dialog("Warning!", uiSkin) {
			protected void result (Object object) {
				boolean b = (Boolean) object;
				if (b) {
					Gdx.input.setInputProcessor(null);
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				}
			}
			}.text("Do you really want to exit?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
		.key(Keys.ESCAPE, false).show(uiStage);
	}
	
	public Table createBuff(String name, Object userObject) {
		Table table = new Table();
		table.setName(name);
		table.setUserObject(userObject);
		
		Image image = null;
		
		if (name.equalsIgnoreCase("bombpower")) {
			image = new Image(new Texture(Gdx.files.internal("img/texture/item1.png")));
		} else if (name.equalsIgnoreCase("bombnumber")) {
			image = new Image(new Texture(Gdx.files.internal("img/texture/item3.png")));
		} else {
			if (userObject instanceof Item) {
				Item item = (Item) userObject;
				if (item.getSprite() != null) {
					image = new Image(item.getSprite().getTexture());
				} else {
					image = new Image();
				}
			}
		}
		image.setName("image");
		
		table.add(image).minSize(25, 25).maxSize(25, 25).padLeft(10).padRight(10);
		
		table.row();
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont(Gdx.files.internal("data/Aharoni.fnt"));
		
		Label label = new Label("", labelStyle);
		label.setName("label");
		table.add(label).maxSize(25, 25);
		
		return table;
	}
	
	public Table createHPBar(String name, Object userObject) {
		Table table = new Table();
		table.setName(name);
		table.setUserObject(userObject);
		
		Label label = new Label("HP ", uiSkin);
		label.setName("label");
		table.add(label);
		
		Slider slider = new Slider(0, 1, 0.1f, false, uiSkin, "hp-horizontal");
		slider.setName("slider");
		slider.setAnimateDuration(0.5f);
		slider.setValue(1);
		slider.setDisabled(true);
		table.add(slider);
		
		return table;
	}

}
