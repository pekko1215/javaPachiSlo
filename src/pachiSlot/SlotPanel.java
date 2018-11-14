package pachiSlot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import pachiSlot.bonus.BonusType5;
import pachiSlot.effect.EffectManager;
import pachiSlot.lots.Lot;
import pachiSlot.replayTime.*;
import pachiSlot.segment.EffectSegment;
import pachiSlot.segment.PaySegment;
import pachiSlot.segment.SimpleSegment;
import utilities.SoundPlayer;

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
	private final int reelMarginTop = 50;
	private final int reelMarginLeft = 180;
	private boolean isFreeze = false;
	private final int FrameTop = 30;
	private final int FrameBottom = 30;
	private ReelLight reelLight;
	private PaySegment paySegment;
	private SimpleSegment creditSegment;
	private EffectSegment effectSegment;
	private EffectManager effectManager;
	private Navigation navi;
	
	public SlotPanel(Slot slot){
		this.slot = slot;
		this.addKeyListener(this);
		this.setFocusable(true);
		this.loadImage();
		this.Start();
		this.reelLight = new ReelLight(this.slot,reelMarginLeft,reelMarginTop + ChipHeight * 3 + FrameTop + FrameBottom + 10);
		this.paySegment = new PaySegment(3, 490);
		this.creditSegment = new SimpleSegment(98, 490);
		this.effectSegment = new EffectSegment(780, 490);
		this.navi = new Navigation(770, 50, 100);
		this.effectManager = new EffectManager(this.slot, this.reelLight,this.navi);
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
		this.reelLight.clearBlink();
		this.navi.clearNabi();
		ArrayList<HitEvent> list = this.slot.control.getHit(this.slot.betCoin);
		int pay = 0;
		System.out.println(list.size());
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
		this.setEffectSeg(graphics);
		this.paySegment.draw(graphics);
		this.creditSegment.draw(graphics);
		this.effectSegment.draw(graphics);
		this.navi.draw(graphics);
		this.creditSegment.value = "" + slot.credit;
		graphics.drawRect(reelMarginLeft, reelMarginTop, ChipWidth*3, ChipHeight*4 - FrameTop - FrameBottom);
		if(this.reelChips.size() != 0) {
			Reel reel = this.slot.reel;
			for(int i = 0;i < 3; i ++) {
				int nowIndex = reel.getReelChar(i);
				int reelDiff = reel.getReelCharPos(i) - nowIndex * reel.reelChipHeight;
				reelDiff = - reelDiff;
				for(int k = -1;k <= 3;k++) {
					int chip = reel.getReelCharByIndex(i,k);
					int imgIndex = reel.getReelChip(i, chip);
					int top = k * reel.reelChipHeight + FrameTop + reelDiff + this.reelMarginTop;
					switch(k) {
						case 0:
						case 1:
						case 2:
							if(top < reelMarginTop) {
								int tmp = ChipHeight + reelDiff + FrameTop;
								graphics.drawImage(
									this.reelChips.get(imgIndex),
									ChipWidth * i + reelMarginLeft,
									reelMarginTop ,//
									ChipWidth * (i+1) + reelMarginLeft,
									reelMarginTop + tmp,//

									0,
									ChipHeight - tmp,//
									ChipWidth,
									ChipHeight,
									null);
								break;
							}
							graphics.drawImage(
								this.reelChips.get(imgIndex),
								ChipWidth * i + reelMarginLeft,
								top,
								null
							);
							break;
						case -1:
							if(reelDiff != 0 && reelDiff < ChipHeight -  FrameTop) continue;
							int tmp = reelDiff == 0 ? ChipHeight : reelDiff;
							graphics.drawImage(
									this.reelChips.get(imgIndex),

									ChipWidth * i + reelMarginLeft,
									reelMarginTop,
									ChipWidth * (i+1) + reelMarginLeft,
									FrameTop + reelMarginTop,

									0,
									ChipHeight - FrameTop,
									ChipWidth,
									ChipHeight,
									null);
							break;
						case 3:
							if(top - reelMarginTop > ChipHeight * 4 - FrameTop - FrameBottom ) continue;

							int chipH = reelMarginTop + ChipHeight * 4 - FrameTop - FrameBottom - top;
							graphics.drawImage(
									this.reelChips.get(imgIndex),
									ChipWidth * i + reelMarginLeft,
									top ,
									ChipWidth * (i+1) + reelMarginLeft,
									reelMarginTop + ChipHeight * 4 - FrameTop - FrameBottom,

									0,
									0,
									ChipWidth,
									chipH,
									null);
							break;
					}
				}
			}
		}
		reelLight.draw(graphics);
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
				this.Bet();
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
		if(this.slot.art != null && this.slot.art.isPlus) {
			PlaySound("uwanose.wav");
		}
		this.slot.Bet(coin);
		this.paySegment.reset();
	}

	private void Bet() {
		this.Bet(this.slot.getMaxBet());
		this.reelLight.clearReservation();
	}

	private void LeverOn() {
		ControlCode code = this.slot.LeverOn();
		if(this.effectManager.onLeverOn(code)) {
			PlaySound("yokoku.wav");
		}else {
			PlaySound("start.wav");	
		}
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
				slot.Replay(slot.betCoin);
				slot.isReplay = false;
				Resume();
			}
		}).start();
		if(this.slot.art != null && this.slot.art.isPlus) {
			PlaySound("uwanose.wav");
		}
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
		this.paySegment.setPay(pay);
	}
	
	private void setEffectSeg(Graphics g) {
		if(slot.bonusFlag != null) return;
		if(this.slot.gamemode != GameMode.Normal) {
			this.effectSegment.value = "" + ((BonusType5)this.slot.bonus).getLast();
		}else {
			if(slot.gameState != GameState.BetWait)return;
			if(this.slot.replayTime instanceof Normal) {
				this.effectSegment.value = "!CZ";
				return;
			}
			if(this.slot.replayTime instanceof HighReplayTime) {
				this.effectSegment.value = "" + ((HighReplayTime)this.slot.replayTime).game;
				return;
			}
			this.effectSegment.value = "";
		}
	}
}
