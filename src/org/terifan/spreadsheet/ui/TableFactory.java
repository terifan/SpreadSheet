package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
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
import javax.swing.table.TableColumnModel;
import org.terifan.spreadsheet.CellValue;
import org.terifan.spreadsheet.SpreadSheetTableColumn;


public class TableFactory
{
	public JScrollPane createTable(CellValue[][] aData, List<SpreadSheetTableColumn> aColumns, int aNumStaticColumns, String aRowHeaderTitle, int aRowHeaderSize, HashMap<Integer, String> aRowHeaders)
	{
		if (aRowHeaders.isEmpty())
		{
			aRowHeaderSize = 0;
		}

		SpreadSheetTableColumn[] dataColumns = new SpreadSheetTableColumn[aColumns.size() - aNumStaticColumns];
		SpreadSheetTableColumn[] staticColumns = new SpreadSheetTableColumn[aNumStaticColumns];

		for (int i = 0; i < aNumStaticColumns; i++)
		{
			staticColumns[i] = aColumns.get(i);
		}
		for (int i = aNumStaticColumns, j = 0; i < aColumns.size(); i++, j++)
		{
			dataColumns[j] = aColumns.get(i);
		}

		CellValue[][] staticData = aData;
		CellValue[][] data = new CellValue[aData.length][dataColumns.length];
		for (int i = 0; i < data.length; i++)
		{
			data[i] = Arrays.copyOfRange(aData[i], aNumStaticColumns, aData[i].length);
		}

		DefaultTableModel model = new DefaultTableModel(data, aColumns.stream().map(e->e.getHeaderValue()).collect(Collectors.toList()).toArray());

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
		while (columnModel.getColumnCount() > 0)
		{
			columnModel.removeColumn(columnModel.getColumn(0));
		}
		for (int i = 0; i < aColumns.size() - aNumStaticColumns; i++)
		{
			SpreadSheetTableColumn col = aColumns.get(aNumStaticColumns + i);
			col.setModelIndex(i);
			columnModel.addColumn(col);
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

		RowNumberTable rowTable = new RowNumberTable(table, staticData, staticColumns, aNumStaticColumns, aRowHeaderSize, aRowHeaders);
		rowTable.setDrawLeftBorder(true);

		ColumnHeaderRenderer corner = new ColumnHeaderRenderer();
		corner.setDrawLeftBorder(true);
		corner.setStaticColumns(staticColumns, aNumStaticColumns);
		corner.setRowHeaders(aRowHeaderTitle, aRowHeaderSize);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEADING_CORNER, corner);
		scrollPane.setBorder(null);

		return scrollPane;
	}
}
