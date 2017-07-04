package uk.fls.h2n0.main.screens;

import java.awt.Graphics;

import fls.engine.main.screen.Screen;
import fls.engine.main.util.Font;
import fls.engine.main.util.Renderer;
import uk.fls.h2n0.main.R;
import uk.fls.h2n0.main.Text;

public class TitleScreen extends Screen {
	
	private Renderer r;
	private float fade;
	private boolean show;
	private int mx, my;
	private boolean p;
	
	public TitleScreen(boolean played){
		this.p = played;
	}
	
	public void postInit(){
		
		//Initialise new Font and Renderer
		Font.newInstance(this.game);
		this.r = new Renderer(this.game);
	}

	@Override
	public void update() {
		
		//Update the magic value to make it colorful
		this.fade = (this.fade + 0.01f) % 2;
		
		this.mx = this.input.mouse.getX();
		this.my = this.input.mouse.getY();
		
		if(this.input.leftMouseButton.justClicked()){ // If the mouse is just and within the 'cursor' hide the cursor and start the game
			if(mx >= (Text.w-24)/2 && mx <= (Text.w-24)/2 + 24){
				if(my >= 175 && my <= 199){
					//this.game.hiderCursor();
					setScreen(new GameScreen(this.p));
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		//Make sure the screen is all black
		this.r.fill(0);
		
		//Nice bit of linear interpolation
		Font.instance.drawStringCenterWithColor(r, "Lemon Manager", this.r.larp(0xFFFFFF, 0xFFFF00, fade>1f?(2f-this.fade):fade));
		
		//Have we done the cursor trick yet
		if(!show) Font.instance.drawStringCenter(r, R.cusor, 175);
		else Font.instance.drawString(r, R.cusor, mx-11, my-12);
		
		//this.r.fillRect((Text.w-24) / 2, 175,24, 24, 0xFF0000);
	}

}
