package org.central

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import org.central.screens.Game
import ktx.app.KtxGame

class App : KtxGame<Screen>() {

    var width = 0f
    var height = 0f

    lateinit var sb: SpriteBatch

    lateinit var cam: OrthographicCamera
    lateinit var view: StretchViewport

    lateinit var stg: Stage

    override fun create() {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()

        this.sb = SpriteBatch()

        this.cam = OrthographicCamera(this.width, this.height)
        this.view = StretchViewport(480f, 360f, this.cam)
        this.stg = Stage(this.view, this.sb)

        val game = Game(this)

        addScreen(game)
        setScreen<Game>()
    }

    override fun resize(width: Int, height: Int) {
        this.width = width.toFloat()
        this.height = height.toFloat()
        this.cam.setToOrtho(false, width.toFloat(), height.toFloat())
        this.stg.batch.projectionMatrix = this.cam.combined
        this.stg.viewport.update(width, height, true)
    }

    override fun dispose() {
        this.sb.dispose()
        this.stg.dispose()
        super.dispose()
    }
}
