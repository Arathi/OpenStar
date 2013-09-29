package org.beh.openstar.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import org.beh.openstar.PopstarEngine;

public class StarCanvas extends JPanel implements MouseListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4770885813394751709L;
	public static final Color[] StarColors={
		Color.BLACK,
		Color.YELLOW,
		Color.GREEN,
		Color.RED,
		Color.BLUE,
		new Color(255,0,255)
	};
	
	public static final int Length=22;
	public static final int Border=1;
	public static final int FPS=24;
	public static final int StartX=1;
	public static final int StartY=1;
	public static final double TimeCost=0.5;
	public static final double Step=(Length+2*Border)/FPS/TimeCost;
	
	public static final int PSEHeight=PopstarEngine.Height;
	public static final int PSEWidth=PopstarEngine.Width;
	
	private int frame;
	private PopstarEngine pse;
	private boolean controlLock;
	private Thread thread;

	/**
	 * Create the panel.
	 */
	public StarCanvas() {
		frame=0;
		//step=
		controlLock=false;
		
		pse=new PopstarEngine();
		String key = "3531522522455225242251333322413531512331245435233252531522222544512522535212242321222424453544242145";
		pse.fillBlocks(key);
		//pse.printBlocks();
		pse.dfs(9, 4, 0);
		pse.clearSelectedBlocks();
		//pse.printBlocks();
		//while (pse.setMoveFlagForBlocks()){
			//pse.moveBlocks();
			//pse.printBlocks();
		//}
		
		//repaint();
		thread=null;
		
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.setColor(Color.BLACK);
		int x,y;
		int basex, basey;
		int drawx, drawy;
		
		g.fillRect(0, 0, StartX+PSEWidth*(Length+2*Border)+Border, 2*StartY+PSEHeight*(Length+2*Border)+Border);
		for (y=PSEHeight-1;y>=0;y--) {
			for (x=0;x<PSEWidth;x++) {
				basex=StartX+x*(Length+2*Border)+Border; //+(x-1)*Border
				basey=StartY+y*(Length+2*Border)+Border; 
				int color =pse.getBlock(x,y);
				if (pse.getLeftFlag(x, y))
					basex-=frame*Step;
				if (pse.getFallFlag(x, y))
					basey+=frame*Step;
				g.setColor(StarColors[color]);
				g.fillRect(basex, basey, Length, Length);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (controlLock) return;
		thread=new Thread(this);
		thread.start();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int f;
		controlLock=true;
		while (pse.setMoveFlagForBlocks()){
			frame=0;
			for (f=0; f<FPS*TimeCost; f++){
				repaint();
				frame++;
				try {
					Thread.sleep(220/FPS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pse.moveBlocks();
		}
		controlLock=false;
	}
	
}
