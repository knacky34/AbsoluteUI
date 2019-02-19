/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui;

import fr.knacky.absoluteui.renderer.DrawableRenderer;
import fr.knacky.absoluteui.renderer.FontRenderer;
import fr.knacky.absoluteui.renderer.MaskFontRenderer;
import fr.knacky.absoluteui.view.ClickableView;
import fr.knacky.absoluteui.view.View;
import java.io.IOException;
import java.util.ArrayList;

public class Gui {
  public static final int KEY_NONE = -1;
  public static int KEY_PRESS;
  public static int KEY_REPEAT;
  public static int KEY_RELEASE;

  private static int width, height;

  private long lastTime = 0L;
  public boolean tick = false;

  private ArrayList<View> views = new ArrayList<>();

  private DrawableRenderer drawableRenderer;
  private FontRenderer fontRenderer;
  private MaskFontRenderer maskFontRenderer;

  public int key_backspace = KEY_NONE;
  public int key_delete = KEY_NONE;
  public int key_left = KEY_NONE;
  public int key_right = KEY_NONE;

  public char[] chars;

  public Gui() throws IOException {
    drawableRenderer = new DrawableRenderer();
    fontRenderer = new FontRenderer();
    maskFontRenderer = new MaskFontRenderer(drawableRenderer, fontRenderer);
  }

  public void update(long frameTimeNano, float x, float y, boolean mouse) {
    for (View view : views) {
      if (view.isVisible()) {
        if (view instanceof ClickableView) {
          ((ClickableView) view).update(this, x, y, mouse);
        }
        view.animate();
        view.render();
      }
    }

    lastTime += frameTimeNano;
    if (lastTime >= 5E8) {
      tick = !tick;
      lastTime = 0L;
    }

    key_backspace = KEY_NONE;
    key_delete = KEY_NONE;
    key_left = KEY_NONE;
    key_right = KEY_NONE;
  }

  public void render() {
    drawableRenderer.render();
    fontRenderer.render();
    maskFontRenderer.render();
    DrawableRenderer.clear();
    FontRenderer.clear();
    MaskFontRenderer.clear();
  }

  public void free() {
    drawableRenderer.free();
    fontRenderer.free();
  }

  public void resize(int viewportWidth, int viewportHeight) {
    Gui.width = viewportWidth;
    Gui.height = viewportHeight;
    for (View view : views) {
      view.resize();
    }
  }

  public void add(View view) {
    this.views.add(view);
  }

  public static float abuiGetAspectRatio() {
    return (float) width / (float) height;
  }

  public static void init(int viewportWidth, int viewportHeight) {
    Gui.width = viewportWidth;
    Gui.height = viewportHeight;
  }

  public static void setKeyConstants(int press, int repeat, int release) {
    KEY_PRESS = press;
    KEY_REPEAT = repeat;
    KEY_RELEASE = release;
  }
}