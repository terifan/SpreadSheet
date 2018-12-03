package org.terifan.spreadsheet.ui;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import org.terifan.spreadsheet.CellValue;


public class TableFactory
{
	public JComponent createTable(CellValue[][] aData, Object[] aColumns)
	{
		JTable table = new JTable(aData, aColumns);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);

		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setDefaultRenderer(new ColumnHeaderRenderer());

		JTable rowTable = new RowNumberTable(table);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setRowHeaderView(rowTable);

		return scrollPane;
	}
}
