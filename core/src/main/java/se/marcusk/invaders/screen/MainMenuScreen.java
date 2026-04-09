package se.marcusk.invaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import se.marcusk.invaders.Invaders;

public class MainMenuScreen implements Screen {

    private final Invaders game;

    public MainMenuScreen(Invaders game) {
        this.game = game;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);

        game.getBatch().begin();
        //draw text. Remember that x and y are in meters
        game.getFont().draw(game.getBatch(), "Welcome to Invaders!!! ", 1, 1.5f);
        game.getFont().draw(game.getBatch(), "Tap anywhere to begin!", 1, 1);
        game.getBatch().end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
