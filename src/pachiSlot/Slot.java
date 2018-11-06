package pachiSlot;
import java.net.URL;

public class Slot {
	public int betCoin;
	public GameMode gamemode = GameMode.Normal;

	public Control control;
	public Reel reel;

	public Slot(URL url) {
		this.control = new Control(url);
	}

}
