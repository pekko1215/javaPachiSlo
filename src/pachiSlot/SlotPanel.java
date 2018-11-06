package pachiSlot;

import java.net.URL;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SlotPanel extends JPanel{
	private Slot slot;
	private ArrayList<BufferedImage> reelChips;
	public SlotPanel(Slot slot){
		this.slot = slot;
		this.loadImage();
	}
	private void loadImage() {
		URL url = getClass().getResource("Resources/img/reelchip.png");
		BufferedImage buffer;
		try {
			buffer = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.reelChips = this.splitImages(buffer);
	}
	private ArrayList<BufferedImage> splitImages(BufferedImage buffer){
		ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
		this.slot.control.chip
		return list;
	}
}
