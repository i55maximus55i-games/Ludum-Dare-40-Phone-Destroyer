package ru.codemonkeystudio.olld40;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import ru.codemonkeystudio.olld40.screens.MainMenuScreen;
import ru.codemonkeystudio.olld40.tools.ControlHandler;

public class CMSGame extends Game {
	public static final String APP_NAME = "MAGAZ";

	@Override
	public void create () {
		Gdx.app.log(APP_NAME, "Application started");
		Controllers.addListener(new ControlHandler());

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		ControlHandler.update();
	}

	@Override
	public void dispose () {
		super.dispose();
		Gdx.app.log(APP_NAME, "Application closed");
	}
}
