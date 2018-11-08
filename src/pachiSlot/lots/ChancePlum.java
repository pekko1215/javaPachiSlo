package pachiSlot.lots;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Slot;

public class ChancePlum extends Lot{

	public Slot slot;

	public String name = "チャンスプラム";
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		int p = new BIG().getProbability(slot) / 3 * 2;
		return p;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		return ControlCode.チャンスプラム;
	}
}
