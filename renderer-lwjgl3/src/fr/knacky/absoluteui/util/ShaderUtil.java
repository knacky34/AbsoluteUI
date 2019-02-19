/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL30C.*;

public class ShaderUtil {
  private static FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);

  public static void createShaderProgram(String vshaderPath, String fshaderPath, IntBuffer buffer, String... attribs) throws IOException {
    buffer.put(0, glCreateProgram());
    buffer.put(1, createShader(vshaderPath, GL_VERTEX_SHADER));
    buffer.put(2, createShader(fshaderPath, GL_FRAGMENT_SHADER));

    glAttachShader(buffer.get(0), buffer.get(1));
    glAttachShader(buffer.get(0), buffer.get(2));

    for (int i = 0; i < attribs.length; i++) {
      glBindAttribLocation(buffer.get(0), i, attribs[i]);
    }

    glLinkProgram(buffer.get(0));
    if (glGetProgrami(buffer.get(0), GL_LINK_STATUS) == 0) {
      System.err.println("\nCould not link program");
      System.err.println(glGetProgramInfoLog(buffer.get(0)));
      System.exit(1);
    }
  }

  private static int createShader(String resource, int type) throws IOException {
    int shader = glCreateShader(type);

    ByteBuffer source = Util.ioResourceToByteBuffer(resource);

    try (MemoryStack stack = MemoryStack.stackPush()) {
      PointerBuffer strings = stack.mallocPointer(1);
      IntBuffer lengths = stack.mallocInt(1);
      strings.put(0, source);
      lengths.put(0, source.remaining());

      glShaderSource(shader, strings, lengths);
    }
    glCompileShader(shader);

    if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
      System.err.println("\nCould not compile shader : " + resource);
      System.err.println(glGetShaderInfoLog(shader));
      System.exit(1);
    }

    return shader;
  }

  public static void loadUniform1f(int uniform, float x) {
    glUniform1f(uniform, x);
  }

  public static void loadUniform2f(int uniform, float x, float y) {
    glUniform2f(uniform, x, y);
  }

  public static void loadUniform2f(int uniform, Vector2f vector2f) {
    glUniform2f(uniform, vector2f.x, vector2f.y);
  }

  public static void loadUniform3f(int uniform, float x, float y, float z) {
    glUniform3f(uniform, x, y, z);
  }

  public static void loadUniform3f(int uniform, Vector3f vector3f) {
    glUniform3f(uniform, vector3f.x, vector3f.y, vector3f.z);
  }

  public static void loadUniform4f(int uniform, float x, float y, float z, float w) {
    glUniform4f(uniform, x, y, z, w);
  }

  public static void loadUniformMatrix4f(int uniform, Matrix4f matrix4f) {
    glUniformMatrix4fv(uniform, false, matrix4f.get(matrixBuffer));
  }

  public static void free() {
    MemoryUtil.memFree(matrixBuffer);
  }
}