package pachiSlot.lots;

import java.util.Random;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Slot;
import pachiSlot.replayTime.Normal;

public class Replay extends Lot{

	public Replay() {
		this.name = "リプレイ";
	}
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 7;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		if(slot.bonusFlag != null && new Random().nextInt(4)<3) {
			return ControlCode.リーチ目リプレイ;
		}
		if(slot.replayTime instanceof Normal) {
			ControlCode arr[] = {
					ControlCode.突入左,
					ControlCode.突入中,
					ControlCode.突入右,
			};
			return arr[new Random().nextInt(3)];
		}
		return ControlCode.リプレイ;
	}
}
