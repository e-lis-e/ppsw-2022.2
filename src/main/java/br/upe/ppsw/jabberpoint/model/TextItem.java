package br.upe.ppsw.jabberpoint.model;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.image.ImageObserver;
import java.text.AttributedString;

public class TextItem extends SlideItem {

  private String text;

  private static final String EMPTYTEXT = "No Text Given";

  public TextItem(int level, String string) {
    super(level);
    text = string;
  }

  public TextItem() {
    this(0, EMPTYTEXT);
  }

  public String getText() {
    return text == null ? "" : text;
  }

  public AttributedString getAttributedString(Style style, float scale) {
    AttributedString attrStr = new AttributedString(getText());

    attrStr.addAttribute(TextAttribute.FONT, style.getFont(scale), 0, text.length());

    return attrStr;
  }

  public String toString() {
    return "TextItem[" + getLevel() + "," + getText() + "]";
  }

/*@Override
public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style style) {
	return null;
}

@Override
public void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer) {
	
}*/

}
