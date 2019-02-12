/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import com.sun.istack.internal.Nullable;
import fr.knacky.absoluteui.callback.ActionCallback;
import fr.knacky.absoluteui.callback.PressedCallback;
import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.renderer.DrawableRenderer;
import fr.knacky.absoluteui.renderer.FontRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Button extends ClickableView {
  public final Drawable background;
  public final Text label;

  public Button(Vector2f position, Vector2f preferedSize, String text, @Nullable ActionCallback callback) {
    super(position, null, callback);
    this.label = new Text(new Vector2f(position), text, new Vector3f(1.0f, 1.0f, 1.0f));

    if (preferedSize.x < this.label.size.x) preferedSize.x = this.label.size.x;
    if (preferedSize.y < this.label.size.y) preferedSize.y = this.label.size.y;
    super.size = preferedSize;

    this.label.position.x = super.position.x + (super.size.x - this.label.size.x) / 2f;
    this.label.position.y = super.position.y + (super.size.y - this.label.size.y) / 2f;

    this.background = new Drawable(super.position, super.size, new Vector3f(0.7f, 0.7f, 0.7f));
  }

  public Button(Vector2f position, Vector2f preferedSize, String text, FontType font, float fontSize, @Nullable ActionCallback callback) {
    super(position, null, callback);
    this.label = new Text(new Vector2f(position), font, fontSize, text, new Vector3f(1.0f, 1.0f, 1.0f));

    if (preferedSize.x < this.label.getSize().x) preferedSize.x = this.label.getSize().x;
    if (preferedSize.y < this.label.getSize().y) preferedSize.y = this.label.getSize().y;
    super.size = preferedSize;

    this.label.position.x = super.position.x + (super.size.x - this.label.size.x) / 2f;
    this.label.position.y = super.position.y + (super.size.y - this.label.size.y) / 2f;

    this.background = new Drawable(super.position, super.size, new Vector3f(0.7f, 0.7f, 0.7f));
  }

  @Override
  public void increasePosition(float x, float y) {
    super.position.add(x, y);
    this.label.position.add(x, y);
  }

  @Override
  public void setPosition(float x, float y) {
    super.position.set(x, y);
    this.label.position.x = super.position.x + (super.size.x - this.label.size.x) / 2f;
    this.label.position.y = super.position.y + (super.size.y - this.label.size.y) / 2f;
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

    if (callback != null && (lastHovered != super.hovered || lastPressed != super.pressed)) {
      callback.onActionPerformed(this, super.hovered, super.pressed);

      if (callback instanceof PressedCallback) {
        if (!lastPressed && super.pressed) {
          ((PressedCallback) callback).onPressed(this, true);
        } else if (lastPressed && !super.pressed && super.hovered) {
          ((PressedCallback) callback).onPressed(this, false);
        }
      }
    }
  }

  @Override
  public void render() {
    DrawableRenderer.add(background);
    FontRenderer.add(label);
  }

  @Override
  public void animate() {

  }
}
