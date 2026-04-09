package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Rocket {
    Sprite sprite;
    Rectangle rectangle;

    private final float speed = 5f;

    public Rocket(Texture texture, float x, float y) {
        float rocketWidth = 1;
        float rocketHeight = 1;

        sprite = new Sprite(texture);
        sprite.setSize(rocketWidth, rocketHeight);
        sprite.setX(x);
        sprite.setY(y);
        rectangle = new Rectangle();
        updateRectangle();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void update(float delta) {
        sprite.translateY(speed * delta);
        updateRectangle();
    }

    public boolean isOffScreen(float worldHeight) {
        return sprite.getY() > worldHeight;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    private void updateRectangle() {
        rectangle.set(sprite.getX(), sprite.getY(),
            sprite.getWidth(), sprite.getHeight());
    }
}
