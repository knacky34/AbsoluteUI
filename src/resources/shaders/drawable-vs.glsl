#version 150

in vec2 position;

out vec2 textureCoord;

uniform vec4 transform;

void main() {
  gl_Position = vec4(position.x * transform.z + transform.x - 1, position.y * transform.w - transform.y + 1 - transform.w, 0.0, 1.0);
  textureCoord = vec2(position.x, position.y * -1 + 1);
}
