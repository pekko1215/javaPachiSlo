package pachiSlot;

import java.util.ArrayList;

public class Reel {
	private ArrayList<ArrayList<Integer>> reelArray;
	public int[] reelCharPos = {0,0,0};
	public int reelChipHeight;
	public int reelLength;
	public ReelState[] reelStates = {ReelState.Stop,ReelState.Stop,ReelState.Stop};
	public int[] slipLengths = {0,0,0};
	
	public Reel(ArrayList<ArrayList<Integer>> reelArray) {
		this.reelArray = reelArray;
		this.reelLength = reelArray.get(0).size();
	}
	public int getReelHeight() {
		return this.reelLength * this.reelChipHeight;
	}
	public void rollReel(int reel,int value) {
		value = -value;
		this.reelCharPos[reel] += value;
		if(this.reelCharPos[reel] > getReelHeight() - 1) {
			this.reelCharPos[reel] -= this.getReelHeight(); 
		}
		if(this.reelCharPos[reel] < 0) {
			this.reelCharPos[reel] += this.getReelHeight();
		}
	}
	public int getReelChar(int reel) {
		return this.reelCharPos[reel] / this.reelChipHeight;
	}
	
	public int getReelCharByIndex(int reel,int index) {
		index = this.getReelChar(reel) + index;
		if(index < 0) index += this.reelLength;
		if(index >= this.reelLength) index -= this.reelLength;
		return index;
	}
	
	
	public int getReelChip(int reel) {
		return this.reelArray.get(reel).get(this.getReelChar(reel));
	}
	public int getReelChip(int reel,int index) {
		return this.reelArray.get(reel).get(index);
	}
	public int getReelCharPos(int reel) {
		return this.reelCharPos[reel];
	}
	public void slipReel(int reel, int reelPower) {
		reelPower = reelPower;
		if(reelPower > this.slipLengths[reel]) reelPower = this.slipLengths[reel];
		this.slipLengths[reel] -= reelPower;
		this.rollReel(reel, reelPower);
		if(this.slipLengths[reel] == 0) {
			this.reelStates[reel] = ReelState.Stop;
		}
	}
	public void Stop(int reel,int slip) {
		if(this.reelStates[reel] != ReelState.Rolling) return;
		this.slipLengths[reel] = slip * this.reelChipHeight +(this.reelCharPos[reel] - this.getReelChar(reel) * this.reelChipHeight);
		this.reelStates[reel] = ReelState.Slipping;
	}
	public void Start() {
		for(int i=0;i<3;i++) {
			this.reelStates[i] = ReelState.Rolling;
		}
	}
}
