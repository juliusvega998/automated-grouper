package main.gui;

import javax.swing.JLabel;

public class LoadingThread extends Thread{
	private boolean isStop;
	private JLabel loading;

	public LoadingThread(JLabel loading){
		this.isStop = false;
		this.loading = loading;
	}

	@Override
	public void run(){
		while(!this.isStop){
			try{
				loading.setText(".");
				Thread.sleep(1000);
				loading.setText("..");
				Thread.sleep(1000);
				loading.setText("...");
				Thread.sleep(1000);
			} catch(Exception ex){
				ex.printStackTrace();
				loading.setText("");
				return;
			}
		}
		
		loading.setText("");
	}

	public void terminate(){
		this.isStop = true;
	}
}