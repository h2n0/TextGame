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
	
	public static GameInfo loadFromFile(DataFile df){
		GameInfo gf = new GameInfo();
		gf.currentMoney = df.getData("Money")==null?0:df.getData("Money").getInt();
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
