package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class EndScreen implements Screen {
	final jack game;
	boolean win;
	
	Texture zone2;
	Texture zone;
	Texture jack;
	Texture wood;
	Texture woodget;
	Texture youwin;
	Texture gameover;
	Texture youlose;
	Texture restart;
	Texture mainmenu;
	Music music;
	

	
	public EndScreen(jack gam, boolean winner) {
		game=gam;
		win=winner;
		
		zone=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/zone.png"));
		wood=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/wood.png"));
		woodget=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/woodget.png"));
		youwin=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/youwin.png"));
		
		zone2=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/zone2.png"));
		jack=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/deadjack.png"));
		gameover=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/gameover.png"));
		youlose=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/youlose.png"));
		
		restart=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/restart.png"));
		mainmenu=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/mainmenu.png"));
		music=Gdx.audio.newMusic(Gdx.files.internal("The Lumberjack Tales/sounds/over.mp3"));
		music.setLooping(true);
		
		
	}


	public void render(float delta) {
		
		//draws win screen
		if (win){
			game.batch.begin();
			game.batch.draw(zone, 0, 0);
			game.batch.draw(wood, 431, 197, 225, 225);
			game.batch.draw(woodget, 366, 425);
			game.batch.draw(youwin, 394, 110);
			game.batch.draw(restart, 795, 25);
			game.batch.draw(mainmenu, 550, -10);
			game.batch.end();
		}
		
		//draws loss screen
		else{
			game.batch.begin();
			game.batch.draw(zone2,0,0);
			game.batch.draw(jack, 434, 195, 220, 230);
			game.batch.draw(gameover, 385, 400);
			game.batch.draw(youlose, 408, 110);
			game.batch.draw(restart, 795, 25);
			game.batch.draw(mainmenu, 550, -10);
			game.batch.end();
		}
		
		//restarts game
		if (Gdx.input.isKeyPressed(Keys.R)){
			game.setScreen(new GameScreen(game));
			dispose();
		}
		
		//goes to main menu
		if (Gdx.input.isKeyPressed(Keys.M)){
			game.setScreen(new MainMenuScreen(game));
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
