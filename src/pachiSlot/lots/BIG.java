package pachiSlot.lots;

import java.util.Random;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Lot;
import pachiSlot.Slot;

public class BIG extends Lot{


	public String name = "BIG";
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 128;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		int r = (new Random()).nextInt(9);
		if(slot.bonusFlag == this) {
			return ControlCode.BIG;
		}
		slot.bonusFlag = this;
		switch(r) {
			case 0:
			case 1:
				return ControlCode.滑りなしリーチ目;
			case 2:
			case 3:
				return ControlCode.プラムハズレリーチ目;
			case 4:
			case 5:
				return ControlCode.並行ベルハズレ;
			case 6:
			case 7:
				return ControlCode.斜めベルハズレ;
			case 8:
				return ControlCode.滑りブランクリーチ目;
		}
		return null;
	}
}
