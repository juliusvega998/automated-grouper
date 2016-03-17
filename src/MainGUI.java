package main;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.Vector;

import main.gui.UneditableTableModel;

public class MainGUI{
	public static final int WIN_WIDTH = 800;
	public static final int WIN_HEIGHT = 600;
	
	public static final int BUTT_WIDTH = 150;
	public static final int BUTT_HEIGHT = 30;

	private JFrame frame;
	private JButton file = new JButton("Input file...");
	private JButton group = new JButton("Group them!");
	private JButton about = new JButton("About us");

	private JTextField outFileText = new JTextField(15);

	public MainGUI(){
		frame = new JFrame("Automatic Grouper");

		frame.setSize(WIN_WIDTH, WIN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this.addToFrame());
		frame.setVisible(true);
	}

	private JPanel addToFrame(){
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(1, 3, 10, 10));

		panel.add(addGridBagPanel("Person Info: ", createPersonTable()));
		panel.add(addGridBagPanel("Group Results: ", createGroupTable()));
		panel.add(addNavigation());

		panel.setBorder(new EmptyBorder(10,10,10,10));

		return panel;
	}

	private JPanel addGridBagPanel(String label, JTable table){
		JPanel panel = new JPanel();
		JPanel tablePanel = new JPanel();
		GridBagConstraints constraints;

		panel.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();

		constraints.anchor = GridBagConstraints.NORTH;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(new JLabel(label), constraints);

		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		tablePanel.add(table, BorderLayout.CENTER);

		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(tablePanel, constraints);

		return panel;
	}

	private JTable createPersonTable(){
		JTable table;
		JScrollPane scrollPane;
		
		table = new JTable(createPersonModel());
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setEnabled(false);

		return table;
	}

	private DefaultTableModel createPersonModel(){
		DefaultTableModel model;
		Vector<String> column = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> dataRow = new Vector<String>();

		column.add("Name");
		column.add("Grade");
		column.add("Bloc");

		dataRow.add("");
		dataRow.add("");
		dataRow.add("");
		data.add(dataRow);

		model = new UneditableTableModel(data, column);

		return model;
	}

	private JTable createGroupTable(){
		JTable table;
		JScrollPane scrollPane;
		
		table = new JTable(this.createGroupModel());
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setEnabled(false);

		return table;
	}

	private DefaultTableModel createGroupModel(){
		DefaultTableModel model;
		Vector<String> column = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> dataRow = new Vector<String>();

		column.add("Name");
		column.add("Group");

		dataRow.add("");
		dataRow.add("");
		data.add(dataRow);

		model = new UneditableTableModel(data, column);

		return model;
	}

	private JPanel addNavigation(){
		JPanel panel = new JPanel();
		JPanel centerPanel = new JPanel();
		GridBagConstraints constraints;

		file.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		group.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		about.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		outFileText.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));

		panel.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.anchor = GridBagConstraints.NORTH;

		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		centerPanel.add(file, constraints);

		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		centerPanel.add(group, constraints);

		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		centerPanel.add(new JLabel("Output filename: "), constraints);

		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 3;
		centerPanel.add(outFileText, constraints);

		panel.add(centerPanel, BorderLayout.CENTER);
		panel.add(about, BorderLayout.PAGE_END);

		return panel;
	}


	public static void main(String[] args){
		new MainGUI();
	}
}