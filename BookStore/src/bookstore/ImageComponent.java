package bookstore;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImageComponent extends Component {

    private BufferedImage img;

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    ImageComponent(String path) {
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dimension getPreferredSize() {
        if (img == null) {
            return new Dimension(100, 10);
        } else {
            return new Dimension(img.getWidth(), img.getHeight());
        }
    }
}
