package main;

import component.PanelGame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Main extends JFrame {
	
	public Main () {
		init();
	}
	
	private void init() {
		setTitle("AI Dot Path Test");
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		PanelGame panelGame = new PanelGame();
		add(panelGame);
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				panelGame.start();
			}
		});
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.setVisible(true);

	}

}
