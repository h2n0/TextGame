package uk.fls.h2n0.main.util;

import java.util.List;
import java.util.ArrayList;

import fls.engine.main.util.Font;
import fls.engine.main.util.Renderer;

public class TypeWriter {

	
	private String[] lines;
	private int lineNumber;
	private String currentLine;
	private boolean finished;
	private boolean waiting;
	private boolean hidden;
	private int rate;
	private int tick;
	private int amtOfCurent;
	private int maxWidth;
	
	public static TypeWriter instance;
	
	public TypeWriter(String data, int rate, int maxWidth){
		this.maxWidth = maxWidth;
		this.lines = makeLines(data);
		this.rate = rate;
		this.tick = 0;
		this.amtOfCurent = 0;
		
		if(!nextLine()){
			throw new IllegalStateException("Not a valid line file");
		}
		
	}
	
	
	public void update(){
		if(hidden)return;
		if(!this.waiting && !this.finished){
			if(++this.tick % rate == 0){
				this.amtOfCurent ++;
				if(this.amtOfCurent == this.currentLine.length()){
					this.waiting = true;
				}
			}
		}
	}
	
	public void render(Renderer r, int x, int y){
		if(hidden)return;
		Font.instance.drawString(r, currentLine.substring(0, amtOfCurent), x, y);
	}
	
	public void render(Renderer r){
		if(hidden)return;
		if(this.finished)Font.instance.drawStringCenter(r,  currentLine);
		else Font.instance.drawStringCenter(r,  currentLine.substring(0, amtOfCurent));
	}
	
	public void next(){
		String lastLine = this.currentLine;
		if(this.waiting && !this.finished){
			this.lineNumber += 1;
			if(!this.nextLine()){
				this.finished = true;
				this.currentLine = lastLine;
			}else{
				this.waiting = false;
			}
		}
	}
	
	public boolean isFinished(){
		return this.finished;
	}
	
	public boolean isWaiting(){
		return this.waiting;
	}
	
	public static void newInstance(String data, int rate, int w){
		TypeWriter.instance = new TypeWriter(data, rate, w);
	}
	
	private boolean nextLine(){
		while(this.lineNumber < this.lines.length){
			if(this.lines[this.lineNumber].isEmpty()){
				this.lineNumber++;
			}else{
				this.currentLine = this.lines[this.lineNumber];
				this.amtOfCurent = 0;
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		String res = "";
		for(int i = 0; i < this.lineNumber; i++){
			res += this.lines[i] + "\n";
		}
		return res;
	}
	
	private String[] makeLines(String data){
		String[] tmp = data.split("\n");
		List<String> lines = new ArrayList<String>();
		int off = 0;
		for(int i = 0; i < tmp.length; i++){
			String l = tmp[i];
			off = 0;
			if(l.length() * 8 > this.maxWidth){
				String[] w = l.split(" ");
				String currentLin = "";
				for(int j = 0; j < w.length; j++){
					String cw = w[j];
					if((currentLin.length() + cw.length()) * 8 - off >= this.maxWidth){
						off += this.maxWidth;
						currentLin += "\n";
					}
					currentLin += cw + " ";
				}
				
				if(!currentLin.isEmpty()){
					lines.add(currentLin.trim());
				}
			}else{
				lines.add(l.trim());
			}
		}
		
		String[] res = new String[lines.size()];
		lines.toArray(res);
		return res;
	}
	
	public void hide(){
		this.hidden = true;
	}
	
	public void show(){
		this.hidden = false;
	}
}
