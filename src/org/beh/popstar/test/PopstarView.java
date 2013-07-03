package org.beh.popstar.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.beh.popstar.*;

public class PopstarView extends JPanel implements MouseListener {
	private static final long serialVersionUID = -8203094648748492497L;
	protected PopstarCore game;
	private BufferedImage[] imgStars;
	public static final int BlockLength=25;
	public static final int BlockSpace=1;
	public static final int HeadHeight=50;
	
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
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (BlockLength+BlockSpace*2)*10, (BlockLength+BlockSpace*2)*10);
		if (game==null) return;
		int x, y;
		for (y=0; y<PopstarCore.Height; y++){
			for (x=0; x<PopstarCore.Width; x++){
				int type=game.getStar(x, y), ry=PopstarCore.Height-y-1;
				g.drawImage(imgStars[type], x*(BlockLength+BlockSpace)+(x+1)*BlockSpace, ry*(BlockLength+BlockSpace)+(ry+1)*BlockSpace, BlockLength, BlockLength, this);
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
		x=mousex/(BlockLength+2*BlockSpace);
		y=mousey/(BlockLength+2*BlockSpace);
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
