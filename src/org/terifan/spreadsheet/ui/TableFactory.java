package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
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
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.terifan.spreadsheet.CellValue;


public class TableFactory
{
	public JScrollPane createTable(CellValue[][] aData, List<TableColumn> aColumns)
	{
		DefaultTableModel model = new DefaultTableModel(aData, aColumns.stream().map(e->e.getHeaderValue()).collect(Collectors.toList()).toArray());

		JTable table = new JTable(model);
		table.setBorder(null);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setGridColor(new Color(0xDADCDD));
		table.setRowHeight(19);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		ListSelectionModel selectionModel = table.getSelectionModel();

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.setColumnSelectionAllowed(true);
		for (int i = 0; i < aColumns.size(); i++)
		{
			columnModel.removeColumn(columnModel.getColumn(0));
		}
		for (int i = 0; i < aColumns.size(); i++)
		{
			columnModel.addColumn(aColumns.get(i));
		}

		selectionModel.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent aEvent)
			{
				table.getTableHeader().repaint();
			}
		});

		columnModel.addColumnModelListener(new TableColumnModelListener()
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
