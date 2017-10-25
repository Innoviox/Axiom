#version 330
//Lots of lines are commented out with "#pragma glslify" on them
//The original shader used imported functions from the internet as it was meant for javascript apps
//The lines have been commented out and their functions written below them
//They remain in case we need them

layout (location=0) in vec3 position;
layout (location=1) in vec2 uv; //texCoord
layout (location=2) in vec3 normal;

//These are constants but not not compile-time constant
uniform mat4 projection;
//uniform mat4 view;
//uniform mat4 model;
uniform mat4 modelViewMatrix;

//Output
out vec3 vNormal;
out vec2 vUv; //outTexCoord
out vec3 vViewPosition;

void main() {
  //Determine view space position
  //mat4 modelViewMatrix = view * model;
  vec4 viewModelPosition = modelViewMatrix * vec4(position, 1.0);

  //Pass varyings to fragment shader
  vViewPosition = viewModelPosition.xyz;
  vUv = uv;
  gl_Position = projection * viewModelPosition;

  //Rotate the object normals by a 3x3 normal matrix.
  //We could also do this CPU-side to avoid doing it per-vertex
  //mat3 normalMatrix = transpose(inverse(mat3(modelViewMatrix)));
  //vNormal = normalize(normalMatrix * normal);
  vNormal = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
}
