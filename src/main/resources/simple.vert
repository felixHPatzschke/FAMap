#version 400

in vec3 position;
in vec3 color;

out vec3 Color;

uniform mat4 objectMatrix, cameraMatrix;

void main()
{
    gl_Position = vec4(position, 1.0)*objectMatrix*cameraMatrix;
    Color = color;
}