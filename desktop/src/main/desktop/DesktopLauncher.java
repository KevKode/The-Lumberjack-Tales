package main.desktop;
 
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import main.jack;
 
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="The Lumberjack Tales";
		config.width=1088;
		config.height=620;
		new LwjglApplication(new jack(), config);
	}
}
   