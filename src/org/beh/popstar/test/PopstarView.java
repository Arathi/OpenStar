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
	public static final int BlockLength=25;
	
	/**
	 * 无参构造器
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
		g.drawRect(0, 0, BlockLength*10, BlockLength*10);
		if (game==null) return;
		int x, y;
		for (y=0; y<PopstarCore.Height; y++){
			for (x=0; x<PopstarCore.Width; x++){
				int type=game.getStar(x, y);
				//g.drawImage(imgStars[type], x*BlockLength, (PopstarCore.Height-y-1)*BlockLength, this);
				g.drawImage(imgStars[type], x*BlockLength, (PopstarCore.Height-y-1)*BlockLength, BlockLength, BlockLength, this);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
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
		x=mousex/BlockLength;
		y=mousey/BlockLength;
		y=PopstarCore.Height-y-1;
		System.out.println("当前点击为置坐标: ("+mousex+","+mousey+")");
		System.out.println(x+","+y);
		if (x>=0 && x<PopstarCore.Width && y>=0 && y<PopstarCore.Height){
			game.touch(x, y);
			game.scan();
			
			repaint();
		}
	}
	
}
