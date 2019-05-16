package org.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.Actor
import org.central.App


class Sun : Actor() {

    var texture = Texture("small_sun.png")

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
    }

    fun dispose() {
        texture.dispose()
    }
}


class Window : Actor() {

    var texture = Texture("window.png")

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
    }

    fun dispose() {
        texture.dispose()
    }
}


class Game(val app: App) : KtxScreen {

    private val sun = Sun()
    private var window = Window()

    private var fbo = FrameBuffer(Pixmap.Format.RGBA8888, app.width.toInt(), app.height.toInt(), false)

    private val occludersFBO = FrameBuffer(Pixmap.Format.RGBA8888, app.width.toInt(), app.height.toInt(), false)
    private val occlusionApprox = FrameBuffer(Pixmap.Format.RGB888, app.width.toInt(), app.height.toInt(), false)

    private val occluderShader = ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/lightshaft.frag"))
    private val occlusionApproxShader = ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/occlusion.frag"))

    override fun render(delta: Float) {
        sun.x = 0f
        sun.y = 0f

        window.x = Gdx.input.x.toFloat()
        window.y = app.height - Gdx.input.y.toFloat()

        // Renders the occluders in black over the white sun
        occludersFBO.begin()

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        app.sb.shader = occluderShader
        app.sb.begin()

        occluderShader.setUniformf("color", Color.PINK)

        sun.draw(app.sb, 1f)
        app.sb.flush()

        for (actor in listOf(window)) {
            actor.draw(app.sb, 1f)
        }

        app.sb.end()
        occludersFBO.end()

        // Applies radial blur in order to obtain the "lightshaft" effect
        occlusionApprox.begin()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        app.sb.disableBlending()
        app.sb.shader = occlusionApproxShader
        app.sb.begin()

        occlusionApproxShader.setUniformf("cent", 0f, 0f)

        app.sb.draw(occludersFBO.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)

        app.sb.end()
        occlusionApprox.end()

        // cleanup and reset operations
        app.view.apply()
        app.sb.enableBlending()
        app.sb.shader = null

        //Render the whole scene in the FBO
        fbo.begin()
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        app.sb.begin()

        // Draw lightshafts
        // app.sb.draw(occludersFBO.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)
        app.sb.draw(occlusionApprox.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)

        app.sb.end()
        fbo.end()

        // Apply post processing
        app.sb.begin()
        app.sb.draw(fbo.colorBufferTexture, 0f, 0f, app.width, app.height, 0f, 0f, 1f, 1f)
        app.sb.end()
    }

    override fun dispose() {
        super.dispose()
        sun.dispose()
        window.dispose()
        fbo.dispose()

        occludersFBO.dispose()
        occlusionApprox.dispose()
        occluderShader.dispose()
        occlusionApproxShader.dispose()
    }
}
