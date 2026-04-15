package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Explosion {

    private final Sprite sprite;
    private final Array<Texture> frames;

    private float timer = 0f;
    private final float frameTime = 0.1f; // time per frame
    private int currentFrame = 0;

    public Explosion(Array<Texture> textures, float x, float y) {
        this.frames = textures;

        sprite = new Sprite(frames.get(0));
        sprite.setSize(1.5f, 1.5f);
        sprite.setOriginCenter();
        sprite.setPosition(x, y);
    }

    // return true when finished
    public boolean update(float delta) {
        timer += delta;

        if (timer >= frameTime) {
            timer -= frameTime; // better than resetting to 0
            currentFrame++;

            if (currentFrame < frames.size) {
                sprite.setTexture(frames.get(currentFrame));
            }
        }

        return currentFrame >= frames.size;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
