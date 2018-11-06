package pachiSlot;

import java.util.ArrayList;

public class Reel {
	private ArrayList<ArrayList<Integer>> reelArray;
	public int[] reelCharPos = {0,0,0};
	public int reelChipHeight;
	public int reelLength;
	public Reel(ArrayList<ArrayList<Integer>> reelArray) {
		this.reelArray = reelArray;
		this.reelLength = reelArray.get(0).size();
	}
	public int getReelHeight() {
		return this.reelLength * this.reelChipHeight;
	}
	public void rollReel(int reel,int value) {
		this.reelCharPos[reel] += value;
		if(this.reelCharPos[reel] > getReelHeight()) {
			this.reelCharPos[reel] -= this.reelCharPos[reel]; 
		}
		if(this.reelCharPos[reel] < 0) {
			this.reelCharPos[reel] += this.reelCharPos[reel];
		}
	}
	public int getReelCharPos(int reel) {
		return this.reelCharPos[reel];
	}
}
