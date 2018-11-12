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
	private int reservation = 0;
	private int[] reelStopArr = {-1,-1,-1};
	public ReelLight(Slot slot,int top,int left) {
		this.slot = slot;
		this.top = top;
		this.left = left;
		loadImage();
		// TODO Auto-generated constructor stub
	}
	
	public void draw(Graphics g) {
		int stoped = 3 - this.slot.reel.getReelCount(ReelState.Rolling);
		BufferedImage image = null;
		for(int i = 0;i < 3;i++) {
			if(this.reelStopArr[i] == -1) {
				if(this.slot.reel.reelStates[i] != ReelState.Rolling) {
					this.reelStopArr[i] = stoped;
				}
			}else {
				if(stoped == 0) this.reelStopArr[i] = -1;
			}
			if(this.reelStopArr[i] != -1 && this.reelStopArr[i] <= this.reservation) {
				image = this.offImage;
			}else {
				image = this.onImage;
			}
			g.drawImage(image, top + image.getWidth()*i, left, null);
			//System.out.printf("%d %d %d%n",reelStopArr[0],reelStopArr[1],reelStopArr[2]);
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
	
	public void clearReservation() {
		this.reservation = 0;
		this.reelStopArr[0] = -1;
		this.reelStopArr[1] = -1;
		this.reelStopArr[2] = -1;
	}
	
	public void setTurnOffReservation(int r) {
		this.reservation = r;
	}
}
