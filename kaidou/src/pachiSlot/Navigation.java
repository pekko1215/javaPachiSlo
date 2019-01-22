package pachiSlot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Navigation {
	private boolean[] blinkFlag = {false,false,false};
	private boolean[] nabiState = {false,false,false};
	private final int interval = 10;
	private int[] blinkTimer = {0,0,0};
	private BufferedImage[] onImage = new BufferedImage[3];
	private BufferedImage[] offImage = new BufferedImage[3];
	private int x,y,margin;
	
	public Navigation(int x,int y,int margin) {
		String arr[] = {
			"red",
			"blue",
			"bar"
		};
		this.x = x;
		this.y = y;
		this.margin = margin;
		try {
			for(int i=0;i<3;i++) {
				this.onImage[i] = ImageIO.read(getClass().getResource("Resources/img/nabi/"+arr[i]+"_on.png"));
				this.offImage[i] = ImageIO.read(getClass().getResource("Resources/img/nabi/"+arr[i]+"_off.png"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g){
		for(int i=0;i<3;i++) {
			g.drawImage(
					this.nabiState[i] ? this.onImage[i] : this.offImage[i],
					x,
					y + margin*i,
				null);
			if(this.blinkFlag[i]) {
				this.blinkTimer[i]++;
				if(this.blinkTimer[i] == this.interval) {
					this.nabiState[i] = !this.nabiState[i];
					this.blinkTimer[i] = 0;
				}
			}
		}
	}
	
	public void setNavi(int idx) {
		this.blinkFlag[idx] = true;
		this.nabiState[idx] = false;
	}
	
	public void clearNabi() {
		for(int i=0;i<3;i++) {
			blinkFlag[i] = false;
			nabiState[i] = false;
			blinkTimer[i] = 0;
		}
	}
}

