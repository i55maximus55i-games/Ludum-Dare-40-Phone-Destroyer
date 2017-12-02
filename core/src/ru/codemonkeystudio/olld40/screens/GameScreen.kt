package ru.codemonkeystudio.olld40.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import ru.codemonkeystudio.olld40.CMSGame
import ru.codemonkeystudio.olld40.objects.Grid
import ru.codemonkeystudio.olld40.tools.ContactListener
import ru.codemonkeystudio.olld40.tools.ControlHandler
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape



class GameScreen(game: CMSGame) : Screen {
    lateinit var batch : SpriteBatch
    lateinit var camera : OrthographicCamera

    lateinit var world : World
    lateinit var debugRenderer : Box2DDebugRenderer

    lateinit var shape : CircleShape
    lateinit var bDef : BodyDef
    lateinit var fDef : FixtureDef

    lateinit var map : TiledMap
    lateinit var mapRenderer : OrthogonalTiledMapRenderer

    lateinit var grid : Grid

    override fun show() {
        batch = SpriteBatch()

        camera = OrthographicCamera()
        camera.setToOrtho(false)

        world = World(Vector2(0f, 0f), false)
        world.setContactListener(ContactListener())

        debugRenderer = Box2DDebugRenderer()


        bDef = BodyDef()
        bDef.type = BodyDef.BodyType.KinematicBody
        bDef.position.set(0f, 0f)

        shape = CircleShape()
        shape.radius = 4f
        fDef = FixtureDef()
        fDef.shape = shape


        map = TmxMapLoader().load("map.tmx")
        mapRenderer = OrthogonalTiledMapRenderer(map)
        grid = Grid()

        createWalls()
    }

    override fun render(delta: Float) {
        camera.position.x = 0f
        camera.position.y = 0f
        camera.update()

        world.step(delta, 10, 10)
        mapRenderer.setView(camera)

        mapRenderer.render()
        debugRenderer.render(world, camera.combined)

        if (ControlHandler.useKeyJustPressed()) {
            val body = world.createBody(bDef)
            body.createFixture(fDef)
            body.userData = "bullet"
            body.linearVelocity = Vector2(100f, 0f).setAngleRad(ControlHandler.dir())
        }


    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun hide() {
    }

    override fun dispose() {
    }

    private fun createWalls() {
        val bDef = BodyDef()
        val shape = PolygonShape()
        val fDef = FixtureDef()
        var body: Body

        for (`object` in map.layers.get("walls").objects.getByType(RectangleMapObject::class.java)) {
            val rect = (`object` as RectangleMapObject).rectangle

            bDef.type = BodyDef.BodyType.StaticBody
            bDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2)

            body = world.createBody(bDef)

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2)
            fDef.shape = shape
            body.createFixture(fDef)
            body.userData = "wall"
        }
    }
}
