/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.Model;
import fr.knacky.absoluteui.Resources;
import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.font.MetaFile;
import fr.knacky.absoluteui.font.TextMeshData;
import fr.knacky.absoluteui.renderer.FontRenderer;
import fr.knacky.absoluteui.util.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20C;

public class Text extends View {
  private FontType font;
  private Model model;
  private int vbo;
  private float fontSize;
  private float sx, sy;

  private Vector3f color;
  public float width = 0.4f, edge = 0.04f, borderWidth = 0.0f, borderEdge = 0.1f;

  private Vector3f outlineColor = new Vector3f(1.0f, 1.0f, 1.0f);
  private Vector2f offset = new Vector2f(0.0f, 0.0f);

  public Text(Vector2f position, String text, Vector3f color) {
    this(position, text, Resources.font.font, Resources.font.size, color);
  }

  public Text(Vector2f position, String text, FontType font, float fontSize, Vector3f color) {
    super(position, new Vector2f());
    this.font = font;
    this.fontSize = Math.max(1f, Math.min(50f, fontSize));
    this.color = color;

    calculateWidthAndEdgeValues();
    loadText(text, true);
  }


  public FontType getFont() {
    return font;
  }

  public float getFontSize() {
    return fontSize;
  }

  public Model getModel() {
    return model;
  }

  public Vector3f getColor() {
    return color;
  }

  public Vector3f getOutlineColor() {
    return outlineColor;
  }

  public Vector2f getOffset() {
    return offset;
  }


  public void setFontSize(float fontSize) {
    this.fontSize = fontSize;
    calculateSize();
  }

  public void setColor(float x, float y, float z) {
    this.color.set(x, y, z);
  }

  public void setColor(Vector3f color) {
    this.color = color;
  }

  public void setOutlineColor(float x, float y, float z) {
    this.outlineColor.set(x, y, z);
  }

  public void setOutlineColor(Vector3f outlineColor) {
    this.outlineColor = outlineColor;
  }

  public void setOffset(float x, float y) {
    this.offset.set(x, y);
  }

  public void setOffset(Vector2f offset) {
    this.offset = offset;
  }


  public void derive(String text) {
    loadText(text, false);
  }

  public void derive(String text, FontType font) {
    this.font = font;
    loadText(text, false);
  }

  public void calculateWidthAndEdgeValues() {
    Vector4f widthEdge = font.getWidthEdge();
    //width calculation
    width = (float) (Math.pow((fontSize - 1f), 0.25f) * (1f / 15f) + widthEdge.x);
    width = Math.min(width, widthEdge.y);

    //edge calculation
    edge = widthEdge.z / fontSize;
    edge = Math.max(edge, widthEdge.w);
  }

  private void calculateSize() {
    super.size.set(this.sx * this.fontSize / Gui.abuiGetAspectRatio(), this.sy * this.fontSize);
  }

  private void loadText(String text, boolean init) {
    TextMeshData data = font.loadText(text);
    if (init) {
      model = Loader.loadToVao(data.data, Loader.VERTEX_POS2D_TEX2D);
      vbo = Loader.getLastVboID();
    } else {
      Loader.updateVbo(vbo, data.data, GL20C.GL_STATIC_DRAW);
      model.setVertexCount(Loader.getVertexCount(data.data.limit(), Loader.VERTEX_POS2D_TEX2D));
    }

    this.sx = data.sx;
    this.sy = data.sy;
    calculateSize();
  }


  @Override
  public void render() {
    FontRenderer.add(this);
  }

  @Override
  public void resize() {
    calculateSize();
    calculateWidthAndEdgeValues();
  }

  @Override
  public void animate() {

  }
}
