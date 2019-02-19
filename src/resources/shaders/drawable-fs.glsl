#version 150

in vec2 pass_textureCoord;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D tex;

void main() {
  if (color.x < -0.5) {
    out_Color = texture(tex, pass_textureCoord);
  } else {
    out_Color = vec4(color, 1.0);
  }
}
