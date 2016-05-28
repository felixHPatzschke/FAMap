#version 400

in vec3 position;
in vec3 color;

out vec3 Color;

uniform mat4 objectMatrix,cameraMatrix;

void main()
{
    vec4 pos = objectMatrix*vec4(position, 1.0);
    gl_Position = cameraMatrix*pos;

    //vec4 pos = cameraMatrix*vec4(position, 1.0);
    //gl_Position = objectMatrix*pos;

    Color = color;
}