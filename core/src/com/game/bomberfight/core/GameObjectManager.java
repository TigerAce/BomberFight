package com.game.bomberfight.core;

import com.game.bomberfight.model.GameObject;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Tong on 2014/7/25.
 */
public class GameObjectManager {

    /**
     * A hash set stores all game objects
     */
    private HashSet<GameObject> gameObjects = new HashSet<GameObject>();

    /**
     * Addes in game object into HashSet
     * @param object The object you want to add in
     */
    public void addGameObject(GameObject object) {
        this.gameObjects.add(object);
    }

    /**
     * Remove object from HashSet
     * @param object The object you want to remove
     */
    public void removeGameObject(GameObject object) {
        this.gameObjects.remove(object);
    }

    /**
     * All logic update for objects in the collection
     * @param delta
     */
    public void updateAll(float delta) {

        Iterator<GameObject> iterator = this.gameObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().update(delta);
        }
    }

    /**
     * Draw all sprites
     */
    public void drawAll() {
        Iterator<GameObject> iterator = this.gameObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().draw();
        }
    }

    /**
     * Dispose all game objects
     */
    public void disposeAll() {
        Iterator<GameObject> iterator = this.gameObjects.iterator();
        while (iterator.hasNext()) {
            iterator.next().dispose();
        }
    }
}
