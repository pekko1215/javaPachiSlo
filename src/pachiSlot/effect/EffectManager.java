package pachiSlot.effect;

import java.awt.Graphics;
import java.util.Random;

import pachiSlot.ControlCode;
import pachiSlot.Navigation;
import pachiSlot.ReelLight;
import pachiSlot.Slot;
import pachiSlot.replayTime.HighReplayTime;
import pachiSlot.replayTime.LowReplayTime;
import utilities.SoundPlayer;

public class EffectManager {
	private Slot slot;
	private ReelLight reelLight;
	private final int nabiBlink = 20;
	private Navigation navi;
	
	public EffectManager(Slot slot,ReelLight reelLight,Navigation navi) {
		this.slot = slot;
		this.reelLight = reelLight;
		this.navi = navi;
	}

	public boolean onLeverOn(ControlCode code) {
		int syoto = 0;
		boolean isYokoku = false;
		if(slot.bonusFlag != null)
			code = ControlCode.BIG;
		switch(this.slot.gamemode) {
			case Normal:
				switch(code) {
					case チャンスプラム:
						syoto = this.randomThree(10,0,20,70);
						if(syoto > 0 && new Random().nextInt(5)<4) {
							isYokoku = true;
						}
						break;
					case スイカ:
						syoto = this.randomThree(10,0,70,20);
						if(syoto > 0 && new Random().nextInt(5)<4) {
							isYokoku = true;
						}
						break;
					case チェリー:
						syoto = this.randomThree(10,80,0,10);
						if(syoto > 0 && new Random().nextInt(5)<2) {
							isYokoku = true;
						}
						break;
					case BIG:
						if(this.slot.replayTime instanceof HighReplayTime && new Random().nextInt(4) == 0) {
							ControlCode[] r = {ControlCode.赤8枚,ControlCode.青8枚,ControlCode.BAR8枚};
							this.onLeverOn(r[new Random().nextInt(3)]);
							break;
						}
						syoto = this.randomThree(10,20,10,60);
						if(syoto > 0 && new Random().nextInt(5)<3) {
							isYokoku = true;
						}
						break;
					case リプレイ:
						syoto = this.randomThree(160,39,0,1);
						if(syoto == 3 && new Random().nextInt(5)<4) {
							isYokoku = true;
						}
					case はずれ:
						syoto = this.randomThree(99,0,0,1);
						if(syoto > 0 && new Random().nextInt(5)<4) {
							isYokoku = true;
						}
						break;
					case 突入左:
						this.Play("navi");
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
						this.Play("navi");
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
						this.Play("navi");
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
								return this.onLeverOn(ControlCode.BIG);
							}else {
								this.reelLight.clearBlink();
								this.Play("navi");
								this.reelLight.setBlink(new Random().nextInt(2)+1,nabiBlink);
							}
							break;
						}
					case 赤8枚:
						if(this.slot.replayTime instanceof LowReplayTime == false) {
							this.navi.setNavi(0);
							this.Play("navi");
						}
						break;
					case 青8枚:
						if(this.slot.replayTime instanceof LowReplayTime == false) {
							this.navi.setNavi(1);
							this.Play("navi");
						}
						break;
					case BAR8枚:
						if(this.slot.replayTime instanceof LowReplayTime == false) {
							this.navi.setNavi(2);
							this.Play("navi");
						}
						break;
				}
				break;
			case Bonus:
				break;
		}
		this.reelLight.setTurnOffReservation(syoto);
		return isYokoku;
	}

	public void draw(Graphics g) {
	}
	
	public void Play(String str) {
		new SoundPlayer(getClass().getResource("../Resources/sound/"+str+".wav")).Play();;		
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
