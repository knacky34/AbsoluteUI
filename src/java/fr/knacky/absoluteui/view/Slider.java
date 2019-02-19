/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.callback.ActionCallback;
import fr.knacky.absoluteui.callback.PressCallback;
import fr.knacky.absoluteui.callback.SlideCallback;
import fr.knacky.absoluteui.renderer.DrawableRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Slider extends ClickableView {
  public final Drawable background;
  public final Drawable control;

  private float min = 0f, max = 10f, step = 1f;
  private float value = min;
  private float maxMin = max - min;

  public Slider(Vector2f position, Vector2f size, ActionCallback callback) {
    super(position, size, callback);
    this.background = new Drawable(position, size, new Vector3f(0.7f, 0.7f, 0.7f));
    this.control = new Drawable(new Vector2f(position), new Vector2f(), new Vector3f(0.5f, 0.5f, 0.5f));
    calculateBoxSize(size.y);
    super.position.y += size.y / 4f;
    super.size.y = size.y / 2f;
  }

  public Slider(Vector2f position, Vector2f size, ActionCallback callback, float min, float max, float step) {
    this(position, size, callback, min, max, step, min);
  }

  public Slider(Vector2f position, Vector2f size, ActionCallback callback, float min, float max, float step, float value) {
    this(position, size, callback);
    this.min = min;
    this.max = max;
    this.step = step;
    this.value = value;
    this.maxMin = max - min;
  }


  public float getMin() {
    return min;
  }

  public float getMax() {
    return max;
  }

  public float getStep() {
    return step;
  }

  public float getValue() {
    return value;
  }


  public void setMin(float min) {
    this.min = min;
  }

  public void setMax(float max) {
    this.max = max;
    this.maxMin = max - min;
  }

  public void setStep(float step) {
    this.step = step;
  }

  public void setValue(float value) {
    this.value = step * (int) (value / step);
  }


  private void calculateBoxSize(float boxHeight) {
    control.size.set(boxHeight / Gui.abuiGetAspectRatio(), boxHeight);
  }


  @Override
  public void update(Gui gui, float x, float y, boolean mouse) {
    boolean lastHovered = super.hovered;
    boolean lastPressed = super.pressed;
    float lastValue = this.value;

    super.update(gui, x, y, mouse);

    if (super.focused && mouse) {
      float pos = (x - position.x) / size.x + size.x / (maxMin / step);
      this.value = step * (int) ((maxMin * pos + min) / step);
      this.value = Math.max(min, Math.min(max, value));
      control.position.x = position.x - control.size.x / 2 + size.x * (value - min) / maxMin;
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
    if (callback instanceof SlideCallback && lastValue != this.value) {
      ((SlideCallback) callback).onValueChanged(this, this.value);
    }
  }

  @Override
  public void render() {
    DrawableRenderer.add(background);
    DrawableRenderer.add(control);
  }

  @Override
  public void resize() {
    calculateBoxSize(control.size.y);
    control.position.x = position.x - control.size.x / 2 + size.x * (value - min) / maxMin;
  }

  @Override
  public void animate() {

  }
}