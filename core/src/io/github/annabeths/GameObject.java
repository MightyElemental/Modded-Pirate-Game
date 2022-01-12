package io.github.annabeths;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface GameObject {
    int x = 0;
    int y = 0;
    float rotation = 0;
    Sprite sprite = null;    

    void Update(float delta);

    void Draw();
}
