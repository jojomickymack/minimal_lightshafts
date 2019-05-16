# Minimal Example With Lightshafts Effect

![minimal_lightshafts.gif](.github/minimal_lightshafts.gif?raw=true)

This example was [originally created by Fabrizio Pietrucci](https://spaghettidevops.com/2017/03/22/rendering-a-godrays-effect-as-postprocess-in-libgdx-using-shaders) and includes a 'post processing pipeline' to process shaders in a frame buffer in a sequence. This is kind of complicated and I back engineered his example and removed things that didn't do anything to condense the effect into a single file example.

There is one effect being applied to a frame buffer - 'occlusion', which blocks the lightshaft effect being cast through any non transparent texture.

The shader is in the android/assets directory.
