#version 330

in vec2 position;
in vec2 textureCoord;

out vec2 pass_textureCoord;

uniform vec2 translation;

void main(void){
    gl_Position = vec4(position.x + translation.x, position.y - translation.y, 0.0, 1.0);
    pass_textureCoord = textureCoord;
}