package pachiSlot;
public class Main {
	private Slot slot;
	private MainFrame frame;
	public static void main(String[] args) {
		Main main = new Main();
	}
	public Main() {
		this.slot = new Slot(getClass().getResource("Resources/bin/control.smr"));
		this.frame = new MainFrame("パチスロ",slot);
		int a,b,c;
		this.slot.control.setReelControlCode(19);
		a = this.slot.control.reelStop(0, 6);
		b = this.slot.control.reelStop(1, 7);
		c = this.slot.control.reelStop(2, 0);
		System.out.printf("%d %d %d",a,b,c);
		//高度なコード
	}

}
