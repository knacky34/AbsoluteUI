/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.renderer;

import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.view.Text;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.system.MemoryStack;

import static fr.knacky.absoluteui.util.ShaderUtil.*;
import static org.lwjgl.opengl.GL30C.*;

public class FontRenderer {
  private static HashMap<FontType, ArrayList<Text>> texts = new HashMap<>();

  private int programID;
  private int vshaderID;
  private int fshaderID;

  private int uniform_transform;
  private int uniform_color;
  private int uniform_widthsAndEdges;
  private int uniform_outlineColor;
  private int uniform_offset;
  private int uniform_fontAtlas;

  public FontRenderer() throws IOException {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer ip = stack.mallocInt(3);

      createShaderProgram("shaders/font-vs.glsl", "shaders/font-fs.glsl", ip, "position", "textureCoord");
      programID = ip.get(0);
      vshaderID = ip.get(1);
      fshaderID = ip.get(2);
    }
    initShaderProgram();
  }

  private void initShaderProgram() {
    uniform_transform = glGetUniformLocation(programID, "transform");
    uniform_color = glGetUniformLocation(programID, "color");
    uniform_widthsAndEdges = glGetUniformLocation(programID, "widthsAndEdges");
    uniform_outlineColor = glGetUniformLocation(programID, "outlineColor");
    uniform_offset = glGetUniformLocation(programID, "offset");
    uniform_fontAtlas =glGetUniformLocation(programID, "fontAtlas");
    glUseProgram(programID);
    glUniform1i(uniform_fontAtlas, 0);
    glUseProgram(0);
  }

  public void render() {
    render(texts);
  }

  public void render(HashMap<FontType, ArrayList<Text>> textsList) {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glDisable(GL_DEPTH_TEST);
    glUseProgram(programID);

    for (FontType font : textsList.keySet()) {
      glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
      for (Text text : textsList.get(font)) {
        glBindVertexArray(text.getModel().getVaoID());
        loadUniform4f(uniform_transform, text.getPosition().x, text.getPosition().y, text.getFontSize() / Gui.abuiGetAspectRatio(), text.getFontSize());
        loadUniform3f(uniform_color, text.getColor());
        loadUniform4f(uniform_widthsAndEdges, text.width, text.edge, text.borderWidth, text.borderEdge);
        loadUniform3f(uniform_outlineColor, text.getOutlineColor());
        loadUniform2f(uniform_offset, text.getOffset());

        glDrawArrays(GL_TRIANGLES, 0, text.getModel().getVertexCount());
      }
    }

    glBindVertexArray(0);
    glBindTexture(GL_TEXTURE_2D, 0);
    glUseProgram(0);
    glDisable(GL_BLEND);
    glEnable(GL_DEPTH_TEST);
  }

  public void free() {
    glUseProgram(0);
    glDeleteShader(vshaderID);
    glDeleteShader(fshaderID);
    glDeleteProgram(programID);
  }

  public static void clear() {
    texts.clear();
  }

  public static void add(Text text) {
    FontType font = text.getFont();
    ArrayList<Text> batch = texts.get(font);
    if (batch != null) {
      batch.add(text);
    } else {
      ArrayList<Text> newBatch = new ArrayList<>();
      newBatch.add(text);
      texts.put(font, newBatch);
    }
  }

  public static void add(ArrayList<Text> textList) {
    for (Text text : textList) {
      add(text);
    }
  }

}
