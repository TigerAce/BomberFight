package com.game.bomberfight.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.bomberfight.core.CollisionListener;
import com.game.bomberfight.core.GameObjectManager;
import com.game.bomberfight.core.InputController;


public class GamePlay implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private CollisionListener collisionListener;

    private GameObjectManager gameObjectManager = new GameObjectManager();

    private final float TIMESTEP = 1 / 60f;
    private final int VELOCITYITERATIONS = 8;
    private final int POSITIONITERATIONS = 3;

    /**
     * Since world.step only accept a fixed TIMESTEP, a timeAccumulator is used to
     * smooth display in different environment with varies FPS
     */
    private float timeAccumulator = 0.0f;

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /**
         * Handles world collision calculation
         */
        this.timeAccumulator += delta;
        while (this.timeAccumulator >= this.TIMESTEP) {
            // Only perform collision calculation when timeAccumulator is greater than default TIMESTEP
            world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
            this.timeAccumulator -= this.TIMESTEP;
        }

        /**
         * update and redraw game objects
         */
        gameObjectManager.updateAll(delta);
        gameObjectManager.drawAll();

        //debug render
        debugRenderer.render(this.world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 10;
        camera.viewportHeight = height / 10;
    }

    @Override
    public void show() {

        /**********************************************************
         *                 environment setup                      *
         **********************************************************/
        /**
         * box2d world setup
         */
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();


        /**
         * camera setup
         */
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        /**
         * collision listener setup
         */
        collisionListener = new CollisionListener();
        world.setContactListener(collisionListener);


        /**********************************************************
         *                 game objects creation                  *
         **********************************************************/


        /**********************************************************
         *                 input listener                         *
         **********************************************************/
        Gdx.input.setInputProcessor(new InputController() {

        });
    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void pause() {


    }

    @Override
    public void resume() {


    }

    @Override
    public void dispose() {
        gameObjectManager.disposeAll();
        world.dispose();
        debugRenderer.dispose();
    }
}
