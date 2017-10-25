#version 330
//#extension GL_OES_standard_derivatives : enable

//Used in the defuse function
#define PI 3.14159265

//This determines the precision of float type variables and can be changed for performance
precision highp float;

//Lots of lines are commented out with "#pragma glslify" on them
//The original shader used imported functions from the internet as it was meant for javascript apps
//The lines have been commented out and their functions written below them
//They remain in case we need them

//our custom Light struct
//This is very important to know
struct Light {
  vec3 position;
  vec3 color;
  vec3 ambient;
  float falloff;
  float radius;
};

//Input from vertex shader
in vec2 vUv;
in vec3 vViewPosition;
in vec3 vNormal;

//import some common functions
out vec4 fragColor;
//Used for flat shading
//#pragma glslify: faceNormals = require('glsl-face-normal')
vec3 faceNormals(vec3 pos) {
  vec3 fdx = dFdx(pos);
  vec3 fdy = dFdy(pos);
  return normalize(cross(fdx, fdy));
}

mat3 cotangentFrame(vec3 N, vec3 p, vec2 uv) {
    // get edge vectors of the pixel triangle
    vec3 dp1 = dFdx(p);
    vec3 dp2 = dFdy(p);
    vec2 duv1 = dFdx(uv);
    vec2 duv2 = dFdy(uv);
    
    // solve the linear system
    vec3 dp2perp = cross(dp2, N);
    vec3 dp1perp = cross(N, dp1);
    vec3 T = dp2perp * duv1.x + dp1perp * duv2.x;
    vec3 B = dp2perp * duv1.y + dp1perp * duv2.y;
    
    // construct a scale-invariant frame
    float invmax = 1.0 / sqrt(max(dot(T,T), dot(B,B)));
    return mat3(T * invmax, B * invmax, N);
}

//Used for normal map shading
//#pragma glslify: perturb = require('glsl-perturb-normal')
vec3 perturb(vec3 map, vec3 N, vec3 V, vec2 texcoord) {
  mat3 TBN = cotangentFrame(N, -V, texcoord);
  return normalize(TBN * map);
}



//Used to calculate diffused light (from nearby surfaces), can be changed to a number of functions
//#pragma glslify: computeDiffuse = require('glsl-diffuse-oren-nayar')
float computeDiffuse(
  vec3 lightDirection,
  vec3 viewDirection,
  vec3 surfaceNormal,
  float roughness,
  float albedo) {
  
  float LdotV = dot(lightDirection, viewDirection);
  float NdotL = dot(lightDirection, surfaceNormal);
  float NdotV = dot(surfaceNormal, viewDirection);

  float s = LdotV - NdotL * NdotV;
  float t = mix(1.0, max(NdotL, NdotV), step(0.0, s));

  float sigma2 = roughness * roughness;
  float A = 1.0 + sigma2 * (albedo / (sigma2 + 0.13) + 0.5 / (sigma2 + 0.33));
  float B = 0.45 * sigma2 / (sigma2 + 0.09);

  return albedo * max(0.0, NdotL) * (A + B * s / t) / PI;
}

//Used to calculate reflected light, can be changed to a number of functions (BRDFs)
//#pragma glslify: computeSpecular = require('glsl-specular-phong')
float computeSpecular(
  vec3 lightDirection,
  vec3 viewDirection,
  vec3 surfaceNormal,
  float shininess) {

  //Calculate Phong power
  vec3 R = -reflect(lightDirection, surfaceNormal);
  return pow(max(0.0, dot(viewDirection, R)), shininess);
}

//Note: the page for this function offers two alternatives
//Idk what this does lol
//#pragma glslify: attenuation = require('./madams-attenuation')
float attenuation(float r, float f, float d) {
  float denom = d / r + 1.0;
  float attenuation = 1.0 / (denom*denom);
  float t = (attenuation - f) / (1.0 - f);
  return max(t, 0.0);
}

//Used to convert in and out of gamma correction in rgb
//#pragma glslify: toLinear = require('glsl-gamma/in')

const float gamma = 2.2;

float toLinear(float v) {
  return pow(v, gamma);
}

vec2 toLinear(vec2 v) {
  return pow(v, vec2(gamma));
}

vec3 toLinear(vec3 v) {
  return pow(v, vec3(gamma));
}

vec4 toLinear(vec4 v) {
  return vec4(toLinear(v.rgb), v.a);
}

//#pragma glslify: toGamma = require('glsl-gamma/out')
float toGamma(float v) {
  return pow(v, 1.0 / gamma);
}

vec2 toGamma(vec2 v) {
  return pow(v, vec2(1.0 / gamma));
}

vec3 toGamma(vec3 v) {
  return pow(v, vec3(1.0 / gamma));
}

vec4 toGamma(vec4 v) {
  return vec4(toGamma(v.rgb), v.a);
}

//some settings for the look and feel of the material
const vec2 UV_SCALE = vec2(8.0, 1.0);
const float specularScale = 0.65;
const float shininess = 20.0;
const float roughness = 1.0;
const float albedo = 0.95;

//Used to convert from gamma corrected rgb to not (look at toLinear and 

uniform sampler2D texDiffuse;
uniform sampler2D texNormal;
uniform sampler2D texSpecular;

uniform int flatShading;
//uniform mat4 model;
//uniform mat4 view;
uniform mat4 modelViewMatrix;
uniform Light light;

//account for gamma-corrected images
vec4 textureLinear(sampler2D uTex, vec2 uv) {
  return toLinear(texture(uTex, uv));
}

void main() {
  //determine the type of normals for lighting
  vec3 normal = vec3(0.0);
  if (flatShading == 1) {
    normal = faceNormals(vViewPosition);
  } else {
    normal = vNormal;
  }

  //determine surface to light direction
  vec4 lightPosition = modelViewMatrix * vec4(light.position, 1.0); //view * 
  vec3 lightVector = lightPosition.xyz - vViewPosition;
  vec3 color = vec3(0.0);

  //calculate attenuation
  float lightDistance = length(lightVector);
  float falloff = attenuation(light.radius, light.falloff, lightDistance);

  //now sample from our repeating brick texture
  //assume its in sRGB, so we need to correct for gamma
  vec2 uv = vUv * UV_SCALE;
  vec3 diffuseColor = textureLinear(texDiffuse, uv).rgb;
  vec3 normalMap = textureLinear(texNormal, uv).rgb * 2.0 - 1.0;
  float specularStrength = textureLinear(texSpecular, uv).r;
  
  //our normal map has an inverted green channel
  normalMap.y *= -1.0;

  vec3 L = normalize(lightVector);              //light direction
  vec3 V = normalize(vViewPosition);            //eye direction
  vec3 N = perturb(normalMap, normal, -V, vUv); //surface normal

  //compute our diffuse & specular terms
  float specular = specularStrength * computeSpecular(L, V, N, shininess) * specularScale * falloff;
  vec3 diffuse = light.color * computeDiffuse(L, V, N, roughness, albedo) * falloff;
  vec3 ambient = light.ambient;

  //add the lighting
  color += diffuseColor * (diffuse + ambient) + specular;

  //re-apply gamma to output buffer
  color = toGamma(color);
    fragColor = vec4(color, 1.0);
}
