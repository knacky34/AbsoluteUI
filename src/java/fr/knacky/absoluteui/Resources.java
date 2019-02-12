/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui;

import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.util.Loader;
import java.io.IOException;

public class Resources {
  public static class textures {
    public static int checkbox_unchecked;
    public static int checkbox_checked;
  }

  public static class font {
    public static FontType font;
    public static float size;
  }

  public static void loadDefaults() throws IOException {
    textures.checkbox_unchecked = Loader.loadTexture("textures/checkbox_unchecked.png", true);
    textures.checkbox_checked = Loader.loadTexture("textures/checkbox_checked.png", true);

    font.font = new FontType(Loader.loadTexture("fonts/candara.png", false), "fonts/candara.fnt");
    font.size = 2.0f;
  }
}
