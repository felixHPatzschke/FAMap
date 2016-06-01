#version 400

in vec3 position;
in vec3 color;

out float ms_z, ws_x, ws_y;
out vec3 Color;

uniform mat4 objectMatrix, cameraMatrix;

void main() {
    Color = color;
    ms_z = position.z;
	vec4 ws = objectMatrix * vec4(position, 1.0);
	ws_x = ws.x;
	ws_y = ws.y;
	gl_Position = cameraMatrix * ws;
}
