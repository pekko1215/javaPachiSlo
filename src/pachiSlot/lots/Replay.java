package pachiSlot.lots;

import java.util.Random;

import pachiSlot.ControlCode;
import pachiSlot.GameMode;
import pachiSlot.Slot;

public class Replay extends Lot{

	public Slot slot;

	public Replay() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public String name = "リプレイ";
	public int getProbability(Slot slot) {
		if(slot.gamemode != GameMode.Normal) return 0;
		return 32768 * 1 / 7;
	}

	@Override
	public ControlCode getControlCode(Slot slot) {
		if(slot.bonusFlag != null && new Random().nextInt(4)<3) {
			return ControlCode.リーチ目リプレイ;
		}
		return ControlCode.リプレイ;
	}
}
