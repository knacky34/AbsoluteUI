/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.Resources;
import fr.knacky.absoluteui.callback.ActionCallback;
import fr.knacky.absoluteui.callback.PressCallback;
import fr.knacky.absoluteui.callback.TextChangeCallback;
import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.renderer.DrawableRenderer;
import fr.knacky.absoluteui.renderer.MaskFontRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Textfield extends ClickableView {
  private static final float OFFSET = 0.01f;

  public final Drawable background;
  public final Text input;
  private final Drawable cursor;

  private final Vector2f preferedSize;
  private StringBuilder text = new StringBuilder();
  private int cursorIndex = 0;

  public Textfield(Vector2f position, Vector2f preferedSize, ActionCallback callback) {
    this(position, preferedSize, Resources.font.font, Resources.font.size, callback);
  }

  public Textfield(Vector2f position, Vector2f preferedSize, FontType font, float fontSzie, ActionCallback callback) {
    super(position, new Vector2f(), callback);
    this.input = new Text(new Vector2f(), "", font, fontSzie, new Vector3f(1.0f, 1.0f, 1.0f));
    this.background = new Drawable(super.position, super.size, new Vector3f(0.7f, 0.7f, 0.7f));

    this.preferedSize = preferedSize;
    computeSizeAndInputPos();
    this.cursor = new Drawable(new Vector2f(input.position.x, input.position.y + input.size.y - (input.size.y * 0.25f)), new Vector2f(0.025f / Gui.abuiGetAspectRatio(), 0.005f), new Vector3f(1.0f, 1.0f, 1.0f));
  }


  private void computeSizeAndInputPos() {
    super.size.set(preferedSize);
    if (preferedSize.y < input.size.y) super.size.y = input.size.y;

    input.position.x = super.position.x + OFFSET;
    input.position.y = super.position.y + (super.size.y - input.size.y) / 2f;
  }

  private void processTextUpdate(boolean t, boolean c) {
    if (t) {
      input.derive(text.toString());
      if (input.size.x > super.size.x) {
        if (cursorIndex == text.length()) {
          input.position.x = super.position.x - input.size.x + super.size.x;
        }
      } else {
        input.position.x = super.position.x + OFFSET;
      }
    }
    if (c) {
      if (cursorIndex == text.length()) {
        cursor.position.x = input.position.x + input.size.x;
      } else {
        float shift = input.getFont().computeTextWidth(text.substring(0, cursorIndex), input.getFontSize());
        cursor.position.x = input.position.x + shift;
      }

      if (cursor.position.x < super.position.x) {
        float shift = super.position.x - cursor.position.x;
        cursor.position.x += shift;
        input.position.x += shift;
      } else if (cursor.position.x > super.position.x + super.size.x) {
        float shift = cursor.position.x - super.position.x - super.size.x;
        cursor.position.x -= shift;
        input.position.x -= shift;
      }
    }
  }


  @Override
  public void update(Gui gui, float x, float y, boolean mouse) {
    boolean lastHovered = super.hovered;
    boolean lastPressed = super.pressed;

    super.update(gui, x, y, mouse);

    boolean textChanged = false;
    if (super.focused) {
      boolean cursorChanged = false;

      if (gui.chars != null) {
        text.insert(cursorIndex, gui.chars);
        cursorIndex += gui.chars.length;
        textChanged = true;
        cursorChanged = true;
      }

      if (text.length() > 0) {
        if ((gui.key_backspace == Gui.KEY_PRESS || gui.key_backspace == Gui.KEY_REPEAT) && cursorIndex > 0) {
          text.deleteCharAt(cursorIndex - 1);
          cursorIndex--;
          textChanged = true;
          cursorChanged = true;
        } else if ((gui.key_delete == Gui.KEY_PRESS || gui.key_delete == Gui.KEY_REPEAT) && cursorIndex < text.length()) {
          text.deleteCharAt(cursorIndex);
          textChanged = true;
          cursorChanged = true;
        } else if ((gui.key_left == Gui.KEY_PRESS || gui.key_left == Gui.KEY_REPEAT) && cursorIndex > 0) {
          cursorIndex--;
          cursorChanged = true;
        } else if ((gui.key_right == Gui.KEY_PRESS || gui.key_right == Gui.KEY_REPEAT) && cursorIndex < text.length()) {
          cursorIndex++;
          cursorChanged = true;
        }
      }

      processTextUpdate(textChanged, cursorChanged);
      cursor.setVisible(gui.tick);
    } else {
      cursor.setVisible(false);
    }

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
    if (textChanged && callback instanceof TextChangeCallback) {
      ((TextChangeCallback) callback).onTextChanged(this, text.toString());
    }
  }

  @Override
  public void render() {
    DrawableRenderer.add(background);
    if (cursor.isVisible()) {
      DrawableRenderer.add(cursor);
    }
    MaskFontRenderer.add(input, background);
  }

  @Override
  public void resize() {
    input.resize();
    computeSizeAndInputPos();
    cursor.getSize().set(0.025f / Gui.abuiGetAspectRatio(), 0.005f);
  }

  @Override
  public void animate() {

  }
}
