package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.util.Vector;
import java.util.ArrayList;

import utilities.FileUtil;
import utilities.GrouperUtil;

import main.gui.UneditableTableModel;
import main.gui.AddPersonFrame;

import actors.Person;

import java.io.File;

public class MainGUI{
	public static final int WIN_WIDTH = 800;
	public static final int WIN_HEIGHT = 600;

	public static final int BUTT_WIDTH = 150;
	public static final int BUTT_HEIGHT = 30;

	public static final int LOAD_WIDTH = 300;
	public static final int LOAD_HEIGHT = 150;

	private JFrame frame;
	private JFrame load;

	private AddPersonFrame apFrame;

	private JButton file;
	private JButton group;
	private JButton about;
	private JButton addPerson;

	private JTextField groupNumText;

	private JTable personTable;
	private JTable groupTable;

	private JFileChooser fileChooser;

	private Person[] arr;
	private ArrayList<ArrayList<Person>> bestGroup;

	public MainGUI(){
		ActionListener aboutAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(frame, 
						"Author: Julius Jireh B. Vega\nCreated for the ACSS-UPLB",
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

					switchComp(true);
				}
			}
		};

		ActionListener groupAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GrouperUtil grouper;
				int nGroups = 1;

				try{
					nGroups = Integer.parseInt(groupNumText.getText());
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(frame, 
							"Please enter the number of groups to make!");
					return;
				}

				switchComp(false);

				load.setVisible(true);
				grouper = new GrouperUtil(arr, nGroups);
				bestGroup = grouper.automatedGrouping();
				load.setVisible(false);

				switchComp(true);

				JOptionPane.showMessageDialog(frame, "Found best group! " + 
						"Output saved to \"groupings.out\"");
				setGroupModel(bestGroup);
			}
		};

		ActionListener addPersonAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				apFrame.setVisible(true);
			}
		};

		this.frame = new JFrame("Automatic Grouper");

		this.file = new JButton("Input file...");
		this.group = new JButton("Group them!");
		this.about = new JButton("About us");
		this.addPerson = new JButton("Add a Person");

		this.groupNumText = new JTextField(5);
		this.fileChooser = new JFileChooser();

		this.arr = null;
		this.personTable = null;
		this.groupTable = null;

		file.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		group.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		about.setPreferredSize(new Dimension(BUTT_WIDTH, BUTT_HEIGHT));
		groupNumText.setPreferredSize(new Dimension(BUTT_WIDTH/2, BUTT_HEIGHT));

		this.switchComp(false);

		this.about.addActionListener(aboutAction);
		this.file.addActionListener(fileAction);
		this.group.addActionListener(groupAction);
		this.addPerson.addActionListener(addPersonAction);

		this.frame.setSize(WIN_WIDTH, WIN_HEIGHT);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.add(this.addToFrame());
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);

		this.initLoadFrame();
		this.initAPFrame();
	}

	private void initAPFrame(){
		this.apFrame = new AddPersonFrame();

		this.apFrame.getConfirm().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Person newPerson = new Person(apFrame.getName(), 
						apFrame.getGWA(), apFrame.getBloc());
				ArrayList<Person> list = new ArrayList<Person>();

				if(arr != null){
					for(Person p : arr)
						list.add(p);
					list.add(newPerson);
					arr = list.toArray(arr);
				} else {
					arr = new Person[1];
					arr[0] = newPerson;
				}

				personTable.setModel(createPersonModel(arr));
				apFrame.setVisible(false);
				apFrame.clear();

				switchComp(true);
			}
		});
	}

	private void initLoadFrame(){
		JPanel panel = new JPanel();

		this.load = new JFrame("Loading...");
		panel.add(new JLabel("Finding the best groupings..."));
		this.load.add(panel, SwingConstants.CENTER);

		this.load.pack();
		this.load.setLocationRelativeTo(null);
		//this.load.setVisible(true);
	}

	private JPanel addToFrame(){
		JPanel panel = new JPanel();
		JPanel personPanel = new JPanel();
		JPanel filePanel = new JPanel();
		JPanel personTablePanel;

		panel.setLayout(new GridLayout(1, 3, 10, 10));

		personPanel.setLayout(new BorderLayout());
		personTablePanel = addGridBagPanel("Person Info: ", createPersonTable());
		personPanel.add(personTablePanel, BorderLayout.CENTER);

		filePanel.setLayout(new GridLayout(2, 1, 3, 3));
		filePanel.add(file);
		filePanel.add(addPerson);
		personPanel.add(filePanel, BorderLayout.PAGE_END);

		panel.add(personPanel);
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

		model = new DefaultTableModel(data, column);

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

		model = new DefaultTableModel(data, column);

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

		GridBagConstraints constraints;

		panel.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.anchor = GridBagConstraints.NORTH;

		groupPanel.setLayout(new BorderLayout());
		groupPanel.add(new JLabel("Number of groups: "), BorderLayout.LINE_START);
		groupPanel.add(groupNumText, BorderLayout.CENTER);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 0.25;
		constraints.gridy = 1;
		centerPanel.add(Box.createGlue(), constraints);

		constraints.gridy = 2;
		constraints.weighty = 0.0;
		centerPanel.add(groupPanel, constraints);

		constraints.fill = GridBagConstraints.NONE;
		constraints.weighty = 0.75;
		constraints.gridy = 3;
		centerPanel.add(group, constraints);

		aboutPanel.add(about);

		panel.add(centerPanel, BorderLayout.CENTER);
		panel.add(aboutPanel, BorderLayout.PAGE_END);

		return panel;
	}

	public void switchComp(boolean flag){
		groupNumText.setEnabled(flag);
		group.setEnabled(flag);
	}


	public static void main(String[] args){
		new MainGUI();
	}
}