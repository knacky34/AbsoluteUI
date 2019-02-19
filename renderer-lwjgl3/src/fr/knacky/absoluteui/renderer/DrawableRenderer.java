/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.renderer;

import fr.knacky.absoluteui.Model;
import fr.knacky.absoluteui.TextureAtlas;
import fr.knacky.absoluteui.view.Drawable;
import fr.knacky.absoluteui.util.Loader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static fr.knacky.absoluteui.util.ShaderUtil.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

public class DrawableRenderer {
  private static Model model;
  private static HashMap<TextureAtlas, ArrayList<Drawable>> drawables = new HashMap<>();

  private int programID;
  private int vshaderID;
  private int fshaderID;

  private int uniform_transfrom;
  private int uniform_color;
  private int uniform_textureCoord;
  private int uniform_texture;

  static {
    createModel();
  }

  public DrawableRenderer() throws IOException {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer ip = stack.mallocInt(3);

      createShaderProgram("shaders/drawable-vs.glsl", "shaders/drawable-fs.glsl", ip, "position");
      programID = ip.get(0);
      vshaderID = ip.get(1);
      fshaderID = ip.get(2);
    }
    initShaderProgram();
  }

  private void initShaderProgram() {
    uniform_transfrom = glGetUniformLocation(programID, "transform");
    uniform_color = glGetUniformLocation(programID, "color");
    uniform_textureCoord = glGetUniformLocation(programID, "textureCoord");
    uniform_texture = glGetUniformLocation(programID, "tex");
    glUseProgram(programID);
    glUniform1i(uniform_texture, 0);
    glUseProgram(0);
  }

  public void render() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glDisable(GL_DEPTH_TEST);
    glUseProgram(programID);
    glBindVertexArray(model.getVaoID());

    for (TextureAtlas texture : drawables.keySet()) {
      if (texture == null) {
        loadUniform3f(uniform_textureCoord, -1f, 0f, 0f);
      } else {
        glBindTexture(GL_TEXTURE_2D, texture.getTexture());
      }

      for (Drawable drawable : drawables.get(texture)) {
        if (texture != null) {
          loadUniform3f(uniform_color, -1f, 0f, 0f);
          loadUniform3f(uniform_textureCoord, texture.getX(drawable.getTextureIndex()), texture.getY(drawable.getTextureIndex()), texture.getSize());
        } else {
          loadUniform3f(uniform_color, drawable.getColor());
        }

        loadUniform4f(uniform_transfrom, drawable.getPosition().x, drawable.getPosition().y, drawable.getSize().x, drawable.getSize().y);

        glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
      }
    }

    glBindTexture(GL_TEXTURE_2D, 0);
    glBindVertexArray(0);
    glUseProgram(0);
    glDisable(GL_BLEND);
    glEnable(GL_DEPTH_TEST);
  }

  public void renderToStencil(ArrayList<Drawable> drawablesList) {
    glUseProgram(programID);
    glBindVertexArray(model.getVaoID());
    loadUniform3f(uniform_color, 1.0f, 1.0f, 1.0f);

    for (Drawable drawable : drawablesList) {
      loadUniform4f(uniform_transfrom, drawable.getPosition().x, drawable.getPosition().y, drawable.getSize().x, drawable.getSize().y);
      glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
    }

    glBindVertexArray(0);
    glUseProgram(0);
  }

  public void free() {
    glUseProgram(0);
    glDeleteShader(vshaderID);
    glDeleteShader(fshaderID);
    glDeleteProgram(programID);
  }

  private static void createModel() {
    ByteBuffer positionsBb = MemoryUtil.memAlloc(4 * 12);
    positionsBb.putFloat(0f).putFloat(0f);
    positionsBb.putFloat(1f).putFloat(1f);
    positionsBb.putFloat(0f).putFloat(1f);
    positionsBb.putFloat(0f).putFloat(0f);
    positionsBb.putFloat(1f).putFloat(0f);
    positionsBb.putFloat(1f).putFloat(1f);
    positionsBb.flip();

    DrawableRenderer.model = Loader.loadToVao(positionsBb, Loader.VERTEX_POS2D);
    MemoryUtil.memFree(positionsBb);
  }

  public static void clear() {
    drawables.clear();
  }

  public static void add(Drawable drawable) {
    TextureAtlas texture = drawable.getTexture();
    ArrayList<Drawable> batch = drawables.get(texture);
    if (batch != null) {
      batch.add(drawable);
    } else {
      ArrayList<Drawable> newBatch = new ArrayList<>();
      newBatch.add(drawable);
      drawables.put(texture, newBatch);
    }
  }

  public static void add(ArrayList<Drawable> drawableList) {
    for (Drawable drawable : drawableList) {
      add(drawable);
    }
  }
}
