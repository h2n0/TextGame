package uk.fls.h2n0.main;

import fls.engine.main.Init;
import fls.engine.main.input.Input;
import fls.engine.main.io.DataFile;
import uk.fls.h2n0.main.screens.TitleScreen;

@SuppressWarnings("serial")
public class Text extends Init{

	public static int w = 400;
	public static int h = 300;
	public static int s = 1;
	public static DataFile data = new DataFile("options");
	
	public Text(boolean played){
		
		//Regular house keeping for the library used
		super("Text adventrue", w * s, h * s);
		setInput(new Input(this, Input.MOUSE, Input.KEYS));
		useCustomBufferedImage(w, h, false);
		setScreen(new TitleScreen(played));
		skipInit(); 
		
		//Set the 'Played' value to true it it was false, or null
		if(!played)Text.data.setValue("Played", "true");
	}
	
	public static void main(String[] args){
		
		//Check to see if the player has played the game before if not run the into "cinimatic"
		new Text(Text.data.getData("Played")==null?false:Text.data.getData("Played").getBool()).start();
	}
}
