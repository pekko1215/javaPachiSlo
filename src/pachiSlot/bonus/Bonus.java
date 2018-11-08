package pachiSlot.bonus;

import pachiSlot.Slot;

public abstract class Bonus {
	private Slot slot;
	abstract public void onPay(int coin);
	abstract public void onStart();
	public Bonus(Slot slot) {
		this.slot = slot;
	}
}
