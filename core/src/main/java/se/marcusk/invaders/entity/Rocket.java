package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Rocket {
    Sprite sprite;
    Rectangle hitBox;

    private final float speed = 5f;

    public Rocket(Texture texture, float x, float y) {
        float rocketWidth = 1;
        float rocketHeight = 1;

        sprite = new Sprite(texture);
        sprite.setSize(rocketWidth, rocketHeight);
        sprite.setX(x);
        sprite.setY(y);
        hitBox = new Rectangle();
        updateHitBox();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void update(float delta) {
        sprite.translateY(speed * delta);
        updateHitBox();
    }

    public boolean isOffScreen(float worldHeight) {
        return sprite.getY() > worldHeight;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    private void updateHitBox() {
        // Tuned for rocket sprite
        hitBox.set(sprite.getX() + 0.45f, sprite.getY() + 0.2f,
            0.1f, 0.5f);
    }
}
