package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.callback.ActionCallback;
import fr.knacky.absoluteui.callback.CheckedCallback;
import fr.knacky.absoluteui.callback.PressedCallback;
import fr.knacky.absoluteui.font.FontType;
import java.io.IOException;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Checkbox extends ClickableView {
  public static final int LABEL_LEFT = 0;
  public static final int LABEL_RIGHT = 1;
  private static final float OFFSET = 0.01f;

  private static final int[] textures = new int[2];

  private boolean checked = false;
  private int alignment;

  public final Drawable box;
  public final Text label;

  static {
    try {
      textures[0] = Loader.loadTexture("textures/checkbox_unchecked.png", true);
      textures[1] = Loader.loadTexture("textures/checkbox_checked.png", true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Checkbox(Vector2f position, String text, ActionCallback callback, int alignment) {
    super(position, null, callback);
    this.label = new Text(new Vector2f(position), text, new Vector3f(1.0f, 1.0f, 1.0f));
    this.box = new Drawable(super.position, new Vector2f((label.size.y - OFFSET)/ DisplayManager.dmGetAspectRatio(), label.size.y - OFFSET), textures[0]);
    super.size = this.box.size;

    this.alignment = alignment;
    if (alignment == LABEL_RIGHT) {
      this.label.position.add(this.box.size.x + OFFSET, 0f);
    } else if (alignment == LABEL_LEFT) {
      super.position.add(this.label.size.x + OFFSET, 0f);
    }
  }

  public Checkbox(Vector2f position, String text, FontType font, float fontSize, ActionCallback callback, int alignment) {
    super(position, null, callback);
    this.label = new Text(new Vector2f(position), font, fontSize, text, new Vector3f(1.0f, 1.0f, 1.0f));
    this.box = new Drawable(super.position, new Vector2f((label.size.y - OFFSET)/ DisplayManager.dmGetAspectRatio(), label.size.y - OFFSET), textures[0]);
    super.size = this.box.size;

    this.alignment = alignment;
    if (alignment == LABEL_RIGHT) {
      this.label.position.add(this.box.size.x + OFFSET, 0f);
    } else if (alignment == LABEL_LEFT) {
      super.position.add(this.label.size.x + OFFSET, 0f);
    }
  }

  public Checkbox(Vector2f position, float boxHeight, String text, ActionCallback callback, int alignment) {
    super(position, null, callback);
    this.label = new Text(new Vector2f(position), text, new Vector3f(1.0f, 1.0f, 1.0f));
    this.box = new Drawable(super.position, new Vector2f(boxHeight / DisplayManager.dmGetAspectRatio(), boxHeight), textures[0]);
    this.label.position.y = super.position.y + (boxHeight - (label.size.y - OFFSET)) / 2f;
    super.size = this.box.size;

    this.alignment = alignment;
    if (alignment == LABEL_RIGHT) {
      this.label.position.add(this.box.size.x + OFFSET, 0f);
    } else if (alignment == LABEL_LEFT) {
      super.position.add(this.label.size.x + OFFSET, 0f);
    }
  }

  public Checkbox(Vector2f position, float boxHeight, String text, FontType font, float fontSize, ActionCallback callback, int alignment) {
    super(position, null, callback);
    this.label = new Text(new Vector2f(position), font, fontSize, text, new Vector3f(1.0f, 1.0f, 1.0f));
    this.box = new Drawable(super.position, new Vector2f(boxHeight / DisplayManager.dmGetAspectRatio(), boxHeight), textures[0]);
    this.label.position.y = super.position.y + (boxHeight - (label.size.y - OFFSET)) / 2f;
    super.size = this.box.size;

    this.alignment = alignment;
    if (alignment == LABEL_RIGHT) {
      this.label.position.add(this.box.size.x + OFFSET, 0f);
    } else if (alignment == LABEL_LEFT) {
      super.position.add(this.label.size.x + OFFSET, 0f);
    }
  }

  @Override
  public void increasePosition(float x, float y) {
    super.position.add(x, y);
    this.label.position.add(x, y);
  }

  @Override
  public void setPosition(float x, float y) {
    super.position.set(x, y);
    this.label.position.set(x, y);
    if (alignment == LABEL_RIGHT) {
      this.label.position.add(this.box.size.x + OFFSET, 0f);
    } else if (alignment == LABEL_LEFT) {
      super.position.add(this.label.size.x + OFFSET, 0f);
    }
  }

  @Override
  public void update(float x, float y, boolean mouse) {
    boolean lastHovered = super.hovered;
    boolean lastPressed = super.pressed;

    if (position.x < x && x < position.x + size.x && position.y < y && y < position.y + size.y) {
      super.hovered = true;
      super.pressed = mouse;
    } else {
      super.hovered = false;
      super.pressed = false;
    }

    if (lastPressed && !super.pressed && super.hovered) {
      checked = !checked;
      box.setTexture(textures[checked ? 1 : 0]);
    }

    if (callback != null && (lastHovered != super.hovered || lastPressed != super.pressed)) {
      callback.onActionPerformed(this, super.hovered, super.pressed);

      if (callback instanceof PressedCallback && !lastPressed && super.pressed) {
        ((PressedCallback) callback).onPressed(this, true);
      } else if (lastPressed && !super.pressed && super.hovered) {
        if (callback instanceof PressedCallback) {
          ((PressedCallback) callback).onPressed(this, false);
        } else if (callback instanceof CheckedCallback) {
          ((CheckedCallback) callback).onChecked(this, checked);
        }
      }

    }
  }

  @Override
  public void render() {
    DrawableRenderer.add(box);
    FontRenderer.add(label);
  }

  @Override
  public void animate() {

  }
}
