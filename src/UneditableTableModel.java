package main.gui;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class UneditableTableModel extends DefaultTableModel{
	public UneditableTableModel(Vector data, Vector column){
		super(data, column);
	}

	@Override
	public boolean isCellEditable(int row, int column){
		return false;
	}
}