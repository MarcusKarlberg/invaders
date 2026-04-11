package se.marcusk.invaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import se.marcusk.invaders.Invaders;
import se.marcusk.invaders.entity.*;

public class GameScreen implements Screen {
    private final Invaders game;
    private float worldWidth;
    private float worldHeight;

    Texture backgroundTexture;
    Texture planeTexture;
    Texture ufoTexture;
    Texture rocketTexture;
    Texture explosionTexture1;
    Texture explosionTexture2;

    Plane plane;
    Array<Rocket> rockets;
    Array<Ufo> ufos;
    Array<Explosion> explosions;

    int wave;
    int ufoCount;
    int rocketCount;

    // camera
    float shakeTime = 0f;
    float shakeDuration = 0f;
    float shakeIntensity = 0f;

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
        explosionTexture1 = new Texture("explosion1_32.png");
        explosionTexture2 = new Texture("explosion2_32.png");

        plane = new Plane(planeTexture, worldWidth);
        rockets = new Array<>();
        ufos = new Array<>();
        explosions = new Array<>();

        //TODO: decide how ufos should be generated
        createUfo();
    }

    @Override
    public void render(float delta) {
        input();
        plane.update(worldWidth);
        updateUfos(delta);
        updatePlayerRockets();
        updateExplosions(delta);
        renderCameraShake();

        draw();
        resetCamera(); // must be after draw
    }

    private void input() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            plane.moveRight(delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            plane.moveLeft(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (rocketCount > 0) {
                createRocket();
            }
        }
    }

    private void updateUfos(float delta) {
        for (int i = ufos.size - 1; i >= 0; i--) {
            Ufo ufo = ufos.get(i);
            ufo.update(delta, worldWidth, worldHeight);
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
                continue;
            }

            for (int u = ufos.size - 1; u >= 0; u--) {
                Ufo ufo = ufos.get(u);

                if (rocket.getHitBox().overlaps(ufo.getHitBox())) {
                    rockets.removeIndex(i);
                    ufos.removeIndex(u);
                    explosions.add(new Explosion(explosionTexture1, explosionTexture2, ufo.getX(), ufo.getY()));
                    shakeCamera(0.25f, 0.1f);
                    break;
                }
            }
        }
    }

    private void updateExplosions(float delta) {
        for (int i = explosions.size - 1; i >= 0; i--) {
            Explosion e = explosions.get(i);
            if (e.update(delta)) {
                explosions.removeIndex(i); // remove when finished
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.getViewport().apply();

        float worldWidth = game.getViewport().getWorldWidth();
        float worldHeight = game.getViewport().getWorldHeight();

        // Draw background
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);
        game.getBatch().begin();
        game.getBatch().draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        game.getBatch().end();

        // Draw game objects
        game.getBatch().begin();

        plane.getSprite().draw(game.getBatch());

        game.getFont().draw(game.getBatch(), "Wave: " + wave, 0, 0.5F);
        game.getFont().draw(game.getBatch(), "Rockets: " + rocketCount, 2, 0.5F);

        for (Ufo ufo : ufos) {
            ufo.draw(game.getBatch());
        }

        for (Rocket rocket : rockets) {
            rocket.draw(game.getBatch());
        }

        for (Explosion explosion : explosions) {
            explosion.draw(game.getBatch());
        }

        game.getBatch().end();
    }

    private void renderCameraShake() {
        if (shakeTime > 0) {
            shakeTime -= Gdx.graphics.getDeltaTime();

            float currentIntensity = shakeIntensity * (shakeTime / shakeDuration);

            float offsetX = (MathUtils.random() - 0.5f) * 2f * currentIntensity;
            float offsetY = (MathUtils.random() - 0.5f) * 2f * currentIntensity;

            game.getViewport().getCamera().position.x += offsetX;
            game.getViewport().getCamera().position.y += offsetY;

            game.getViewport().getCamera().update();
        }
    }

    public void resetCamera() {
        game.getViewport().getCamera().position.set(
            worldWidth / 2f,
            worldHeight / 2f,
            0
        );
        game.getViewport().getCamera().update();
    }

    public void shakeCamera(float duration, float intensity) {
        shakeDuration = duration;
        shakeTime = duration;
        shakeIntensity = intensity;
    }

    private void createUfo() {
        ufos.add(new Ufo(ufoTexture, worldWidth, worldHeight));
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
    public void show() {}

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        planeTexture.dispose();
        ufoTexture.dispose();
        rocketTexture.dispose();
        explosionTexture1.dispose();
        explosionTexture2.dispose();
    }
}
