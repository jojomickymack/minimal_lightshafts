package org.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Matrix4
import org.central.App


class Game(val app: App) : KtxScreen {

    private var window = Texture("small_window_wall.png")

    private val occludersFbo = FrameBuffer(Pixmap.Format.RGBA8888, app.width.toInt(), app.height.toInt(), false)
    private val occlusionApprox = FrameBuffer(Pixmap.Format.RGB888, app.width.toInt(), app.height.toInt(), false)

    private val occlusionApproxShader = ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/occlusion.frag"))

    private val postProcProj = Matrix4().setToOrtho2D(0f, 0f, app.width, app.height)

    override fun render(delta: Float) {

        // draws the black shape that blocks out the light
        occludersFbo.begin()

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        app.sb.begin()
        app.sb.draw(window, Gdx.input.x.toFloat() - window.width / 2, app.height - Gdx.input.y.toFloat() - window.height / 2)
        app.sb.end()

        occludersFbo.end()

        // calculates all of the lightrays
        occlusionApprox.begin()
        app.sb.shader = occlusionApproxShader

        app.sb.begin()
        occlusionApproxShader.setUniformf("cent", 0.5f, 0.5f)
        app.sb.draw(occludersFbo.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)
        app.sb.end()

        occlusionApprox.end()

        // cleanup and reset operations
        app.view.apply()
        app.sb.shader = null

        // Apply post processing
        app.sb.begin()

        app.sb.projectionMatrix = postProcProj
        app.sb.disableBlending()

        app.sb.draw(occlusionApprox.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)

        app.sb.projectionMatrix = app.cam.combined
        app.sb.enableBlending()
        app.sb.end()
    }

    override fun dispose() {
        window.dispose()
        occludersFbo.dispose()
        occlusionApprox.dispose()
        occlusionApproxShader.dispose()
    }
}
