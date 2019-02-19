/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.TextureAtlas;
import fr.knacky.absoluteui.renderer.DrawableRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Drawable extends View {
  private Vector3f color;
  private TextureAtlas texture;
  private int textureIndex;

  public Drawable(Vector2f position, Vector2f size, Vector3f color) {
    super(position, size);
    this.color = color;
  }

  public Drawable(Vector2f position, Vector2f size, TextureAtlas texture, int textureIndex) {
    super(position, size);
    this.texture = texture;
    this.textureIndex = textureIndex;
  }

  public Vector3f getColor() {
    return color;
  }

  public TextureAtlas getTexture() {
    return texture;
  }

  public int getTextureIndex() {
    return textureIndex;
  }


  public void setColor(float x, float y, float z) {
    if (this.color != null) {
      this.color.set(x, y, z);
    } else {
      this.color = new Vector3f(x, y, z);
    }
    this.texture = null;
  }

  public void setColor(Vector3f color) {
    this.color = color;
    this.texture = null;
  }

  public void setTexture(TextureAtlas texture, int textureIndex) {
    this.color = null;
    this.texture = texture;
    this.textureIndex = textureIndex;
  }

  public void setTextureIndex(int textureIndex) {
    this.textureIndex = textureIndex;
  }


  @Override
  public void render() {
    DrawableRenderer.add(this);
  }

  @Override
  public void resize() {

  }

  @Override
  public void animate() {

  }
}
