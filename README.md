# AbsoluteUI
A simple Java OpenGL UI library that uses absolute coordinates for layouting.

The libray actually supports renderer :
- LWJGL 3 with STB image loading binding

\
\
Special Thanks to [ThinMatrix](https://www.youtube.com/user/ThinMatrix/) that had written the base code for text mesh creation.

## Usage
### Get started
in init method, in Main class
```java
Gui.init(1280, 720); //viewport width and height
Gui.setKeyConstants(GLFW_PRESS, GLFW_REPEAT, GLFW_RELEASE); //constants that are used by the input system (in my case GLFW)
Ressources.loadDefaults(); //You can call this method or load textures and font by yourself

Gui gui = new Gui();

/* Add views */
gui.add(new Button(new Vector2f(0.8f, 0.2f), new Vector2f(0.4f, 0.1f), "Button Title", new PressedCallback() {
    @Override
    public void onPressed(ClickableView view, boolean pressed) {
        /* Event fire when the button is pressed and released */
    }
    
    @Override
    public void onActionPerformed(ClickableView view, boolean hovered, boolean pressed) {
        /* Event fire when mouse moves hover button and when button is pressed and released */
    }
}));
gui.add(new Checkbox(new Vector2f(0.2f, 0.2f), "Checkbox Label", null, Checkbox.LABEL_LEFT));
```
\
then, in update/render loop\
`gui.update(float x, float y, boolean mouse);` update views and fire events. `<x>` and `<x>` is mouse position in the coordinate system that's used by the library [Coord System Image](https://github.com/knacky34/AbsoluteUI/blob/master/images/coordinates.png),
`<mouse>` is the current left mouse button state.\
`gui.render();` render visible views.\
`gui.resize(int width, int height)` resize views when aspect ratio is changed\
\
before exit the app you need to clear everything.
```
Loader.free();
ShaderUtil.free();
gui.free();
```
<br />
<br />

### Font file creation
1. Generate font file and texture atlas with distance fields, I'm using [Hiero](https://libgdx.badlogicgames.com/tools.html) ([OpenGL 3D Game Tutorial 33](https://youtu.be/d8cfgcJR9Tk?t=91))

1. Use the Font tool to clear unused lines and add more information for distance field (The tool will be aviable soon)

## License
This software is distributed under GNU GPLv3 license :
```
Copyright (C) 2019-present  Knacky34

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
```

## Dependencies
**JOML :** [github.com](https://github.com/JOML-CI/JOML), MIT License