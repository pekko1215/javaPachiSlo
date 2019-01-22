package pachiSlot.lots;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Slot;

public class Melon extends Lot{

	public Melon() {
		this.name = "スイカ";
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 64;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		return ControlCode.スイカ;
	}
}
