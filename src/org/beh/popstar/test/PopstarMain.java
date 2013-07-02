package org.beh.popstar.test;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.beh.popstar.PopstarCore;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopstarMain {

	private JFrame frame;
	private PopstarView view;
	private PopstarCore game;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PopstarMain window = new PopstarMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PopstarMain() {
		initialize();
		
		game=view.game;
		game.begin();
		view.repaint();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 480, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		view = new PopstarView();
		frame.getContentPane().add(view, BorderLayout.CENTER);

		frame.addMouseListener(view);
	}

}
