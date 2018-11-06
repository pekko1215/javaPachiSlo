package pachiSlot;

import java.net.URL;
import java.util.ArrayList;
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
	
	public SlotPanel(Slot slot){
		this.slot = slot;
		this.loadImage();
		thread = new Thread(this);
		thread.start();
		
	}
	private void loadImage() {
		URL url = getClass().getResource("Resources/img/reelchip.png");
		BufferedImage buffer;
		try {
			buffer = ImageIO.read(url);
			this.reelChips = this.splitImages(buffer);
			this.slot.reel.reelChipHeight = this.reelChips.get(0).getHeight();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void paint(Graphics graphics) {
		this.slot.reel.rollReel(0, 10);
		if(this.reelChips.size() != 0) {
			//System.out.println(this.slot.reel.getReelCharPos(0));
			graphics.drawImage(this.reelChips.get(0), 0, slot.reel.getReelCharPos(0), null);
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
		while(true) {
			repaint();
		}
	}
}
