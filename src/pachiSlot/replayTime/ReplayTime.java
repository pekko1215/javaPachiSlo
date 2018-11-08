package pachiSlot.replayTime;

import pachiSlot.Role;
import pachiSlot.Slot;
import pachiSlot.lots.Lot;

public abstract class ReplayTime {
	Slot slot;
	public ReplayTime(Slot slot) {
		this.slot = slot;
	}
	abstract public Lot onLot(Lot original);

	abstract public void onHit(Role role);
}
