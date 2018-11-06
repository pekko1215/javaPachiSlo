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
	}

}
