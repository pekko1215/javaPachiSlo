package pachiSlot.replayTime;

import pachiSlot.Role;
import pachiSlot.Slot;
import pachiSlot.lots.Lot;

public class LowReplayTime extends ReplayTime {

	public int game;
	public LowReplayTime(Slot slot,int game) {
		super(slot);
		this.game = game;
		System.out.println("リプレイ低確率RT移行");
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public Lot onLot(Lot original) {
		this.game--;
		if(this.game == 0)this.slot.replayTime = new Normal(this.slot);
		// TODO 自動生成されたメソッド・スタブ
		return original;
	}

	@Override
	public void onHit(Role role) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
