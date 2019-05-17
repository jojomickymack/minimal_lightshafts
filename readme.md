# Minimal Example With Lightshafts Effect

![minimal_lightshafts.gif](.github/minimal_lightshafts.gif?raw=true)

This example was [originally created by Fabrizio Pietrucci](https://spaghettidevops.com/2017/03/22/rendering-a-godrays-effect-as-postprocess-in-libgdx-using-shaders) and included a 'post processing pipeline' to process glow and occlusion shaders in frame buffers in a sequence. I only wanted to do the lightshafts without the glow in the simplest form possible - this example is only 75 lines.

There is one effect being applied to a frame buffer - 'occlusion', which blocks the lightshaft effect being cast through any non transparent texture. Anything drawn on the first frame buffer will have light shining around it, and the shader's uniform called 'cent' dictates the position of the light on the screen.

The shader is in the android/assets directory - have fun tweeking this amazing effect!
