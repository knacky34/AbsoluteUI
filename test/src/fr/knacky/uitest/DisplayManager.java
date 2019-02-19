package fr.knacky.uitest;

import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class DisplayManager {
  private static String title = "Absolute UI - Test Demo";
  private static long window;
  private static int width = 1280;
  private static int height = 720;

  private static GLFWErrorCallback errCallback;
  private static GLFWFramebufferSizeCallback framebufCallback;

  private static boolean resizeFrameBuffer = true;

  private static long lastFrameTime = 0L;
  private static long delta = 0L;

  public static void dmCreateWindow() {
    glfwSetErrorCallback(errCallback = new GLFWErrorCallback() {
      private  GLFWErrorCallback delegate = GLFWErrorCallback.createPrint(System.err);

      @Override
      public void invoke(int error, long description) {
        if (error == GLFW_VERSION_UNAVAILABLE) System.err.println(title + " requires OpenGL 3.0 or higher. Make sure your drivers are up-to-date.");
        delegate.invoke(error, description);
      }

      @Override
      public void free() {
        delegate.free();
      }
    });

    if (!glfwInit()) throw new IllegalStateException("Unable to initailize GLFW.");

    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

    window = glfwCreateWindow(width, height, title, NULL, NULL);
    if (window == NULL) throw new AssertionError("Failed to create the GLFW window.");

    glfwSetFramebufferSizeCallback(window, framebufCallback = new GLFWFramebufferSizeCallback() {
      @Override
      public void invoke(long window, int width, int height) {
        if (width > 0 && height > 0 && (DisplayManager.width != width || DisplayManager.height != height)) {
          DisplayManager.width = width;
          DisplayManager.height = height;
          DisplayManager.resizeFrameBuffer = true;
        }
      }
    });
  }

  public static void dmInitGL() {
    GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
    glfwMakeContextCurrent(window);
    glfwSwapInterval(1);
    glfwShowWindow(window);

    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer framebufferSize = stack.mallocInt(2);
      nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
      width = framebufferSize.get(0);
      height = framebufferSize.get(1);
    }

    GL.createCapabilities();

    lastFrameTime = System.nanoTime();
  }

  private static void dmUpdateFrameTimeCounter() {
    long currentFrameTime = System.nanoTime();
    delta = currentFrameTime - lastFrameTime;
    lastFrameTime = currentFrameTime;
  }

  public static void dmUpdate() {
    dmUpdateFrameTimeCounter();
    glfwPollEvents();
  }

  public static void dmDestroy() {
    errCallback.free();
    framebufCallback.free();

    glfwDestroyWindow(window);
  }

  public static long dmGetWindow() {
    return window;
  }

  public static double dmGetFrameTime() {
    return (double) delta / 1E9;
  }

  public static long dmGetFrameTimeNano() {
    return delta;
  }

  public static int dmGetWidth() {
    return width;
  }

  public static int dmGetHeight() {
    return height;
  }

  public static float dmGetAspectRatio() {
    return (float) width / (float) height;
  }

  public static boolean dmShouldResizeFrameBuffer() {
    return resizeFrameBuffer;
  }

  public static void dmSetShouldResizeFrameBuffer(boolean resizeFrameBuffer) {
    DisplayManager.resizeFrameBuffer = resizeFrameBuffer;
  }
}
