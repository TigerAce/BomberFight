package com.game.bomberfight.core;


import java.util.HashSet;
import java.util.Iterator;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.bomberfight.model.GameObject;

/**
 * Created by Tong on 2014/7/25.
 */
public class GameObjectManager {

    /**
     * A hash set stores all game objects
     */
	
    private HashSet<GameObject> gameObjects = new HashSet<GameObject>();

    /**
     * A hash set stores all new game objects added in this cycle
     * These newly added object will be added into main HashSet in next frame
     * The reason of this second HashSet exists is to avoid ConcurrentModificationException
     * while we adding/removing and iterating through HashSet at same time.
     */
    private HashSet<GameObject> newGameObjects = new HashSet<GameObject>();

    /**
     * Add in game object into HashSet
     * @param object The object you want to add in
     */
    public void addGameObject(GameObject object) {
        this.newGameObjects.add(object);
    }

    /**
     * All logic update for objects in the collection
     * @param delta
     */
    public void updateAll(float delta) {
        // Add newGameObjects to main HashSet
   
        if (!this.newGameObjects.isEmpty()) {
            this.gameObjects.addAll(this.newGameObjects);
            this.newGameObjects.clear();
        }

        Iterator<GameObject> iterator = this.gameObjects.iterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            gameObject.update(delta);
            // Check if GameObject is RECYCLE
            if (gameObject.getState() == GameObject.RECYCLE) {
                iterator.remove();
            }
        }
    
    }

    /**
     * Draw all sprites
     */
    public void drawAll(SpriteBatch batch) {
        Iterator<GameObject> iterator = this.gameObjects.iterator();
        batch.begin();
        while (iterator.hasNext()) {
            iterator.next().draw(batch);
        }
        batch.end();
    }

    /**
     * Dispose all game objects
     */
    public void disposeAll() {
        Iterator<GameObject> iterator = this.gameObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().dispose();
            iterator.remove();
        }
    }
    
    public GameObject findGameObject(int id) {

    	Iterator<GameObject> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
        	GameObject gameObject = iterator.next();
        	if (gameObject.getId() == id) {
				return gameObject;
			}
        }
        return null;
  
    }
}
