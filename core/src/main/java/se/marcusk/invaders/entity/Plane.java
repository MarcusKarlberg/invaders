package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Plane {
    Sprite sprite;
    Rectangle rectangle;

    float speed = 8f;

    public Plane(Texture texture, float worldWidth) {
        sprite = new Sprite(texture);
        sprite.setSize(1, 1);
        sprite.setX(worldWidth / 2);
        sprite.setY(2);

        rectangle = new Rectangle();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void update(float worldWidth) {
        float planeWidth = sprite.getWidth();
        float planeHeight = sprite.getHeight();

        sprite.setX(MathUtils.clamp(sprite.getX(), 0, worldWidth - planeWidth));
        rectangle.set(sprite.getX(), sprite.getY(), planeWidth, planeHeight);
    }

    public void moveRight(float delta) {
        sprite.translateX(speed * delta);
    }

    public void moveLeft(float delta) {
        sprite.translateX(-speed * delta);
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
}
