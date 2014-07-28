package com.game.bomberfight.utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Config {
	
	private static Config instance = null;
	private Map<String, Object> pairs;

	protected Config() {
		// TODO Auto-generated constructor stub
		pairs = new HashMap<String, Object>();
		parse("data/config.xml");
	}
	
	public static Config getInstance() {
		if (instance == null) {
			return instance = new Config();
		}
		return instance;
	}
	
	public void parse(String filename) {
		XmlReader reader = new XmlReader();
		Element root = null;
		try {
			root = reader.parse(Gdx.files.internal(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			Object value = null;
			if (root.getChild(i).getAttribute("type").equalsIgnoreCase("integer")) {
				value = Integer.parseInt(root.getChild(i).getText());
			} else if (root.getChild(i).getAttribute("type").equalsIgnoreCase("float")) {
				value = Float.parseFloat(root.getChild(i).getText());
			} else if (root.getChild(i).getAttribute("type").equalsIgnoreCase("boolean")) {
				value = Boolean.parseBoolean(root.getChild(i).getText());
			} else if (root.getChild(i).getAttribute("type").equalsIgnoreCase("double")) {
				value = Double.parseDouble(root.getChild(i).getText());
			} else {
				Class<? extends Object> type;
				try {
					type = Class.forName(root.getChild(i).getAttribute("type"));
					value = type.cast(root.getChild(i).getText());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pairs.put(root.getChild(i).getName(), value);
		}
	}
	
	public <T extends Object> T get(String name, Class<T> type) {
		return type.cast(pairs.get(name));
	}

}
