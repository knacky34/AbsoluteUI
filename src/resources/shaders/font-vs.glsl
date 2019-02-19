#version 150

in vec2 position;
in vec2 textureCoord;

out vec2 pass_textureCoord;

uniform vec4 transform;

void main(void){
    gl_Position = vec4(position.x * transform.z + transform.x - 1, position.y * -transform.w - transform.y + 1, 0.0, 1.0);
    pass_textureCoord = textureCoord;
}