package main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class jack extends Game {
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer shapeRenderer;
	
	
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		this.setScreen(new MainMenuScreen(this));
	}
	
	public void render () {
		super.render();
	}
	
	public void dispose(){
		batch.dispose();
		font.dispose();
	}
}
