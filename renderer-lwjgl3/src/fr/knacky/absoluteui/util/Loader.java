/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.util;

import fr.knacky.absoluteui.Model;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.opengl.GL30C.GL_FLOAT;
import static org.lwjgl.opengl.GL30C.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL30C.glDeleteTextures;
import static org.lwjgl.opengl.GL30C.glTexParameterf;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class Loader {
  public static final int VERTEX_POS2D = 0x4;
  public static final int VERTEX_POS2D_TEX2D = 0x5;

  private static ArrayList<Integer> vaos = new ArrayList<>();
  private static ArrayList<Integer> vbos = new ArrayList<>();
  private static ArrayList<Integer> texs = new ArrayList<>();

  public static Model loadToVao(ByteBuffer vertices, int vertexFormat) {
    int vao = createVao();
    int vertexCount = getVertexCount(vertices.limit(), vertexFormat);
    glBindVertexArray(vao);
    storeVertexBuffer(vertices, vertexFormat);
    glBindVertexArray(0);
    return new Model(vao, vertexCount);
  }

  public static int loadTexture(String resource, boolean mipmapping) throws IOException {
    ByteBuffer data;
    int width, height;

    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer components = stack.mallocInt(1);
      data = stbi_load_from_memory(Util.ioResourceToByteBuffer(resource, 1024), w, h, components, 4);
      width = w.get(0);
      height = h.get(0);
    }

    int tex = glGenTextures();
    texs.add(tex);
    glBindTexture(GL_TEXTURE_2D, tex);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    if (mipmapping) {
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.2f);
    } else {
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
    if (mipmapping) {
      glGenerateMipmap(GL_TEXTURE_2D);
    }
    stbi_image_free(data);

    return tex;
  }

  private static int createVao() {
    int vao = glGenVertexArrays();
    vaos.add(vao);
    return vao;
  }

  public static void storeVertexBuffer(ByteBuffer bb, int vertexFormat) {
    int vbo = glGenBuffers();
    vbos.add(vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, bb, GL_STATIC_DRAW);

    if (vertexFormat == VERTEX_POS2D) {
      glEnableVertexAttribArray(0);
      glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0L);
    } else if (vertexFormat == VERTEX_POS2D_TEX2D) {
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * (2 + 2), 0L);
    glEnableVertexAttribArray(1);
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * (2 + 2), 8L);
  } else {
      throw new IllegalArgumentException("Vertex format not found, please use ints from enum 'Loader.VERTEX_'");
    }

    glBindBuffer(GL_ARRAY_BUFFER, 0);
    MemoryUtil.memFree(bb);
  }

  private static int getVertexCount(int bufferSize, int vertexFormat) {
    if (vertexFormat == VERTEX_POS2D) {
      return bufferSize / (4 * 2);
    } else if (vertexFormat == VERTEX_POS2D_TEX2D) {
      return bufferSize / (4 * (2 + 2));
    }

    throw new IllegalArgumentException("Vertex format not found, please use ints from enum 'Loader.VERTEX_'");
  }

  public static void free() {
    for (int vao : vaos) {
      glDeleteVertexArrays(vao);
    }
    for (int vbo : vbos) {
      glDeleteBuffers(vbo);
    }
    for (int tex : texs) {
      glDeleteTextures(tex);
    }
  }

  public static ByteBuffer listToByteBuffer(ArrayList<Float> positions, ArrayList<Float> textureCoords) {
    ByteBuffer bb = MemoryUtil.memAlloc(8 * positions.size());
    for (int i = 0; i < positions.size(); i+=2) {
      bb.putFloat(positions.get(i)).putFloat(positions.get(i + 1));
      bb.putFloat(textureCoords.get(i)).putFloat(textureCoords.get(i + 1));
    }

    bb.flip();
    return bb;
  }
}