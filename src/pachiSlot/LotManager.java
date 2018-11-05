package pachiSlot;

import java.util.ArrayList;
import java.util.Random;

public class LotManager {
	private ArrayList<Lot> lots;
	public LotManager() {
		this.lots = new ArrayList<Lot>();
	}
	public void add(Lot lot) {
		this.lots.add(lot);
	}
	public Lot lot(LotAction action) {
		int r = (new Random()).nextInt(32768-1);
		int i;
		for(i = 0;i < this.lots.size();i ++ ) {
			r -= action.lot(this.lots.get(i));
			if(r < 0) break;
		}
		return r < 0 ? lots.get(i) : new Lot();
	}
	interface LotAction {
		public int lot(Lot lot);
	}
}
