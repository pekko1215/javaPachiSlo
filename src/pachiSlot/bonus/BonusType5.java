package pachiSlot.bonus;

import pachiSlot.GameMode;
import pachiSlot.Role;
import pachiSlot.Slot;
import pachiSlot.replayTime.Normal;

public class BonusType5 extends Bonus {

	public Role role;
	public int max;
	public int payd = 0;
	private Slot slot;
	public BonusType5(Slot slot,int max,Role role) {
		super(slot);
		this.max = max;
		this.role = role;
		this.slot = slot;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onPay(int coin) {
		this.payd += coin;
		if(this.getLast() <= 0) {
			this.slot.bonus = null;
			this.slot.gamemode = GameMode.Normal;
			this.slot.replayTime = new Normal(this.slot);
		}
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onStart() {
		// TODO 自動生成されたメソッド・スタブ

	}

	public int getLast() {
		return this.max - this.payd;
	}

}
