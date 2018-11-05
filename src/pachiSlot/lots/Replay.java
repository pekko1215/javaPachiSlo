package pachiSlot.lots;

import pachiSlot.GameMode;
import pachiSlot.Lot;
import pachiSlot.Slot;

public class Replay extends Lot{

	public Slot slot;

	public Replay() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public String name = "リプレイ";
	public static int getProbability(GameMode mode, int bet) {
		if(mode != GameMode.Normal) return 0;
		return 32768 * 1 / 7;
	}

}
