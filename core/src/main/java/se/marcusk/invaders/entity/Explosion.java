package se.marcusk.invaders.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {

    private final Sprite sprite;
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    public Explosion(Animation<TextureRegion> animation, float x, float y) {
        this.animation = animation;

        sprite = new Sprite(animation.getKeyFrame(0));
        sprite.setSize(1.5f, 1.5f);
        sprite.setOriginCenter();
        sprite.setPosition(x, y);
    }

    public boolean update(float delta) {
        stateTime += delta;
        sprite.setRegion(animation.getKeyFrame(stateTime));

        return animation.isAnimationFinished(stateTime);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
