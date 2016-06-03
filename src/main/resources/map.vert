#version 400

in vec3 position;
in vec3 color;

out float ms_z, ws_x, ws_y, ws_z;
out vec3 frag_default_color;

uniform mat4 objectMatrix, cameraMatrix;

void main() {
    frag_default_color = color;
    ms_z = position.z;
	vec4 ws = objectMatrix * vec4(position, 1.0);
	ws_x = ws.x;
	ws_y = ws.y;
	ws_z = ws.z;
	gl_Position = cameraMatrix * ws;
}
