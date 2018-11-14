package pachiSlot.effect;

import java.util.Random;

import pachiSlot.ControlCode;

public class Art {
	public int stock;
	private final int[] startStockTable = {50,31,10,5,3,1};
	private final int[][] modeMap = {
			{70,30,70,30,70,30},
			{30,70,30,70,30,70},
			{25,25,25,80,80,80},
			{80,80,80,25,25,25},
			{100,100,100,100,100}
	};
	private final int[] modeTable = {30,30,25,10,5};
	private int modeIdx = -1;
	private int[] loopStockTable = {5,10,33};
	private boolean nextStepUp = false;
	public ARTMode baseMode;
	public int[] baseTable;
	public int tableIndex;
	public boolean noLot = false;
	public boolean isPlus = false;
	public Art() {
		this.stock = this.Lot(this.startStockTable);
	}

	public void nextArt() {
		int m = 0;
		if(this.modeIdx == -1 || this.modeIdx == this.baseTable.length) {
			this.baseMapLot();
			System.out.println("ARTテーブル:"+this.tableIndex);
		}
		int r = new Random().nextInt(100);
		if(r < this.baseTable[modeIdx]) {
			m++;
		}
		if(nextStepUp) {
			m++;
			nextStepUp = false;
		}
		this.modeIdx ++;
		ARTMode[] arr = {ARTMode.Low,ARTMode.High,ARTMode.VeryHigh};
		this.baseMode = arr[m];
		System.out.println("ARTモード:"+this.baseMode);
	}

	private void baseMapLot() {
		this.tableIndex = this.Lot(modeTable);
		this.baseTable = modeMap[this.tableIndex];
		this.modeIdx = 0;
	}

	public static int Lot(int[] table) {
		int r = new Random().nextInt(100);
		for(int i = 0;;i++) {
			r -= table[i];
			if(r<0) return i;
		}
	}

	public int onLot(ControlCode rcc) {
		int ret = 0;
		this.isPlus = false;
		int[] table = {0,0,0};
		switch(rcc) {
			case チェリー:
				table[0] = 10;
				table[1] = 30;
				table[2] = 100;
				break;
			case スイカ:
				table[0] = 20;
				table[1] = 50;
				table[2] = 100;
				break;
			case チャンスプラム:
				table[0] = 50;
				table[1] = 90;
				table[2] = 100;
				break;
			case リーチ目リプレイ:
				table[0] = 1;
				table[1] = 3;
				table[2] = 77;
				break;
		}
		int r = new Random().nextInt(100);
		if(r < table[this.baseMode.ordinal()]) {
			ret = 1;
			int loop = loopStockTable[this.baseMode.ordinal()];
			while(new Random().nextInt(100) < loop) ret++;
		}
		if(ret > 0) this.isPlus = true;
		if(ret > 0 && new Random().nextInt(10) == 0) {
			this.nextStepUp = true;
			System.out.println("次回高モード確定");
		}
		this.stock += ret;
		return ret;
	}
}
