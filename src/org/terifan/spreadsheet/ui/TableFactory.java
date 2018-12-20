package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Dimension;
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
import org.terifan.spreadsheet.CellStyle;
import org.terifan.spreadsheet.CellValue;
import org.terifan.spreadsheet.Map;
import org.terifan.spreadsheet.SpreadSheetTableColumn;


public class TableFactory
{
	public JScrollPane createTable(CellValue[][] aData, List<SpreadSheetTableColumn> aColumns, String aRowHeaderTitle, int aRowNumberSize, int aRowHeaderSize, HashMap<Integer, String> aRowHeaders, Map<CellStyle> aStyles)
	{
		addMissingColumns(aData, aColumns);

		DefaultTableModel model = new DefaultTableModel(aData, aColumns.stream().map(e -> e.getHeaderValue()).collect(Collectors.toList()).toArray());

		FixedTable table = new FixedTable(model);
		table.setBorder(null);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setGridColor(new Color(0xDADCDD));
		table.setRowHeight(19);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setDefaultRenderer(Object.class, new TableCellRenderer(table, aStyles));
		table.setSelectionForeground(null);
		table.setSelectionBackground(null);

		ListSelectionModel selectionModel = table.getSelectionModel();

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.setColumnSelectionAllowed(true);

		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setDefaultRenderer(new ColumnHeaderRenderer("", aRowNumberSize, 0, table));

		RowNumberTable rowTable = new RowNumberTable(table, aRowNumberSize, aRowHeaderSize, aRowHeaders, aStyles);

		ColumnHeaderRenderer cornerLeft = new ColumnHeaderRenderer(aRowHeaderTitle, aRowNumberSize, aRowHeaderSize, table);
		cornerLeft.setDrawLeftBorder(true);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerLeft);
		scrollPane.setBorder(null);

		selectionModel.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent aEvent)
			{
				scrollPane.repaint();
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
				scrollPane.repaint();
			}
		});

		return scrollPane;
	}


	private void addMissingColumns(CellValue[][] aData, List<SpreadSheetTableColumn> aColumns)
	{
		int columnCount = 0;

		for (CellValue[] row : aData)
		{
			columnCount = Math.max(columnCount, row.length);
		}

		while (columnCount > aColumns.size())
		{
			aColumns.add(new SpreadSheetTableColumn(aColumns.size(), Character.toString((char)('A' + aColumns.size()))));
		}
	}
}
