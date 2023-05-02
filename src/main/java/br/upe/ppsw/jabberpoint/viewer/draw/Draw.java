package br.upe.ppsw.jabberpoint.viewer.draw;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import br.upe.ppsw.jabberpoint.model.Style;

public abstract class Draw {

    public abstract void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver o);

    public abstract Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle);
}
