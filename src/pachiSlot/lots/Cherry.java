package pachiSlot.lots;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Lot;
import pachiSlot.Slot;

public class Cherry extends Lot{

	public Slot slot;

	public Cherry() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public String name = "チェリー";
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 32;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		return ControlCode.チェリー;
	}
}
