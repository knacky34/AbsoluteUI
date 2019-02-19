/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.uitest;

import fr.knacky.absoluteui.Gui;
import fr.knacky.absoluteui.Resources;
import fr.knacky.absoluteui.callback.*;
import fr.knacky.absoluteui.util.Loader;
import fr.knacky.absoluteui.util.ShaderUtil;
import fr.knacky.absoluteui.view.*;
import java.io.IOException;
import org.joml.Vector2f;

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
  }

  private void createUi() throws IOException {
    Gui.init(dmGetWidth(), dmGetHeight());
    Gui.setKeyConstants(GLFW_PRESS, GLFW_REPEAT, GLFW_RELEASE);
    Resources.loadDefaults();

    gui = new Gui();
    inputHandler = new InputHandler(gui);

    Button quitButton = new Button(new Vector2f(0.8f, 0.1f), new Vector2f(0.4f, 0.1f), "Button Quit", new PressCallback() {
      @Override
      public void onPressed(ClickableView view, boolean pressed) {
        if (!pressed) {
          glfwSetWindowShouldClose(dmGetWindow(), true);
        }
      }
      @Override
      public void onActionPerformed(ClickableView view, boolean hovered, boolean pressed) {}
    });
    quitButton.setVisible(false);
    gui.add(quitButton);

    gui.add(new Checkbox(new Vector2f(0.8f, 0.3f), "Show Quit Button", new CheckCallback() {
      @Override
      public void onChecked(Checkbox view, boolean checked) {
        quitButton.setVisible(checked);
      }

      @Override
      public void onActionPerformed(ClickableView view, boolean hovered, boolean pressed) {

      }
    }, Checkbox.LABEL_LEFT));

    gui.add(new Checkbox(new Vector2f(0.8f, 0.5f), 0.1f, null));

    gui.add(new Slider(new Vector2f(0.8f, 0.7f), new Vector2f(0.4f, 0.1f), new SlideCallback() {
      @Override
      public void onValueChanged(ClickableView view, float value) {
        glClearColor(value, 0.0f, 0.0f, 1.0f);
      }

      @Override
      public void onActionPerformed(ClickableView view, boolean hovered, boolean pressed) {}
    }, 0.0f, 1.0f, 0.01f, 0.0f));

    gui.add(new Textfield(new Vector2f(0.8f, 0.9f), new Vector2f(0.4f, 0.1f), new TextChangeCallback() {
      @Override
      public void onTextChanged(ClickableView view, String text) {
        System.out.println("You're writing something new !");
      }

      @Override
      public void onActionPerformed(ClickableView view, boolean hovered, boolean pressed) {

      }
    }));

    Button noBackButton = new Button(new Vector2f(0.8f, 1.1f), null, "Button without background", Resources.font.font, Resources.font.size, (view, hovered, pressed) -> {
      if (hovered) {
        if (pressed) {
          ((Button) view).label.setColor(0.6f, 0.6f, 0.6f);
        } else {
          ((Button) view).label.setColor(1f, 1f, 1f);
        }
      } else {
        ((Button) view).label.setColor(0.8f, 0.8f, 0.8f);
      }
    }, false);
    noBackButton.label.setColor(0.8f, 0.8f, 0.8f);
    gui.add(noBackButton);

  }

  private void update() {
    inputHandler.update();
    gui.chars = inputHandler.getChars();
    gui.update(dmGetFrameTimeNano(), inputHandler.getMouseX(), inputHandler.getMouseY(), inputHandler.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT));

    if (dmShouldResizeFrameBuffer()) {
      dmSetShouldResizeFrameBuffer(false);
      glViewport(0, 0, dmGetWidth(), dmGetHeight());
      gui.resize(dmGetWidth(), dmGetHeight());
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
