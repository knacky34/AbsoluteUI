/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import com.sun.istack.internal.Nullable;
import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.callback.ActionCallback;
import org.joml.Vector2f;

public abstract class ClickableView extends View {
  private boolean lastMouse = false;

  protected boolean hovered = false;
  protected boolean pressed = false;
  protected boolean focused = false;

  protected ActionCallback callback;

  public ClickableView(Vector2f position, Vector2f size, @Nullable ActionCallback callback) {
    super(position, size);
    this.callback = callback;
  }

  public void update(Gui gui, float x, float y, boolean mouse) {
    if (super.position.x < x && x < super.position.x + super.size.x && super.position.y < y && y < super.position.y + super.size.y) {
      hovered = true;
      pressed = mouse;
      if (mouse) {
        focused = true;
      }
    } else {
      hovered = false;
      pressed = false;
      if (mouse && !lastMouse) {
        focused = false;
      }
    }

    lastMouse = mouse;
  }


  public boolean isHovered() {
    return hovered;
  }

  public boolean isPressed() {
    return pressed;
  }

  public boolean isFocused() {
    return focused;
  }


  public void setHovered(boolean hovered) {
    this.hovered = hovered;
  }

  public void setPressed(boolean pressed) {
    this.pressed = pressed;
  }

  public void setFocused(boolean focused) {
    this.focused = focused;
  }

  public void setCallback(ActionCallback callback) {
    this.callback = callback;
  }
}
