#version 400

#define PI 3.1415926535897932

#define FRAG_NONE 0
#define FRAG_DEFAULT 1
#define FRAG_HEIGHTMAP 2
#define FRAG_WATER 4
#define FRAG_HEIGHT_LEVEL_LINES 8
#define FRAG_TOOL_BLOB 16
#define FRAG_FOG 128
#define FRAG_DYNAMICLIGHTING 256

#define HLL_INVERSE_THICKNESS 48
#define HLL_AMOUNT 12
#define HLL_OFFSET 20


in float ms_z, cs_z;
in vec4 ws;
in vec3 frag_default_color;

out vec4 frag_color;

uniform int frag_enum;
uniform float tool_x, tool_y, tool_r_sqr;
uniform float h_min, h_max;
uniform float transp;
uniform vec3 u_campos;
uniform vec3 lightpos = vec3(0.0, 0.0, 1000.0);

const vec3 normal = vec3(0.0, 0.0, 1.0);
vec3 campos;


vec4 default_color()
{
    return vec4(frag_default_color, transp);
}

vec4 heightmap_color()
{
    float h = (ms_z-h_min)/(h_max-h_min);
    return vec4(h/2, h/2, h/2, transp);
}

vec4 water_color()
{
    return vec4(0.0, 0.0, 1.0, transp);
}

vec4 dynamiclighting_color(vec4 prev_col)
{
    float a = abs(/*acos(*/dot((vec3(ws)-lightpos), normal)/(length(vec3(ws)-lightpos), length(normal)));//);
    return a*prev_col;
}

vec4 tool_blob_color(vec4 prev_col)
{
    float diff_sqr = (ws.x-tool_x)*(ws.x-tool_x) + (ws.y-tool_y)*(ws.y-tool_y);
    if(diff_sqr < tool_r_sqr)
    {
        float f = (diff_sqr/tool_r_sqr);
        vec4 cdiff = vec4(0.0, 1.0, 0.0, transp) - prev_col;
        return prev_col+(f*cdiff);
    }
    /*diff_sqr = (ws.x-campos.x)*(ws.x-campos.x) + (ws.y-campos.y)*(ws.y-campos.y);
    if(diff_sqr < tool_r_sqr)
    {
        float f = (diff_sqr/tool_r_sqr);
        vec4 cdiff = vec4(1.0, 0.0, 1.0, transp) - prev_col;
        return prev_col+(f*cdiff);
    }*/
    else
        return prev_col;
}

vec4 fog_color(vec4 prev_col)
{
    vec4 cdiff = vec4(1.0, 1.0, 1.0, 1.0) - prev_col;
    float xdiff = length(vec2(ws)-vec2(campos));
    return prev_col+((0.000001*xdiff*xdiff)*cdiff);
}

vec4 height_level_lines_color(vec4 prev_col)
{
    float h = (ms_z-h_min)/(h_max-h_min);
    if(int(h*HLL_INVERSE_THICKNESS*HLL_AMOUNT)%HLL_INVERSE_THICKNESS == HLL_OFFSET)
        return vec4(1.0, 0.0, 0.0, 1.0);
    else
        return prev_col;
}

void main() {

    frag_color = vec4(0.0, 0.0, 0.0, transp);
    campos = -1.0*u_campos;
    if((frag_enum & FRAG_DEFAULT) != 0)
	    frag_color = default_color();
	if((frag_enum & FRAG_HEIGHTMAP) != 0)
	    frag_color = heightmap_color();
	if((frag_enum & FRAG_WATER) != 0)
	    frag_color = water_color();
	//if((frag_enum & FRAG_DYNAMICLIGHTING) != 0)
	    //frag_color = dynamiclighting_color(frag_color);
	if((frag_enum & FRAG_HEIGHT_LEVEL_LINES) != 0)
	    frag_color = height_level_lines_color(frag_color);
	if((frag_enum & FRAG_TOOL_BLOB) != 0)
	    frag_color = tool_blob_color(frag_color);
	if((frag_enum & FRAG_FOG) != 0)
	    frag_color = fog_color(frag_color);

}
