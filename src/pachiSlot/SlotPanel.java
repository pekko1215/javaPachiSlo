package pachiSlot;

import java.net.URL;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.scene.input.KeyCode;
import utilities.SoundPlayer;

import java.util.TimerTask;
import java.util.Timer;
public class SlotPanel extends JPanel implements Runnable , KeyListener{
	private Slot slot;
	private Timer timer;
	private Thread thread;
	private ArrayList<BufferedImage> reelChips;
	private final int FPS = 60;
	private double sleepTime = 1000. / this.FPS;
	private final double reelSpeed = 780;
	private int ChipWidth;
	private int ChipHeight;
	private int reelMargin = 30;
	private boolean isFreeze = false;
	
	public SlotPanel(Slot slot){
		this.slot = slot;
		this.addKeyListener(this);
		this.setFocusable(true);
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
	
	private void Pay() {
		this.slot.gameState = GameState.Pay;
		ArrayList<HitEvent> list = this.slot.control.getHit(this.slot.betCoin);
		int pay = 0;
		for(HitEvent e : list) {
			pay += this.slot.getPay(e.yaku);
		}
		System.out.println("pay:"+pay);
		this.PayEffect(pay);
		this.slot.gameState = GameState.BetWait;
	}
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		this.updateReel();
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
					int top = k * reel.reelChipHeight + reelDiff + this.reelMargin;
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
	

	public boolean stopReel(int reel) {
		//重視
		if(this.slot.gameState != GameState.Rolling || this.slot.reel.reelStates[reel] != ReelState.Rolling) return false;
		int pos = this.slot.reel.getReelChar(reel);
		int slip = this.slot.control.reelStop(reel,pos);
		this.slot.reel.Stop(reel,slip);
		PlaySound("stop.wav");
		return true;
	}
	
	private void updateReel() {
		int reelPower = (int)((1000. / FPS ) / reelSpeed * this.slot.reel.getReelHeight());
		boolean stopFlag = true;
		for(int i = 0;i < 3; i++) {
			switch(this.slot.reel.reelStates[i]) {
				case Stop:
					//なにもしない
					break;
				case Rolling:
					this.slot.reel.rollReel(i, reelPower);
					stopFlag = false;
					break;
				case Slipping:
					this.slot.reel.slipReel(i, reelPower);
					stopFlag = false;
			}
		}
		if(stopFlag && this.slot.gameState == GameState.Rolling) {
			this.Pay();
			if(this.slot.isReplay) {
				Replay();
			}
		}
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
	@Override
	public void keyPressed(KeyEvent e) {
		if(this.isFreeze)return;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if(this.slot.gameState == GameState.BetWait) {
				this.Bet(3);
				break;
			}
			if(this.slot.gameState == GameState.Beted) {
				this.LeverOn();
				this.slot.WaitEnd();
				this.Freeze();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Resume();
					}
				}).start();
				break;
			}
			for(int i=0;i<3;i++) {
				if(this.slot.reel.reelStates[i] == ReelState.Rolling) {
					this.stopReel(i);
					return;
				}
			}
			break;
		case KeyEvent.VK_LEFT:
			this.stopReel(0);
			break;
		case KeyEvent.VK_DOWN:
			this.stopReel(1);
			break;
		case KeyEvent.VK_RIGHT:
			this.stopReel(2);
			break;
		case KeyEvent.VK_SHIFT:
			break;
		case KeyEvent.VK_CONTROL:
			break;
		}
	}
	
	private void Bet(int coin) {
		if(!this.slot.isReplay) {
			PlaySound("bet.wav");
		}
		this.slot.Bet(coin);
	}
	
	private void LeverOn() {
		this.slot.LeverOn();
		PlaySound("start.wav");
	}

	private long PlaySound(String name) {
		SoundPlayer s = new SoundPlayer(getClass().getResource("Resources/sound/"+name));
		s.Play();
		long length = s.clip.getFrameLength() * 1000;
		float sampling = s.clip.getFormat().getSampleRate();
		return (int)(length / sampling);
	}
	
	private void LoopSound(String name) {
		new SoundPlayer(getClass().getResource("Resources/sound/"+name),true).Play();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	private void Freeze() {
		this.isFreeze = true;
	}
	
	private void Resume() {
		this.isFreeze = false;
	}
	
	private void Replay() {
		this.Freeze();
		long len = this.PlaySound("replay.wav");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(len);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Bet(slot.betCoin);
				slot.isReplay = false;
				Resume();
			}
		}).start();
	}
	private void PayEffect(int pay) {
		if(pay == 0) return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				Freeze();
				long len = PlaySound("pay.wav");
				try {
					Thread.sleep(len);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Resume();
			}
		}).start();
	}
}
