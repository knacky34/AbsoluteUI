/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.renderer;

import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.view.Drawable;
import fr.knacky.absoluteui.view.Text;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11C.*;

public class MaskFontRenderer {
  private DrawableRenderer drawableRenderer;
  private FontRenderer fontRenderer;

  private static ArrayList<Drawable> masks = new ArrayList<>();
  private static HashMap<FontType, ArrayList<Text>> texts = new HashMap<>();

  public MaskFontRenderer(DrawableRenderer drawableRenderer, FontRenderer fontRenderer) {
    this.drawableRenderer = drawableRenderer;
    this.fontRenderer = fontRenderer;
  }

  public void render() {
    glEnable(GL_STENCIL_TEST);
    prepareDrawToStencil();
    drawableRenderer.renderToStencil(masks);

    prepareDrawToScene();
    fontRenderer.render(texts);
    glDisable(GL_STENCIL_TEST);
  }

  private static void prepareDrawToStencil() {
    glStencilFunc(GL_ALWAYS, 1, 0xFF);
    glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
    glStencilMask(0xFF);
    glColorMask(false, false, false, false);
    glDepthMask(false);
  }

  private static void prepareDrawToScene() {
    glColorMask(true, true, true, true);
    glDepthMask(true);
    glStencilFunc(GL_EQUAL, 1, 0xFF);
    glStencilMask(0x00);
  }

  public static void add(Text text, Drawable mask) {
    FontType font = text.getFont();
    ArrayList<Text> batch = texts.get(font);
    if (batch != null) {
      batch.add(text);
    } else {
      ArrayList<Text> newBatch = new ArrayList<>();
      newBatch.add(text);
      texts.put(font, newBatch);
    }

    masks.add(mask);
  }

  public static void clear() {
    masks.clear();
    texts.clear();
  }
}
