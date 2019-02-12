/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui;

import fr.knacky.absoluteui.renderer.DrawableRenderer;
import fr.knacky.absoluteui.renderer.FontRenderer;
import fr.knacky.absoluteui.view.ClickableView;
import fr.knacky.absoluteui.view.View;
import java.io.IOException;
import java.util.ArrayList;

public class Gui {
  private static int width, height;

  private ArrayList<View> views = new ArrayList<>();

  private DrawableRenderer drawableRenderer;
  private FontRenderer fontRenderer;

  public Gui() throws IOException {
    drawableRenderer = new DrawableRenderer();
    fontRenderer = new FontRenderer();
  }

  public void update(float x, float y, boolean mouse) {
    for (View view : views) {
      if (view.isVisible()) {
        if (view instanceof ClickableView) {
          ((ClickableView) view).update(x, y, mouse);
        }
        view.animate();
        view.render();
      }
    }
  }

  public void render() {
    drawableRenderer.render();
    fontRenderer.render();
    DrawableRenderer.clear();
    FontRenderer.clear();
  }

  public void free() {
    drawableRenderer.free();
    fontRenderer.free();
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
}
