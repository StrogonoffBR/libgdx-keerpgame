package com.keerp.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class WorldController extends InputAdapter {
	
	private static final String TAG = WorldController.class.getName();
	
	public Sprite[] testSprites;
	public int selectedSprite;
	
	private int spriteCount;
	
	public CameraHelper cameraHelper;
	public float cameraSpeed;
	
	
	public WorldController()
	{
		spriteCount = 5;
		
		init();
		
		cameraHelper = new CameraHelper();
		cameraSpeed = 5.0f;
	}
	
	
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		initTestObjects();
    }
	
	
	private void initTestObjects()
	{
		testSprites = new Sprite[spriteCount];
		
		int width = 32;
		int height = 32;
		
		Pixmap pixmap = createProceduralPixmap(width, height);
		
		Texture texture = new Texture(pixmap);
		
		for(int i = 0; i < testSprites.length; i++)
		{
			Sprite spr = new Sprite(texture);
			spr.setSize(1, 1);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
			
			// calculating random position for each sprite
			
			float randomX = MathUtils.random(-2.0f, 2.0f);
			float randomY = MathUtils.random(-2.0f, 2.0f);
			
			spr.setPosition(randomX, randomY);
			
			// put new sprite into array
			testSprites[i] = spr;
			
		}
		
		
		selectedSprite = 0;
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		// reset game world
		if(keycode == Keys.R)
		{
			init();            // it calls init() again, so everything will be "new-generated"
			Gdx.app.debug(TAG, "Game world reseted");			
		}
		else if(keycode == Keys.SPACE)
		{
			selectedSprite = (selectedSprite + 1) % testSprites.length;
			Gdx.app.debug(TAG, "Sprite: " + selectedSprite + " selected");
			
			// selecting the camera to new sprite
			if(cameraHelper.hasTarget())
				cameraHelper.setTarget(testSprites[selectedSprite]);		
		}
		else if(keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(testSprites[selectedSprite]);
		}

	
		return false;
	}
	
	
	private Pixmap createProceduralPixmap(int width, int height)
	{
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		
		// fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		
		// draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		
		// draw a cyan-colored border around square
		
		pixmap.setColor(1, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		
		
		return pixmap;
	}
	
	private void updateTestObjects(float deltaTime)
	{
		// get current rotation from selected sprite
		float rotation = testSprites[selectedSprite].getRotation();
		
		// rotate sprite by 90 degrees per second 
		rotation += 90 * deltaTime;
		
		// wrap around at 360 degrees
		rotation %= 360;
		
		// set new rotation value to selected sprite
		testSprites[selectedSprite].setRotation(rotation);	
		
	}
	
	
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		updateTestObjects(deltaTime);
		cameraHelper.update(deltaTime);
	}
	
	private void handleDebugInput(float deltaTime)
	{
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
		// moving selected sprite
		
		float sprMoveSpeed = 5 * deltaTime;
		
		if(Gdx.input.isKeyPressed(Keys.A))
			moveSelectedSprite(-sprMoveSpeed, 0);
		if(Gdx.input.isKeyPressed(Keys.D))
			moveSelectedSprite(sprMoveSpeed, 0);
		if(Gdx.input.isKeyPressed(Keys.W))
			moveSelectedSprite(0, sprMoveSpeed);
		if(Gdx.input.isKeyPressed(Keys.S))
			moveSelectedSprite(0, -sprMoveSpeed);
		
		// moving the camera
		
		float cameraSpeed = 5 * deltaTime;
		float cameraAccelarationFactor = 5.0f;
		
		if(Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT))
			cameraSpeed *= cameraAccelarationFactor;
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			moveCamera(-cameraSpeed, 0);
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
			moveCamera(cameraSpeed, 0);
		if(Gdx.input.isKeyPressed(Keys.UP))
			moveCamera(0, cameraSpeed);
		if(Gdx.input.isKeyPressed(Keys.DOWN))
			moveCamera(0, -cameraSpeed);
		
		// zoom the camera
		
		float cameraZoomSpeed = 1 * deltaTime;
		float zoomAccelarationFactor = 5.0f;
		
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			cameraZoomSpeed *= zoomAccelarationFactor;
		if(Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(cameraZoomSpeed);
		if(Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-cameraZoomSpeed);
		if(Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
			
		
		
	}
	
	
	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
	
	private void moveSelectedSprite(float x, float y)
	{
		testSprites[selectedSprite].translate(x, y);
	}

}
