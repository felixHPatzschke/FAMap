#version 400

in vec3 Color;
in float frag_height;

out vec4 fragColor;

uniform float transparency=1;

void main()
{
    if(int(frag_height/20)%100 == 0)
        fragColor = vec4(1.0, 0.0, 0.0, transparency);
    else
        fragColor = vec4(Color, transparency);
}