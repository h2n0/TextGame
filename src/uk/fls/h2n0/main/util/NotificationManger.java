package uk.fls.h2n0.main.util;

import fls.engine.main.util.Font;
import fls.engine.main.util.Renderer;

public class NotificationManger {

	
	private String[] lines;
	private int[] types;
	
	public NotificationManger(){
		this.lines = new String[6];
		this.types = new int[6];
	}
	
	
	public void render(Renderer r, int x, int y){
		Font.instance.drawString(r, "-- Notifications --", x, y);
		for(int i = 0; i < this.lines.length; i++){
			if(this.lines[i] == null || this.lines[i].length() == 0)continue;
			Font.instance.drawString(r, this.lines[i], x, y + 8 * (2 + i), r.getShadedColor(this.types[i]==0?0xFFFFFF:this.types[i]==1?0xFF0000:0xCCCC00, 1 - (0.1f + (0.125f * i))));
		}
	}
	
	public void addLine(String line){
		if(this.lines==null){
			this.lines = new String[6];
			this.types = new int[6];
		}
		for(int i = lines.length-1; i > 0; i--){
			this.lines[i] = this.lines[i - 1];
			this.types[i] = this.types[i - 1];
		}
		this.lines[0] = line;
		this.types[0] = 0;
	}
	
	public void addError(String error){
		addLine(error);
		this.types[0] = 1;
	}
	
	public void addBonus(String bonus){
		addLine(bonus);
		this.types[0] = 2;
	}
}
