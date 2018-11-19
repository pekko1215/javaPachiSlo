package pachiSlot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BetLight {
	private BufferedImage[] onImage = new BufferedImage[3];
	private BufferedImage[] offImage = new BufferedImage[3];
	private int x;
	private int y;
	private boolean[] lightState = {false,false,false,false,false};
	private int[] lightNums = {3,2,1,2,3};
	private int margin;
	private int[] blinkNum = {0,0,0,0,0};
	private int[] blinkTimer = {0,0,0,0,0};
	
	public BetLight(int x,int y) {
		this.x = x;
		this.y = y;
		this.load();
	}
	
	private void load() {
		for(int i=0;i<3;i++) {
			try {
				System.out.println();
				this.onImage[i] = ImageIO.read(getClass().getResource("Resources/img/bet/"+(i+1)+".png"));
				int width,height;
				width = this.onImage[i].getWidth();
				height = this.onImage[i].getHeight();
				this.margin = height;
				this.offImage[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				for(int x = 0;x < width;x++) {
					for(int y=0;y<height;y++) {
						int c = this.onImage[i].getRGB(x, y);
						int val = (r(c) + g(c) + b(c))/3;
						this.offImage[i].setRGB(x, y, rgb(val,val,val));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void draw(Graphics g) {
		for(int i = 0;i < 5;i++) {
			BufferedImage buff;
			if(lightState[i]) {
				buff = this.onImage[lightNums[i]-1];
			}else {
				buff = this.offImage[lightNums[i]-1];
			}
			g.drawImage(buff, this.x,this.y+this.margin*i,null);
			if(this.blinkNum[i] != 0) {
				if(this.blinkTimer[i] >= this.blinkNum[i]) {
					this.lightState[i] = !this.lightState[i];
					this.blinkTimer[i] = 0;
				}else {
					this.blinkTimer[i]++;
				}
			}
		}
	}
	
	private int r(int c){
		return c>>16&0xff;
	}
	private int g(int c){
		return c>>8&0xff;
	}
	private int b(int c){
		return c&0xff;
	}
	private int rgb(int r,int g,int b){
        return 0xff000000 | r <<16 | g <<8 | b;
    }
	public void setLight(int idx) {
		this.lightState[idx] = true;
	}
	public void setLight(int idx,int timer) {
		this.lightState[idx] = true;
		this.blinkNum[idx] = timer;
		this.blinkTimer[idx] = 0;
	
	}
	public void setLightbyNum(int num) {
		for(int i=0;i<this.lightNums.length;i++) {
			if(this.lightNums[i] == num) {
				this.setLight(i);
			}
		}
	}

	public void clearLight(int idx) {
		this.lightState[idx] = false;
		this.blinkTimer[idx] = 0;
		this.blinkNum[idx] = 0;
	}
	
	public void clearLight() {
		for(int i=0;i<this.lightNums.length;i++) {
			this.clearLight(i);
		}
	}
}
