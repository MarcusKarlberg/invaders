package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Laser {
    Sprite sprite;
    Rectangle hitBox;

    private float speed = 10f;

    public Laser(Texture texture, float x, float y) {
        float laserWidth = 1.5f;
        float laserHeight = 2f;

        sprite = new Sprite(texture);
        sprite.setSize(laserWidth, laserHeight);
        sprite.setX(x);
        sprite.setY(y);
        hitBox = new Rectangle();
        updateHitBox();
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    private void updateHitBox() {
        hitBox.set(
            sprite.getX() + 0.2f,
            sprite.getY() + 0.1f,
            sprite.getWidth() - 0.4f,
            sprite.getHeight() - 0.2f
        );
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void update(float delta) {
        sprite.translateY(- speed * delta);
        updateHitBox();
    }

    public boolean isOffScreen(float worldHeight) {
        return sprite.getY() > worldHeight;
    }
}
