package pachiSlot;

import java.util.ArrayList;

public class HitEvent {
	public boolean[][] matrix;
	public Yaku yaku;
	public int line;
	public HitEvent(boolean[][] matrix,Yaku yaku,int line) {
		this.matrix = matrix;
		this.line = line;
		this.yaku = yaku;
	}
}
