package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Ufo {
    float width = 1;
    float height = 1;

    Sprite sprite;
    Rectangle hitBox;

    public Ufo(Texture texture, float worldWidth, float worldHeight) {
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setX(MathUtils.random(0F, worldWidth - width));
        sprite.setY(worldHeight -2);

        hitBox = new Rectangle();
    }

    public void update(float delta) {
        updateHitbox();
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
}
