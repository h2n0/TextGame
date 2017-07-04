package uk.fls.h2n0.main.screens;

import java.awt.Graphics;

import fls.engine.main.screen.Screen;
import fls.engine.main.util.Font;
import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;
import uk.fls.h2n0.main.R;

public class EndDayScreen extends Screen {

	private GameScreen parent;
	private Renderer r;
	private int mx, my;
	
	public EndDayScreen(GameScreen gameScreen, Renderer r){
		this.parent = gameScreen;
		this.r = r;
	}
	
	@Override
	public void update() {
		this.mx = this.input.mouse.getX();
		this.my = this.input.mouse.getY();
		
		if(this.input.leftMouseButton.justClicked()){
			if(this.mx >= 169 && this.mx <= 169 + 64){
				if(this.my >= 250 && this.my <= 250 + 24){
					this.input.relaseMouseButtons();
					this.parent.gf.currentMoney = 0;
					setScreen(this.parent);
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		r.fill(0);
		Font.instance.drawStringCenter(r, "End of the day", 32);
		
		Font.instance.drawStringCenter(r, "Past 7 Days", 64);
		
		
		int xo = 120;
		int yo = 72 + 24;
		int w = 160;
		
		//Dashes on the bottom of the line
		for(int i = 0; i < 7; i++){
			r.drawLine(xo + (w/ 7) * (i+1) - 10, yo + 120, xo + (w/ 7) * (i+1) - 10, (this.parent.day-1)%7==i?yo:yo + 130, (this.parent.day-1)%7==i?0xFF0000:0xFFFFFF);
		}
		
		//Draws the Axis lines
		r.drawLine(xo, yo, xo, yo + 120);
		r.drawLine(xo, yo + 120, xo + w, yo + 120);
		
		int min = 99999;
		int max = 0;
		
		//Get the min and max values for magic
		for(int i = 0; i < this.parent.gf.money.length; i++){
			int amt = this.parent.gf.money[i];
			min = Math.min(amt, min);
			max = Math.max(amt, max);
		}
		
		Point pp = null;
		
		//Draw the line segments
		for(int i = 0; i < this.parent.gf.money.length-1; i++){
			int amt = this.parent.gf.money[i];
			int namt = this.parent.gf.money[i + 1];
			
			Point p0 = new Point(xo + (w/ 7) * (i + 1) - 10, yo + 120 - (int)(((float)amt / (float)max) * 120f));
			Point p1 = new Point(xo + (w/ 7) * (i + 2) - 10, yo + 120 - (int)(((float)namt / (float)max) * 120f));
			r.drawLine(p0, p1, 0x44CC00);
			r.drawCircle(p0.getIX(), p0.getIY(), 4, 0xAACC00);
			
			
			pp = p1;
		}
		

		r.drawCircle(pp.getIX(), pp.getIY(), 4, 0xAACC00);
		
		int h = 120;
		int lines = 5;
		for(int i = 0; i < lines; i++){
			r.drawLine(xo, yo + (h/lines) * i, xo - 10, yo + (h/lines) * i);
			Font.instance.drawString(r, "" + Math.abs(((max/lines) * (i-lines))), xo - 48, yo + (h/lines) * i - 4);
		}
		
		Font.instance.drawStringCenter(r, "+------+\n| Okay |\n+------+", 250);
		
		Font.instance.drawString(r, R.cusor, this.mx - 11, this.my - 12);
	}

}
