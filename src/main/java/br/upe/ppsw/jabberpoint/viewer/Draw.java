package br.upe.ppsw.jabberpoint.viewer;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import br.upe.ppsw.jabberpoint.model.SlideItem;

public abstract class Draw {
    
    public abstract void draw(int x, int y, float scale, Graphics g, ImageObserver observer, SlideItem item);
    
}