#version 400

in vec3 Color;

out vec4 fragColor;

uniform float transparency=1;

void main()
{
    fragColor = vec4(Color, transparency);
}