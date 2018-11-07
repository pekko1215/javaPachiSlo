package pachiSlot;

import java.net.URL;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.TimerTask;
import java.util.Timer;
public class SlotPanel extends JPanel implements Runnable{
	private Slot slot;
	private Timer timer;
	private Thread thread;
	private ArrayList<BufferedImage> reelChips;
	private final int FPS = 60;
	private double sleepTime = 1000. / this.FPS;
	private final double reelSpeed = 780;
	private int ChipWidth;
	private int ChipHeight;
	
	public SlotPanel(Slot slot){
		this.slot = slot;
		this.loadImage();
		this.Start();
		this.setBackground(Color.gray); 
	}
	private void loadImage() {
		URL url = getClass().getResource("Resources/img/reelchip.png");
		BufferedImage buffer;
		try {
			buffer = ImageIO.read(url);
			this.reelChips = this.splitImages(buffer);
			this.ChipHeight = this.slot.reel.reelChipHeight = this.reelChips.get(0).getHeight();
			this.ChipWidth = this.reelChips.get(0).getWidth();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private synchronized void Start() {
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private synchronized void Stop() {
		if(thread != null) {
			thread = null;
		}
	}
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		int reelPower = (int)((1000. / FPS ) / reelSpeed * this.slot.reel.getReelHeight());
		this.slot.reel.rollReel(0, reelPower);
		this.slot.reel.rollReel(1, reelPower);
		this.slot.reel.rollReel(2, reelPower);
		/*
		 * 描写するのは5つのみ
		 * 枠上、上、中、下、枠下
		 */
		if(this.reelChips.size() != 0) {
			Reel reel = this.slot.reel;
			for(int i = 0;i < 3; i ++) {
				int nowIndex = reel.getReelChar(i);
				int reelDiff = reel.getReelCharPos(i) - nowIndex * reel.reelChipHeight;
				reelDiff = - reelDiff;
				for(int k = -1;k <= 3;k++) {
					int chip = reel.getReelCharByIndex(i,k);
					int imgIndex = reel.getReelChip(i, chip);
					int top = k * reel.reelChipHeight + reelDiff;
					graphics.drawImage(this.reelChips.get(imgIndex), ChipWidth * i, top, null);
				}
			}
		}
	}
	
	private ArrayList<BufferedImage> splitImages(BufferedImage buffer){
		ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
		int length = this.slot.control.chipLength;
		int height = buffer.getHeight() / length;
		
		for(int i=0;i < length;i ++) {
			list.add(buffer.getSubimage(0,i*height,buffer.getWidth(),height));
		}
		return list;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		double nextTime = System.currentTimeMillis() + sleepTime;
		while(thread != null) {
			try {
				long res = (long)nextTime - System.currentTimeMillis();
				if(res < 0) res = 0;
				Thread.sleep(res);
				nextTime += sleepTime;
				repaint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
