/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import com.sun.istack.internal.Nullable;
import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.Resources;
import fr.knacky.absoluteui.callback.ActionCallback;
import fr.knacky.absoluteui.callback.PressCallback;
import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.renderer.DrawableRenderer;
import fr.knacky.absoluteui.renderer.FontRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Button extends ClickableView {
  public final Drawable background;
  public final Text label;

  private final Vector2f preferedSize;

  public Button(Vector2f position, @Nullable Vector2f preferedSize, String text, @Nullable ActionCallback callback) {
    this(position, preferedSize, text, Resources.font.font, Resources.font.size, callback, true);
  }

  public Button(Vector2f position, @Nullable Vector2f preferedSize, String text, FontType font, float fontSize, @Nullable ActionCallback callback) {
    this(position, preferedSize, text, font, fontSize, callback, true);
  }

  public Button(Vector2f position, @Nullable Vector2f preferedSize, String text, FontType font, float fontSize, @Nullable ActionCallback callback, boolean background) {
    super(position, new Vector2f(), callback);
    this.label = new Text(new Vector2f(), text, font, fontSize, new Vector3f(1.0f, 1.0f, 1.0f));
    if (background) {
      this.background = new Drawable(super.position, super.size, new Vector3f(0.7f, 0.7f, 0.7f));
      this.preferedSize = preferedSize;
    } else {
      this.background = null;
      this.preferedSize = null;
    }

    computeSizeAndLabelPos();
  }


  @Override
  public void increasePosition(float x, float y) {
    super.position.add(x, y);
    label.position.add(x, y);
  }

  @Override
  public void setPosition(float x, float y) {
    super.position.set(x, y);
    label.position.x = super.position.x + (super.size.x - label.size.x) / 2f;
    label.position.y = super.position.y + (super.size.y - label.size.y) / 2f;
  }


  private void computeSizeAndLabelPos() {
    if (preferedSize == null) {
      super.size.set(label.size);
      label.position.set(super.position);
      return;
    }

    super.size.set(preferedSize);
    if (preferedSize.x < label.size.x) super.size.x = label.size.x;
    if (preferedSize.y < label.size.y) super.size.y = label.size.y;

    label.position.x = super.position.x + (super.size.x - label.size.x) / 2f;
    label.position.y = super.position.y + (super.size.y - label.size.y) / 2f;
  }


  @Override
  public void update(Gui gui, float x, float y, boolean mouse) {
    boolean lastHovered = super.hovered;
    boolean lastPressed = super.pressed;

    super.update(gui, x, y, mouse);

    if (callback != null && (lastHovered != super.hovered || lastPressed != super.pressed)) {
      callback.onActionPerformed(this, super.hovered, super.pressed);

      if (callback instanceof PressCallback) {
        if (!lastPressed && super.pressed) {
          ((PressCallback) callback).onPressed(this, true);
        } else if (lastPressed && !super.pressed && super.hovered) {
          ((PressCallback) callback).onPressed(this, false);
        }
      }
    }
  }

  @Override
  public void render() {
    if (background != null) {
      DrawableRenderer.add(background);
    }
    FontRenderer.add(label);
  }

  @Override
  public void resize() {
    label.resize();
    computeSizeAndLabelPos();
  }

  @Override
  public void animate() {

  }
}
