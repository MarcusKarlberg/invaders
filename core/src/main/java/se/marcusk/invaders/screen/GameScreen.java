package se.marcusk.invaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import se.marcusk.invaders.Invaders;
import se.marcusk.invaders.entity.*;

public class GameScreen implements Screen {
    private final Invaders game;
    private final float worldWidth;
    private final float worldHeight;

    private final Texture backgroundTexture;
    private final Texture planeTexture;
    private final Texture ufoTexture;
    private final Texture rocketTexture;

    private Animation<TextureRegion> explosionAnimation;

    private final Plane plane;
    private final Array<Rocket> rockets;
    private final Array<Ufo> ufos;
    private final Array<Explosion> explosions;

    private int wave;
    private int rocketCount;

    // Camera shake
    private float shakeTime = 0f;
    private float shakeDuration = 0f;
    private float shakeIntensity = 0f;

    public GameScreen(Invaders game) {
        this.game = game;

        worldWidth = game.getViewport().getWorldWidth();
        worldHeight = game.getViewport().getWorldHeight();

        rocketCount = 10;

        // Load textures
        backgroundTexture = new Texture("background.png");
        planeTexture = new Texture("plane32.png");
        ufoTexture = new Texture("ufo32.png");
        rocketTexture = new Texture("rocket.png");

        loadExplosionAnimation();

        // Entities
        plane = new Plane(planeTexture, worldWidth);
        rockets = new Array<>();
        ufos = new Array<>();
        explosions = new Array<>();

        createUfo();
    }

    private void loadExplosionAnimation() {

        Texture explosionSheet = new Texture("explosion_sheet.png");

        int FRAME_WIDTH = 32;
        int FRAME_HEIGHT = 32;

        TextureRegion[][] tmp = TextureRegion.split(
            explosionSheet,
            FRAME_WIDTH,
            FRAME_HEIGHT
        );

        Array<TextureRegion> frames = new Array<>();

        for (int row = 0; row < tmp.length; row++) {
            for (int col = 0; col < tmp[row].length; col++) {
                frames.add(tmp[row][col]);   // ✅ store region directly
            }
        }

        explosionAnimation = new Animation<>(0.08f, frames);
    }

    @Override
    public void render(float delta) {
        input();
        plane.update(worldWidth);
        updateUfos(delta);
        updatePlayerRockets(delta);
        updateExplosions(delta);
        renderCameraShake();

        draw();
        resetCamera();
    }

    private void input() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            plane.moveRight(delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            plane.moveLeft(delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && rocketCount > 0) {
            createRocket();
        }
    }

    private void updateUfos(float delta) {
        for (Ufo ufo : ufos) {
            ufo.update(delta, worldWidth, worldHeight);
        }
    }

    private void updatePlayerRockets(float delta) {
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

                    explosions.add(new Explosion(
                        explosionAnimation,
                        ufo.getX(),
                        ufo.getY()
                    ));

                    shakeCamera(0.25f, 0.15f);
                    break;
                }
            }
        }
    }

    private void updateExplosions(float delta) {
        for (int i = explosions.size - 1; i >= 0; i--) {
            if (explosions.get(i).update(delta)) {
                explosions.removeIndex(i);
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.getViewport().apply();

        float w = game.getViewport().getWorldWidth();
        float h = game.getViewport().getWorldHeight();

        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);
        game.getBatch().begin();

        game.getBatch().draw(backgroundTexture, 0, 0, w, h);

        plane.getSprite().draw(game.getBatch());

        game.getFont().draw(game.getBatch(), "Wave: " + wave, 0, 0.5f);
        game.getFont().draw(game.getBatch(), "Rockets: " + rocketCount, 2, 0.5f);

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

    private void shakeCamera(float duration, float intensity) {
        shakeDuration = duration;
        shakeTime = duration;
        shakeIntensity = intensity;
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

    private void resetCamera() {
        game.getViewport().getCamera().position.set(
            worldWidth / 2f,
            worldHeight / 2f,
            0
        );
        game.getViewport().getCamera().update();
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
    public void pause() {}

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
