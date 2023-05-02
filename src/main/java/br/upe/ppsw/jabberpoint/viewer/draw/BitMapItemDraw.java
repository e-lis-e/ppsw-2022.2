package br.upe.ppsw.jabberpoint.viewer.draw;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import br.upe.ppsw.jabberpoint.model.Style;

public class BitMapItemDraw extends Draw{

    private BufferedImage bufferedImage;

    @Override
    public void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver observer) {
        int width = x + (int) (myStyle.indent * scale);
	    int height = y + (int) (myStyle.leading * scale);

	    g.drawImage(bufferedImage, width, height, (int) (bufferedImage.getWidth(observer) * scale),
	        (int) (bufferedImage.getHeight(observer) * scale), observer);
    }

    @Override
    public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle) {
        return new Rectangle((int) (myStyle.indent * scale), 0,
        (int) (bufferedImage.getWidth(observer) * scale),
        ((int) (myStyle.leading * scale)) + (int) (bufferedImage.getHeight(observer) * scale));
    }

    
}
