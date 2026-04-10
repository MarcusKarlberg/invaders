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
    Rectangle rectangle;

    public Ufo(Texture texture, float worldWidth, float worldHeight) {
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setX(MathUtils.random(0F, worldWidth - width));
        sprite.setY(worldHeight -2);

        rectangle = new Rectangle();
    }

    //TODO: add movement and collision logic

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
