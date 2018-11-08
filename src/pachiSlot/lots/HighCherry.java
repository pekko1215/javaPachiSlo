package pachiSlot.lots;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Lot;
import pachiSlot.Slot;

public class HighCherry extends Lot{

	public Slot slot;
	public String name = "強チェリー";
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 210;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		return ControlCode.強チェリー;
	}
}
