package br.upe.ppsw.jabberpoint.control;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.upe.ppsw.jabberpoint.model.BitmapItem;
import br.upe.ppsw.jabberpoint.model.Presentation;
import br.upe.ppsw.jabberpoint.model.Slide;
import br.upe.ppsw.jabberpoint.model.TextItem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONAccessor extends Accessor {
	
	  protected static final String SHOWTITLE = "showtitle";
	  protected static final String SLIDETITLE = "title";
	  protected static final String SLIDE = "slide";
	  protected static final String ITEM = "item";
	  protected static final String LEVEL = "level";
	  protected static final String KIND = "kind";
	  protected static final String TEXT = "text";
	  protected static final String IMAGE = "image";

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
        String content = itemJson.getString("content");

        if ("text".equals(kind)) {
            slide.append(new TextItem(level, content));
        } else if (IMAGE.equals(kind)) {
            slide.append(new BitmapItem(level, content));
        } else {
            System.err.println("Unknown item type: " + kind);
        }
    }


	@Override
	public void saveFile(Presentation presentation, String fileName) throws IOException {
		// TODO Auto-generated method stub
		
	}
}