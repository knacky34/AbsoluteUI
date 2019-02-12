#version 330

in vec2 pass_textureCoord;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D fontAtlas;

uniform vec4 widthsAndEdges;
uniform vec2 offset;
uniform vec3 outlineColor;

void main(void){
    float distance = 1.0 - texture(fontAtlas, pass_textureCoord).a;
    float alpha = 1.0 - smoothstep(widthsAndEdges.x, widthsAndEdges.x + widthsAndEdges.y, distance);

    float distance2 = 1.0 - texture(fontAtlas, pass_textureCoord + offset).a;
    float outlineAlpha = 1.0 - smoothstep(widthsAndEdges.z, widthsAndEdges.z + widthsAndEdges.w, distance2);

    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);

    out_Color = vec4(overallColor, overallAlpha);
    //out_Color = vec4(1);
}