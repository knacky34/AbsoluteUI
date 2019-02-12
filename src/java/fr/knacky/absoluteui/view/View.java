/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.motion.Motionable;
import org.joml.Vector2f;

public abstract class View implements Motionable {
  protected Vector2f position;
  protected Vector2f size;

  protected boolean visible = true;

  public View(Vector2f position, Vector2f size) {
    this.position = position;
    this.size = size;
  }

  public abstract void render();


  public Vector2f getPosition() {
    return position;
  }

  public Vector2f getSize() {
    return size;
  }

  public boolean isVisible() {
    return visible;
  }


  public void increasePosition(float x, float y) {
    this.position.add(x, y);
  }

  public void setPosition(float x, float y) {
    this.position.set(x, y);
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }
}
