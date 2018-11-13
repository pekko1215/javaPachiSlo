package pachiSlot.effect;

import java.awt.Graphics;
import java.util.Random;

import pachiSlot.ControlCode;
import pachiSlot.ReelLight;
import pachiSlot.Slot;
import pachiSlot.replayTime.HighReplayTime;

public class EffectManager {
	private Slot slot;
	private ReelLight reelLight;
	private final int nabiBlink = 20;
	public EffectManager(Slot slot,ReelLight reelLight) {
		this.slot = slot;
		this.reelLight = reelLight;
	}

	public void onLeverOn(ControlCode code) {
		int syoto = 0;
		if(slot.bonusFlag != null)
			code = ControlCode.BIG;
		switch(this.slot.gamemode) {
			case Normal:
				switch(code) {
					case チャンスプラム:
						syoto = this.randomThree(10,0,20,70);
						break;
					case スイカ:
						syoto = this.randomThree(10,0,70,20);
						break;
					case 弱チェリー:
						syoto = this.randomThree(10,80,0,10);
						break;
					case 強チェリー:
						syoto = this.randomThree(0,80,0,20);
						break;
					case BIG:
						syoto = this.randomThree(10,20,10,60);
						break;
					case リプレイ:
						syoto = this.randomThree(80,19,0,1);
					case はずれ:
						syoto = this.randomThree(99,0,0,1);
						break;
					case 突入左:
						if(this.slot.art != null && this.slot.art.stock > 0) {
							this.reelLight.setBlink(0,nabiBlink);
						} else {
							this.reelLight.setBlink(0,nabiBlink);
							this.reelLight.setBlink(1,nabiBlink);
							this.reelLight.setBlink(2,nabiBlink);
						}
						break;
					case 突入中:
						if(this.slot.replayTime instanceof HighReplayTime) break;
						if(this.slot.art != null && this.slot.art.stock > 0) {
							this.reelLight.setBlink(1,nabiBlink);
						} else {
							this.reelLight.setBlink(0,nabiBlink);
							this.reelLight.setBlink(1,nabiBlink);
							this.reelLight.setBlink(2,nabiBlink);
						}
						break;
					case 突入右:
						if(this.slot.replayTime instanceof HighReplayTime) break;
						if(this.slot.art != null && this.slot.art.stock > 0) {
							this.reelLight.setBlink(2,nabiBlink);
						} else {
							this.reelLight.setBlink(0,nabiBlink);
							this.reelLight.setBlink(1,nabiBlink);
							this.reelLight.setBlink(2,nabiBlink);
						}
						break;
					case リーチ目リプレイ:
						if(this.slot.replayTime instanceof HighReplayTime) {
							if(this.slot.art.isPlus) {
								this.onLeverOn(ControlCode.BIG);
							}else {
								this.reelLight.clearBlink();
								this.reelLight.setBlink(new Random().nextInt(2)+1,nabiBlink);
							}
							break;
						}
				}
				break;
			case Bonus:
				break;
		}
		this.reelLight.setTurnOffReservation(syoto);
	}

	public void draw(Graphics g) {

	}

	public int randomThree(int... value) {
		int sum = 0;
		for(int i=0;i<value.length;i++) {
			sum += value[i];
		}
		int r = new Random().nextInt(sum);
		for(int i=0;i<value.length;i++) {
			r-=value[i];
			if(r < 0) {
				return i;
			}
		}
		return -1;
	}
}
