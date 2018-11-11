package pachiSlot.segment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EffectSegment extends Segment {

	BufferedImage img;

	public EffectSegment(int x, int y) {
		super(x, y, 3, 56f, new Color(200,0,0),new Color(50,0,0));
		this.value = "!!";
		try {
			this.img = ImageIO.read(getClass().getResource("../Resources/img/effectseg.png"));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	@Override
	public void draw(Graphics g) {
		g.drawImage(this.img,this.x-4,this.y-4,null);
		super.draw(g);
	}
}
