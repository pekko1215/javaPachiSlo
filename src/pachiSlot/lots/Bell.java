package pachiSlot.lots;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Lot;
import pachiSlot.Slot;

public class Bell extends Lot{

	public String name = "ベル";
	public int getProbability(Slot slot) {
		if(slot.gamemode == GameMode.Normal) {
			return 32768 * 1 / 5;	
		}
		return 32768;
	}
	
	@Override
	public ControlCode getControlCode(Slot slot) {
		return ControlCode.プラム;
	}
}
