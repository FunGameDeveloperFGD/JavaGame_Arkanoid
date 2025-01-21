import java.awt.*;

public class Sprite {
    private int x;
    private int y;
    private final Texture texture;
    private final Rectangle bounds;

    public Sprite(Texture texture) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        this.x = x;
        this.bounds.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
        this.bounds.y = y;
    }

    public int getY() {
        return y;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
