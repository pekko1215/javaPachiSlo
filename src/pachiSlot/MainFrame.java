package pachiSlot;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements Runnable,KeyListener {
	private Slot slot;
	private SlotPanel slotPanel;
	public MainFrame(String title,Slot slot) {
		this.slot = slot;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(940,650);
		this.setLocationRelativeTo(null);
		//this.setResizable(false);
		this.setTitle(title);
		this.slotPanel = new SlotPanel(this.slot);
		this.add(this.slotPanel);
		this.setVisible(true);
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
