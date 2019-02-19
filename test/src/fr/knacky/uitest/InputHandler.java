package fr.knacky.uitest;

import fr.knacky.absoluteui.Gui;
import java.nio.DoubleBuffer;
import java.util.Arrays;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.system.MemoryUtil;

import static fr.knacky.uitest.DisplayManager.dmGetHeight;
import static fr.knacky.uitest.DisplayManager.dmGetWidth;
import static fr.knacky.uitest.DisplayManager.dmGetWindow;
import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
  private static final int CODEPOINTS_INITIAL_LENGTH = 16;
  private static final int CODEPOINTS_MAXIMUM_LENGTH = 256;

  private GLFWKeyCallback keyCallback;
  private GLFWCharCallback charCallback;
  private GLFWMouseButtonCallback mouseBtnCallback;

  private DoubleBuffer pos = MemoryUtil.memAllocDouble(2);
  private boolean[] mouseDown = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];

  private char[] chars = new char[CODEPOINTS_INITIAL_LENGTH];
  private int pointer = 0;

  public InputHandler(Gui gui) {
    glfwSetKeyCallback(dmGetWindow(), keyCallback = new GLFWKeyCallback() {
      @Override
      public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_UNKNOWN) {
          return;
        } else if (key == GLFW_KEY_BACKSPACE) {
          gui.key_backspace = action;
        } else if (key == GLFW_KEY_DELETE) {
          gui.key_delete = action;
        } else if (key == GLFW_KEY_LEFT) {
          gui.key_left = action;
        } else if (key == GLFW_KEY_RIGHT) {
          gui.key_right = action;
        } else if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
          glfwSetWindowShouldClose(window, true);
        }
      }
    });

    glfwSetCharCallback(dmGetWindow(), charCallback = new GLFWCharCallback() {
      @Override
      public void invoke(long window, int codepoint) {
        if (pointer < CODEPOINTS_MAXIMUM_LENGTH) {
          chars[pointer] = (char) codepoint;
          pointer++;

          if (pointer >= chars.length && chars.length < CODEPOINTS_MAXIMUM_LENGTH) {
            chars = Arrays.copyOf(chars, chars.length * 2);
          }
        }
      }
    });

    glfwSetMouseButtonCallback(dmGetWindow(), mouseBtnCallback = new GLFWMouseButtonCallback() {
      @Override
      public void invoke(long window, int button, int action, int mods) {
        mouseDown[button] = action == GLFW_PRESS;
      }
    });
  }


  public char[] getChars() {
    if (pointer == 0) {
      return null;
    }
    char[] copy = Arrays.copyOf(chars, pointer);
    clearChars();
    return copy;
  }

  public void clearChars() {
    pointer = 0;
  }

  public void update() {
    nglfwGetCursorPos(dmGetWindow(), MemoryUtil.memAddress(pos), MemoryUtil.memAddress(pos) + 8);
  }

  public void free() {
    keyCallback.free();
    charCallback.free();
    mouseBtnCallback.free();
    MemoryUtil.memFree(pos);
  }

  public boolean isMouseButtonDown(int button) {
    return mouseDown[button];
  }

  public float getMouseX() {
    return ((float) pos.get(0) / (float) dmGetWidth()) * 2f;
  }

  public float getMouseY() {
    return ((float) pos.get(1) / (float) dmGetHeight()) * 2f;
  }
}
