package trcc;

import processing.core.*;

import java.util.ArrayList;

/**
 * trcc Propos
 */

public class Propos {

	public float padding = 10;

	public int poster_w;
	public int poster_h;

	public float stroke_weight = 1;
	public float fg;
	public float bg;

	PApplet app;

	public final static String VERSION = "##library.prettyVersion##";

	public PGraphics buffer;

	/**
	 * Constructor
	 * 
	 * @example Hello
	 * @param theParent the parent PApplet
	 */
	public Propos(PApplet theParent) {
		app = theParent;

//		padding = 10;
//		
//		poster_w = 586;
//		poster_h = 810;
//		
		fg = 0;
		bg = 255;
//		
		poster_w = 586;
		poster_h = 810;

//		stroke_weight = 1;

		buffer = app.createGraphics(poster_w, poster_h, PConstants.P2D);

	}

	/**
	 * The surface of the poster
	 * 
	 * @return PGraphics
	 */

	public PGraphics ground() {
		buffer.beginDraw();
		buffer.background(bg);
		buffer.endDraw();
		return buffer;
	}

	/**
	 * calculateFontSize Utility-Funktion
	 * 
	 * @return float
	 */

	public float calculateFontSize(String headline, PFont font) {
		
		// Bug: Headline ist etwas zu schmal
		
		float val = 0;
		
		buffer.beginDraw();

		while (buffer.textWidth(headline) < poster_w) {
			val += 1;
			buffer.textSize(val);
		}
		
		buffer.endDraw();
		
		return val;

	}

	/**
	 * Headline
	 * 
	 * @return PGraphics
	 */

	public PGraphics headline(String txt, PFont font, char align, float fontSize, float lineHeight) {
		buffer.beginDraw();
		buffer.clear();
		buffer.textMode(PConstants.SHAPE);
		buffer.fill(fg);
		buffer.noStroke();
		buffer.textFont(font);
		buffer.textSize(fontSize);
		buffer.textLeading(fontSize * lineHeight);

		if (align == 'L') {

			double d = -fontSize * 0.2;
			float y = (float) d;

			buffer.textAlign(PConstants.LEFT, PConstants.TOP);
			buffer.text(txt, 0, y);

		} else if (align == 'C') {

			double d = -fontSize * 0.2;
			float y = (float) d;

			buffer.textAlign(PConstants.CENTER, PConstants.TOP);
			buffer.text(txt, buffer.width / 2, y);
		}
		buffer.endDraw();
		return buffer;
	}

	/**
	 * Display a grid of elements
	 * 
	 * @return PGraphics
	 */

	public PGraphics grid(float cols, float rows) {

		float tile_w = poster_w / cols;
		float tile_h = poster_h / rows;

		buffer.beginDraw();
		buffer.clear();
		buffer.noFill();
		buffer.stroke(fg);
		buffer.strokeWeight(stroke_weight);
		for (int x = 1; x < cols; x++) {
			buffer.line(x * tile_w, 0, x * tile_w, buffer.height);
		}
		for (int y = 1; y < rows; y++) {
			buffer.line(0, y * tile_h, buffer.height, y * tile_h);
		}
		buffer.endDraw();

		return buffer;
	}

	/**
	 * Display an image
	 * 
	 * @return PGraphics
	 */

	public PGraphics img(PImage image, float x, float y, int w, int h) {

		if (app.frameCount == 1) {
			image.resize(w, h);
		}
		buffer.beginDraw();
		buffer.clear();
		buffer.imageMode(PConstants.CENTER);
		buffer.push();
		buffer.translate(buffer.width / 2 + x, buffer.height / 2 + y);
		buffer.image(image, 0, 0);
		buffer.pop();
		buffer.endDraw();

		return buffer;
	}

	/**
	 * Display circles
	 * 
	 * @return PGraphics
	 */

	public PGraphics circles() {
		buffer.beginDraw();
		buffer.clear();
		buffer.stroke(fg);
		buffer.noFill();
		buffer.strokeWeight(stroke_weight);
		buffer.push();
		buffer.ellipse(buffer.width / 2, buffer.height / 2, buffer.width, buffer.width);
		buffer.pop();
		buffer.endDraw();
		return buffer;
	}

	/**
	 * Display a scratch
	 * 
	 * @return PGraphics
	 */

	public ArrayList<PVector> points;

	public PGraphics scratch(int pts) {

		if (app.frameCount == 1) {
			points = new ArrayList<PVector>();
			for (int i = 0; i < pts; i++) {
				float x = app.random(buffer.width);
				float y = app.random(buffer.height);
				points.add(new PVector(x, y));
			}
		}
		buffer.beginDraw();
		buffer.clear();
		buffer.noFill();
		buffer.stroke(fg);
		buffer.strokeWeight(stroke_weight);
		buffer.beginShape();
		for (int i = 0; i < points.size(); i++) {
			buffer.curveVertex(points.get(i).x, points.get(i).y);
		}

		buffer.endShape();

		buffer.endDraw();
		return buffer;
	}

	/**
	 * rasterizer
	 * 
	 * @return PGraphics
	 */

	public PGraphics rasterize(PImage img, float tilesX, float tilesY) {
		PGraphics buffer2 = app.createGraphics(poster_w, poster_h);

		if (app.frameCount == 1) {
			img.resize(buffer.width, 0);
		}

		buffer2.beginDraw();
		buffer2.clear();
		buffer2.imageMode(PConstants.CENTER);
		buffer2.image(img, buffer.width / 2, buffer.height / 2);
		buffer2.endDraw();

		float tileW = buffer.width / tilesX;
		float tileH = buffer.height / tilesY;

		buffer.beginDraw();
		buffer.noStroke();
		buffer.clear();
		buffer.fill(fg);

		PImage bufferImg = buffer2.get();
		for (int x = 0; x < tilesX; x++) {
			for (int y = 0; y < tilesY; y++) {
				int px = Math.round(x * tileW);
				int py = Math.round(y * tileH);
				int c = bufferImg.get(px, py);
				float b = app.map(app.brightness(c), 0, 255, 0, 1);

				buffer.fill(fg);
				buffer.push();
				buffer.translate(x * tileW, y * tileH);
				buffer.rect(0, 0, tileW * b, tileH * b);
				buffer.pop();
			}
		}
		buffer.endDraw();

		return buffer;
	}

	/**
	 * meta
	 * 
	 * @return PGraphics
	 */

	public PGraphics meta(PFont font, float fontsize, float lineHeight, float offsetY, String text) {

		buffer.beginDraw();
		buffer.clear();
		buffer.noStroke();
		buffer.textMode(PConstants.SHAPE);
		buffer.fill(0);
		buffer.textFont(font);

		buffer.textAlign(PConstants.CENTER, PConstants.TOP);
		buffer.textSize(fontsize);
		buffer.textLeading(fontsize * lineHeight);
		buffer.push();
		buffer.translate(buffer.width / 2, buffer.height + offsetY);
		buffer.text(text, 0, 0);
		buffer.pop();
		buffer.endDraw();
		return buffer;
	}

	public PGraphics meta(PFont font, float fontsize, float lineHeight, float offsetY, String text1, String text2) {

		buffer.beginDraw();
		buffer.clear();
		buffer.noStroke();
		buffer.textMode(PConstants.SHAPE);
		buffer.fill(0);
		buffer.textFont(font);

		buffer.textAlign(PConstants.LEFT, PConstants.TOP);
		buffer.textSize(fontsize);
		buffer.textLeading(fontsize * lineHeight);
		buffer.push();
		buffer.translate(padding, buffer.height + offsetY);
		buffer.text(text1, 0, 0);
		buffer.pop();

		buffer.push();
		buffer.translate(buffer.width / 2, buffer.height + offsetY);
		buffer.text(text2, 0, 0);
		buffer.pop();
		buffer.endDraw();
		return buffer;
	}

	public PGraphics meta(PFont font, float fontsize, float lineHeight, float offsetY, String text1, String text2,
			String text3) {

		buffer.beginDraw();
		buffer.clear();
		buffer.noStroke();
		buffer.textMode(PConstants.SHAPE);
		buffer.fill(0);
		buffer.textFont(font);

		buffer.textAlign(PConstants.LEFT, PConstants.TOP);
		buffer.textSize(fontsize);
		buffer.textLeading(fontsize * lineHeight);
		buffer.push();
		buffer.textAlign(PConstants.LEFT, PConstants.TOP);
		buffer.translate(padding, buffer.height + offsetY);
		buffer.text(text1, 0, 0);
		buffer.pop();

		buffer.push();
		buffer.textAlign(PConstants.CENTER, PConstants.TOP);
		buffer.translate(buffer.width / 2, buffer.height + offsetY);
		buffer.text(text2, 0, 0);
		buffer.pop();

		buffer.push();
		buffer.textAlign(PConstants.RIGHT, PConstants.TOP);
		buffer.translate(buffer.width - padding, buffer.height + offsetY);
		buffer.text(text3, 0, 0);
		buffer.pop();
		buffer.endDraw();
		return buffer;
	}

	/**
	 * timestamp
	 * 
	 * @return String
	 */

	public String timestamp() {
		int y = PApplet.year(); // 2003,2004, 2005, etc.
		int m = PApplet.month(); // Values from 1 - 12
		int d = PApplet.day(); // Values from 1 - 31
		int h = PApplet.hour();
		int mi = PApplet.minute();
		int sec = PApplet.second();
		int mill = app.millis();

		String val = "_" + String.valueOf(y) + String.valueOf(m) + String.valueOf(d) + "_" + String.valueOf(h) + "_"
				+ String.valueOf(mi) + "_" + String.valueOf(sec) + "_" + String.valueOf(mill);

		return val;
	}

}
