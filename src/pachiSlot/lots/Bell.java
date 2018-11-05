package pachiSlot.lots;

import pachiSlot.GameMode;
import pachiSlot.Lot;
import pachiSlot.Slot;

public class Bell extends Lot{

	public Slot slot;
	public String name = "ベル";
	public static int getProbability(GameMode mode, int bet) {
		if(mode != GameMode.Normal) return 0;
		return 32768 * 1 / 7;
	}

}
