package pachiSlot;
import java.net.URL;

import pachiSlot.bonus.Bonus;
import pachiSlot.bonus.BonusType5;
import pachiSlot.effect.Art;
import pachiSlot.lots.BIG;
import pachiSlot.lots.Bell;
import pachiSlot.lots.ChancePlum;
import pachiSlot.lots.Lot;
import pachiSlot.lots.LowCherry;
import pachiSlot.lots.Melon;
import pachiSlot.lots.Replay;
import pachiSlot.replayTime.LowReplayTime;
import pachiSlot.replayTime.Normal;
import pachiSlot.replayTime.ReplayTime;
import utilities.SoundPlayer;

public class Slot {
	public int coin = 0;
	public int betCoin;
	public int playCount = 0;
	public GameMode gamemode = GameMode.Normal;
	public Control control;
	public Reel reel;
	private LotManager lotManager = new LotManager();
	public GameState gameState = GameState.BetWait;
	public Lot bonusFlag = null;
	public ControlCode controlCode;
	public boolean isReplay = false;
	public Bonus bonus = null;
	public ReplayTime replayTime = new LowReplayTime(this,250);
	public int credit = 50;
	public Art art;

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
		this.lotManager.add(new LowCherry());
		this.lotManager.add(new ChancePlum());
		this.lotManager.add(new BIG());
	}


	public boolean Bet(int coin) {
		if(this.gameState != GameState.BetWait) return false;
		this.betCoin = coin;
		this.coin -= this.betCoin;
		this.gameState = GameState.Beted;
		this.addCredit(-coin);
		return true;
	}

	public ControlCode LeverOn() {
		if(this.gameState != GameState.Beted) return null;
		Lot lot = this.Lottery();
		this.controlCode = lot.getControlCode(this);
		if(this.art != null) {
			System.out.println("ストック数:"+this.art.stock);
			if(!this.art.noLot) {
				if(this.bonusFlag != null) {
					this.art.noLot = true;
				}
				ControlCode tmp = this.controlCode;
				if(this.bonusFlag != null) {
					tmp = ControlCode.BIG;
				}
				int stock = this.art.onLot(tmp);
				if(stock != 0) {
					System.out.println("ストック"+stock+"獲得");
				}
			}else {
				this.art.isPlus = false;
			}
			if(this.bonusFlag == null) this.art.noLot = false;
		}
		if(this.gamemode == GameMode.Bonus) {
			this.playCount = 0;
		}else {
			this.playCount++;
		}
		this.control.setReelControlCode(this.controlCode.ordinal());
		this.gameState = GameState.Wait;
		return this.controlCode;
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
				lot = this.replayTime.onLot(lot);
				break;
			case Bonus:
				lot = new Bell();
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
				if(this.betCoin == 3) {
					this.isReplay = true;
				}else {
					pay =  14;
				}
				break;
			case スイカ:
				arr[0] = 0;
				arr[1] = 0;
				arr[2] = 3;
				pay = arr[this.betCoin - 1];
				break;
			case チェリー1:
			case チェリー2:
			case チェリー3:
				this.isReplay = true;
				break;
			case 赤8枚役:
			case 青8枚役:
			case BAR8枚役:
				pay = this.betCoin == 3 ? 8 : 15;
				break;
			case 赤7:
			case 青7:
			case BAR:
				this.gamemode = GameMode.Bonus;
				this.bonusFlag = null;
				this.bonus = new BonusType5(this,210,yaku.role);
				pay = 0;
				break;
			case 突入リプレイ1:
			case 突入リプレイ2:
			case 突入リプレイ3:
			case 突入リプレイ4:
			case 突入リプレイ5:
			case リーチ目リプレイ1:
			case リーチ目リプレイ2:
			case リーチ目リプレイ3:
			case リーチ目リプレイ4:
			case リーチ目リプレイ5:
			case リーチ目リプレイ6:
			case リーチ目リプレイ7:
				isReplay = true;
		}
		if(this.bonus != null && pay != 0) {
			this.bonus.onPay(pay);
		}
		System.out.println(yaku.role);
		this.replayTime.onHit(yaku.role);
		this.addCredit(pay);
		coin += pay;
		return pay;
	}

	public int getMaxBet() {
		switch(this.gamemode) {
			case Normal:
				return 3;
			case Bonus:
				return 2;
		}
		return 0;
	}

	private void addCredit(int coin) {
		this.credit += coin;
		if(this.credit < 0)this.credit = 0;
		if(this.credit > 50)this.credit = 50;
	}

	public void Replay(int coin) {
		if(this.gameState != GameState.BetWait) return;
		this.betCoin = coin;
		this.gameState = GameState.Beted;
	}
}
