#version 150

in vec2 position;

out vec2 pass_textureCoord;

uniform vec4 transform;
uniform vec3 textureCoord;

void main() {
  gl_Position = vec4(position.x * transform.z + transform.x - 1, position.y * transform.w - transform.y + 1 - transform.w, 0.0, 1.0);
  if (textureCoord.x > -0.5) {
    pass_textureCoord = vec2(position.x * textureCoord.z + textureCoord.x, (position.y * -1 + 1) * textureCoord.z + textureCoord.y);
  }
}
