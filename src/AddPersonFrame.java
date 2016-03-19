package main.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddPersonFrame extends JFrame{
	public static final int WIN_WIDTH = 500;
	public static final int WIN_HEIGHT = 300;

	private JTextField name;
	private JTextField gwa;
	private JTextField bloc;

	private JButton confirm;

	public AddPersonFrame(){
		super("Add a Person");

		name = new JTextField(30);
		gwa = new JTextField(30);
		bloc = new JTextField(30);
		confirm = new JButton("OK");

		super.add(addComponents());
		super.pack();
		super.setLocationRelativeTo(null);
	}

	public JPanel addComponents(){
		JPanel panel = new JPanel();
		JPanel okPanel = new JPanel();

		okPanel.add(confirm);

		panel.setLayout(new GridLayout(4, 1, 3, 3));
		panel.add(createBorderLayoutPanel(new JLabel("Name: "), name));
		panel.add(createBorderLayoutPanel(new JLabel("GWA: "), gwa));
		panel.add(createBorderLayoutPanel(new JLabel("Bloc: "), bloc));
		panel.add(okPanel);
		panel.setBorder(new EmptyBorder(5,5,5,5));

		return panel;
	}

	public JPanel createBorderLayoutPanel(JLabel label, JTextField textField){
		JPanel panel = new JPanel();
		JPanel textFieldWrapper = new JPanel();

		textFieldWrapper.add(textField);

		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.LINE_START);
		panel.add(textFieldWrapper, BorderLayout.CENTER);

		return panel;
	}

	public String getName(){
		String toReturn = name.getText();
		return toReturn;
	}

	public float getGWA(){
		float toReturn = Float.parseFloat(gwa.getText());
		return toReturn;
	}

	public String getBloc(){
		String toReturn = bloc.getText();
		return toReturn;
	}

	public JButton getConfirm(){
		return this.confirm;
	}

	public void clear(){
		bloc.setText("");
		name.setText("");
		gwa.setText("");
	}
}