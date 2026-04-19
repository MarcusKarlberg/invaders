package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Ufo {
    private final float width = 1;
    private final float height = 1;
    private final float speed = 3;
    private float direction = 1f;
    private boolean dropping = true;
    private final float dropDistance = 2f; // how far it should drop
    private final float startY;
    private float rotation = 90f;

    Sprite sprite;
    Rectangle hitBox;

    public Ufo(Texture texture, float worldWidth, float worldHeight) {
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setX(MathUtils.random(0F, worldWidth - width));
        sprite.setY(worldHeight);
        sprite.setOriginCenter();
        startY = sprite.getY();

        hitBox = new Rectangle();
    }

    public void update(float delta, float worldWidth, float worldHeight) {
        updateMovement(delta, worldWidth);
        updateHitbox();
    }

    private void updateMovement(float delta, float worldWidth) {

        if (dropping) {
            sprite.translateY(-speed * delta);

            if (sprite.getY() <= startY - dropDistance) {
                dropping = false;
            }
        }
        // Sideways movement
        sprite.translateX(speed * direction * delta);
        sprite.rotate(rotation * delta);

        if (sprite.getX() <= 0) {
            sprite.setX(0);
            rotation = 90f;
            direction = 1f;
        }

        if (sprite.getX() >= worldWidth - sprite.getWidth()) {
            sprite.setX(worldWidth - sprite.getWidth());
            rotation = -90f;
            direction = -1f;
        }
    }

    private void updateHitbox() {
        float shrink = 0.2f; // 20% smaller

        float w = sprite.getWidth() * (1f - shrink);
        float h = sprite.getHeight() * (1f - shrink);

        float x = sprite.getX() + (sprite.getWidth() - w) / 2f;
        float y = sprite.getY() + (sprite.getHeight() - h) / 2f;

        hitBox.set(x, y, w, h);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDirection() {
        return direction;
    }

    public boolean isDropping() {
        return dropping;
    }

    public float getDropDistance() {
        return dropDistance;
    }

    public float getStartY() {
        return startY;
    }

    public float getRotation() {
        return rotation;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
