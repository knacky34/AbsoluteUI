/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.uitest;

import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.Resources;
import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.util.Loader;
import fr.knacky.absoluteui.util.ShaderUtil;
import fr.knacky.absoluteui.view.Button;
import fr.knacky.absoluteui.view.Checkbox;
import fr.knacky.absoluteui.view.Text;
import java.io.IOException;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static fr.knacky.uitest.DisplayManager.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30C.*;

public class Main {
  private InputHandler inputHandler;
  private Gui gui;

  static {
    System.setProperty("org.lwjgl.opengl.maxVersion", "3.3");
  }

  private void init() throws IOException {
    dmCreateWindow();
    dmInitGL();

    /* Create all GL resources */
    createUi();

    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);

    inputHandler = new InputHandler();
  }

  private void createUi() throws IOException {
    Gui.init(dmGetWidth(), dmGetHeight());
    Resources.loadDefaults();

    gui = new Gui();

    gui.add(new Button(new Vector2f(0.8f, 0.2f), new Vector2f(0.4f, 0.1f), "Resume Game", null));
    gui.add(new Button(new Vector2f(0.8f, 0.5f), new Vector2f(0.4f, 0.1f), "Setings", null));
    gui.add(new Button(new Vector2f(0.8f, 0.8f), new Vector2f(0.4f, 0.3f), "Quit", null));
    gui.add(new Checkbox(new Vector2f(0.2f, 0.2f), "Antialiasing : ", null, Checkbox.LABEL_LEFT));
    gui.add(new Text(new Vector2f(0f, 0f), "Hello World !", new Vector3f(1f, 1f, 1f)));
  }

  private void update() {
    inputHandler.update();
    gui.update(inputHandler.getMouseX(), inputHandler.getMouseY(), inputHandler.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT));

    if (dmShouldResizeFrameBuffer()) {
      dmSetShouldResizeFrameBuffer(false);
      glViewport(0, 0, dmGetWidth(), dmGetHeight());
    }
  }

  private void render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    gui.render();
  }


  private void loop() {
    long lastTime = 0L;

    while (!glfwWindowShouldClose(dmGetWindow())) {
      dmUpdate();

      update();
      render();

      glfwSwapBuffers(dmGetWindow());
      lastTime += dmGetFrameTimeNano();
      if (lastTime >= 1E9) {
        System.out.println((int) (1E9 / dmGetFrameTimeNano()) + " frame/sec");
        lastTime = 0;
      }
    }
  }

  private void run() {
    try {
      init();
      loop();

      inputHandler.free();
      gui.free();
      ShaderUtil.free();
      Loader.free();
      dmDestroy();
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      glfwTerminate();
    }
  }

  public static void main(String[] args) {
    new Main().run();
  }
}
