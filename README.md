# AbsoluteUI
A simple Java OpenGL UI library that uses absolute coordinates for layouting.

The libray actually supports renderer :
- LWJGL 3 with STB image loading binding

\
\
Special Thanks to [ThinMatrix](https://www.youtube.com/user/ThinMatrix/) that had written the code for text mesh creation.

## Usage
### Get started
in init method, in Main class
```java
/* You can use this method or create your own to load textures and default font */
Ressources.loadDefaults();

Gui.init(1280, 720); //viewport with and height
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

1. You should add infos about width and edge of the font under the second line of the fnt file (after lines with `padding` and `lineHeight` infos)

    ```
    fontsize=<min size>,<max size> fontwidth=<min width>,<max width> fontedge=<min edge>,<max edge>
    ```
    `<min size>` and `<max size>` are sizes where width and edge values are defined. (defaults: 1.0, 50.0)
    
    `<min width>` and `<max width>` are character's width related to thier size. Width needs to increase when font size increase. (defaults: 0.4, 0.55)
    
    `<min edge>` and `<max edge>` are character's antialiasing values. `<min edge>` should be greater than `<max edge>`. (defaults: 0.2, 0.04)
  
1. Finally, open the fnt file and delete all the lines that begins with `kernings` and `kerning`

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