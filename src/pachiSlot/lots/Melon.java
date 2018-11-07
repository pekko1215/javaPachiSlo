package pachiSlot.lots;

import java.util.Random;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Lot;
import pachiSlot.Slot;

public class Melon extends Lot{

	public Slot slot;

	public Melon() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public String name = "スイカ";
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 64;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		int r = (new Random()).nextInt(4);
		return r == 0 ? ControlCode.並行スイカ : ControlCode.斜めスイカ;
	}
}
