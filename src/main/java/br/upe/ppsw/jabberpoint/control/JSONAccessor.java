package br.upe.ppsw.jabberpoint.control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.upe.ppsw.jabberpoint.model.BitmapItem;
import br.upe.ppsw.jabberpoint.model.Presentation;
import br.upe.ppsw.jabberpoint.model.Slide;
import br.upe.ppsw.jabberpoint.model.SlideItem;
import br.upe.ppsw.jabberpoint.model.TextItem;

public class JSONAccessor extends Accessor {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private static final String SHOWTITLE = "showtitle";
  private static final String SLIDETITLE = "title";
  private static final String SLIDES = "slides";
  private static final String SLIDE = "slide";
  private static final String ITEMS = "items";
  private static final String LEVEL = "level";
  private static final String KIND = "kind";
  private static final String TEXT = "text";
  private static final String IMAGE = "image";

  protected static final String IOEXCEPTION = "IOException";

  public void loadFile(Presentation presentation, String filename) throws IOException {
    JsonObject jsonObject = gson.fromJson(new File(filename), JsonObject.class);
    presentation.setTitle(jsonObject.get(SHOWTITLE).getAsString());

    for (JsonElement slideElement : jsonObject.getAsJsonArray(SLIDES)) {
      JsonObject slideObject = slideElement.getAsJsonObject();
      Slide slide = new Slide();
      slide.setTitle(slideObject.get(SLIDETITLE).getAsString());
      presentation.append(slide);

      for (JsonElement itemElement : slideObject.getAsJsonArray(ITEMS)) {
        JsonObject itemObject = itemElement.getAsJsonObject();
        loadSlideItem(slide, itemObject);
      }
    }
  }

  protected void loadSlideItem(Slide slide, JsonObject itemObject) {
    int level = itemObject.get(LEVEL).getAsInt();

    String kind = itemObject.get(KIND).getAsString();
    if (TEXT.equals(kind)) {
      slide.append(new TextItem(level, itemObject.get(TEXT).getAsString()));
    } else {
      if (IMAGE.equals(kind)) {
        slide.append(new BitmapItem(level, itemObject.get(IMAGE).getAsString()));
      } else {
        System.err.println(UNKNOWNTYPE);
      }
    }
  }

  public void saveFile(Presentation presentation, String filename) throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(SHOWTITLE, presentation.getTitle());

    Vector<Slide> slides = presentation.getSlides();
    JsonElement slidesElement = gson.toJsonTree(slides);
    jsonObject.add(SLIDES, slidesElement);

    PrintWriter out = new PrintWriter(new FileWriter(filename));
    out.println(gson.toJson(jsonObject));
    out.close();
  }
}
