package fr.knacky.absoluteui;

import fr.knacky.absoluteui.view.ClickableView;
import fr.knacky.absoluteui.view.View;
import java.io.IOException;
import java.util.ArrayList;

public class Gui {
  private ArrayList<View> views = new ArrayList<>();

  private DrawableRenderer drawableRenderer;
  private FontRenderer fontRenderer;

  public Gui() throws IOException {
    drawableRenderer = new DrawableRenderer();
    fontRenderer = new FontRenderer();
  }

  public void update(InputHandler inputHandler) {
    float x = inputHandler.getMouseX();
    float y = inputHandler.getMouseY();
    boolean mouse = inputHandler.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT);

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
}
