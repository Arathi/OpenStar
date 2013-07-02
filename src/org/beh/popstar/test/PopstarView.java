package org.beh.popstar.test;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.beh.popstar.*;

public class PopstarView extends JPanel implements MouseListener {
	protected PopstarCore game;
	private BufferedImage[] imgStars;
	/**
	 * Create the panel.
	 */
	public PopstarView() {
		game=new PopstarCore();
		
		BufferedImage imgStarsAll=null;
		try {
			imgStarsAll=ImageIO.read( ClassLoader.getSystemResourceAsStream("popstars.png") );
			int size=imgStarsAll.getHeight(); //44
			
			imgStars=new BufferedImage[6];
			for (int i=0; i<6; i++){
				imgStars[i]=imgStarsAll.getSubimage(i*size, 0, size, size);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawRect(0, 0, 450, 450);
		if (game==null) return;
		int x, y;
		for (y=0; y<PopstarCore.Height; y++){
			for (x=0; x<PopstarCore.Width; x++){
				int type=game.getStar(x, y);
				g.drawImage(imgStars[type], x*45, (PopstarCore.Height-y-1)*45, this);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if (game.isGameEnd()) return;
		int mousex=e.getX(), mousey=e.getY();
		int x,y;
		x=mousex/45;
		y=mousey/45;
		y=PopstarCore.Height-y;
		System.out.println(x+","+y);
		if (x>=0 && x<PopstarCore.Width && y>=0 && y<PopstarCore.Height){
			game.touch(x, y);
			game.scan();
			
			repaint();
		}
	}
	
}
