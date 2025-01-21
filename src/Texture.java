import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Texture {
    private BufferedImage image;
    private int width;
    private int height;

    public Texture(String path) {
        loadFromFile(path);
        if (image != null) {
            width = image.getWidth();
            height = image.getHeight();
        }
    }

    public void loadFromFile(String path) {
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
