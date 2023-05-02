package br.upe.ppsw.jabberpoint.model.accessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.upe.ppsw.jabberpoint.model.Presentation;
import br.upe.ppsw.jabberpoint.model.Slide;
import br.upe.ppsw.jabberpoint.model.items.BitmapItem;
import br.upe.ppsw.jabberpoint.model.items.SlideItem;
import br.upe.ppsw.jabberpoint.model.items.TextItem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class JSONAccessor extends Accessor {
	
	  protected static final String SHOWTITLE = "showtitle";
	  protected static final String SLIDETITLE = "title";
	  protected static final String SLIDE = "slide";
	  protected static final String ITEM = "item";
	  protected static final String LEVEL = "level";
	  protected static final String KIND = "kind";
	  protected static final String TEXT = "text";
	  protected static final String IMAGE = "image";
      protected static final String CONTENT = "content";

	  protected static final String PCE = "Parser Configuration Exception";
	  protected static final String UNKNOWNTYPE = "Unknown Element type";
	  protected static final String NFE = "Number Format Exception";
	
	@Override
    public void loadFile(Presentation presentation, String filename) throws IOException {
		
        String jsonString = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);

        try {
            JSONObject json = new JSONObject(jsonString);
            presentation.setTitle(json.getString(SHOWTITLE));

            JSONArray slides = json.getJSONArray(SLIDE);

            for (int slideNumber = 0; slideNumber < slides.length(); slideNumber++) {
                JSONObject slideJson = slides.getJSONObject(slideNumber);
                Slide slide = new Slide();
                slide.setTitle(slideJson.getString(SLIDETITLE));
                presentation.append(slide);

                JSONArray slideItems = slideJson.getJSONArray(ITEM);
                for (int itemNumber = 0; itemNumber < slideItems.length(); itemNumber++) {
                    JSONObject itemJson = slideItems.getJSONObject(itemNumber);
                    loadSlideItem(slide, itemJson);
                }
            }
        } catch (JSONException e) {
            System.err.println(e.toString());
        }
        
    }

    protected void loadSlideItem(Slide slide, JSONObject itemJson) {
        int level = itemJson.getInt(LEVEL);
        String kind = itemJson.getString(KIND);
        String content = itemJson.getString(CONTENT);

        if ("text".equals(kind)) {
            slide.append(new TextItem(level, content));
        } else if (IMAGE.equals(kind)) {
            slide.append(new BitmapItem(level, content));
        } else {
            System.err.println("Unknown item type: " + kind);
        }
    }


	@Override
    public void saveFile(Presentation presentation, String filename) throws IOException {
        
        JSONObject presentationObject = new JSONObject();
        presentationObject.put(SHOWTITLE, presentation.getTitle());

        JSONArray slideArray = new JSONArray();
        for (int slideNumber = 0; slideNumber < presentation.getSize(); slideNumber++) {
            Slide slide = presentation.getSlide(slideNumber);

            JSONObject slideObject = new JSONObject();
            slideObject.put(SLIDETITLE, slide.getTitle());

            JSONArray slideItemArray = new JSONArray();
            Vector<SlideItem> slideItems = slide.getSlideItems();
            for (int itemNumber = 0; itemNumber < slideItems.size(); itemNumber++) {
                SlideItem slideItem = slideItems.elementAt(itemNumber);

                JSONObject slideItemObject = new JSONObject();
                slideItemObject.put(LEVEL, slideItem.getLevel());

                if (slideItem instanceof TextItem) {
                    slideItemObject.put(KIND, TEXT);
                    slideItemObject.put(TEXT, ((TextItem) slideItem).getText());
                } else if (slideItem instanceof BitmapItem) {
                    slideItemObject.put(KIND, IMAGE);
                    slideItemObject.put(IMAGE, ((BitmapItem) slideItem).getName());
                }

                slideItemArray.put(slideItemObject);
            }

            slideObject.put(ITEM, slideItemArray);
            slideArray.put(slideObject);
        }

        presentationObject.put(SLIDE, slideArray);

        PrintWriter out = new PrintWriter(new FileWriter(filename));
        out.println(presentationObject.toString());
        out.close();
    }
}