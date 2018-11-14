package pachiSlot.lots;

import java.util.Random;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Slot;
import pachiSlot.replayTime.BonusReplayTime;

public class BIG extends Lot{

	public BIG () {
		this.name = "BIG";
	}
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 180;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		int r = (new Random()).nextInt(13);
		if(slot.bonusFlag == this) {
			return ControlCode.BIG;
		}
		slot.bonusFlag = this;
		slot.replayTime = new BonusReplayTime(slot);
		switch(r) {
			case 0:
			case 1:
			case 2:
				return ControlCode.チャンスプラム;
			case 6:
			case 7:
				return ControlCode.スイカ;
			case 8:
				return ControlCode.チェリー;
			case 9:
			case 10:
			case 11:
			case 12:
			case 3:
			case 4:
			case 5:
				return ControlCode.リーチ目リプレイ;

		}
		return null;
	}
}
