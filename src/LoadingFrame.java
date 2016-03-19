package main.gui;

import java.awt.*;
import javax.swing.*;

public class LoadingFrame extends JFrame{
	public static final int WIN_WIDTH = 300;
	public static final int WIN_HEIGHT = 100;

	private JLabel message;

	public LoadingFrame(){
		super("Loading");
		message = new JLabel("Finding the best group...", SwingConstants.CENTER);
		this.setSize(WIN_WIDTH, WIN_HEIGHT);
		this.add(addComponents());
		this.setLocationRelativeTo(null);
	}

	private JPanel addComponents(){
		JPanel panel = new JPanel();
		panel.add(message);

		return panel;
	}
}