package se.marcusk.invaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import se.marcusk.invaders.Invaders;
import se.marcusk.invaders.entity.Plane;
import se.marcusk.invaders.entity.Rocket;

/**
 * TODO: Move Sprites to entities folder:
 *
 * *Entities should:
 *
 * Store their own sprite
 * Handle their own movement logic
 * Optionally expose a Rectangle for collision
 *
 *  * GameScreen should:
 *
 *  Handle spawning
 *  Call update() on entities
 *  Call draw()
 * */

public class GameScreen implements Screen {
    private final Invaders game;
    private float worldWidth;
    private float worldHeight;

    Texture backgroundTexture;
    Texture planeTexture;
    Texture ufoTexture;
    Texture rocketTexture;

    Array<Sprite> ufoSprites;

    Rectangle ufoRectangle;

    Plane plane;
    Array<Rocket> rockets;

    int wave;
    int ufoCount;
    int rocketCount;

    public GameScreen(Invaders game) {
        this.game = game;
        worldWidth = game.getViewport().getWorldWidth();
        worldHeight = game.getViewport().getWorldHeight();

        ufoCount = 0;
        rocketCount = 10;
        backgroundTexture = new Texture("background.png");
        planeTexture = new Texture("plane32.png");
        ufoTexture = new Texture("ufo32.png");
        rocketTexture = new Texture("rocket.png");

        plane = new Plane(planeTexture, worldWidth);
        rockets = new Array<>();

        ufoRectangle = new Rectangle();

        ufoSprites = new Array<>();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        input();
        plane.update(worldWidth);
        updatePlayerRockets();
        draw();
    }

    private void input() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            plane.moveRight(delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            plane.moveLeft(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if(rocketCount > 0) {
                createRocket();
            }
        }
    }

    private void updatePlayerRockets() {
        float worldHeight = game.getViewport().getWorldHeight();
        float delta = Gdx.graphics.getDeltaTime();

        for (int i = rockets.size - 1; i >= 0; i--) {
            Rocket rocket = rockets.get(i);
            rocket.update(delta);

            if (rocket.isOffScreen(worldHeight)) {
                rockets.removeIndex(i);
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.DARK_GRAY);

        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);
        game.getBatch().begin();

        float worldWidth = game.getViewport().getWorldWidth();
        float worldHeight = game.getViewport().getWorldHeight();

        game.getBatch().draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        plane.getSprite().draw(game.getBatch());

        game.getFont().draw(game.getBatch(), "Wave: " + wave, 0, 0.5F);
        game.getFont().draw(game.getBatch(), "Rockets: " + rocketCount, 2, 0.5F);

        for (Sprite ufo: ufoSprites) {
            ufo.draw(game.getBatch());
        }

        for (Rocket rocket: rockets) {
            rocket.draw(game.getBatch());
        }

        game.getBatch().end();
    }

    private void createUfo() {
        float ufoWidth = 1;
        float ufoHeight = 1;
        float worldWidth = game.getViewport().getWorldWidth();
        float worldHeight = game.getViewport().getWorldHeight();

        Sprite ufoSprite = new Sprite(ufoTexture);
        ufoSprite.setSize(ufoWidth, ufoHeight);
        ufoSprite.setX(MathUtils.random(0F, worldWidth - ufoWidth));
        ufoSprite.setY(worldHeight -2); //TODO: decide where and if they show drop down to this position
        ufoSprites.add(ufoSprite);
        ufoCount++;
    }

    private void createRocket() {
        rockets.add(new Rocket(rocketTexture, plane.getX(), plane.getY()));
        rocketCount--;
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        planeTexture.dispose();
        ufoTexture.dispose();
        rocketTexture.dispose();
    }
}
