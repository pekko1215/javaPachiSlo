package pachiSlot.lots;

import java.util.Random;

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
		ControlCode arr[] = {ControlCode.赤8枚,ControlCode.青8枚,ControlCode.BAR8枚};
		return arr[new Random().nextInt(arr.length)];
	}
}
