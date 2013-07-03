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
	public static final int BlockLength=24;
	public static final int BlockSpace=1;
	public static final int HeadHeight=50;
	//private PopstarMain context;
	
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
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (BlockLength+BlockSpace*2)*10, (BlockLength+BlockSpace*2)*10+HeadHeight);
		if (game==null) return;
		g.setColor(Color.RED);
		//绘制
		int x, y, ry, type;
		for (y=0; y<PopstarCore.Height; y++){
			for (x=0; x<PopstarCore.Width; x++){
				type=game.getStar(x, y);
				ry=PopstarCore.Height-y-1;
				if (game.isPoped(x, y)){
					g.fillRect(x*(BlockLength+BlockSpace)+x*BlockSpace, ry*(BlockLength+BlockSpace)+ry*BlockSpace+HeadHeight, BlockLength+2*BlockSpace, BlockLength+2*BlockSpace);
				}
				g.drawImage(imgStars[type], x*(BlockLength+BlockSpace)+(x+1)*BlockSpace, ry*(BlockLength+BlockSpace)+(ry+1)*BlockSpace+HeadHeight, BlockLength, BlockLength, this);
			}
		}
		//绘制分数
		g.setColor(Color.WHITE);
		g.drawString("当前分数："+game.getScore(), 10, 20);
		if (game.isGameEnd()){
			g.setColor(Color.RED);
			g.drawString("游戏结束", 10, 40);
		}
		else g.drawString("目标分数："+game.getTarget(), 10, 40);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		if (game.isGameEnd()){
			game.begin();
			repaint();
			return;
		}
		int mousex=e.getX(), mousey=e.getY();
		System.out.println("当前点击为置坐标: ("+mousex+","+mousey+")");
		if (mousey<HeadHeight) return;
		mousey-=HeadHeight;
		int x,y;
		x=mousex/(BlockLength+2*BlockSpace);
		y=mousey/(BlockLength+2*BlockSpace);
		y=PopstarCore.Height-y-1;
		System.out.println(x+","+y);

		if (x<0 || x>=PopstarCore.Width || y<0 && y>=PopstarCore.Height)
			return;
		if (game.isPoped(x, y)){
			game.confirm();
		}
		else{
			game.select(x,y);
		}
		System.out.println("view正在重绘");
		repaint();
	}
	
}
