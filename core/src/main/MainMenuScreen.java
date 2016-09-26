package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {
	
	final jack game;
	Texture zone;
	Texture title;
	Texture copy;
	Texture musicby;
	Texture rules;
	Texture spacebar;
	Texture sq;
	Texture jack;
	Texture story;
	Music music;
	
	public MainMenuScreen(final jack gam){
		game=gam;
		
		zone=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/zone.png"));
		title=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/title4.png"));
		sq=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/sq.png"));
		jack=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/jack.png"));
		rules=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/rules.png"));
		spacebar=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/spacebar.png"));
		musicby=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/musicby.png"));
		copy=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/copy.png"));
		story=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/story.png"));
		
		music=Gdx.audio.newMusic(Gdx.files.internal("The Lumberjack Tales/sounds/grave.mp3"));
		music.setLooping(true);
	}
	
	public void render(float delta) {
		 game.batch.begin();
		 game.batch.draw(zone, 0, 0);
		 game.batch.draw(title, 170, 430, 790, 110);
		 game.batch.draw(rules, 350, 235);
		 game.batch.draw(spacebar, 265, 175);
		 game.batch.draw(copy, 10, 10, 10, 10);
		 game.batch.draw(musicby, 5, -15);
		 game.batch.draw(sq, 800, 80, 55, 55);
		 game.batch.draw(jack, 175, 85, 120, 130);
		 game.batch.draw(story, 368, 390);
		 game.batch.end();
		 
		 if (Gdx.input.isKeyPressed(Keys.SPACE)){
			 game.setScreen(new GameScreen(game));
			 dispose();
		 }

	}

	
	public void resize(int width, int height) {
		 

	}

	
	public void show() {
		 music.play();

	}

	
	public void hide() {
		 

	}

	
	public void pause() {
		 

	}

	
	public void resume() {
		 

	}

	
	public void dispose() {
		music.dispose();

	}

}
