package pachiSlot.effect;

import java.awt.Graphics;
import java.util.Random;

import pachiSlot.ControlCode;
import pachiSlot.ReelLight;
import pachiSlot.Slot;
import pachiSlot.lots.Lot;

public class EffectManager {
	private Slot slot;
	private ReelLight reelLight;
	public EffectManager(Slot slot,ReelLight reelLight) {
		this.slot = slot;
		this.reelLight = reelLight;
	}
	
	public void onLeverOn(ControlCode code) {
		this.reelLight.setTurnOffReservation(new Random().nextInt(4));
	}
	
	public void draw(Graphics g) {
		
	}
}
