package pachiSlot.lots;

import pachiSlot.GameMode;
import pachiSlot.Lot;
import pachiSlot.Slot;

public class Cherry extends Lot{

	public Slot slot;

	public Cherry() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public String name = "チェリー";
	public static int getProbability(GameMode mode, int bet) {
		if(mode != GameMode.Normal) return 0;
		return 32768 * 1 / 32;
	}

}
