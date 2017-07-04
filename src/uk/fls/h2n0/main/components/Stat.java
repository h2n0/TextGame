package uk.fls.h2n0.main.components;

import fls.engine.main.util.Font;
import fls.engine.main.util.Renderer;

public class Stat {

	
	public final String name;
	private int cost;
	private float lvlCost;
	private int level;
	private int maxLevel;
	
	public Stat(String name, int base, float lvl, int maxLevel){
		this.name = name;
		this.cost = base;
		this.lvlCost = lvl;
		this.level = 1;
		this.maxLevel = maxLevel;
	}
	
	public void render(Renderer r, int x, int y){
		int w = 10;
		String o = "[";
		for(int i = 0; i < w; i++){
			o += " ";
		}
		o += "]";
		
		String i = "";
		for(int j = 0; j <= 8; j++){
			i += "|";
		}
		String n = name;
		Font.instance.drawString(r, o, x + (n.length() + 1) * 8, y);
		Font.instance.drawString(r, i, x + (n.length() + 2) * 8, y, 0x007700);
		r.fillRect(x + (n.length() + 2) * 8, y, 8 + (int)(((float)this.level / (float)this.maxLevel) * 64f), 8, 0x00FF00);
		Font.instance.drawString(r, n, x, y);
	}
	
	public boolean levelup(int money){
		if(this.level == this.maxLevel || money < this.cost)return false;
		levelupFree();
		return true;
	}
	
	public void levelupFree(){
		this.level ++;
		this.cost = (int)(this.cost * this.lvlCost);
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public int getLevel(){
		return this.level;
	}
}
