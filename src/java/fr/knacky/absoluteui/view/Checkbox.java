/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.Resources;
import fr.knacky.absoluteui.TextureAtlas;
import fr.knacky.absoluteui.callback.ActionCallback;
import fr.knacky.absoluteui.callback.CheckCallback;
import fr.knacky.absoluteui.callback.PressCallback;
import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.renderer.DrawableRenderer;
import fr.knacky.absoluteui.renderer.FontRenderer;
import java.io.IOException;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Checkbox extends ClickableView {
  public static final int LABEL_LEFT = 1;
  public static final int LABEL_RIGHT = 2;
  private static final float OFFSET = 0.01f;

  private static TextureAtlas texture;

  private boolean checked = false;
  private int alignment = 0;

  public final Text label;
  public final Drawable box;

  static {
    texture = Resources.textures.atlas;
  }

  public Checkbox(Vector2f position, String text, ActionCallback callback, int alignment) {
    this(position, text, Resources.font.font, Resources.font.size, callback, alignment);
  }

  public Checkbox(Vector2f position, String text, FontType font, float fontSize, ActionCallback callback, int alignment) {
    super(position, new Vector2f(), callback);
    this.label = new Text(new Vector2f(position), text, font, fontSize, new Vector3f(1.0f, 1.0f, 1.0f));
    this.box = new Drawable(super.position, calculateBoxSize(this.label.size.y - OFFSET), texture, 0);

    this.alignment = alignment;
    calculateAlignment();
  }

  public Checkbox(Vector2f position, float boxHeight, ActionCallback callback) {
    super(position, new Vector2f(), callback);
    this.label = null;
    this.box = new Drawable(super.position, calculateBoxSize(boxHeight), texture, 0);
  }


  public boolean isChecked() {
    return checked;
  }


  @Override
  public void increasePosition(float x, float y) {
    super.position.add(x, y);
    if (label != null) {
      label.position.add(x, y);
    }
  }

  @Override
  public void setPosition(float x, float y) {
    super.position.set(x, y);
    if (label != null) {
      label.position.set(x, y);
      calculateAlignment();
    }
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }


  private void calculateAlignment() {
    if (alignment == LABEL_RIGHT) {
      label.position.set(super.position);
      label.position.add(box.size.x + OFFSET, 0f);
    } else if (alignment == LABEL_LEFT) {
      super.position.set(label.position);
      super.position.add(label.size.x + OFFSET, 0f);
    }
  }

  private Vector2f calculateBoxSize(float boxHeight) {
    return super.size.set(boxHeight / Gui.abuiGetAspectRatio(), boxHeight);
  }


  @Override
  public void update(Gui gui, float x, float y, boolean mouse) {
    boolean lastHovered = super.hovered;
    boolean lastPressed = super.pressed;

    super.update(gui, x, y, mouse);

    if (lastPressed && !super.pressed && super.hovered) {
      checked = !checked;
      box.setTextureIndex(checked ? 1 : 0);
    }

    if (callback != null && (lastHovered != super.hovered || lastPressed != super.pressed)) {
      callback.onActionPerformed(this, super.hovered, super.pressed);

      if (callback instanceof PressCallback) {
        if (!lastPressed && super.pressed) {
          ((PressCallback) callback).onPressed(this, true);
        } else if (lastPressed && !super.pressed && super.hovered) {
          ((PressCallback) callback).onPressed(this, false);
        }
      } else if (callback instanceof CheckCallback) {
        if (lastPressed && !super.pressed && super.hovered) {
          ((CheckCallback) callback).onChecked(this, checked);
        }
      }

    }
  }

  @Override
  public void render() {
    DrawableRenderer.add(box);
    if (label != null) {
      FontRenderer.add(label);
    }
  }

  @Override
  public void resize() {
    if (label != null) {
      label.resize();
      calculateBoxSize(label.size.y - OFFSET);
      calculateAlignment();
    } else {
      calculateBoxSize(box.size.y);
    }
  }

  @Override
  public void animate() {

  }
}
