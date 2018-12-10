package org.terifan.spreadsheet.ui;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.terifan.spreadsheet.CellValue;


public class TableFactory
{
	public JScrollPane createTable(CellValue[][] aData, Object[] aColumns)
	{
		DefaultTableModel model = new DefaultTableModel(aData, aColumns);

		JTable table = new JTable(model);
		table.setBorder(null);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		table.setGridColor(new Color(0xDADCDD));
		table.setRowHeight(19);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent aEvent)
			{
				table.getTableHeader().repaint();
			}
		});
		table.getColumnModel().addColumnModelListener(new TableColumnModelListener()
		{
			@Override
			public void columnAdded(TableColumnModelEvent aE)
			{
			}
			@Override
			public void columnRemoved(TableColumnModelEvent aE)
			{
			}
			@Override
			public void columnMoved(TableColumnModelEvent aE)
			{
			}
			@Override
			public void columnMarginChanged(ChangeEvent aE)
			{
			}
			@Override
			public void columnSelectionChanged(ListSelectionEvent aE)
			{
				table.getTableHeader().repaint();
			}
		});

		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setDefaultRenderer(new ColumnHeaderRenderer());

		JTable rowTable = new RowNumberTable(table);

		ColumnHeaderRenderer corner = new ColumnHeaderRenderer();
		corner.setDrawLeftBorder(true);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEADING_CORNER, corner);
		scrollPane.setBorder(null);

		return scrollPane;
	}
}
