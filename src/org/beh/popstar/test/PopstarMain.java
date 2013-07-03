package org.beh.popstar.test;

import java.awt.EventQueue;
import javax.swing.JFrame;
import org.beh.popstar.PopstarCore;
import java.awt.BorderLayout;

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
		frmPopstarsimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		view = new PopstarView();
		frmPopstarsimulator.getContentPane().add(view, BorderLayout.CENTER);

		view.addMouseListener(view);
		frmPopstarsimulator.setBounds(100, 100, PopstarCore.Width*(PopstarView.BlockLength+PopstarView.BlockSpace*2)+6, PopstarCore.Height*(PopstarView.BlockLength+PopstarView.BlockSpace*2)+28);
	}

}
