package main.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import actors.Person;
import utilities.GrouperUtil;
import main.MainGUI;

public class GroupButtAction implements ActionListener{
	private MainGUI main;

	public GroupButtAction(MainGUI main){
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		final GrouperUtil grouper;
		final LoadingThread loaderLoop = new LoadingThread(main.getLoading());

		SwingWorker<Void, Void> worker;
		int nGroups = 1;

		try{
			nGroups = Integer.parseInt(main.getGroupNumText().getText());
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(main.getFrame(), 
					"Please enter the number of groups to make!");
			return;
		}

		grouper = new GrouperUtil(main.getArr(), nGroups);

		worker = new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception{
				main.getFrame().setTitle(MainGUI.WIN_TITLE + 
						" - Finding best group...");
				switchAllComp(false);
				loaderLoop.start();
				grouper.automatedGrouping();
				return null;
			}

			@Override
			public void done(){
				main.getFrame().toFront();
				main.getFrame().setTitle(MainGUI.WIN_TITLE);

				loaderLoop.terminate();

				main.setBestGroup(grouper.getBestGroup());
				switchAllComp(true);
				
				JOptionPane.showMessageDialog(main.getFrame(), 
						"Found best group!\n" + 
						"Output saved to \"groupings.out\"");
				main.setGroupModel(main.getBestGroup());
			}

			public void switchAllComp(boolean flag){
				main.getGroupNumText().setEnabled(flag);
				main.getGroup().setEnabled(flag);
				main.getFile().setEnabled(flag);
				main.getAddPerson().setEnabled(flag);
			}
		};

		worker.execute();
	}
}