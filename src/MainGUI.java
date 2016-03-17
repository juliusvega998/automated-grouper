package main;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.util.Vector;
import java.util.ArrayList;

import utilities.FileUtil;
import utilities.GrouperUtil;

import main.gui.UneditableTableModel;
import main.gui.LoadingFrame;

import actors.Person;

import java.io.File;

public class MainGUI{
	public static final int WIN_WIDTH = 800;
	public static final int WIN_HEIGHT = 600;

	public static final int BUTT_WIDTH = 150;
	public static final int BUTT_HEIGHT = 30;

	private JFrame frame;
	private JButton file;
	private JButton group;
	private JButton about;

	private JTextField outFileText;
	private JTextField groupNumText;

	private JTable personTable;
	private JTable groupTable;

	private JFileChooser fileChooser;

	private Person[] arr;
	private ArrayList<ArrayList<Person>> bestGroup;

	private LoadingFrame loading;

	public MainGUI(){
		this.loading = new LoadingFrame();
		this.frame = new JFrame("Automatic Grouper");
		this.file = new JButton("Input file...");
		this.group = new JButton("Group them!");
		this.about = new JButton("About us");
		this.outFileText = new JTextField(15);
		this.groupNumText = new JTextField();
		this.fileChooser = new JFileChooser();
		this.arr = null;
		this.personTable = null;
		this.groupTable = null;

		outFileText.setEnabled(false);
		groupNumText.setEnabled(false);
		group.setEnabled(false);

		ActionListener aboutAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(frame, 
						"Author: Julius Jireh B. Vega",
						"About us", JOptionPane.PLAIN_MESSAGE);
			}
		};

		ActionListener fileAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
					try{
						File file = fileChooser.getSelectedFile();
						arr = FileUtil.addToArray(file);
						personTable.setModel(createPersonModel(arr));
					} catch(Exception ex) {
						return;
					}

					outFileText.setEnabled(true);
					groupNumText.setEnabled(true);
					group.setEnabled(true);
				}
			}
		};

		ActionListener groupAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int nGroups = 1;	
				try{
					nGroups = Integer.parseInt(groupNumText.getText());
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(frame, "Please enter the number of groups to make!");
					return;
				}

				GrouperUtil grouper = new GrouperUtil(arr, nGroups);

				outFileText.setEnabled(false);
				groupNumText.setEnabled(false);
				group.setEnabled(false);


				loading.setVisible(true);

				bestGroup = grouper.automatedGrouping();
				loading.setVisible(false);

				outFileText.setEnabled(true);
				groupNumText.setEnabled(true);
				group.setEnabled(true);

				JOptionPane.showMessageDialog(frame, "Found best group!");

				setGroupModel(bestGroup);
			}
		};

		about.addActionListener(aboutAction);
		file.addActionListener(fileAction);
		group.addActionListener(groupAction);

		this.frame.setSize(WIN_WIDTH, WIN_HEIGHT);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.add(this.addToFrame());
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
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
		JScrollPane scrollPane;
		
		personTable = new JTable(createPersonModel());
		scrollPane = new JScrollPane(personTable);
		personTable.setFillsViewportHeight(true);
		personTable.setEnabled(false);

		return personTable;
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

	private DefaultTableModel createPersonModel(Person[] arr){
		DefaultTableModel model;
		Vector<String> column = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();

		column.add("Name");
		column.add("Grade");
		column.add("Bloc");

		for(Person p : arr){
			Vector<String> dataRow = new Vector<String>();

			dataRow.add(p.getName());
			dataRow.add(Float.toString(p.getGWA()));
			dataRow.add(p.getBloc());
			data.add(dataRow);
		}

		model = new UneditableTableModel(data, column);

		return model;
	}

	private JTable createGroupTable(){
		JScrollPane scrollPane;
		
		groupTable = new JTable(this.createGroupModel());
		scrollPane = new JScrollPane(groupTable);
		groupTable.setFillsViewportHeight(true);
		groupTable.setEnabled(false);

		return groupTable;
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

	public void setGroupModel(ArrayList<ArrayList<Person>> group){
		DefaultTableModel model;
		Vector<String> column = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		int i=1;

		column.add("Name");
		column.add("Group");

		for(ArrayList<Person> l : group){
			for(Person p : l){
				Vector<String> dataRow = new Vector<String>();
				dataRow.add(p.getName());
				dataRow.add(Integer.toString(i));
				data.add(dataRow);
			}
			i++;
		}

		model = new UneditableTableModel(data, column);
		groupTable.setModel(model);
	}

	private JPanel addNavigation(){
		JPanel panel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel aboutPanel = new JPanel();
		JPanel groupPanel = new JPanel();
		JPanel filePanel = new JPanel();
		JPanel outPanel = new JPanel();

		GridBagConstraints constraints;

		file.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		group.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		about.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		outFileText.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		groupNumText.setPreferredSize(new Dimension(BUTT_WIDTH/2, BUTT_HEIGHT));

		panel.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.anchor = GridBagConstraints.NORTH;

		filePanel.add(file);
		panel.add(filePanel, BorderLayout.PAGE_START);

		groupPanel.setLayout(new BorderLayout());
		groupPanel.add(new JLabel("Groups of: "), BorderLayout.LINE_START);
		groupPanel.add(groupNumText, BorderLayout.CENTER);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 0.25;
		constraints.gridy = 1;
		centerPanel.add(Box.createGlue(), constraints);

		outPanel.setLayout(new BorderLayout());
		outPanel.add(new JLabel("Output filename: "), BorderLayout.LINE_START);
		outPanel.add(outFileText, BorderLayout.CENTER);

		constraints.gridy = 2;
		constraints.weighty = 0.0;
		centerPanel.add(groupPanel, constraints);

		constraints.gridy = 3;
		centerPanel.add(outPanel, constraints);

		constraints.fill = GridBagConstraints.NONE;
		constraints.weighty = 0.75;
		constraints.gridy = 4;
		centerPanel.add(group, constraints);

		aboutPanel.add(about);

		panel.add(centerPanel, BorderLayout.CENTER);
		panel.add(aboutPanel, BorderLayout.PAGE_END);

		return panel;
	}


	public static void main(String[] args){
		new MainGUI();
	}
}