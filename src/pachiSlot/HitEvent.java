package pachiSlot;

import java.util.ArrayList;

public class HitEvent {
	public boolean[][] matrix;
	public Yaku yaku;
	public HitEvent(boolean[][] matrix,Yaku yaku) {
		this.matrix = matrix;
		this.yaku = yaku;
	}
}
