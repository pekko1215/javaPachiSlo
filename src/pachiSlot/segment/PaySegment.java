package pachiSlot.segment;

import java.awt.Graphics;

public class PaySegment extends SimpleSegment{

	private int targetPay;
	private int currentPay;
	private int refreshTimer;
	public boolean isPay = false;
	final private int refreshLate = 2;
	public PaySegment(int x, int y) {
		super(x, y);
	}
	public void setPay(int pay) {
		this.targetPay = pay;
		this.currentPay = 0;
		this.isPay = true;
		this.refreshTimer = 0;
	}
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if(!this.isPay) return;
		if(this.refreshTimer > 0) {
			this.refreshTimer--;
			return;
		}
		this.value = "" + this.currentPay;
		this.value = ("!!" + this.value);
		this.value = this.value.substring(this.value.length() - 2);
		if(this.currentPay == this.targetPay) {
			this.isPay = false;
		}else {
			this.currentPay++;
			this.refreshTimer = this.refreshLate;
		}
	}
}
