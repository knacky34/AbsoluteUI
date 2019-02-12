/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.renderer;

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

  private int uniform_translation;
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
    uniform_translation = glGetUniformLocation(programID, "translation");
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
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glDisable(GL_DEPTH_TEST);
    glUseProgram(programID);

    for (FontType font : texts.keySet()) {
      glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
      for (Text text : texts.get(font)) {
        glBindVertexArray(text.getModel().getVaoID());
        loadUniformVector2f(uniform_translation, text.getPosition());
        loadUniformVector3f(uniform_color, text.getColor());
        loadUniformVector4f(uniform_widthsAndEdges, text.width, text.edge, text.borderWidth, text.borderEdge);
        loadUniformVector3f(uniform_outlineColor, text.getOutlineColor());
        loadUniformVector2f(uniform_offset, text.getOffset());

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
  }

}
