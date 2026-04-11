package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Explosion {

    private final Sprite sprite;
    private final Texture frame1;
    private final Texture frame2;

    private float timer = 0f;
    private float frameTime = 0.15f; // switch every 0.15s

    public Explosion(Texture frame1, Texture frame2, float x, float y) {
        this.frame1 = frame1;
        this.frame2 = frame2;

        sprite = new Sprite(frame1);
        sprite.setSize(1.5f, 1.5f);
        sprite.setOriginCenter();
        sprite.setPosition(x, y);
    }

    public boolean update(float delta) {
        timer += delta;

        if (timer < frameTime) {
            sprite.setTexture(frame1);
        } else if (timer < frameTime * 2) {
            sprite.setTexture(frame2);
        }

        return timer >= frameTime * 2; // remove after both frames
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
