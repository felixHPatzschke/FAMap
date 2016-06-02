#version 400

#define PI 3.1415926535897932

#define FRAG_NONE 0
#define FRAG_DEFAULT 1
#define FRAG_HEIGHT_LEVEL 2
#define FRAG_TOOL_BLOB 4


in float ms_z, ws_x, ws_y;
in vec3 Color;

out vec4 frag_color;

uniform int frag_enum;
uniform float tool_x, tool_y, tool_r_sqr;
uniform float h_min, h_max;
uniform float transp;


vec4 default_color()
{
    return vec4(Color, transp);
}

vec4 tool_blob_color(vec4 prev_col)
{
    float diff_sqr = (ws_x-tool_x)*(ws_x-tool_x) + (ws_y-tool_y)*(ws_y-tool_y);
    if(diff_sqr < tool_r_sqr)
    {
        return vec4(0.0, 1.0, 0.0, transp);
    }
    else
        return prev_col;
}

vec4 height_level_color(vec4 prev_col)
{
    float h = (ms_z-h_min)/(h_max-h_min);
    return vec4(h, h, h, 1.0);
}

void main() {

    frag_color = vec4(0.0, 0.0, 0.0, transp);
	if((frag_enum & FRAG_DEFAULT) != 0)
	    frag_color = default_color();
	if((frag_enum & FRAG_HEIGHT_LEVEL) != 0)
	    frag_color = height_level_color(frag_color);
	if((frag_enum & FRAG_TOOL_BLOB) != 0)
	    frag_color = tool_blob_color(frag_color);

}
