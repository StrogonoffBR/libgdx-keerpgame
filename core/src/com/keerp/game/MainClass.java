package com.keerp.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.ApplicationListener;

public class MainClass implements ApplicationListener {
	
	private static final String TAG = MainClass.class.getName();
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private boolean paused;

	
	@Override
	public void create () 
	{
		// set libgdx log level to DEBUG	
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		
		// game world is active on start
		paused = false;
		
	}
	
	@Override
	public void dispose()
	{
		worldRenderer.dispose();
	}
	
	
	@Override
	public void render () {
		
		if(!paused)
		{
			worldController.update(Gdx.graphics.getDeltaTime());
		}
				
		// setting screen color
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f,0xff/255.0f);
		
		// clear the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// render game world to screen
		worldRenderer.render();
	}
	
	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width, height);
	}
	
	@Override
	public void pause()
	{
		paused = true;
	}
	
	@Override
	public void resume()
	{
		paused = false;
	}
}
