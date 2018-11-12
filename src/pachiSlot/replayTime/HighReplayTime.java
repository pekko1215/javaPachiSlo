package pachiSlot.replayTime;

import pachiSlot.Role;
import pachiSlot.Slot;
import pachiSlot.lots.Lot;
import pachiSlot.lots.Replay;

public class HighReplayTime extends ReplayTime {

	public int game;
	public HighReplayTime(Slot slot,int game) {
		super(slot);
		this.game = game;
		System.out.println("リプレイ高確率RT移行");
		// TODO 自動生成されたコンストラクター・スタブ
	}
	@Override
	public Lot onLot(Lot original) {
		// TODO 自動生成されたメソッド・スタブ
		if(original.name == null) {
			return new Replay();
		}
		return original;
	}

	@Override
	public void onHit(Role role) {
		this.game--;
		if(this.game == 0)this.slot.replayTime = new Normal(this.slot);
	}

}
