package pachiSlot.replayTime;

import java.util.Random;

import pachiSlot.Role;
import pachiSlot.Slot;
import pachiSlot.lots.Lot;
import pachiSlot.lots.Replay;

public class BonusReplayTime extends ReplayTime {

	public BonusReplayTime(Slot slot) {
		super(slot);
		System.out.println("ボーナスリプレイ高確率RT移行");
	}
	@Override
	public Lot onLot(Lot original) {
		if(original.name == null && new Random().nextInt(4)<3) {
			return new Replay();
		}
		return original;
	}

	@Override
	public void onHit(Role role) {

	}

}
