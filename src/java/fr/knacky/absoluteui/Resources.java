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
    public static TextureAtlas atlas;
  }

  public static class font {
    public static FontType font;
    public static float size;
  }

  public static void loadDefaults() throws IOException {
    textures.atlas = new TextureAtlas(Loader.loadTexture("textures/gui_atlas.png", true), 4);

    font.font = new FontType(Loader.loadTexture("fonts/candara.png", false), "fonts/candara.fnt");
    font.size = 4.0f;
  }
}
