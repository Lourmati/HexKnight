package ca.qc.bdeb.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * A Sprite is a Component containing an image
 * 
 * @author Eric Wenaas
 */
public class Sprite extends JComponent {

    // The image contained in the Sprite
    private BufferedImage image;
    private char niveauAlpha;

    public Sprite() {
        niveauAlpha = 255;
    }

    /**
     * Constructor
     * 
     * @param image an image
     */
    public Sprite(BufferedImage image) {
        super();
        this.image = image;
    }

    /**
     * An image of a Sprite can be changed.
     * 
     * @param image the image
     */
    public final void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setNiveauAlpha(char alpha) {
        if (alpha == this.niveauAlpha) {
            return;
        }
        this.niveauAlpha = alpha;

        if (image == null){
            return;
        }
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int couleur = image.getRGB(x, y);
                couleur |= 0xff000000;
                int mc = (alpha << 24) | 0x00ffffff;
                int nouvelle = couleur & mc;
                image.setRGB(x, y, nouvelle);
            }
        }
    }

    public char getNiveauAlpha() {
        return niveauAlpha;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
