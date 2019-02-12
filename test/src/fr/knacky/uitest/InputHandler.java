package fr.knacky.uitest;

import java.nio.DoubleBuffer;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.system.MemoryUtil;

import static fr.knacky.uitest.DisplayManager.dmGetHeight;
import static fr.knacky.uitest.DisplayManager.dmGetWidth;
import static fr.knacky.uitest.DisplayManager.dmGetWindow;
import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
  private GLFWKeyCallback keyCallback;
  private GLFWMouseButtonCallback mouseBtnCallback;
  private DoubleBuffer pos = MemoryUtil.memAllocDouble(2);
  private double lastPosX = 0d, lastPosY = 0d;

  private boolean[] keyDown = new boolean[GLFW_KEY_LAST + 1];
  private boolean[] mouseDown = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];
  private double mouseDX, mouseDY;

  public InputHandler() {
    glfwSetKeyCallback(dmGetWindow(), keyCallback = new GLFWKeyCallback() {
      @Override
      public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_UNKNOWN) {
          return;
        }
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
          glfwSetWindowShouldClose(window, true);
        }
        if (key == GLFW_KEY_F3) {
          if (action == GLFW_PRESS) GL11C.glPolygonMode(GL11C.GL_FRONT_AND_BACK, GL11C.GL_LINE);
          else if (action == GLFW_RELEASE) GL11C.glPolygonMode(GL11C.GL_FRONT_AND_BACK, GL11C.GL_FILL);
        }
        keyDown[key] = action == GLFW_PRESS || action == GLFW_REPEAT;
      }
    });

    glfwSetMouseButtonCallback(dmGetWindow(), mouseBtnCallback = new GLFWMouseButtonCallback() {
      @Override
      public void invoke(long window, int button, int action, int mods) {
        mouseDown[button] = action == GLFW_PRESS;
      }
    });

    //glfwSetInputMode(dmGetWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
  }

  public void update() {
    nglfwGetCursorPos(dmGetWindow(), MemoryUtil.memAddress(pos), MemoryUtil.memAddress(pos) + 8);

    mouseDX = pos.get(0) - lastPosX;
    mouseDY = pos.get(1) - lastPosY;
    lastPosX = pos.get(0);
    lastPosY = pos.get(1);
  }

  public void free() {
    keyCallback.free();
    mouseBtnCallback.free();
    MemoryUtil.memFree(pos);
  }

  public boolean isKeyDown(int key) {
    return keyDown[key];
  }

  public boolean isMouseButtonDown(int button) {
    return mouseDown[button];
  }

  public double getMouseDX() {
    return mouseDX;
  }

  public double getMouseDY() {
    return mouseDY;
  }

  public float getMouseX() {
    return ((float) lastPosX / (float) dmGetWidth()) * 2f;
  }

  public float getMouseY() {
    return ((float) lastPosY / (float) dmGetHeight()) * 2f;
  }
}
