package pachiSlot.replayTime;

import pachiSlot.Role;
import pachiSlot.Slot;
import pachiSlot.effect.Art;
import pachiSlot.lots.Lot;

public class Normal extends ReplayTime {

	public Normal(Slot slot) {
		super(slot);
		System.out.println("通常移行");
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public Lot onLot(Lot original) {
		return original;
	}

	@Override
	public void onHit(Role role) {
		// TODO 自動生成されたメソッド・スタブ
		switch(role) {
			case リプレイ:
				this.slot.replayTime = new LowReplayTime(this.slot,250);
				this.slot.art = null;
				break;
			case 突入リプレイ1:
			case 突入リプレイ2:
			case 突入リプレイ3:
			case 突入リプレイ4:
				this.slot.replayTime = new HighReplayTime(this.slot,20);
				if(this.slot.art == null) {
					this.slot.art = new Art();
				}else {
					if(this.slot.art.stock > 0) {
						this.slot.art.stock --;
					}
				}
				this.slot.art.nextArt();
				System.out.println(this.slot.art);
		}
	}

}
