#version 400

in vec3 position;
in vec3 color;

out float ms_z, cs_z;
out vec4 ws;
out vec3 frag_default_color;

uniform mat4 objectMatrix, cameraMatrix;

void main() {
    frag_default_color = color;
    ms_z = position.z;
	ws = objectMatrix * vec4(position, 1.0);
	gl_Position = cameraMatrix * ws;
	cs_z = gl_Position.z;
}
