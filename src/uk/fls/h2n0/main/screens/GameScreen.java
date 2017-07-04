package uk.fls.h2n0.main.screens;

import java.awt.Graphics;

import fls.engine.main.io.FileIO;
import fls.engine.main.screen.Screen;
import fls.engine.main.util.Font;
import fls.engine.main.util.Renderer;
import uk.fls.h2n0.main.Res;
import uk.fls.h2n0.main.Text;
import uk.fls.h2n0.main.components.Stat;
import uk.fls.h2n0.main.util.GameInfo;
import uk.fls.h2n0.main.util.NotificationManger;
import uk.fls.h2n0.main.util.TypeWriter;

public class GameScreen extends Screen {

	public Renderer r;
	public int mx, my;
	
	public String stand;
	public String river;
	public int t;
	private float fade;
	private boolean intro;
	private boolean showStall;
	private String cursor;
	private NotificationManger nm;
	private Stat[] stats;
	private boolean played;
	private int hour;
	private int minute;
	public int day;
	private int mtick;
	public GameInfo gf;
	private  boolean loaded;
	
	public GameScreen(boolean p){
		this.played = p;
	}
	
	
	public void postInit(){
		
		//Have we been on this screen before when loading an old instance
		if(!this.loaded){
			// If not load the defaults in
			this.r = new Renderer(this.game);
			this.stand = FileIO.instance.readInternalFile("/Stand.txt");
			this.river = getRiver();
			this.t = 0;
			this.showStall = true;
			r.renderImage(Res.sprites[0][0], 10, 10, 8);
			this.cursor = "+-+\n| |\n+-+";
			this.loaded = true;
			
	
			this.stats = new Stat[]{new Stat("Juice", 10, 1.1f, 10), new Stat("Lemons", 10, 1.1f, 10), new Stat("People", 10, 1.1f, 10)};
			
			if(!this.played){ // First time
				
				//Load the text into the TypeWriter class
				TypeWriter.newInstance(FileIO.instance.readInternalFile("/intro.txt"), 2, 148);
				this.intro = true;
				Text.data.setValue("Juice", "0");
				Text.data.setValue("Lemons", "0");
				Text.data.setValue("People", "0");
				
				//Playing day 1
				this.day = 1;
				this.hour = 9;
				this.minute = 0;
				
				Text.data.setValue("Days", ""+this.day);
				Text.data.setValue("Hour", ""+this.hour);
				Text.data.setValue("Minute", ""+this.minute);
				this.gf = new GameInfo();
				
			}else{ // Other times
				this.intro = false;
				this.game.hiderCursor();
				
				this.day = Text.data.getData("Days").getInt();
				this.hour = Text.data.getData("Hour").getInt();
				this.minute = Text.data.getData("Minute").getInt();
				this.gf = GameInfo.loadFromFile(Text.data);
				this.fade = 1f;
			}
			
			this.nm = new NotificationManger();
		}else{// Back from the days end screen
			
			//Update the values in the save
			Text.data.setValue("Days", ""+this.day);
			Text.data.setValue("Hour", ""+this.hour);
			Text.data.setValue("Minute", ""+this.minute);
			

			Text.data.setValue("Juice", ""+this.stats[0].getLevel());
			Text.data.setValue("Lemons", ""+this.stats[1].getLevel());
			Text.data.setValue("People", ""+this.stats[2].getLevel());
		}
	}
	
	@Override
	public void update() {
		this.mx = this.input.mouse.getX();
		this.my = this.input.mouse.getY();
		
		this.t = (this.t + 1) % 60;
		if(this.t == 0){
			this.river = getRiver();
		}
		
		if(!this.intro){
			if(this.t % 15 == 0 && showStall){
				if(this.fade < 1)this.fade += 0.05f;
			}
			
			if(this.t == 0){
				this.mtick = (this.mtick + 1) % 2;
				if(this.mtick == 0){
					advanceTime();
				}
			}
			
			if(this.input.isKeyPressed(this.input.space)){
				advanceTime();
				
				if(Math.random() > 0.25f){
					this.gf.currentMoney += 100 * Math.random();
				}
			}
			
			if(this.input.leftMouseButton.justClicked()){
				if(this.mx >= 256 + 12 && this.mx <= 256 + 12 + 80){
					if(this.my >= 216 + 12 && this.my <= 216 + 12 + 24){
						this.gf.addToPot(10);
						this.advanceTime();
					}
				}
			}
			
		}else{
			if(this.input.isKeyPressed(this.input.space)){
				//setScreen(new Popup(this, "This is a test error", "Okay"));
				TypeWriter.instance.next();
			}
			
			if(this.input.leftMouseButton.justClicked()){
				if(this.mx >= 191 && this.mx <= 191 + 16){
					if(this.my >= 172 && this.my <= 172 + 16){
						this.game.hiderCursor();
						TypeWriter.instance.hide();
						this.intro = false;
					}
				}
			}

			TypeWriter.instance.update();
		}
	}

	@Override
	public void render(Graphics g) {
		r.fill(0);
		
		
		if(intro){
			TypeWriter.instance.render(r);
			if(TypeWriter.instance.isFinished()){
				//Make it flash so the user knows they can click it
				drawCursor(168, (t >= 15)?0x999999 :0xFFFFFF);
			}
		}else{
			if(this.fade > 0){
				Font.instance.drawStringCenterWithColor(r, this.stand + this.river, r.larp(0x000000, 0xFFFFFF, this.fade));
			}
			
			Font.instance.drawStringCenter(r, "Day: "+this.day + "\n"+String.format("%02d", (this.hour)) + ":" + String.format("%02d", this.minute), 80);
			
			for(int i = 0; i < stats.length; i++){
				stats[i].render(r, 8, 8 + i * 12);
			}
			
			
			this.nm.render(r, 16, 216);
			
			Font.instance.drawString(r, "|\n|\n|\n|\n|\n|\n|\n|\n|", 200, 216);
			
			Font.instance.drawString(r, "-- Actions --", 256, 216);
			Font.instance.drawString(r, "+--------+\n|  Pick  |\n+--------+", 256 + 12, 216 + 12);

			String money = "Money: "+this.gf.getMoney();
			Font.instance.drawString(r, money, Text.w - 8 - (money.length() * 8), 16, 0x44CC00);

			drawCursor();
		}
		long milli = System.currentTimeMillis();
		int seconds = (int)((milli / 1000) % 60);
		int minutes = (int)((milli / (1000*60)) % 60);
		
		
		float perS = (float)seconds / (float)60f;
		float perM = (float)minutes / (float)60f;
		
		float sec = (float)(((perS * 360)-90) / 180f * Math.PI);
		float min = (float)(((perM * 360)-90) / 180f * Math.PI);
		
		/**
		int l = 40;
		int xo = 40;
		for(int i = -1; i <= 1; i++){
			r.drawLine(40 + i + xo, 40 + i + xo, 40 + xo + (int)(Math.cos(sec) * l), 40 + xo + (int)(Math.sin(sec) * l), 0xFF0000);
		}
		
		for(int i = -1; i <= 1; i++){
			r.drawLine(40 + i + xo, 40 + i + xo, 40 + xo + (int)(Math.cos(min) * l/2), 40 + xo + (int)(Math.sin(min) * l/2), 0x00FF00);
		}**/
	}
	
	//Draw the cursor easiy without having to faff with values
	private void drawCursor(){
		//Offset so that the real cursor is in the middle of the fake cursor
		Font.instance.drawString(r, this.cursor, this.mx - 11, this.my - 12, this.input.leftMouseButton.isHeld()?0x333333:0xFFFFFF);
	}
	
	private void drawCursor(int y, int col){
		Font.instance.drawStringCenter(r, this.cursor, y, col);
	}
	
	//Generates a new river string
	public String getRiver(){
		String s = "";
		float last = 0;
		for(int i = 0; i < 22; i++){
			float r = (float)Math.random();
			if(r + last > 1){
				last = 1 - r;
				s += "_";
			}else{
				s += " ";
				last += r;
			}
		}
		return s;
	}
	
	//Called when time needs to advance and sets the screen to the end day screen
	private void advanceTime(){
		this.minute += 5;
		if(this.minute >= 60){
			this.minute = 0;
			this.hour ++;
			if(this.hour == 17){
				this.gf.addMoney(day);
				newDay();
				setScreen(new EndDayScreen(this, this.r));
			}
		}
	}
	
	
	//Called at the end of the day to do time managment
	public void newDay(){
		this.nm.addLine("Ended day: "+this.day);
		if(this.gf.currentMoney > this.gf.money[this.day%7]){
			this.nm.addBonus("More money than yesterday!");
		}
		this.day++;
		this.hour = 9;
		this.minute = 0;
		
		Text.data.setValue("Money", ""+this.gf.getMoney());
		int[] pdays = this.gf.money;
		String pd = "";
		for(int i = 0; i < pdays.length; i++){
			pd = pd + pdays[i] + ",";
		}
		pd = pd.substring(0, pd.length() - 1);
		Text.data.setValue("Pdays", pd);
	}

}
