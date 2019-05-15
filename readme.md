# Minimal Example With Lightshafts Effect

![minimal_lightshafts.gif](.github/minimal_lightshafts.gif?raw=true)

This example was originally created by Fabrizio Pietrucci and includes a 'post processing pipeline' to process shaders in a frame buffer in a 
sequence. This is kind of complicated and I back engineered his example and removed things that didn't do anything to condense the effect into 
a single file example.

There are two textures loaded - a sun and a window - these are actors for no particular reason besides their having a draw method and basic 
attributes like width and height - probably could've just used sprites but it's not important.

There are two effects being applied to a frame buffer - a glow that comes from the sun texture, and 'occlusion', which blocks the glow going 
through the border of the window and adds definition to the borders of the corners of the transparent part of the window texture.

The shaders are in the android/assets directory - to tweek this powerful example, edit those. Keep in mind that the angle of the lightshafts 
is not fixed, and is dependent on the positions of the sun and the window textures - move them around, change their sizes, and marvel at this 
dynamic and realistic looking effect.
