package uk.fls.h2n0.main.util;

import fls.engine.main.io.DataFile;

public class GameInfo {

	
	public int[] money;
	public int currentMoney;
	public int total;
	
	
	public GameInfo(){
		this.currentMoney = 0;
		this.money = new int[7];
	}
	
	//Return a new instance of this class with all the values filled in
	public static GameInfo loadFromFile(DataFile df){
		GameInfo gf = new GameInfo();
		gf.total = df.getData("Money")==null?0:df.getData("Money").getInt();
		
		//Make sure that 'Pdays' exists
		if(df.getData("Pdays") != null){
			String[] p = df.getData("Pdays").getString().split(",");
			for(int i = 0; i < p.length; i++){
				gf.money[i] = Integer.parseInt(p[i]);
			}
		}
		return gf;
	}
	
	public void addMoney(int day){
		this.money[day % 7] = this.currentMoney;
		this.total += this.currentMoney;
	}
	
	public void addToPot(int c){
		this.currentMoney += c;
	}
	
	public int getMoney(){
		return this.total + this.currentMoney;
	}
}
