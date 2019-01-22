package pachiSlot.segment;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;

public class Segment {
	private int count;
	private Font font;
	public String value = "";
	public int x;
	public int y;
	private Color front;
	private Color back;
	public Segment (int x,int y,int count,float size,Color front,Color back) {
		this.count = count;
		this.back = back;
		this.front = front;
		this.x = x;
		this.y = y;
		try {
			this.font = Font.createFont(Font.TRUETYPE_FONT,getClass().getResource("font/DSEG14Modern-Regular.ttf").openStream()).deriveFont(size);
		} catch (FontFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void draw(Graphics g) {
		g.setFont(this.font);
		this.clear(g);
		g.setColor(front);
		String tmp = this.value;
		for(int i=0;i<this.count;i++) {
			tmp = "!"+tmp;
		}
		tmp = tmp.substring(tmp.length() - this.count);
		g.drawString(tmp,this.x,this.y+g.getFontMetrics().getAscent());
	}
	public void clear(Graphics g) {
		g.setFont(this.font);
		g.setColor(this.back);
		String backStr = "";
		for(int i=0;i<this.count;i++) {
			backStr += "~";
		}
		g.setColor(back);
		g.drawString(backStr, this.x, this.y+g.getFontMetrics().getAscent());
	}
	public void reset() {
		this.value = "";
	}
}
