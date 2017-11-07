# Weekly Reports
A compilation of weekly reports used to track progress and stay on schedule.

## Proposed Schedule (as of 10/12/17)
Week 1-3: Research and experimentation

Week 4-5: First model rendering

Week 6-9: Able to load models and have movement from both model and control

Week 10-14: First playable (movement) version of game

Week 15-25: Finished product (not most important)

Week 26+: Continued work on engine

## Week 1
Running and interactable window with a textured cube with basic shaders. Interactability consists of basic XYZ motion with mouse, mouse wheel, arrow keys, and and keys q,w,e,a,s, and d. Rotation and scaling are posible as well. One minor bug is present: scrolling gives a constant increase in scaling instead of it being based on the distance of the scroll. Shaders can be loaded through a class able to handle multiple shaders, but the current shader doesn't use light sources and simply relies on X,Y, and Z coordinates. Still not able to load a model from a file

## Week 2
Added ability to load models.

## Week 3
Added a basic one-light source phong shader that implements perturbed ambient, diffuse, specular, and attenuant light. Implemented an idea for a Collidable interface that would detect basic rectangular prism-hitboxed collisions between items.

## Week 4
Fully implemented the collisions started in the previous week. Added a basic HUD class to render 2D text and objects onto the screen.

## Week 5
Fixed HUD class rendering glitch. Added a SkyBox class to encapsulate the world and provide a realistic-looking feel.
