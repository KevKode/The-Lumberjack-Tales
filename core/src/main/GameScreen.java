package main;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	final jack game;
	
	
	Texture zone;
	Texture sqL;
	Texture sqR;
	Texture jack;
	Texture blood;
	Texture kills;
	Texture healthy;
	Texture restart;
	Texture mainmenu;
	Texture chopL;
	Texture chopR;
	Music music;
	Music low;
	Sound squeak;
	Sound grunt;
	Sound pop;
	Rectangle axe;
	Rectangle lumber;
	Array<RectangleSq> squr;
	Array<RectangleBl> bloods;
	long lastSqSpawnTime;
	long lastKillTime;
	long lastGruntTime;
	long lastHarmTime;
	int sqKilled;
	int health;
	
	//animation
	int cols=4;
	int rows=1;
	Texture jackspritesheet;
	Texture jackspritesheet2;
	Animation walkAnimation;
	TextureRegion[] walkRFrames;
	TextureRegion[] walkLFrames;
	TextureRegion currentFrame;
	float stateTime;

	public GameScreen(final jack gam) {
		game=gam;
		health=100;
		
		//load the zone, lumberjack, and squirrel 
		zone=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/zone.png"));
		jack=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/jack.png"));
		sqL=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/sq.png"));
		sqR=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/sqR.png"));
		blood=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/blood.png"));
		healthy=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/health.png"));
		kills=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/kills.png"));
		restart=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/restart.png"));
		mainmenu=new Texture(Gdx.files.internal("The Lumberjack Tales/screens/mainmenu.png"));
		
		//load the music and the squeak and the huh
		music=Gdx.audio.newMusic(Gdx.files.internal("The Lumberjack Tales/sounds/tavern.mp3"));
		music.setLooping(true);
		low=Gdx.audio.newMusic(Gdx.files.internal("The Lumberjack Tales/sounds/low.mp3"));
		low.setLooping(true);
		squeak=Gdx.audio.newSound(Gdx.files.internal("The Lumberjack Tales/sounds/squeak.mp3"));
		grunt=Gdx.audio.newSound(Gdx.files.internal("The Lumberjack Tales/sounds/huh.mp3"));
		pop=Gdx.audio.newSound(Gdx.files.internal("The Lumberjack Tales/sounds/pop.mp3"));
		
		//make the lumberjacks rectangle
		lumber=new Rectangle();
		lumber.x=175;
		lumber.y=85;
		lumber.height=130;
		lumber.width=80;
		
		//make the axes rectangle
		axe=new Rectangle();
		axe.x=lumber.x+120;
		axe.y=85;
		axe.height=45;
		axe.width=45;
		
		//make an array of squirrel rectangles and spawns the first squirrel
		squr=new Array<RectangleSq>();
		spawnSqOriginal();
		
		//makes an array of blood rectangles
		bloods=new Array<RectangleBl>();
		
		//animation
		jackspritesheet=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/spritesheet.png"));
		jackspritesheet2=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/spritesheet2.png"));
		chopR=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/chopR.png"));
		chopL=new Texture(Gdx.files.internal("The Lumberjack Tales/sprites/chopL.png"));
		TextureRegion[][] tmpR = TextureRegion.split(jackspritesheet, jackspritesheet.getWidth()/cols, jackspritesheet.getHeight()/rows);
		TextureRegion[][] tmpL = TextureRegion.split(jackspritesheet2, jackspritesheet2.getWidth()/cols, jackspritesheet2.getHeight()/rows);
		walkRFrames = new TextureRegion[cols * rows];
		walkLFrames= new TextureRegion[cols * rows];
		
		int x = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                walkRFrames[x++] = tmpR[i][j];
            }
        }
        
		int y = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = cols-1; j >= 0; j--) {
                walkLFrames[y++] = tmpL[i][j];
            }
        }
       
        walkAnimation = new Animation(.15f, walkRFrames); 
        stateTime = 0f;
	}
	
	//spawns first squirrel in the same spot as the main menu screen squirrel
	private void spawnSqOriginal() {
		RectangleSq sq = new RectangleSq(lumber);
        sq.x = 800;
        sq.y = 80;
        sq.width = 55;
        sq.height = 55;
        squr.add(sq);
        lastSqSpawnTime = TimeUtils.nanoTime();
	}

	//spawns squirrels
	private void spawnSq() {
		RectangleSq sq = new RectangleSq(lumber);
		if (sq.spawnType==0)
			sq.x = lumber.x+15;//spawns squirrels under lumberjack 50% of the time
		else {
			if (sq.sqZoner==1){
				sq.x=sq.zoneRight(lumber);//spawns the squirrels in the right zone 25% of the time if both zones are visible 
			}
			else{						 
				sq.x=sq.zoneLeft(lumber);//spawns the squirrels in the left zone 25% of the time if both zones are visible 
			}
		}
        sq.y = -55;
        sq.width = 55;
        sq.height = 55;
        squr.add(sq);
        lastSqSpawnTime = TimeUtils.nanoTime();
	}
	
	//spawns blood
	private void spawnBlood(float x, float y) {
		RectangleBl blood=new RectangleBl();
		blood.x=x;
		blood.y=y;
		blood.width=70;
		blood.height=115;
		blood.BloodSpawnTime=TimeUtils.nanoTime();
		bloods.add(blood);
	}
 

	public void render(float delta) {
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		
		//draws current state
		game.batch.begin();
		game.batch.draw(zone, 0, 0);
		if (Gdx.input.isKeyPressed(Keys.SPACE) && walkAnimation.getKeyFrames()==walkRFrames) 
			game.batch.draw(chopR, lumber.x-10, lumber.y);
		else if (Gdx.input.isKeyPressed(Keys.SPACE) && walkAnimation.getKeyFrames()==walkLFrames)
			game.batch.draw(chopL, lumber.x-80, lumber.y);
		else{
			if(walkAnimation.getKeyFrames()==walkRFrames)
				game.batch.draw(currentFrame, lumber.x, lumber.y, lumber.width+40, lumber.height);
			else
				game.batch.draw(currentFrame, lumber.x-40, lumber.y, lumber.width+40, lumber.height);
		}
		for (RectangleSq s : squr) {
			if(s.x>s.nextX)
				game.batch.draw(sqL, s.x, s.y, s.width, s.height);
			else
				game.batch.draw(sqR, s.x, s.y, s.width, s.height);
		}
        for (RectangleBl bl : bloods)
        	game.batch.draw(blood, bl.x, bl.y, bl.width, bl.height);
        game.batch.draw(restart, 795, 515);
        game.batch.draw(mainmenu, 550, 550);
        game.batch.draw(kills, 25, 545);
        game.batch.draw(healthy, 25, 495);
        game.font.draw(game.batch,""+sqKilled, 175, 600);
        game.font.draw(game.batch,""+health, 175, 545);
		game.batch.end();
		
		/*//draws boxes
		game.shapeRenderer.begin(ShapeType.Line);//outline
		game.shapeRenderer.setColor(0, 1, 0, 0);//green
		game.shapeRenderer.rect(lumber.x, 0, lumber.width, 80);//draws underjack spawn box
		game.shapeRenderer.rect(lumber.x-400, 0, 200+55, 80);//draws left spawn box
		game.shapeRenderer.rect(lumber.x+lumber.width+200, 0, 200+55, 80);//draws right spawn box
		game.shapeRenderer.rect(lumber.x-650, 80, 300+55, 55);//draws left move box
		game.shapeRenderer.rect(lumber.x+lumber.width+350, 80, 300+55, 55);//draws right move box
		game.shapeRenderer.setColor(1, 0, 0, 0);//red
		game.shapeRenderer.rect(lumber.x, lumber.y, lumber.width, lumber.height);//draws lumberjacks hitbox
		game.shapeRenderer.rect(axe.x, axe.y, axe.width, axe.height);//draws axe hitbox
		for (RectangleSq sq : squr)
			game.shapeRenderer.rect(sq.x, sq.y, sq.width, sq.height);//draws squirrels hitboxes
		game.shapeRenderer.end();//*/
		
		//checks win
		if (sqKilled>=100){
			game.setScreen(new EndScreen(game, true));
			dispose();
		}
		
		//checks loss
		if (health<=0){
			game.setScreen(new EndScreen(game, false));
			dispose();
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
		
		//moves lumberjack
		if (Gdx.input.isKeyPressed(Keys.LEFT) && (!(Gdx.input.isKeyPressed(Keys.RIGHT)))){
           lumber.x -= 5;
           walkAnimation = new Animation(.15f, walkLFrames);
           stateTime += Gdx.graphics.getDeltaTime();  
           
           //moves lumberjack slower if chopping
           if (Gdx.input.isKeyPressed(Keys.SPACE)){
        	   lumber.x+=3;//movement of 2
           }
           
           //updates axe
           axe.x=lumber.x-85;
		}
        if (Gdx.input.isKeyPressed(Keys.RIGHT) && (!(Gdx.input.isKeyPressed(Keys.LEFT)))){
           lumber.x += 5; 
           walkAnimation = new Animation(.15f, walkRFrames);
           stateTime += Gdx.graphics.getDeltaTime();  
           
           //moves lumberjack slower if chopping
           if (Gdx.input.isKeyPressed(Keys.SPACE)){
        	   lumber.x-=3;//movement of 2
           }
          
           //updates axe
           axe.x=lumber.x+120;
        }
        /*makes sound if .25s passed
        if (Gdx.input.isKeyPressed(Keys.SPACE)){
        	if (TimeUtils.nanoTime()- lastGruntTime > 400000000){
        		grunt.play();
        		lastGruntTime=TimeUtils.nanoTime();
        	}
        }*/
        
        //binds lumberjack to the screen
        if(lumber.x<0)
        	lumber.x=0;
        if(lumber.x>1008)
        	lumber.x=1008;
        
        //plays low health if health low
        if (health==20)
        	low.play();
        
      
        Iterator<RectangleSq> iterSq=squr.iterator();
        while (iterSq.hasNext()){
        	RectangleSq sq=iterSq.next();
    		if (sq.y<80)//moves squirrels out of ground
    			sq.y+=2.5;
    		else{//moves all squirrels on surface
    			if(sq.move>0){
	        		//System.out.println("called1 "+s.move+" "+s.nextX+" "+s.x);
	    			sq.x+=3;
	    			if(sq.x>=sq.nextX){
	    				//System.out.println("called2 "+s.move+" "+s.nextX+" "+s.x);
	    				sq.update();
	    			}
	    		}
	        	else{
	    			//System.out.println("called3 "+s.move+" "+s.nextX+" "+s.x);
	    			sq.x-=3;
	    			if(sq.x<=sq.nextX){
	    				//System.out.println("called4 "+s.move+" "+s.nextX+" "+s.x);
	    				sq.update();
	    			}
	    		}
    		}
    		//kills squirrels and spawns blood and plays death noise
    		if (sq.overlaps(axe) && Gdx.input.isKeyPressed(Keys.SPACE)){
    			lastKillTime=TimeUtils.nanoTime();
    			squeak.play();
    			spawnBlood(sq.x, sq.y);
    			sqKilled++;
    			iterSq.remove();
    		}
    		//lumberjack loses health if overlapped by squirrel
    		if (sq.overlaps(lumber) && TimeUtils.nanoTime() - lastHarmTime > 75000000){
    			health--;
    			lastHarmTime=TimeUtils.nanoTime();
    			pop.play();
    		}
        }
        
       //makes the squirrels that spawn under the lumberjack follow*/
       for (RectangleSq s : squr){										
        	if(s.spawnType==0)											
        		s.nextX=lumber.x+15;
       }//*/
        
        //removes blood if 1s has passed
        Iterator<RectangleBl> iterBlood=bloods.iterator();
        while (iterBlood.hasNext()){
        	RectangleBl bl=iterBlood.next();
        	if (TimeUtils.nanoTime() - bl.BloodSpawnTime > 1000000000)
        		iterBlood.remove();
        }
        
        /*spawns another squirrel if X seconds has passed*/
        if (TimeUtils.nanoTime() - lastSqSpawnTime > 500000000)//2000000000 2s 1500000000 1.5s 1000000000 1s 750000000 .75 500000000 .5s 250000000 .25s
        	spawnSq();//*/
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
		low.dispose();
		
	}

}
