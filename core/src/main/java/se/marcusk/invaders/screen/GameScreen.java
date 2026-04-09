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

    Texture backgroundTexture;
    Texture planeTexture;
    Texture ufoTexture;
    Texture rocketTexture;

    Sprite planeSprite;
    Array<Sprite> ufoSprites;
    Array<Sprite> rocketSprites;

    Rectangle planeRectangle;
    Rectangle ufoRectangle;
    Rectangle rocketRectangle;

    int wave;
    int ufoCount;
    int rocketCount;

    public GameScreen(Invaders game) {
        this.game = game;
        ufoCount = 0;
        rocketCount = 10;
        backgroundTexture = new Texture("background.png");
        planeTexture = new Texture("plane32.png");
        ufoTexture = new Texture("ufo32.png");
        rocketTexture = new Texture("rocket.png");

        planeSprite = new Sprite(planeTexture);
        planeSprite.setSize(1, 1);
        planeSprite.setX(game.getViewport().getWorldWidth() / 2);
        planeSprite.setY(2);

        planeRectangle = new Rectangle();
        ufoRectangle = new Rectangle();
        rocketRectangle = new Rectangle();

        ufoSprites = new Array<>();
        rocketSprites = new Array<>();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        input();
        planeLogic();
        rocketLogic();
        draw();
    }

    private void input() {
        float speed = 8f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            planeSprite.translateX(speed * delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            planeSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if(rocketCount > 0) {
                createRocket();
            }
        }
    }

    private void planeLogic() {
        float worldWidth = game.getViewport().getWorldWidth();
        float planeWidth = planeSprite.getWidth();
        float planeHeight = planeSprite.getHeight();

        planeSprite.setX(MathUtils.clamp(planeSprite.getX(), 0, worldWidth - planeWidth));
        planeRectangle.set(planeSprite.getX(), planeSprite.getY(), planeWidth, planeHeight);
    }

    private void rocketLogic() {
        float worldHeight = game.getViewport().getWorldHeight();
        float delta = Gdx.graphics.getDeltaTime();

        for (int i = rocketSprites.size - 1; i >= 0; i--) {
            Sprite rocketSprite = rocketSprites.get(i);

            rocketSprite.translateY(5f * delta);
            //TODO: set rocketRectangle for collision logic

            // Remove rocket if it leaves the screen
            if (rocketSprite.getY() > game.getViewport().getWorldHeight()) {
                rocketSprites.removeIndex(i);
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
        planeSprite.draw(game.getBatch());

        game.getFont().draw(game.getBatch(), "Wave: " + wave, 0, 0.5F);
        game.getFont().draw(game.getBatch(), "Rockets: " + rocketCount, 2, 0.5F);

        for (Sprite ufo: ufoSprites) {
            ufo.draw(game.getBatch());
        }

        for (Sprite rocket: rocketSprites) {
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
        float rocketWidth = 1;
        float rocketHeight = 1;

        Sprite rocketSprite = new Sprite(rocketTexture);
        rocketSprite.setSize(rocketWidth, rocketHeight);
        rocketSprite.setX(planeSprite.getX());
        rocketSprite.setY(planeSprite.getY());
        rocketSprites.add(rocketSprite);
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
