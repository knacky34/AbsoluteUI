/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.renderer.DrawableRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Drawable extends View {
  private Vector3f color;
  private int texture = 0;

  public Drawable(Vector2f position, Vector2f size, Vector3f color) {
    super(position, size);
    this.color = color;
  }

  public Drawable(Vector2f position, Vector2f size, int texture) {
    super(position, size);
    this.texture = texture;
  }

  public Vector3f getColor() {
    return color;
  }

  public int getTexture() {
    return texture;
  }


  public void setColor(float x, float y, float z) {
    if (this.color != null) {
      this.color.set(x, y, z);
    } else {
      this.color = new Vector3f(x, y, z);
    }
    this.texture = 0;
  }

  public void setColor(Vector3f color) {
    this.color = color;
    this.texture = 0;
  }

  public void setTexture(int texture) {
    this.color = null;
    this.texture = texture;
  }


  @Override
  public void render() {
    DrawableRenderer.add(this);
  }

  @Override
  public void animate() {

  }
}
