package uk.fls.h2n0.main.screens.popups;

import java.awt.Graphics;

import fls.engine.main.screen.Screen;
import fls.engine.main.util.Font;
import fls.engine.main.util.Renderer;
import uk.fls.h2n0.main.Text;

public class Popup extends Screen {

	private Screen p;
	private String border;
	private String op;
	private Renderer r;
	private int endY;
	
	private int mx, my;
	
	public Popup(Screen s, String msg, String op){
		super();
		this.p = s;
		this.border = generateBorder("\nError!\n\n" + msg + "\n \n");
		this.op = generateButton(op);
		this.endY = this.border.split("\n").length * 8;
		this.endY = (Text.h - endY) / 2 + this.endY;
	}
	
	public void postInit(){
		this.r = new Renderer(this.game);
	}
	
	@Override
	public void update() {
		if(this.input.isKeyPressed(this.input.space))setScreen(this.p);
		this.mx = this.input.mouse.getX() / 2;
		this.my = this.input.mouse.getY() / 2;
		
		if(this.input.leftMouseButton.justClicked()){
			checkOptions();
		}
	}

	@Override
	public void render(Graphics g) {
		r.fill(0);
		Font.instance.drawStringCenter(r, border);
		drawOptions();
	}
	
	private String generateBorder(String msg){
		String[] lines = msg.split("\n");
		int amt = msg.split("\n").length;
		int wid = 0;
		for(int i = 0; i < amt; i++){
			if(lines[i].length() > wid)wid = lines[i].length();
		}
		boolean colored = false;
		wid += 2;
		amt += 2;
		if(wid % 2 == 1)wid += 1;
		String res = "";
		for(int i = 0; i < amt; i++){
			if(i == 0 || i == amt - 1){
				res += "+";
				for(int j = 0; j < wid; j++){
					res += "=";
				}
				res += "+";
			}else{
				String l = lines[i-1];
				res += "|";
				int off = 0;
				int d = Math.round((float)((wid - l.length())/2));
				for(int j = 0; j < d; j++){
					res += " ";
					off++;
				}
				if(!colored){
					if(l.trim().equals("")){
						res += l;
						off += l.length();
					}else{
						off += l.length();
						l = "%255,100,0%"+l+"%";
						res += l;
						colored = true;
					}
				}else{
					res += l;
					off += l.length();
				}
				for(int j = off; j < wid; j++){
					res += " ";
				}
				res += "|";
			}
			res += "\n";
		}
		return res;
	}
	
	private String generateButton(String op){
		String res = "";
		int padding = 1;
		int w = op.length() + ((padding) * 2) + 2;
		res += generateLine(w, true);
		res += "\n|";
		for(int j = 0; j < padding; j++){
			res += " ";
		}
		res += op;
		for(int j = 0; j < padding; j++){
			res += " ";
		}
		res += "|\n";
		res += generateLine(w, true);
		return res;
	}
	
	private String generateLine(int l, boolean end){
		String res = "";
		for(int i = 0; i < l; i++){
			
			if(end){
				if(i==0||i==l-1) res += "+";
				else res += "=";
			}else{
				if(i==0||i==l-1) res += "|";
				else res += " ";
			}
		}
		return res;
	}
	
	private void drawOptions(){
		Font.instance.drawStringCenter(r, op, this.endY);
	}
	
	private void checkOptions(){
		int ox = this.op.split("\n")[0].length() * 8;
		int cx = (Text.w - ox) / 2;
		if(this.mx >= cx && this.mx <= cx + ox){
			if(this.my >= this.endY && this.my <= this.endY + this.op.split("\n").length * 8){
				System.out.println("HUI");
			}
		}
	}

}
