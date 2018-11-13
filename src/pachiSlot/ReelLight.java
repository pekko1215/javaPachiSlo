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
	private int[] blinkTimer = {0, 0, 0};
	private int[] blinkReservation = {-1,-1,-1};
	private boolean[] blinkFlag = {false,false,false};
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
			
			if(this.blinkReservation[i] != -1) {
				this.blinkTimer[i]++;
				if(this.blinkTimer[i] == this.blinkReservation[i]) {
					this.blinkFlag[i] = !this.blinkFlag[i];
					this.blinkTimer[i] = 0;
				}
				image = this.blinkFlag[i] ? this.onImage : this.offImage;
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
		this.clearBlink();
	}
	
	
	public void setTurnOffReservation(int r) {
		this.reservation = r;
	}

	public void setBlink(int index, int timer) {
		this.blinkTimer[index] = 0;
		this.blinkReservation[index] = timer;
	}
	
	public void clearBlink(int idx) {
		this.blinkTimer[idx]  = 0;
		this.blinkReservation[idx] = -1;
	}
	
	public void clearBlink() {
		this.reelStopArr[0] = -1;
		this.reelStopArr[1] = -1;
		this.reelStopArr[2] = -1;
		this.clearBlink(0);
		this.clearBlink(1);
		this.clearBlink(2);
	}
}
