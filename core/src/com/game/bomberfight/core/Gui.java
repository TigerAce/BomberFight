package com.game.bomberfight.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
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
import com.game.bomberfight.utility.TiledNinePatch;

public class Gui {
	
	private Stage uiStage;
	private Skin uiSkin;
	private Table stateTable;
	private Viewport gamePlayViewport;
	private Dialog waitingDialog = null;
	private Table statusPanel;

	public Gui() {
		// TODO Auto-generated constructor stub
		uiStage = new Stage(new ScreenViewport());
		InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(uiStage);
		uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		uiSkin.add("container_background", new NinePatch(new Texture(Gdx.files.internal("data/pattern.png")), 1, 1, 1, 1));
		uiSkin.add("scrollBG", new NinePatch(new Texture(Gdx.files.internal("data/progressBG.png")), 1, 1, 1, 1));
		
		stateTable = new Table();
		stateTable.setFillParent(true);
		
		statusPanel = new Table();
		stateTable.add(statusPanel).expand().left().top();
		
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
		for (Actor actor : statusPanel.getChildren()) {
			updateBuffs((Group) actor);
		}
		for (Actor actor : uiStage.getActors()) {
			if (actor.getName() != null) {
				if (actor.getName().equalsIgnoreCase("Label_Name")) {
					Player player = (Player) actor.getUserObject();
					Label label = (Label) actor;
					label.setText(player.getName());
					float x = gamePlayViewport.project(player.getBox2dBody().getPosition()).x;
					float y = gamePlayViewport.project(player.getBox2dBody().getPosition()).y;
					float nameWidth = label.getMinWidth();
					float nameHeight = label.getMinHeight();
					actor.setPosition(x - nameWidth / 2.f, y + nameHeight / 2.f + player.getHeight() / 2.f);
				}
			}
		}
//		for (Actor actor : uiStage.getActors()) {
//			if (actor instanceof Table) {
//				Group group = ((Table) actor).findActor("fixedstatusbar");
//				if (group != null) {
//					updateBuffs(group);
//				}
//			} else if (actor instanceof HorizontalGroup) {
//				updateBuffs((HorizontalGroup) actor);
//				Player player = (Player) actor.getUserObject();
//				if (gamePlayViewport != null) {
//					if (player.getState() == GameObject.RECYCLE) {
//						actor.remove();
//						actor.setUserObject(null);
//					} else {
//						float x = gamePlayViewport.project(player.getBox2dBody().getPosition()).x;
//						float y = gamePlayViewport.project(player.getBox2dBody().getPosition()).y;
//						actor.setPosition(x, y);
//					}
//				}
//			}
//		}
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
		Table table = new Table();
		table.setUserObject(player);
		table.setName("fixedstatusbar");
		
		statusPanel.add(table).expandX().left();
		statusPanel.row();
		
		table.add(createHPBar("hpbar", player));
		table.row();
		
		HorizontalGroup horizontalGroup = new HorizontalGroup();
		horizontalGroup.setName("HorizontalGroup_BuffGroup");
		table.add(horizontalGroup).left();
		
		horizontalGroup.addActor(createBuff("bombnumber", player));
		horizontalGroup.addActor(createBuff("bombpower", player));
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = ((GamePlay)(((Game) Gdx.app.getApplicationListener()).getScreen())).getAssetManager().get("data/default.fnt", BitmapFont.class);
		
		Label nameLabel = new Label(player.getName(), labelStyle);
		nameLabel.setUserObject(player);
		nameLabel.setName("Label_Name");
		nameLabel.getStyle().font.setScale(0.8f);
		
		uiStage.addActor(nameLabel);
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
		
		float hp = player.getAttr().getMaxLife();
		
		horizontalGroup.addActor(createBuff("bombnumber", player));
		horizontalGroup.addActor(createBuff("bombpower", player));
		
		Table table = createHPBar("hpbar", player);
		Slider slider = table.findActor("slider");
		slider.setRange(0, hp);
		slider.setStepSize(hp / 100f);
		slider.setValue(hp);
		horizontalGroup.addActor(table);
	}
	
	public void updateBuffs(Group fixedStatusBar) {
		Table hpbar = fixedStatusBar.findActor("hpbar");
		if (hpbar != null) {
			Slider slider = hpbar.findActor("slider");
			Player player = (Player) hpbar.getUserObject();
			
			if (slider.getMaxValue() != player.getAttr().getMaxLife()) {
				TiledNinePatch tiledNinePatch = (TiledNinePatch) slider.getStyle().disabledKnobBefore;
				//tiledNinePatch.setNinePatchWidth(tiledNinePatch.getNinePatchWidth() - 100.f / player.getAttr().getMaxLife());
				tiledNinePatch.setNinePatchWidth(slider.getWidth() / (player.getAttr().getMaxLife() / 50.f));
			}
			
			slider.setRange(0, player.getAttr().getMaxLife());
			slider.setStepSize(player.getAttr().getMaxLife()/100);
			slider.setValue(player.getAttr().getCurrLife());
			
			Label label = hpbar.findActor("label");
			label.setText(player.getName());
		}
		HorizontalGroup buffGroup = fixedStatusBar.findActor("HorizontalGroup_BuffGroup");
		if (buffGroup != null) {
			for (Actor actor : buffGroup.getChildren()) {
				Group group = (Group) actor;
				if (group.getName().equalsIgnoreCase("bombnumber")) {
					Label label = group.findActor("label");
					Player player = (Player) group.getUserObject();
					label.setText(""+player.getAttr().getNumBombPerRound());
				} else if (group.getName().equalsIgnoreCase("bombpower")) {
					Label label = group.findActor("label");
					Player player = (Player) group.getUserObject();
					label.setText(""+(int)player.getAttr().getPowerX());
				} else {
					if (group.getUserObject() instanceof Item) {
						Item item = (Item) group.getUserObject();
						if (item.getAffectTime() <= 0) {
							group.setUserObject(null);
							group.remove();
						} else {
							Label label = group.findActor("label");
							label.setText(""+(int)item.getAffectTime());
						}
					}
				}
			}
		}
	}
	
	public void pickUpBuff(Player player, Item item) {
		for (Actor actor : statusPanel.getChildren()) {
			HorizontalGroup buffGroup = ((Table) actor).findActor("HorizontalGroup_BuffGroup");
			if (buffGroup != null) {
				Player tempPlayer = (Player) actor.getUserObject();
				if (player == tempPlayer) {
					//horizontalGroup.addActorAt(0, createBuff(item.getName(), item));
					buffGroup.addActor(createBuff(item.getName(), item));
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
					GamePlay.client.close();
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				}
			}
			}.text("Do you really want to exit?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
		.key(Keys.ESCAPE, false).show(uiStage);
	}
	
	public void showDisconnectDialog() {
		@SuppressWarnings("unused")
		Dialog dialog = new Dialog("Warning!", uiSkin) {
			protected void result (Object object) {
				boolean b = (Boolean) object;
				if (b) {
					Gdx.input.setInputProcessor(null);
					GamePlay.client.close();
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				}
			}
			}.text("Your connection to server has been disconnected!\nClick 'Yes' to return to main menu!").button("Yes", true).key(Keys.ENTER, true).show(uiStage);
	}
	
	public Table createBuff(String name, Object userObject) {
		Table table = new Table();
		table.setName(name);
		table.setUserObject(userObject);
		
		Image image = null;
		
		if (name.equalsIgnoreCase("bombpower")) {
			image = new Image(((GamePlay)(((Game) Gdx.app.getApplicationListener()).getScreen())).getAssetManager().get("img/texture/item1.png", Texture.class));
		} else if (name.equalsIgnoreCase("bombnumber")) {
			image = new Image(((GamePlay)(((Game) Gdx.app.getApplicationListener()).getScreen())).getAssetManager().get("img/texture/item3.png", Texture.class));
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
		
		table.add(image).minSize(15, 15).maxSize(15, 15).padRight(5);
		
		table.row();
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = ((GamePlay)(((Game) Gdx.app.getApplicationListener()).getScreen())).getAssetManager().get("data/Aharoni.fnt", BitmapFont.class);
		labelStyle.font.setScale(0.5f);
		
		Label label = new Label("", labelStyle);
		label.setName("label");
		table.add(label).minSize(15, 15).maxSize(15, 15);
		
		table.layout();
		
		return table;
	}
	
	public Table createHPBar(String name, Object userObject) {
		Table table = new Table();
		table.setName(name);
		table.setUserObject(userObject);
		
		Label label = new Label("HP ", uiSkin);
		label.setName("label");
		table.add(label).maxHeight(20).left();
		
		table.row();
		
		Player player = (Player) userObject;
		float hp = player.getAttr().getMaxLife();
		
		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.background = uiSkin.getDrawable("scrollBG");
		
		Slider slider = new Slider(0, 1, 0.1f, false, sliderStyle);
		//Slider slider = new Slider(0, 1, 0.1f, false, uiSkin, "hp-horizontal");
		
		TiledNinePatch tiledNinePatch = new TiledNinePatch(uiSkin.getPatch("container_background"), 
				80 / (player.getAttr().getMaxLife() / 50.f), 10);
		slider.getStyle().disabledKnobBefore = tiledNinePatch;
		
		slider.setRange(0, hp);
		slider.setStepSize(hp / 100f);
		slider.setValue(hp);
		slider.setName("slider");
		slider.setAnimateDuration(0.5f);
		slider.setValue(1);
		slider.setDisabled(true);
		table.add(slider).maxSize(80, 10).left();
		
		return table;
	}
	
	public void showWaitingDialog(boolean isShow) {
		if (isShow) {
			if (waitingDialog == null) {
				waitingDialog = new Dialog("Waiting!", uiSkin) {
					}.text("Waiting for other players...(Use W,A,S,D,SPACE or touchpad to control your character)").button("Yes", true).key(Keys.ENTER, true).show(uiStage);
			} else {
				waitingDialog.show(uiStage);
			}
		} else {
			if (waitingDialog != null) {
				waitingDialog.hide();
			}
		}
	}

}
