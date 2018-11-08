package pachiSlot;
import java.net.URL;

import pachiSlot.lots.BIG;
import pachiSlot.lots.Bell;
import pachiSlot.lots.HighCherry;
import pachiSlot.lots.LowCherry;
import pachiSlot.lots.Melon;
import pachiSlot.lots.Replay;

public class Slot {
	public int betCoin;
	public GameMode gamemode = GameMode.Normal;
	public Control control;
	public Reel reel;
	private LotManager lotManager = new LotManager();
	public GameState gameState = GameState.BetWait;
	public Lot bonusFlag = null;
	public ControlCode controlCode;
	public boolean isReplay = false;

	public Slot(URL url) {
		this.control = new Control(url);
		this.reel = control.reel;
		this.Initialize();
	}

	private void Initialize() {
		this.LotInitialize();

	}
	private void LotInitialize() {
		this.lotManager.add(new Replay());
		this.lotManager.add(new Bell());
		this.lotManager.add(new Melon());
		this.lotManager.add(new HighCherry());
		this.lotManager.add(new LowCherry());
		this.lotManager.add(new BIG());
	}


	public boolean Bet(int coin) {
		if(this.gameState != GameState.BetWait) return false;
		this.betCoin = coin;
		this.gameState = GameState.Beted;
		return true;
	}

	public boolean LeverOn() {
		if(this.gameState != GameState.Beted) return false;
		Lot lot = this.Lottery();
		this.controlCode = lot.getControlCode(this);
		System.out.println(this.controlCode);
		this.control.setReelControlCode(this.controlCode.ordinal());
		this.gameState = GameState.Wait;
		return true;
	}

	public boolean WaitEnd() {
		if(this.gameState != GameState.Wait) return false;
		this.gameState = GameState.Rolling;
		this.reel.Start();
		return true;
	}

	public Lot Lottery() {
		Lot lot = new Lot();
		switch(gamemode) {
			case Normal:
				lot = lotManager.lot((e)->{
					return e.getProbability(this);
				});
				break;
			case Bonus:
				break;
			case Jac:
				break;
		}
		return lot;
	}
	public int getPay(Yaku yaku) {
		int pay = 0;
		int arr[] = new int[3];
		System.out.println(yaku.role);
		switch(yaku.role) {
			case リプレイ:
				this.isReplay = true;
				break;
			case プラム:
				arr[0] = 14;
				arr[1] = 14;
				arr[2] = 1;
				pay = arr[this.betCoin - 1];
				break;
			case スイカ:
				arr[0] = 0;
				arr[1] = 0;
				arr[2] = 3;
				pay = arr[this.betCoin - 1];
				break;
			case 弱チェリー:
			case 強チェリー:
				arr[0] = 0;
				arr[1] = 0;
				arr[2] = 4;
				pay = arr[this.betCoin - 1];
				break;
			case 赤7:
			case 青7:
			case BAR:
				pay = 0;
		}
		return pay;
	}
}
