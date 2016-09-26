package main;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class RectangleSq extends Rectangle{
	public float move;
	public float nextX;
	public int spawnType;
	public int sqZoner;
	
	//constructs and sets the 1st move location
	public RectangleSq(Rectangle lumber){

		spawnType=MathUtils.random(0,1);//sets the spawn type to under lumberjack or random loc
		
		//spawns squirrels in zones
		if (spawnType==1){
			sqZoner=MathUtils.random(0,1);//selects the zone
			
			binder();
		}		
		else{
			nextX=lumber.x+15;//controls first move of the squirrels that spawn under the lumberjack
		}
		
	}
	//sets the next move location
	public void update() {
		move=MathUtils.random(-200, 200);
		nextX=x+move;	
		
		binder();
	} 
	
	//spawn squirrels in the left zone
	public float zoneLeft(Rectangle lumber){
		float spawn=MathUtils.random(lumber.x-200, lumber.x-400);//selects the location in the left zone that the squirrel will spawn
		nextX=MathUtils.random(lumber.x-350, lumber.x-600);//first move is 100 within the spawn zone and 300 outside
		binder();
		if (spawn<0){
			return zoneRight(lumber);//if the spawn point is less than 0 spawn the squirrel in the other zone
		}
		return spawn;
	}
	//spawn squirrels in the right zone
	public float zoneRight(Rectangle lumber){
		float spawn=MathUtils.random(lumber.x+lumber.width+200, lumber.x+lumber.width+400);//selects the location in the right zone that the squirrel will spawn
		nextX=MathUtils.random(lumber.x+lumber.width+350, lumber.x+lumber.width+600);//first move is 100 within the spawn zone and 300 outside
		binder();
		if (spawn>1033){
			return zoneLeft(lumber);//if the spawn point is greater than 1033 spawn the squirrel in the other zone
		}
		return spawn;
		
	}
	
	//binds squirrels to screen
	public void binder(){
		if (nextX>1033)
			nextX=1033;
		if (nextX<0)
			nextX=0;
	}
	
}
