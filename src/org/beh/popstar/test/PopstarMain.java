package org.beh.popstar.test;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.beh.popstar.PopstarCore;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopstarMain {

	private JFrame frmPopstarsimulator;
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
					window.frmPopstarsimulator.setVisible(true);
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
		frmPopstarsimulator = new JFrame();
		frmPopstarsimulator.setResizable(false);
		frmPopstarsimulator.setTitle("Popstar!Simulator");
		//frmPopstarsimulator.setBounds(100, 100, 458, 478);
		frmPopstarsimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		view = new PopstarView();
		frmPopstarsimulator.getContentPane().add(view, BorderLayout.CENTER);

		//frmPopstarsimulator.addMouseListener(view);
		view.addMouseListener(view);
		frmPopstarsimulator.setBounds(100, 100, PopstarCore.Width*PopstarView.BlockLength+7, PopstarCore.Height*PopstarView.BlockLength+29);
	}

}
