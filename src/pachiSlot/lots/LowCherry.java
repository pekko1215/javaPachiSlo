package pachiSlot.lots;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Slot;

public class LowCherry extends Lot{

	public LowCherry() {
		this.name = "弱チェリー";
	}
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 64;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		return ControlCode.弱チェリー;
	}
}
