package org.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import org.central.App


class Game(val app: App) : KtxScreen {

    private var window = Texture("small_window_wall.png")

    private var fbo = FrameBuffer(Pixmap.Format.RGBA8888, app.width.toInt(), app.height.toInt(), false)

    private val occludersFBO = FrameBuffer(Pixmap.Format.RGBA8888, app.width.toInt(), app.height.toInt(), false)
    private val occlusionApprox = FrameBuffer(Pixmap.Format.RGB888, app.width.toInt(), app.height.toInt(), false)

    private val occlusionApproxShader = ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/occlusion.frag"))

    override fun render(delta: Float) {

        // Renders the occluders in black over the white sun
        occludersFBO.begin()

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        app.sb.begin()

        app.sb.draw(window, Gdx.input.x.toFloat() - window.width / 2, app.height - Gdx.input.y.toFloat() - window.height / 2)

        app.sb.end()
        occludersFBO.end()

        // Applies radial blur in order to obtain the "lightshaft" effect
        occlusionApprox.begin()

        app.sb.shader = occlusionApproxShader
        app.sb.begin()

        occlusionApproxShader.setUniformf("cent", 0.5f, 0.5f)

        app.sb.draw(occludersFBO.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)

        app.sb.end()
        occlusionApprox.end()

        // cleanup and reset operations
        app.view.apply()
        app.sb.shader = null

        //Render the whole scene in the FBO
        fbo.begin()
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        app.sb.begin()

        // Draw lightshafts
        app.sb.draw(occlusionApprox.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)

        app.sb.end()
        fbo.end()

        // Apply post processing
        app.sb.begin()
        app.sb.draw(fbo.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)
        app.sb.end()
    }

    override fun dispose() {
        window.dispose()
        fbo.dispose()
        occludersFBO.dispose()
        occlusionApprox.dispose()
        occlusionApproxShader.dispose()
    }
}
