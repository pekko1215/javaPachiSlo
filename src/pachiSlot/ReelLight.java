package pachiSlot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ReelLight {
	private Slot slot;
	private boolean isLoaded = false;
	private BufferedImage onImage;
	private BufferedImage offImage;
	private int top;
	private int left;
	public ReelLight(Slot slot,int top,int left) {
		this.slot = slot;
		this.top = top;
		this.left = left;
		loadImage();
		// TODO Auto-generated constructor stub
	}
	
	public void draw(Graphics g) {
		for(int i = 0; i < 3; i++) {
			ReelState state = this.slot.reel.reelStates[i];
			BufferedImage image = null;
			switch(state) {
			case Rolling:
				image = this.onImage;
				break;
			case Stop:
			case Slipping:
				image = this.offImage;
				 break;
			}
			g.drawImage(image, top + image.getWidth()*i, left, null);
		}
	}
	
	private void loadImage() {
		try {
			this.onImage = ImageIO.read(getClass().getResource("Resources/img/stop_on.png"));
			this.offImage = ImageIO.read(getClass().getResource("Resources/img/stop_off.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
